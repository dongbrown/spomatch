<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${program.programName}</title>
    <!-- CSS -->
    <link rel="stylesheet" href="${path}/resources/css/common/style.css">
    <link rel="stylesheet" href="${path}/resources/css/program/common.css">
    <link rel="stylesheet" href="${path}/resources/css/program/detail.css">
    <!-- JavaScript -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<!-- Header -->
<c:import url="/WEB-INF/views/common/header.jsp" />

<!-- Main Content -->
<main>
    <div class="container">
        <div class="program-detail">
            <!-- 프로그램 기본 정보 -->
            <section class="program-header">
                <div class="program-title">
                    <h1>${program.programName}</h1>
<%--                    <div class="program-status">--%>
<%--                        <span class="status-badge ${program.status == '모집중' ? 'active' : ''}">${program.status}</span>--%>
<%--                    </div>--%>
                </div>

                <div class="program-image">
<%--                    <c:choose>--%>
<%--                        <c:when test="${not empty program.imageUrl}">--%>
<%--                            <img src="${program.imageUrl}" alt="${program.programName}"--%>
<%--                                 onError="this.onerror=null;this.src='/resources/images/program/default.jpg';">--%>
<%--                        </c:when>--%>
<%--                        <c:otherwise>--%>
                            <img src="/resources/images/program/default.jpeg" alt="기본 이미지" width="350" height="200">
                            <img src="/resources/images/program/default2.jpeg" alt="기본 이미지" width="350" height="200">
<%--                        </c:otherwise>--%>
<%--                    </c:choose>--%>
                </div>

                <div class="facility-info">
                    <h2>시설 정보</h2>
                    <div class="info-content">
                        <h3>${program.facility.facilityName}</h3>
                        <p class="facility-type">${program.facility.facilityType}</p>
                        <p class="facility-address">
                            <i class="fas fa-map-marker-alt"></i>
                            ${program.facility.address}
                        </p>
                        <p class="facility-contact">
                            <i class="fas fa-phone"></i>
                            연락처: ${program.facility.contact}
                        </p>
                        <c:if test="${not empty program.facility.homepage}">
                            <p class="facility-homepage">
                                <i class="fas fa-globe"></i>
                                <a href="${program.facility.homepage}" target="_blank"
                                   rel="noopener noreferrer">홈페이지 바로가기</a>
                            </p>
                        </c:if>
                    </div>
                </div>
            </section>

            <!-- 프로그램 상세 정보 -->
            <section class="program-info">
                <div class="info-group program-details">
                    <h2>프로그램 정보</h2>
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="label">대상</span>
                            <span class="value">${program.classInfo.targetName}</span>
                        </div>
                        <div class="info-item">
                            <span class="label">요일</span>
                            <span class="value">${program.classInfo.weekdays}</span>
                        </div>
                        <div class="info-item">
                            <span class="label">시간</span>
                            <span class="value">${program.classInfo.time}</span>
                        </div>
                        <div class="info-item">
                            <span class="label">기간</span>
                            <span class="value">${program.classInfo.startDate} ~ ${program.classInfo.endDate}</span>
                        </div>
                        <div class="info-item">
                            <span class="label">가격</span>
                            <span class="value price">${program.classInfo.price}원</span>
                        </div>
                        <div class="info-item">
                            <span class="label">모집인원</span>
                            <span class="value">${program.classInfo.currentParticipants} / ${program.classInfo.recruitNumber}명</span>
                        </div>
                    </div>
                </div>

                <c:if test="${not empty program.classInfo.educationGoal}">
                    <div class="info-group education-goal">
                        <h2>교육 목표</h2>
                        <div class="info-content">
                            <p>${program.classInfo.educationGoal}</p>
                        </div>
                    </div>
                </c:if>

                <c:if test="${not empty program.facility.safetyManagement}">
                    <div class="info-group safety-info">
                        <h2>안전관리</h2>
                        <div class="info-content">
                            <p>${program.facility.safetyManagement}</p>
                        </div>
                    </div>
                </c:if>

                <div class="info-group facility-location">
                    <h2>시설 위치</h2>
                    <div id="map" class="facility-map"></div>
                </div>
            </section>

            <!-- 버튼 영역 -->
            <section class="action-buttons">
                <button id="likeButton" class="btn btn-like" data-program-id="${program.id}">
                    <i class="far fa-heart"></i>
                    <span>찜하기</span>
                </button>
                <button id="shareButton" class="btn btn-share">
                    <i class="fas fa-share-alt"></i>
                    <span>공유하기</span>
                </button>
                <button onclick="history.back()" class="btn btn-back">
                    <i class="fas fa-arrow-left"></i>
                    <span>목록으로</span>
                </button>
            </section>
        </div>
    </div>
</main>

<!-- Footer -->
<c:import url="/WEB-INF/views/common/footer.jsp" />

<!-- Share Modal -->
<div id="shareModal" class="modal">
    <div class="modal-content">
        <h3>공유하기</h3>
        <div class="share-options">
            <button class="share-btn kakao">
                <i class="fas fa-comment"></i>
                카카오톡
            </button>
            <button class="share-btn facebook">
                <i class="fab fa-facebook-f"></i>
                페이스북
            </button>
            <button class="share-btn twitter">
                <i class="fab fa-twitter"></i>
                트위터
            </button>
            <button class="share-btn link">
                <i class="fas fa-link"></i>
                링크 복사
            </button>
        </div>
        <button class="modal-close">닫기</button>
    </div>
</div>

<!-- Font Awesome -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<!-- JavaScript -->
<script src="${path}/resources/js/common.js"></script>
<script src="${path}/resources/js/program/detail.js"></script>

</body>
</html>