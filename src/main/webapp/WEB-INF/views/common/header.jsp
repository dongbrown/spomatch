<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<header>
    <div class="container">
        <a href="${path}/" class="logo">
            <img src="${path}/resources/images/logo.svg" alt="SPOMATCH">
            <span></span>
        </a>

        <nav>
            <a href="${path}/program/list">체육 프로그램</a>
            <a href="${path}/board/list">참여마당</a>
            <a href="${path}/notice/list">알림마당</a>
            <a href="${path}/dataroom/list">자료실</a>
        </nav>

        <div class="auth-links">
            <c:if test="${empty sessionScope.memberId}">
                <!-- 비로그인 상태 -->
                <a href="${path}/login">로그인</a>
                <span>|</span>
                <a href="${path}/register">회원가입</a>
            </c:if>
            <c:if test="${not empty sessionScope.memberId}">
                <!-- 로그인 상태 -->
                <span class="member-name">${sessionScope.memberName}님</span>
                <span>|</span>
                <a href="${path}/member/liked-programs" class="wishlist-link">찜 목록</a>
                <span>|</span>
                <form id="logoutForm" action="${path}/logout" method="post" style="display: inline;">
                    <a href="#" onclick="logout(event)">로그아웃</a>
                </form>
            </c:if>
        </div>
    </div>
</header>
<script>
    function logout(event) {
        event.preventDefault();
        if(confirm('로그아웃 하시겠습니까?')) {
            document.getElementById('logoutForm').submit();
        }
    }
</script>