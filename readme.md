# 구현 목록
#### 가입
- [x] GET /member/join : 가입 폼
- [x] POST /member/join : 가입 폼 처리

#### 로그인
- [x] GET /member/login : 로그인 폼
- [x] POST /member/login : 로그인 폼 처리

#### 로그아웃
- [x] POST /member/logout : 로그아웃

#### 홈
- [x] GET / : 홈
  (최신글 30개 노출)

#### 글 목록 조회
- [x] GET /post/list : 전체 글 리스트
- [x] \+ 공개된 글만 노출

#### 내 글 목록 조회
- [x] GET /post/myList : 내 글 리스트

#### 글 상세내용 조회
- [x] GET /post/1 : 1번 글 상세보기

#### 글 작성
- [x] GET /post/write : 글 작성 폼
- [x] POST /post/write : 글 작성 처리

#### 글 수정
- [x] GET /post/1/modify : 1번 글 수정 폼
- [x] PUT /post/1/modify : 1번 글 수정 폼 처리

#### 글 삭제
- [x] DELETE /post/1/delete : 1번 글 삭제

#### 특정 회원의 글 모아보기
- [x] GET /b/user1 : 회원 user1 의 전체 글 리스트
- [x] GET /b/user1/3 : 회원 user1 의 글 중에서 3번글 상세보기

---
## 추가 기능 구현
- [x] 조회수
- [x] 추천
- [X] 댓글 CRUD