<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>참여마당</title>
    <link rel="stylesheet" href="${path}/resources/css/board.css">
    <link rel="stylesheet" href="${path}/resources/css/main.css">
    <link rel="stylesheet" href="${path}/resources/css/common/style.css">
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />

<main class="board-container">
    <h1>참여마당</h1>

    <!-- 검색 영역 -->
    <div class="search-area">
        <select id="searchType">
            <option value="title">제목</option>
            <option value="content">내용</option>
            <option value="writer">작성자</option>
        </select>
        <input type="text" id="searchKeyword" placeholder="검색어를 입력하세요">
        <button onclick="searchBoard()"></button>
    </div>

    <!-- 게시글 목록 -->
    <table class="board-table">
        <thead>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>10</td>
            <td>테니스 동호회 정기모임 후기</td>
            <td>테니스매니아</td>
            <td>2024-12-08</td>
            <td>67</td>
        </tr>
        <tr>
            <td>9</td>
            <td>배드민턴 클럽 회원 모집합니다</td>
            <td>셔틀콕</td>
            <td>2024-12-07</td>
            <td>98</td>
        </tr>
        <tr>
            <td>8</td>
            <td>요가 수업 후기 공유해요</td>
            <td>요가사랑</td>
            <td>2024-12-06</td>
            <td>142</td>
        </tr>
        <tr>
            <td>7</td>
            <td>농구 동호회 회원 모집</td>
            <td>슬램덩크</td>
            <td>2024-12-05</td>
            <td>89</td>
        </tr>
        <tr>
            <td>6</td>
            <td>헬스장 이용 꿀팁 공유</td>
            <td>헬스왕</td>
            <td>2024-12-04</td>
            <td>234</td>
        </tr>
        <tr>
            <td>5</td>
            <td>필라테스 초보 도전기</td>
            <td>필라걸</td>
            <td>2024-12-03</td>
            <td>156</td>
        </tr>
        <tr>
            <td>4</td>
            <td>축구 동호회 친선경기 후기</td>
            <td>축구왕</td>
            <td>2024-12-02</td>
            <td>112</td>
        </tr>
        <tr>
            <td>3</td>
            <td>수영 동호회 회원 모집합니다</td>
            <td>수영매니아</td>
            <td>2024-12-01</td>
            <td>42</td>
        </tr>
        <tr>
            <td>2</td>
            <td>탁구 대회 참가자 모집</td>
            <td>탁구왕</td>
            <td>2024-11-30</td>
            <td>85</td>
        </tr>
        <tr>
            <td>1</td>
            <td>체육관 이용 후기입니다</td>
            <td>운동하자</td>
            <td>2024-11-29</td>
            <td>120</td>
        </tr>
        </tbody>
    </table>

    <!-- 페이지네이션 -->
    <div class="pagination">
        <a href="#" class="page-link">&laquo;</a>
        <a href="#" class="page-link active">1</a>
        <a href="#" class="page-link">2</a>
        <a href="#" class="page-link">3</a>
        <a href="#" class="page-link">&raquo;</a>
    </div>

    <!-- 글쓰기 버튼 (로그인 사용자용) -->
    <div class="button-area">
        <button onclick="location.href='write.jsp'" class="write-btn">글쓰기</button>
    </div>
</main>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<script src="${path}/resources/js/board.js"></script>
</body>
</html>