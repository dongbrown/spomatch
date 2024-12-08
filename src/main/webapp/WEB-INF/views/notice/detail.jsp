<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>공지사항 상세보기</title>
    <link rel="stylesheet" href="${path}/resources/css/notice.css">
    <link rel="stylesheet" href="${path}/resources/css/main.css">
    <link rel="stylesheet" href="${path}/resources/css/common/style.css">
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />

<main class="notice-container">
    <div class="notice-detail">
        <div class="notice-header">
            <h2 class="notice-title">
                <c:if test="${notice.isNotice == 'Y'}">
                    <span class="notice-badge">공지</span>
                </c:if>
                ${notice.title}
            </h2>
            <div class="notice-info">
                <span class="writer">작성자: ${notice.writer}</span>
                <span class="date">작성일: ${notice.createDate}</span>
                <span class="views">조회수: ${notice.views}</span>
            </div>
        </div>

        <div class="notice-content">
            ${notice.content}
        </div>

        <c:if test="${not empty notice.attachments}">
            <div class="attachment-list">
                <h3>첨부파일</h3>
                <ul>
                    <c:forEach items="${notice.attachments}" var="file">
                        <li>
                            <a href="/download/${file.id}" class="file-link">
                                <i class="file-icon"></i>
                                    ${file.originalName}
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <div class="notice-navigation">
            <c:if test="${not empty prevNotice}">
                <a href="/notice/detail/${prevNotice.id}" class="prev-notice">
                    <span class="nav-label">이전글</span>
                    <span class="nav-title">${prevNotice.title}</span>
                </a>
            </c:if>
            <c:if test="${not empty nextNotice}">
                <a href="/notice/detail/${nextNotice.id}" class="next-notice">
                    <span class="nav-label">다음글</span>
                    <span class="nav-title">${nextNotice.title}</span>
                </a>
            </c:if>
        </div>

        <div class="button-group">
            <a href="/notice/list" class="btn btn-default">목록</a>
            <c:if test="${isAdmin}">
                <a href="/notice/edit/${notice.id}" class="btn btn-primary">수정</a>
                <button onclick="deleteNotice(${notice.id})" class="btn btn-danger">삭제</button>
            </c:if>
        </div>
    </div>
</main>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<script src="${path}/resources/js/notice.js"></script>
</body>
</html>