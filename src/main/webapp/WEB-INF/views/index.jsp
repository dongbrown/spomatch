<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SAMPLE</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tailwindcss/2.2.19/tailwind.min.css">
    <link rel="stylesheet" href="/resources/css/main.css">
    <link rel="stylesheet" href="/resources/css/common/style.css">
    <link rel="stylesheet" href="/resources/css/program/common.css">
    <link rel="stylesheet" href="/resources/css/program/detail.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=1eruck1cfu"></script>
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />

<!-- 메인 비주얼 -->
<section class="main-visual">
    <div class="container mx-auto py-8">
        <div class="flex space-x-8">
            <!-- 왼쪽: 지도 영역 -->
            <div class="w-2/3 bg-white rounded-lg shadow-lg">
                <div id="map" class="h-[600px] rounded-lg"></div>
            </div>

            <!-- 오른쪽: 인기 프로그램 영역 -->
            <div class="w-1/3">
                <div class="bg-white rounded-lg shadow-lg p-6">
                    <h2 class="text-2xl font-bold mb-6 text-gray-800">인기 프로그램</h2>
                    <div class="space-y-4">
                        <div class="popular-program bg-gray-50 p-4 rounded-lg transition-all hover:shadow-md">
                            <h3 class="font-bold text-lg text-gray-800 mb-2">수영 강습 프로그램</h3>
                            <p class="text-sm text-gray-600 mb-2">올림픽 체육관</p>
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-blue-600">참여자 120명</span>
                                <span class="bg-blue-100 text-blue-800 px-2 py-1 rounded">진행중</span>
                            </div>
                        </div>

                        <div class="popular-program bg-gray-50 p-4 rounded-lg transition-all hover:shadow-md">
                            <h3 class="font-bold text-lg text-gray-800 mb-2">테니스 교실</h3>
                            <p class="text-sm text-gray-600 mb-2">시민 체육공원</p>
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-blue-600">참여자 85명</span>
                                <span class="bg-blue-100 text-blue-800 px-2 py-1 rounded">진행중</span>
                            </div>
                        </div>

                        <div class="popular-program bg-gray-50 p-4 rounded-lg transition-all hover:shadow-md">
                            <h3 class="font-bold text-lg text-gray-800 mb-2">요가 클래스</h3>
                            <p class="text-sm text-gray-600 mb-2">건강스포츠센터</p>
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-blue-600">참여자 95명</span>
                                <span class="bg-blue-100 text-blue-800 px-2 py-1 rounded">진행중</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- 텍스트 섹션 -->
<section class="bg-gray-50 py-12">
    <div class="container mx-auto">
        <div class="bg-white rounded-lg shadow-lg p-8">
            <h2 class="text-2xl font-bold mb-6">프로그램 소개</h2>
            <div class="prose max-w-none">
                <p class="mb-4">스포매치는 다양한 체육시설과 프로그램을 제공하여 여러분의 건강한 생활을 지원합니다.</p>
                <p class="mb-4">지도에서 원하시는 지역의 체육시설을 확인하고, 관심있는 프로그램에 참여해보세요.</p>
                <div class="grid grid-cols-3 gap-6 mt-8">
                    <div class="text-center">
                        <div class="text-4xl font-bold text-blue-600 mb-2">150+</div>
                        <div class="text-gray-600">등록된 시설</div>
                    </div>
                    <div class="text-center">
                        <div class="text-4xl font-bold text-blue-600 mb-2">300+</div>
                        <div class="text-gray-600">진행중인 프로그램</div>
                    </div>
                    <div class="text-center">
                        <div class="text-4xl font-bold text-blue-600 mb-2">5000+</div>
                        <div class="text-gray-600">월간 참여자</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<script src="/resources/js/map.js"></script>
</body>
</html>