<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>알림마당</title>
    <link rel="stylesheet" href="${path}/resources/css/notice.css">
    <link rel="stylesheet" href="${path}/resources/css/main.css">
    <link rel="stylesheet" href="${path}/resources/css/common/style.css">
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />

<main class="notice-container">
    <h1>공지사항</h1>

    <!-- 검색 영역 -->
    <div class="search-area">
        <select id="searchType">
            <option value="title">제목</option>
            <option value="content">내용</option>
            <option value="writer">작성자</option>
        </select>
        <input type="text" id="searchKeyword" placeholder="검색어를 입력하세요">
        <button onclick="searchNotice()"></button>
    </div>

    <!-- 공지사항 목록 -->
    <table class="notice-table">
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
            <td>3</td>
            <td><span class="notice-badge">공지</span> 체육관 이용시간 변경 안내</td>
            <td>관리자</td>
            <td>2024-12-08</td>
            <td>245</td>
        </tr>
        <tr>
            <td>2</td>
            <td>수영장 정기점검 일정 안내</td>
            <td>관리자</td>
            <td>2024-12-07</td>
            <td>156</td>
        </tr>
        <tr>
            <td>1</td>
            <td>12월 휴관일 안내</td>
            <td>관리자</td>
            <td>2024-12-06</td>
            <td>189</td>
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

    <!-- 글쓰기 버튼 (관리자용) -->
    <div class="button-area">
        <button onclick="location.href='write.jsp'" class="write-btn">글쓰기</button>
    </div>
</main>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<script src="${path}/resources/js/notice.js"></script>
</body>
</html>