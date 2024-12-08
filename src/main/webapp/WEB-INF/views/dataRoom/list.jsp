<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>자료실</title>
    <link rel="stylesheet" href="/resources/css/dataRoom.css">
    <link rel="stylesheet" href="/resources/css/main.css">
    <link rel="stylesheet" href="/resources/css/common/style.css">
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />

<main class="data-container">
    <h1>자료실</h1>

    <div class="page-info">전체 2건 1 페이지</div>

    <div class="content-wrapper">
        <!-- 검색 영역 -->
        <div class="search-area">
            <select id="searchType">
                <option value="title">제목</option>
                <option value="content">내용</option>
                <option value="writer">작성자</option>
            </select>
            <div class="search-input-group">
                <input type="text" id="searchKeyword" placeholder="검색어를 입력하세요">
                <button type="button" onclick="searchData()">
                    <i class="search-icon"></i>
                </button>
            </div>
        </div>

        <!-- 자료 목록 -->
        <table class="data-table">
            <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>작성자</th>
                <th>조회</th>
                <th>작성일자</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td class="center"><span class="notice-badge">공지</span></td>
                <td>
                    <a href="#" class="title-link">
                        2024년 생활체육프로그램 운영계획 및 각종 양식
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">홍성군체육회</td>
                <td class="center">223</td>
                <td class="center">2024-03-05</td>
            </tr>
            <tr>
                <td class="center">1</td>
                <td>
                    <a href="#" class="title-link">
                        2021년 생활체육프로그램사업 강사 배부용 자료
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">홍성군체육회</td>
                <td class="center">1729</td>
                <td class="center">2021-03-11</td>
            </tr>
            </tbody>
        </table>
    </div>
</main>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<script src="/resources/js/dataRoom.js"></script>
</body>
</html>