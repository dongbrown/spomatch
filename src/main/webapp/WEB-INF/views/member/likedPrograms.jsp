<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>찜한 프로그램 목록</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/likedPrograms.css">
</head>
<body>
<h1>찜한 프로그램 목록</h1>
<div class="program-list">
    <c:choose>
        <c:when test="${empty likedPrograms}">
            <p>찜한 프로그램이 없습니다.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="program" items="${likedPrograms}">
                <div class="program-item">
                    <h2>${program.programName} (${program.facilityName})</h2>
                    <p><strong>위치:</strong> ${program.cityName} ${program.districtName}</p>
                    <p><strong>기간:</strong> ${program.programBeginDate} ~ ${program.programEndDate}</p>
                    <p><strong>모집 인원:</strong> ${program.programRecruitNumber}명</p>
                    <p><strong>가격:</strong> ${program.programPrice} (${program.programPriceTypeName})</p>
                    <p><strong>전화번호:</strong> ${program.facilityPhoneNumber}</p>
                    <p><a href="${program.homepageUrl}" target="_blank">자세히 보기</a></p>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
<script>
    window.onload = function() {
        fetch('/member/liked-programs')
            .then(response => response.json())
            .then(data => {
                const listContainer = document.getElementById('program-list');
                if (data.length === 0) {
                    listContainer.innerHTML = '<p>찜한 프로그램이 없습니다.</p>';
                } else {
                    data.forEach(program => {
                        const item = document.createElement('div');
                        item.innerHTML = `
                            <h2>${program.programName} (${program.facilityName})</h2>
                            <p>위치: ${program.cityName} ${program.districtName}</p>
                            <p>기간: ${program.programBeginDate} ~ ${program.programEndDate}</p>
                            <p>모집 인원: ${program.programRecruitNumber}명</p>
                            <p>가격: ${program.programPrice} (${program.programPriceTypeName})</p>
                            <p><a href="${program.homepageUrl}" target="_blank">자세히 보기</a></p>
                        `;
                        listContainer.appendChild(item);
                    });
                }
            });
    };
</script>
<div id="program-list"></div>
