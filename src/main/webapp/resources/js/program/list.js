$(document).ready(function() {
    // 정렬 상태 저장소에서 불러오기
    const savedSortBy = localStorage.getItem('programSortBy');
    if (savedSortBy) {
        $('#sortBy').val(savedSortBy);
    }

    // 검색 폼 제출
    $('#searchForm').on('submit', function(e) {
        e.preventDefault();
        loadPrograms(1);
    });

    // 검색 폼 초기화
    $('#searchForm').on('reset', function(e) {
        setTimeout(() => {
            localStorage.removeItem('programSortBy');
            $('#sortBy').val('latest');
            loadPrograms(1);
        }, 100);
    });

    // 정렬 변경
    $('#sortBy').on('change', function() {
        const selectedValue = $(this).val();
        localStorage.setItem('programSortBy', selectedValue);
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
        // 로딩 표시
        showLoading();

        // 검색 파라미터 구성
        const searchParams = {
            cityName: $('select[name="cityName"]').val(),
            programTargetName: $('select[name="programTargetName"]').val(),
            events: [$('select[name="event"]').val()].filter(Boolean), // 빈 값 제거
            weekdays: [],
            minPrice: $('input[name="minPrice"]').val() || null,
            maxPrice: $('input[name="maxPrice"]').val() || null,
            sortBy: $('#sortBy').val(),
            page: page,
            size: 9
        };

        // 체크된 요일 수집
        $('input[name="weekdays"]:checked').each(function() {
            searchParams.weekdays.push($(this).val());
        });

        $.ajax({
            url: '/program/api/list',
            type: 'GET',
            data: searchParams,
            traditional: true, // 배열 파라미터 전송을 위해 필요
            success: function(response) {
                if (response) {
                    updateProgramList(response);
                    if (response.paging) {
                        updatePagination(response.paging);
                    }
                    maintainFilterSelections(searchParams);
                    // URL 업데이트
                    updateURL(searchParams);
                } else {
                    showAlert('데이터 형식이 올바르지 않습니다.');
                }
                hideLoading();
            },
            error: function(xhr) {
                showAlert('프로그램 목록을 불러오는데 실패했습니다.');
                console.error('Error:', xhr);
                hideLoading();
            }
        });
    }

    // 프로그램 목록 업데이트
    function updateProgramList(response) {
        const programList = $('.program-list');
        programList.empty();

        if (response.programs && response.programs.length > 0) {
            const fragment = document.createDocumentFragment();

            response.programs.forEach(program => {
                const selectedEvent = $('select[name="event"]').val();
                let programName = program.programName || '';

                // 선택된 종목이 있으면 하이라이트 처리
                if (selectedEvent && programName.includes(selectedEvent)) {
                    programName = programName.replace(
                        new RegExp(selectedEvent, 'g'),
                        `<span class="highlight">${selectedEvent}</span>`
                    );
                }

                const card = document.createElement('div');
                card.className = 'program-card';
                card.dataset.programId = program.id || '';
                card.innerHTML = `
                    <div class="program-info">
                        <h3>${programName}</h3>
                        <p class="facility">${program.facilityName || ''}</p>
                        <p class="location">${program.cityName || ''} ${program.districtName || ''}</p>
                        <p class="schedule">
                            ${program.programOperationDays || ''} ${program.programOperationTime || ''}
                        </p>
                        <p class="price">${formatPrice(program.programPrice || 0)}원</p>
                        <p class="target">${program.programTargetName || ''}</p>
                    </div>
                `;

                fragment.appendChild(card);
            });

            programList.append(fragment);

            // 프로그램 카드 클릭 이벤트 재바인딩
            $('.program-card').on('click', function() {
                const programId = $(this).data('program-id');
                location.href = `/program/detail/${programId}`;
            });
        } else {
            programList.append('<p class="no-results">검색 결과가 없습니다.</p>');
        }
    }

    // 페이징 업데이트
    function updatePagination(paging) {
        const pagination = $('.pagination');
        pagination.empty();

        const makePageLink = (page, text, title = '') => {
            return `<a href="#" class="page-link ${page === paging.currentPage ? 'active' : ''}" 
                       data-page="${page}" ${title ? `title="${title}"` : ''}>${text}</a>`;
        };

        // 맨 앞으로 가기 버튼
        if (paging.currentPage > 1) {
            pagination.append(makePageLink(1, '<<', '맨 앞으로'));
        }

        // 이전 페이지 그룹으로 가기
        if (paging.hasPrevious) {
            pagination.append(makePageLink(paging.startPage - 1, '<', '이전'));
        }

        // 페이지 번호
        for (let i = paging.startPage; i <= paging.endPage; i++) {
            pagination.append(makePageLink(i, i));
        }

        // 다음 페이지 그룹으로 가기
        if (paging.hasNext) {
            pagination.append(makePageLink(paging.endPage + 1, '>', '다음'));
        }

        // 맨 뒤로 가기 버튼
        if (paging.currentPage < paging.totalPages) {
            pagination.append(makePageLink(paging.totalPages, '>>', '맨 뒤로'));
        }

        // 페이지 클릭 이벤트 재바인딩
        $('.page-link').on('click', function(e) {
            e.preventDefault();
            const page = $(this).data('page');
            loadPrograms(page);
            // 페이지 상단으로 스크롤
            $('html, body').animate({ scrollTop: 0 }, 300);
        });
    }

    // 가격 포맷팅
    function formatPrice(price) {
        return new Intl.NumberFormat('ko-KR').format(price);
    }

    // 필터 선택 상태 유지
    function maintainFilterSelections(params) {
        if (params.cityName) {
            $('select[name="cityName"]').val(params.cityName);
        }
        if (params.programTargetName) {
            $('select[name="programTargetName"]').val(params.programTargetName);
        }
        if (params.events && params.events.length > 0) {
            $('select[name="event"]').val(params.events[0]);
        }
        if (params.weekdays) {
            $('input[name="weekdays"]').prop('checked', false);
            params.weekdays.forEach(day => {
                $(`input[name="weekdays"][value="${day}"]`).prop('checked', true);
            });
        }
        if (params.minPrice) {
            $('input[name="minPrice"]').val(params.minPrice);
        }
        if (params.maxPrice) {
            $('input[name="maxPrice"]').val(params.maxPrice);
        }

        // localStorage에서 정렬 상태 불러오기
        const savedSortBy = localStorage.getItem('programSortBy');
        if (savedSortBy) {
            $('#sortBy').val(savedSortBy);
        }
    }

    // URL 업데이트
    function updateURL(params) {
        const urlParams = new URLSearchParams();
        Object.entries(params).forEach(([key, value]) => {
            if (value && (Array.isArray(value) ? value.length > 0 : true)) {
                if (Array.isArray(value)) {
                    value.forEach(v => urlParams.append(key, v));
                } else {
                    urlParams.set(key, value);
                }
            }
        });
        window.history.replaceState({}, '', `${window.location.pathname}?${urlParams.toString()}`);
    }

    // 로딩 표시
    function showLoading() {
        if (!$('.loading-overlay').length) {
            $('body').append('<div class="loading-overlay"><div class="loading-spinner"></div></div>');
        }
        $('.loading-overlay').fadeIn(200);
    }

    // 로딩 숨기기
    function hideLoading() {
        $('.loading-overlay').fadeOut(200);
    }

    // 알림 표시
    function showAlert(message) {
        alert(message);
    }

    // URL 파라미터로 초기 검색 조건 설정
    function initializeFromURL() {
        const urlParams = new URLSearchParams(window.location.search);
        const searchParams = {
            cityName: urlParams.get('cityName'),
            programTargetName: urlParams.get('programTargetName'),
            events: urlParams.getAll('events'),
            weekdays: urlParams.getAll('weekdays'),
            minPrice: urlParams.get('minPrice'),
            maxPrice: urlParams.get('maxPrice'),
            sortBy: urlParams.get('sortBy') || localStorage.getItem('programSortBy') || 'latest'
        };
        maintainFilterSelections(searchParams);
    }

    // 초기화
    initializeFromURL();
    loadPrograms(1);
});