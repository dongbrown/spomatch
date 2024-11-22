<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>프로그램 목록</title>
    <!-- CSS -->
    <link rel="stylesheet" href="/resources/css/common/style.css">
    <link rel="stylesheet" href="/resources/css/program/common.css">
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />

<div class="container">
    <!-- 검색 필터 영역 -->
    <div class="search-filter">
        <form id="searchForm">
            <!-- 지역 선택 -->
            <div class="filter-group">
                <label>지역</label>
                <select name="cityName">
                    <option value="">전체</option>
                    <c:forEach items="${result.filters.cities}" var="city">
                        <option value="${city}">${city}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- 연령대 선택 -->
            <div class="filter-group">
                <label>대상</label>
                <select name="programTargetName">
                    <option value="">전체</option>
                    <option value="유아">유아</option>
                    <option value="청소년">청소년</option>
                    <option value="성인">성인</option>
                    <option value="노인">노인</option>
                </select>
            </div>

            <!-- 종목 선택 -->
            <div class="filter-group">
                <label>종목</label>
                <input type="checkbox" name="events" value="수영"> 수영
                <input type="checkbox" name="events" value="골프"> 골프
                <input type="checkbox" name="events" value="탁구"> 탁구
                <input type="checkbox" name="events" value="배드민턴"> 배드민턴
                <input type="checkbox" name="events" value="댄스"> 댄스
                <input type="checkbox" name="events" value="필라테스"> 필라테스
                <input type="checkbox" name="events" value="헬스"> 헬스
                <input type="checkbox" name="events" value="테니스"> 테니스
                <input type="checkbox" name="events" value="에어로빅"> 에어로빅
                <input type="checkbox" name="events" value="기타"> 기타
            </div>

            <!-- 요일 선택 -->
            <div class="filter-group">
                <label>요일</label>
                <input type="checkbox" name="weekdays" value="월"> 월
                <input type="checkbox" name="weekdays" value="화"> 화
                <input type="checkbox" name="weekdays" value="수"> 수
                <input type="checkbox" name="weekdays" value="목"> 목
                <input type="checkbox" name="weekdays" value="금"> 금
                <input type="checkbox" name="weekdays" value="토"> 토
                <input type="checkbox" name="weekdays" value="일"> 일
            </div>

            <!-- 가격 범위 -->
            <div class="filter-group">
                <label>가격</label>
                <input type="number" name="minPrice" placeholder="최소가격">
                ~
                <input type="number" name="maxPrice" placeholder="최대가격">
            </div>

            <button type="submit">검색</button>
        </form>
    </div>

    <!-- 정렬 옵션 -->
    <div class="sort-options">
        <select id="sortBy" name="sortBy">
            <option value="latest">최신순</option>
            <option value="popularity">인기순</option>
            <option value="deadline">마감임박순</option>
        </select>
    </div>

    <!-- 프로그램 목록 -->
    <div class="program-list">
        <c:forEach items="${result.programs}" var="program">
            <div class="program-card" data-program-id="${program.id}">
                <div class="program-info">
                    <h3>${program.programName}</h3>
                    <p class="facility">${program.facilityName}</p>
                    <p class="location">${program.cityName} ${program.districtName}</p>
                    <p class="schedule">
                            ${program.programOperationDays} ${program.programOperationTime}
                    </p>
                    <p class="price">${program.programPrice}원</p>
                    <p class="target">${program.programTargetName}</p>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- 페이징 -->
    <!-- 페이징 -->
    <div class="pagination">
        <c:if test="${result.paging.hasPrevious}">
            <a href="#" class="page-link" data-page="${result.paging.startPage - 1}">&laquo;</a>
        </c:if>

        <c:forEach begin="${result.paging.startPage}" end="${result.paging.endPage}" var="pageNum">
            <a href="#" class="page-link ${pageNum == result.paging.currentPage ? 'active' : ''}"
               data-page="${pageNum}">${pageNum}</a>
        </c:forEach>

        <c:if test="${result.paging.hasNext}">
            <a href="#" class="page-link" data-page="${result.paging.endPage + 1}">&raquo;</a>
        </c:if>
    </div>
</div>
<c:import url="/WEB-INF/views/common/footer.jsp" />

<!-- JavaScript -->
<script src="/resources/js/common.js"></script>
<script src="/resources/js/program/list.js"></script>
</body>
</html>