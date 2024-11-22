<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${program.programName}</title>
    <!-- CSS -->
    <link rel="stylesheet" href="/resources/css/common/style.css">
    <link rel="stylesheet" href="/resources/css/program/common.css">
    <link rel="stylesheet" href="/resources/css/program/detail.css">
    <!-- JavaScript -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY"></script>
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />
<div class="container">
    <div class="program-detail">
        <!-- 프로그램 기본 정보 -->
        <div class="program-header">
            <h1>${program.programName}</h1>
            <div class="program-image">
                <img src="/resources/images/program/${program.id}.jpg" alt="${program.programName}">
            </div>
            <div class="facility-info">
                <h3>${program.facility.facilityName}</h3>
                <p>${program.facility.facilityType}</p>
                <p>${program.facility.address}</p>
                <p>연락처: ${program.facility.contact}</p>
                <c:if test="${not empty program.facility.homepage}">
                    <p><a href="${program.facility.homepage}" target="_blank">홈페이지 바로가기</a></p>
                </c:if>
            </div>
        </div>

        <!-- 프로그램 상세 정보 -->
        <div class="program-info">
            <div class="info-group">
                <h3>프로그램 정보</h3>
                <p>대상: ${program.classInfo.targetName}</p>
                <p>요일: ${program.classInfo.weekdays}</p>
                <p>시간: ${program.classInfo.time}</p>
                <p>기간: ${program.classInfo.startDate} ~ ${program.classInfo.endDate}</p>
                <p>가격: ${program.classInfo.price}원</p>
                <p>모집인원: ${program.classInfo.currentParticipants} / ${program.classInfo.recruitNumber}명</p>
            </div>

            <c:if test="${not empty program.classInfo.educationGoal}">
                <div class="info-group">
                    <h3>교육 목표</h3>
                    <p>${program.classInfo.educationGoal}</p>
                </div>
            </c:if>

            <c:if test="${not empty program.facility.safetyManagement}">
                <div class="info-group">
                    <h3>안전관리</h3>
                    <p>${program.facility.safetyManagement}</p>
                </div>
            </c:if>

            <div class="info-group">
                <h3>시설 위치</h3>
                <div id="map" style="height: 400px;"></div>
            </div>
        </div>

        <!-- 버튼 영역 -->
        <div class="action-buttons">
            <button id="likeButton" class="btn-like" data-program-id="${program.id}">
                찜하기
            </button>
            <button id="shareButton" class="btn-share">공유하기</button>
            <button onclick="history.back()" class="btn-back">목록으로</button>
        </div>
    </div>
</div>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<script src="/resources/js/common.js"></script>
<script src="/resources/js/program/detail.js"></script>
<script>
    function initMap() {
        const mapContainer = document.getElementById('map');
    }

    initMap();
</script>
</body>
</html>