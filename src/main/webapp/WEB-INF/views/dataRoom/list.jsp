<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>자료실</title>
    <link rel="stylesheet" href="${path}/resources/css/dataRoom.css">
    <link rel="stylesheet" href="${path}/resources/css/main.css">
    <link rel="stylesheet" href="${path}/resources/css/common/style.css">
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
                <td class="center">관리자</td>
                <td class="center">223</td>
                <td class="center">2024-03-05</td>
            </tr>
            <tr>
                <td class="center">9</td>
                <td>
                    <a href="#" class="title-link">
                        체육시설 안전관리 매뉴얼
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">관리자</td>
                <td class="center">156</td>
                <td class="center">2024-03-04</td>
            </tr>
            <tr>
                <td class="center">8</td>
                <td>
                    <a href="#" class="title-link">
                        체육시설 이용 신청서 양식
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">관리자</td>
                <td class="center">342</td>
                <td class="center">2024-03-03</td>
            </tr>
            <tr>
                <td class="center">7</td>
                <td>
                    <a href="#" class="title-link">
                        체육동호회 등록 신청서
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">관리자</td>
                <td class="center">198</td>
                <td class="center">2024-03-02</td>
            </tr>
            <tr>
                <td class="center">6</td>
                <td>
                    <a href="#" class="title-link">
                        체육시설 대관 규정집
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">관리자</td>
                <td class="center">267</td>
                <td class="center">2024-03-01</td>
            </tr>
            <tr>
                <td class="center">5</td>
                <td>
                    <a href="#" class="title-link">
                        생활체육 지도자 신청 양식
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">관리자</td>
                <td class="center">189</td>
                <td class="center">2024-02-28</td>
            </tr>
            <tr>
                <td class="center">4</td>
                <td>
                    <a href="#" class="title-link">
                        체육시설 회원 가입 신청서
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">관리자</td>
                <td class="center">423</td>
                <td class="center">2024-02-27</td>
            </tr>
            <tr>
                <td class="center">3</td>
                <td>
                    <a href="#" class="title-link">
                        체육프로그램 제안서 양식
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">관리자</td>
                <td class="center">167</td>
                <td class="center">2024-02-26</td>
            </tr>
            <tr>
                <td class="center">2</td>
                <td>
                    <a href="#" class="title-link">
                        체육시설 환불 신청서
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">관리자</td>
                <td class="center">298</td>
                <td class="center">2024-02-25</td>
            </tr>
            <tr>
                <td class="center">1</td>
                <td>
                    <a href="#" class="title-link">
                        2021년 생활체육프로그램사업 강사 배부용 자료
                        <i class="download-icon"></i>
                    </a>
                </td>
                <td class="center">관리자관리자</td>
                <td class="center">1729</td>
                <td class="center">2021-03-11</td>
            </tr>
            </tbody>
        </table>
    </div>
</main>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<script src="${path}/resources/js/dataRoom.js"></script>
</body>
</html>