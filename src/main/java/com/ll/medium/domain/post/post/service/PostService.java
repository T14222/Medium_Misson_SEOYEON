package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.entity.PostDetail;
import com.ll.medium.domain.post.post.repository.PostDetailRepository;
import com.ll.medium.domain.post.post.repository.PostRepository;
import com.ll.medium.domain.post.postComment.entity.PostComment;
import com.ll.medium.domain.post.postComment.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final PostDetailRepository postDetailRepository;
    private final PostCommentRepository postCommentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Post write(Member author, String title, String body, boolean published) {
        memberRepository.save(author);

        Post post = Post.builder()
                .author(author)
                .title(title)
                .body(body)
                .published(published)
                .build();

        postRepository.save(post);

        saveBody(post, body);

        return post;
    }

    public Object findTop30ByPublishedOrderByIdDesc(boolean published) {
        return postRepository.findTop30ByPublishedOrderByIdDesc(published);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public Page<Post> search(String kw, Pageable pageable) {
        return postRepository.search(true, kw, pageable);
    }

    public Page<Post> search(Member author, Boolean published, String kw, Pageable pageable) {
        return postRepository.search(author, published, kw, pageable);
    }

    public boolean canLike(Member author, Post post) {
        if (author == null) return false;

        return !post.hasLike(author);
    }

    public boolean canCancelLike(Member author, Post post) {
        if (author == null) return false;

        return post.hasLike(author);
    }

    public boolean canModify(Member author, Post post) {
        if (author == null) return false;
        
        return author.equals(post.getAuthor());
    }

    @Transactional
    public void edit(Post post, String title, String body, boolean published, int minMembershipLevel) {
        post.setTitle(title);
        post.setPublished(published);
        post.setMinMembershipLevel(minMembershipLevel);

        saveBody(post, body);
    }

    private void saveBody(Post post, String body) {
        post.setBody(body);

        PostDetail postDetailBody = findDetail(post, "common__body");

        postDetailBody.setVal(body);
    }

    private PostDetail findDetail(Post post, String name) {
        Optional<PostDetail> opPostDetailBody = postDetailRepository.findByPostAndName(post, name);

        PostDetail postDetailBody = opPostDetailBody.orElseGet(() -> postDetailRepository.save(
                PostDetail.builder()
                        .post(post)
                        .name(name)
                        .build()
        ));

        return postDetailBody;
    }

    public boolean canDelete(Member author, Post post) {
        if (author == null) return false;

        if (author.isAdmin()) return true;

        return author.equals(post.getAuthor());
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete((post));
    }

    @Transactional
    public void increaseHit(Post post) {
        post.increaseHit();
    }

    @Transactional
    public void like(Member author, Post post) {
        post.addLike(author);
    }

    @Transactional
    public void cancelLike(Member author, Post post) {
        post.deleteLike(author);
    }

    @Transactional
    public PostComment writeComment(Member author, Post post, String body) {
        return post.writeComment(author, body);
    }

    public boolean canModifyComment(Member actor, PostComment comment) {
        if (actor == null) return false;

        return actor.equals(comment.getAuthor());
    }

    public boolean canDeleteComment(Member actor, PostComment comment) {
        if (actor == null) return false;

        if (actor.isAdmin()) return true;

        return actor.equals(comment.getAuthor());
    }

    public Optional<PostComment> findCommentById(long id) {
        return postCommentRepository.findCommentById(id);
    }

    @Transactional
    public void modifyComment(PostComment postComment, String body) {
        postComment.setBody(body);
    }

    @Transactional
    public void deleteComment(PostComment postComment) {
        postCommentRepository.delete(postComment);
    }

    public Post findTempOrMake(Member author) {
        return postRepository.findByAuthorAndPublishedAndTitle(author, false, "임시글")
                .orElseGet(() -> write(author, "임시글", "", false));
    }
}