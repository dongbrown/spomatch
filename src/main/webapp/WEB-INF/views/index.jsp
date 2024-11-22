<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SAMPLE</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tailwindcss/2.2.19/tailwind.min.css">
    <style>
        .main-visual {
            height: 100vh;
            background-position: center;
            background-size: cover;
            position: relative;
        }
        .main-visual::after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0,0,0,0.3);
        }
        .menu-hover:hover {
            color: #4A90E2;
        }
        .calendar-cell {
            width: 14.28%;
            aspect-ratio: 1;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .photo-gallery img {
            transition: transform 0.3s ease;
        }
        .photo-gallery img:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />


<!-- 메인 비주얼 -->
<section class="main-visual relative">
    <img src="/api/placeholder/1920/1080" alt="메인 비주얼" class="w-full h-full object-cover">
    <div class="absolute inset-0 z-10 flex items-center px-4">
        <div class="container mx-auto text-white">
            <h1 class="text-5xl font-bold mb-4">
                샘플체육회와 함께하는<br>
                건강한 내일
            </h1>
            <p class="text-lg opacity-80 mb-8">
                모든 체육인과 함께 즐기며 만들어가는 스포츠를 통하여 우리의 성장을<br>
                문화가 되기까지 체육문화의 토대를 만들어 가겠습니다.
            </p>
        </div>
    </div>
</section>

<!-- 캘린더 섹션 -->
<section class="py-16 bg-gray-50">
    <div class="container mx-auto px-4">
        <div class="grid md:grid-cols-2 gap-12">
            <!-- 캘린더 -->
            <div class="bg-white p-6 rounded-lg shadow">
                <div class="flex justify-between items-center mb-4">
                    <h2 class="text-xl font-bold">2024 / 03</h2>
                    <div class="flex space-x-2">
                        <button class="p-2">&lt;</button>
                        <button class="p-2">&gt;</button>
                    </div>
                </div>
                <div class="grid grid-cols-7 gap-2">
                    <div class="calendar-cell text-red-500">일</div>
                    <div class="calendar-cell">월</div>
                    <div class="calendar-cell">화</div>
                    <div class="calendar-cell">수</div>
                    <div class="calendar-cell">목</div>
                    <div class="calendar-cell">금</div>
                    <div class="calendar-cell text-blue-500">토</div>
                </div>
            </div>

            <!-- 공지사항 -->
            <div class="bg-white p-6 rounded-lg shadow">
                <h2 class="text-xl font-bold mb-4">공지사항</h2>
                <ul class="space-y-4">
                    <li>
                        <span class="block text-gray-500">2024-03-19</span>
                        <a href="#" class="block hover:text-blue-600">제 2회 전국 체육대회</a>
                    </li>
                    <li>
                        <span class="block text-gray-500">2024-03-15</span>
                        <a href="#" class="block hover:text-blue-600">체육시설 이용 안내문</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</section>

<!-- 포토 갤러리 -->
<section class="py-16">
    <div class="container mx-auto px-4">
        <h2 class="text-2xl font-bold mb-8">포토 갤러리</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 photo-gallery">
            <div class="overflow-hidden rounded-lg">
                <img src="/api/placeholder/400/300" alt="갤러리1" class="w-full h-64 object-cover">
            </div>
            <div class="overflow-hidden rounded-lg">
                <img src="/api/placeholder/400/300" alt="갤러리2" class="w-full h-64 object-cover">
            </div>
            <div class="overflow-hidden rounded-lg">
                <img src="/api/placeholder/400/300" alt="갤러리3" class="w-full h-64 object-cover">
            </div>
        </div>
    </div>
</section>

<!-- Quick Menu -->
<section class="py-16 bg-gray-800 text-white">
    <div class="container mx-auto px-4">
        <div class="grid grid-cols-3 gap-8">
            <div class="text-center">
                <div class="w-16 h-16 mx-auto mb-4 bg-blue-500 rounded-full flex items-center justify-center">
                    <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
                    </svg>
                </div>
                <h3 class="text-lg font-bold">일정안내</h3>
            </div>
            <div class="text-center">
                <div class="w-16 h-16 mx-auto mb-4 bg-blue-500 rounded-full flex items-center justify-center">
                    <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z"/>
                    </svg>
                </div>
                <h3 class="text-lg font-bold">소통게시판</h3>
            </div>
            <div class="text-center">
                <div class="w-16 h-16 mx-auto mb-4 bg-blue-500 rounded-full flex items-center justify-center">
                    <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                    </svg>
                </div>
                <h3 class="text-lg font-bold">회원자료</h3>
            </div>
        </div>
    </div>
</section>

<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>