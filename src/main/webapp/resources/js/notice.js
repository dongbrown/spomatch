// notice.js

// 공지사항 검색 함수
function searchNotice() {
    const searchType = document.getElementById('searchType').value;
    const keyword = document.getElementById('searchKeyword').value;

    if (keyword.trim() === '') {
        alert('검색어를 입력해주세요.');
        return;
    }

    // TODO: 실제 검색 API 호출 및 결과 처리
    console.log('검색 유형:', searchType);
    console.log('검색어:', keyword);
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    // 테이블 행 클릭 시 상세 페이지로 이동
    const tableRows = document.querySelectorAll('.notice-table tbody tr');
    tableRows.forEach(row => {
        row.addEventListener('click', function() {
            const noticeId = this.cells[0].textContent;
            location.href = `notice-detail.jsp?id=${noticeId}`;
        });
    });

    // 페이지네이션 클릭 이벤트
    const pageLinks = document.querySelectorAll('.page-link');
    pageLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const page = this.textContent;
            loadPage(page);
        });
    });
});

// 페이지 로드 함수
function loadPage(page) {
    // TODO: 실제 페이지 데이터 로드 API 호출 및 결과 처리
    console.log('페이지 로드:', page);
}

// 글쓰기 권한 체크
function checkWritePermission() {
    // TODO: 실제 사용자 권한 체크 로직 구현
    return true; // 임시로 true 반환
}

// 날짜 포맷팅 함수
function formatDate(dateStr) {
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}