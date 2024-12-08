<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>프로그램 목록</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${path}/resources/css/common/style.css">
    <link rel="stylesheet" href="${path}/resources/css/program/common.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="container">
        <!-- 검색 필터 영역 -->
        <div class="search-filter">
            <form id="searchForm">
                <div class="filter-row">
                    <!-- 지역 선택 -->
                    <div class="filter-group">
                        <label>지역</label>
                        <select name="cityName" class="form-select">
                            <option value="">전체</option>
                            <c:forEach items="${result.filters.cities}" var="city">
                                <option value="${city}">${city}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- 연령대 선택 -->
                    <div class="filter-group">
                        <label>대상</label>
                        <select name="programTargetName" class="form-select">
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
                        <select name="event" class="form-select">
                            <option value="">전체</option>
                            <option value="수영">수영</option>
                            <option value="골프">골프</option>
                            <option value="탁구">탁구</option>
                            <option value="배드민턴">배드민턴</option>
                            <option value="댄스">댄스</option>
                            <option value="필라테스">필라테스</option>
                            <option value="헬스">헬스</option>
                            <option value="테니스">테니스</option>
                            <option value="에어로빅">에어로빅</option>
                            <option value="기타">기타</option>
                        </select>
                    </div>
                </div>

                <div class="filter-row">
                    <!-- 요일 선택 -->
                    <div class="filter-group weekday-group">
                        <label>요일</label>
                        <div class="weekday-checkboxes">
                            <label class="weekday-label">
                                <input type="checkbox" name="weekdays" value="월">
                                <span>월</span>
                            </label>
                            <label class="weekday-label">
                                <input type="checkbox" name="weekdays" value="화">
                                <span>화</span>
                            </label>
                            <label class="weekday-label">
                                <input type="checkbox" name="weekdays" value="수">
                                <span>수</span>
                            </label>
                            <label class="weekday-label">
                                <input type="checkbox" name="weekdays" value="목">
                                <span>목</span>
                            </label>
                            <label class="weekday-label">
                                <input type="checkbox" name="weekdays" value="금">
                                <span>금</span>
                            </label>
                            <label class="weekday-label">
                                <input type="checkbox" name="weekdays" value="토">
                                <span>토</span>
                            </label>
                            <label class="weekday-label">
                                <input type="checkbox" name="weekdays" value="일">
                                <span>일</span>
                            </label>
                        </div>
                    </div>


                    <!-- 가격 범위 -->
                    <div class="filter-group">
                        <label>가격</label>
                        <div class="price-range">
                            <input type="number" name="minPrice" placeholder="최소가격" class="form-input">
                            <span class="price-separator">~</span>
                            <input type="number" name="maxPrice" placeholder="최대가격" class="form-input">
                        </div>
                    </div>
                </div>

                <div class="filter-actions">
                    <button type="submit" class="btn btn-primary">검색</button>
                    <button type="reset" class="btn btn-secondary">초기화</button>
                </div>
            </form>
        </div>

        <!-- 정렬 옵션 -->
        <div class="sort-options">
            <select id="sortBy" name="sortBy" class="form-select">
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
                        <div class="title-section">
                            <div class="program-title" title="${program.programName}">${program.programName}</div>
                            <div class="view-count">
                                <i class="fas fa-eye"></i>
                                <span><fmt:formatNumber value="${program.viewCount}" pattern="#,###"/></span>
                            </div>
                        </div>
                        <p class="facility">${program.facilityName}</p>
                        <p class="location">${program.cityName} ${program.districtName}</p>
                        <p class="schedule">${program.programOperationDays} ${program.programOperationTime}</p>
                        <p class="price"><fmt:formatNumber value="${program.programPrice}" type="number"/>원</p>
                        <p class="target">${program.programTargetName}</p>
                    </div>
                </div>
            </c:forEach>
        </div>

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
</main>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<script src="${path}/resources/js/common.js"></script>
<script src="${path}/resources/js/program/list.js"></script>
</body>
</html>