$(document).ready(function() {
    // 검색 폼 제출
    $('#searchForm').on('submit', function(e) {
        e.preventDefault();
        loadPrograms(1);
    });

    // 정렬 변경
    $('#sortBy').on('change', function() {
        loadPrograms(1);
    });

    // 페이지 클릭
    $('.page-link').on('click', function(e) {
        e.preventDefault();
        const page = $(this).data('page');
        loadPrograms(page);
    });

    // 프로그램 카드 클릭
    $('.program-card').on('click', function() {
        const programId = $(this).data('program-id');
        location.href = `/program/detail/${programId}`;
    });

    // 프로그램 목록 로드
    function loadPrograms(page) {
        const formData = new FormData($('#searchForm')[0]);
        formData.append('page', page);
        formData.append('sortBy', $('#sortBy').val());

        $.ajax({
            url: '/program/api/list',
            type: 'GET',
            data: Object.fromEntries(formData),
            success: function(response) {
                updateProgramList(response);
                updatePagination(response.paging);
            },
            error: function(xhr) {
                showAlert('프로그램 목록을 불러오는데 실패했습니다.');
            }
        });
    }

    // 프로그램 목록 업데이트
    function updateProgramList(response) {
        const programList = $('.program-list');
        programList.empty();

        response.programs.forEach(program => {
            const card = `
                <div class="program-card" data-program-id="${program.id}">
                    <div class="program-info">
                        <h3>${program.programName}</h3>
                        <p class="facility">${program.facilityName}</p>
                        <p class="location">${program.cityName} ${program.districtName}</p>
                        <p class="schedule">
                            ${program.programOperationDays} ${program.programOperationTime}
                        </p>
                        <p class="price">${formatPrice(program.programPrice)}원</p>
                        <p class="target">${program.programTargetName}</p>
                    </div>
                </div>
            `;
            programList.append(card);
        });
    }

    // 페이징 업데이트
    function updatePagination(paging) {
        const pagination = $('.pagination');
        pagination.empty();

        if (paging.hasPrevious) {
            pagination.append(`
                <a href="#" class="page-link" data-page="${paging.previousPageBlock}">&laquo;</a>
            `);
        }

        for (let i = paging.startPage; i <= paging.endPage; i++) {
            pagination.append(`
                <a href="#" class="page-link ${i === paging.page ? 'active' : ''}" 
                   data-page="${i}">${i}</a>
            `);
        }

        if (paging.hasNext) {
            pagination.append(`
                <a href="#" class="page-link" data-page="${paging.nextPageBlock}">&raquo;</a>
            `);
        }

        // 페이지 클릭 이벤트 재바인딩
        $('.page-link').on('click', function(e) {
            e.preventDefault();
            const page = $(this).data('page');
            loadPrograms(page);
        });
    }
});