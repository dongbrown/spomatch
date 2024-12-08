<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>찜한 프로그램 목록</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${path}/resources/css/common/style.css">
    <link rel="stylesheet" href="${path}/resources/css/program/common.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        const contextPath = '${path}';  // JSP의 path 변수를 JavaScript 변수로 선언
    </script>
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="container">
        <h1 class="page-title">찜한 프로그램 목록</h1>

        <!-- 정렬 옵션 -->
        <div class="sort-options">
            <select id="sortBy" name="sortBy" class="form-select">
                <option value="latest">최신순</option>
                <option value="deadline">마감임박순</option>
                <option value="price">가격순</option>
            </select>
        </div>

        <!-- 프로그램 목록 -->
        <div class="program-list">
            <c:choose>
                <c:when test="${empty likedPrograms}">
                    <div class="no-items">
                        <i class="fas fa-heart-broken"></i>
                        <p>찜한 프로그램이 없습니다.</p>
                        <a href="${path}/program/list" class="btn btn-primary">프로그램 둘러보기</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="program" items="${likedPrograms}">
                        <!-- ID 값 확인을 위한 테스트 출력 -->
                        <p style="display:none;">Program ID: ${program.id}</p>

                        <div class="program-card" data-program-id="${program.id}" id="program-${program.id}">
                            <div class="program-info">
                                <div class="title-section">
                                    <div class="program-title" title="${program.programName}">
                                            ${program.programName}
                                    </div>
                                    <button class="like-btn active" onclick="unlikeProgram(${program.id})">
                                        <i class="fas fa-heart"></i>
                                    </button>
                                </div>
                                <p class="facility">${program.facilityName}</p>
                                <p class="location">${program.cityName} ${program.districtName}</p>
                                <div class="date-range">
                                    <i class="far fa-calendar-alt"></i>
                                        ${program.programBeginDate} ~ ${program.programEndDate}
                                </div>
                                <p class="schedule">${program.programOperationDays} ${program.programOperationTime}</p>
                                <p class="recruit">
                                    <i class="fas fa-users"></i>
                                    모집인원: ${program.programRecruitNumber}명
                                </p>
                                <div class="bottom-info">
                                    <p class="price">
                                        <i class="fas fa-won-sign"></i>
                                        <fmt:formatNumber value="${program.programPrice}" type="number"/>원
                                        <span class="price-type">(${program.programPriceTypeName})</span>
                                    </p>
                                    <a href="${program.homepageUrl}" target="_blank" class="detail-link">
                                        자세히 보기 <i class="fas fa-chevron-right"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<script>
    function unlikeProgram(programId) {
        if(!confirm('찜 목록에서 삭제하시겠습니까?')) {
            return;
        }

        // 먼저 카드를 화면에서 제거
        const cardElement = document.getElementById(`program-${programId}`);
        if (cardElement) {
            cardElement.remove();

            // 남은 카드 수 확인하고 필요시 빈 상태 메시지 표시
            const remainingCards = document.querySelectorAll('.program-card');
            if (remainingCards.length === 0) {
                const programList = document.querySelector('.program-list');
                programList.innerHTML = `
                <div class="no-items">
                    <i class="fas fa-heart-broken"></i>
                    <p>찜한 프로그램이 없습니다.</p>
                    <a href="${contextPath}/program/list" class="btn btn-primary">프로그램 둘러보기</a>
                </div>
            `;
            }
        }

        // 서버에 삭제 요청
        const url = contextPath + '/program/api/unlike/' + programId;
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            credentials: 'same-origin'
        })
            .then(response => response.json())
            .then(data => {
                if (!data.success) {
                    // 삭제 실패 시 페이지 새로고침하여 상태 복원
                    location.reload();
                    alert('처리 중 오류가 발생했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                location.reload();
                alert('처리 중 오류가 발생했습니다.');
            });
    }
    // 정렬 기능
    document.getElementById('sortBy').addEventListener('change', function() {
        const sortBy = this.value;
        const cards = Array.from(document.querySelectorAll('.program-card'));
        const container = document.querySelector('.program-list');

        cards.sort((a, b) => {
            switch(sortBy) {
                case 'latest':
                    return b.dataset.programId - a.dataset.programId;
                case 'deadline':
                    // 마감일 기준 정렬 로직 구현
                    return 0;
                case 'price':
                    const priceA = parseInt(a.querySelector('.price').textContent.replace(/[^0-9]/g, ''));
                    const priceB = parseInt(b.querySelector('.price').textContent.replace(/[^0-9]/g, ''));
                    return priceA - priceB;
            }
        });

        container.innerHTML = '';
        cards.forEach(card => container.appendChild(card));
    });
</script>

</body>
</html>