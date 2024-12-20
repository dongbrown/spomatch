$(document).ready(function() {
    // 정렬 상태 저장소에서 불러오기
    const savedSortBy = localStorage.getItem('programSortBy');
    if (savedSortBy) {
        $('#sortBy').val(savedSortBy);
    }

    // 필터 초기 상태 설정
    function initializeFilterState() {
        const isFilterCollapsed = localStorage.getItem('filterCollapsed') === 'true';
        if (isFilterCollapsed) {
            $('.search-filter').hide();
            $('.filter-toggle-btn').addClass('collapsed')
                .find('i').removeClass('fa-chevron-up').addClass('fa-chevron-down');
        }
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
            cityName: $('select[name="cityName"]').val() || null,
            programTargetName: $('select[name="programTargetName"]').val() || null,
            events: $('select[name="event"]').val() ? [$('select[name="event"]').val()] : [],
            weekdays: ['월', '화', '수', '목', '금', '토', '일'].filter(day =>
                $(`input[name="weekdays"][value="${day}"]:checked`).length > 0),
            minPrice: $('input[name="minPrice"]').val() || null,
            maxPrice: $('input[name="maxPrice"]').val() || null,
            sortBy: $('#sortBy').val() || 'createdAt',
            page: page,
            size: 9
        };
        console.log("검색 필터 데이터:", searchParams);

        // 체크된 요일 수집
        $('input[name="weekdays"]:checked').each(function() {
            searchParams.weekdays.push($(this).val());
        });

        $.ajax({
            url: '/program/api/list',
            type: 'GET',
            data: searchParams,
            traditional: true,
            success: function(response) {
                if (response) {
                    updateProgramList(response);
                    if (response.paging) {
                        updatePagination(response.paging);
                    }
                    maintainFilterSelections(searchParams);
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
    function updateProgramList(response) {
        const programList = $('.program-list');
        programList.empty();

        if (response.programs && response.programs.length > 0) {
            const facilitiesMap = new Map();

            // 시설별로 프로그램 그룹화
            response.programs.forEach(program => {
                if (!facilitiesMap.has(program.facilityName)) {
                    facilitiesMap.set(program.facilityName, {
                        facilityName: program.facilityName,
                        cityName: program.cityName,
                        districtName: program.districtName,
                        programs: []
                    });
                }
                facilitiesMap.get(program.facilityName).programs.push(program);
            });

            const fragment = document.createDocumentFragment();

            // 각 시설별 카드 생성
            facilitiesMap.forEach((facilityData) => {
                const facilityCard = document.createElement('div');
                facilityCard.className = 'program-card';

                if (facilityData.programs.length === 1) {
                    // 단일 프로그램 카드
                    const program = facilityData.programs[0];
                    facilityCard.dataset.programId = program.id;
                    facilityCard.innerHTML = `
                    <div class="program-info">
                        <div class="title-section">
                            <div class="program-title">${program.programName}</div>
                            <div class="view-count">
                                <i class="fas fa-eye"></i>
                                <span>${program.viewCount || 0}</span>
                            </div>
                        </div>
                        <p class="facility-name">${program.facilityName}</p>
                        <p class="location">${program.cityName} ${program.districtName}</p>
                        <p class="schedule">${program.programOperationDays || ''} ${program.programOperationTime || ''}</p>
                        <p class="price">${formatPrice(program.programPrice || 0)}원</p>
                        <p class="target">${program.programTargetName || ''}</p>
                    </div>
                `;
                } else {
                    // 다수 프로그램 카드
                    facilityCard.className = 'program-card facility-group';

                    // 시설 헤더 생성
                    const facilityHeader = document.createElement('div');
                    facilityHeader.className = 'facility-header';
                    facilityHeader.innerHTML = `
                    <div class="program-info">
                        <div class="title-section">
                            <div class="facility-title">${facilityData.facilityName}</div>
                            <button class="toggle-button">
                                <span class="toggle-icon">▼</span>
                            </button>
                        </div>
                        <p class="program-summary">${facilityData.programs.length}개의 프로그램</p>
                        <p class="location">${facilityData.cityName} ${facilityData.districtName}</p>
                    </div>
                `;

                    // 프로그램 목록 컨테이너 생성
                    const programsContainer = document.createElement('div');
                    programsContainer.className = 'programs-container hidden';

                    // 프로그램 리스트 래퍼 생성
                    const programsWrapper = document.createElement('div');
                    programsWrapper.className = 'programs-wrapper';
                    programsWrapper.dataset.currentPage = '0';
                    programsWrapper.dataset.totalPages = Math.ceil(facilityData.programs.length / 3);

                    // 프로그램 데이터 저장 (필요한 데이터만 선택하여 저장)
                    const programsData = facilityData.programs.map(program => ({
                        id: program.id,
                        programName: program.programName,
                        viewCount: program.viewCount || 0,
                        programOperationDays: program.programOperationDays || '',
                        programOperationTime: program.programOperationTime || '',
                        programPrice: program.programPrice || 0,
                        programTargetName: program.programTargetName || ''
                    }));
                    programsWrapper.setAttribute('data-programs', JSON.stringify(programsData));

                    // 첫 페이지 프로그램 렌더링
                    const programsList = document.createElement('div');
                    programsList.className = 'programs-list';

                    // 첫 3개 프로그램 추가
                    facilityData.programs.slice(0, 3).forEach(program => {
                        const programItem = document.createElement('div');
                        programItem.className = 'program-item';
                        programItem.dataset.programId = program.id;
                        programItem.innerHTML = `
                        <div class="program-info">
                            <div class="title-section">
                                <div class="program-title">${program.programName}</div>
                                <div class="view-count">
                                    <i class="fas fa-eye"></i>
                                    <span>${program.viewCount || 0}</span>
                                </div>
                            </div>
                            <p class="schedule">${program.programOperationDays || ''} ${program.programOperationTime || ''}</p>
                            <p class="price">${formatPrice(program.programPrice || 0)}원</p>
                            <p class="target">${program.programTargetName || ''}</p>
                        </div>
                    `;
                        programsList.appendChild(programItem);
                    });

                    programsWrapper.appendChild(programsList);

                    // 네비게이션 버튼 추가 (3개 초과시에만)
                    if (facilityData.programs.length > 3) {
                        const navButtons = document.createElement('div');
                        navButtons.className = 'program-nav-buttons';
                        navButtons.innerHTML = `
                        <button class="nav-button prev" disabled>❮</button>
                        <button class="nav-button next">❯</button>
                    `;
                        programsContainer.appendChild(programsWrapper);
                        programsContainer.appendChild(navButtons);
                    } else {
                        programsContainer.appendChild(programsWrapper);
                    }

                    facilityCard.appendChild(facilityHeader);
                    facilityCard.appendChild(programsContainer);
                }

                fragment.appendChild(facilityCard);
            });

            programList.append(fragment);

            // 이벤트 바인딩
            bindEvents();
        } else {
            programList.append('<p class="no-results">검색 결과가 없습니다.</p>');
        }
    }

// 이벤트 바인딩 함수
    function bindEvents() {
        $('.filter-toggle-btn').off('click').on('click', function() {
            const searchFilter = $('.search-filter');
            $(this).find('i').toggleClass('fa-chevron-up fa-chevron-down');
            $(this).toggleClass('collapsed');
            searchFilter.slideToggle(300);

            // 토글 상태 저장
            const isCollapsed = !searchFilter.is(':visible');
            localStorage.setItem('filterCollapsed', isCollapsed);
        });
        // 시설 헤더 클릭 이벤트
        $('.facility-header').off('click').on('click', function() {
            const container = $(this).parent().find('.programs-container');
            const icon = $(this).find('.toggle-icon');

            // 다른 모든 컨테이너 닫기
            $('.programs-container').not(container).slideUp(300);
            $('.toggle-icon').not(icon).removeClass('rotated');

            // 현재 컨테이너 토글
            container.slideToggle(300);
            icon.toggleClass('rotated');
        });

        // 네비게이션 버튼 클릭 이벤트
        $('.nav-button').off('click').on('click', function(e) {
            e.stopPropagation();
            const wrapper = $(this).closest('.programs-container').find('.programs-wrapper');
            const programsList = wrapper.find('.programs-list');

            try {
                const currentPage = parseInt(wrapper.data('currentPage') || 0);
                const totalPages = parseInt(wrapper.data('totalPages') || 0);
                const programs = JSON.parse(wrapper.attr('data-programs'));
                const direction = $(this).hasClass('prev') ? -1 : 1;
                const newPage = currentPage + direction;

                if (newPage >= 0 && newPage < totalPages) {
                    // 새 페이지의 프로그램들
                    const start = newPage * 3;
                    const pagePrograms = programs.slice(start, start + 3);

                    // 현재 목록 페이드 아웃
                    programsList.fadeOut(150, function() {
                        // 새 프로그램 목록 렌더링
                        programsList.empty();
                        pagePrograms.forEach(program => {
                            const programItem = document.createElement('div');
                            programItem.className = 'program-item';
                            programItem.dataset.programId = program.id;
                            programItem.innerHTML = `
                            <div class="program-info">
                                <div class="title-section">
                                    <div class="program-title">${program.programName}</div>
                                    <div class="view-count">
                                        <i class="fas fa-eye"></i>
                                        <span>${program.viewCount}</span>
                                    </div>
                                </div>
                                <p class="schedule">${program.programOperationDays} ${program.programOperationTime}</p>
                                <p class="price">${formatPrice(program.programPrice)}원</p>
                                <p class="target">${program.programTargetName}</p>
                            </div>
                        `;
                            // 프로그램 아이템에 페이드 인 효과 적용
                            $(programItem).css('opacity', '0');
                            programsList.append(programItem);
                            $(programItem).animate({ opacity: 1 }, 150);
                        });

                        // 새 목록 페이드 인
                        programsList.fadeIn(150);

                        // 프로그램 클릭 이벤트 재바인딩
                        bindProgramItemEvents();

                        // 현재 페이지 업데이트
                        wrapper.data('currentPage', newPage);

                        // 버튼 상태 업데이트
                        const container = wrapper.closest('.programs-container');
                        container.find('.nav-button.prev').prop('disabled', newPage === 0);
                        container.find('.nav-button.next').prop('disabled', newPage === totalPages - 1);

                        // 버튼 효과 적용
                        if (newPage === 0) {
                            container.find('.nav-button.prev').addClass('disabled');
                        } else {
                            container.find('.nav-button.prev').removeClass('disabled');
                        }
                        if (newPage === totalPages - 1) {
                            container.find('.nav-button.next').addClass('disabled');
                        } else {
                            container.find('.nav-button.next').removeClass('disabled');
                        }
                    });
                }
            } catch (error) {
                console.error('데이터 파싱 에러:', error);
            }
        });

        // 프로그램 아이템 클릭 이벤트 바인딩
        bindProgramItemEvents();

        // 네비게이션 버튼 호버 효과
        $('.nav-button').hover(
            function() {
                if (!$(this).prop('disabled')) {
                    $(this).css('transform', 'scale(1.1)');
                }
            },
            function() {
                $(this).css('transform', 'scale(1)');
            }
        );
    }

// 프로그램 아이템 클릭 이벤트 바인딩 함수
    function bindProgramItemEvents() {
        $('.program-card:not(.facility-group), .program-item').off('click').on('click', function(e) {
            e.stopPropagation();
            const programId = $(this).data('program-id');
            if (programId) {
                location.href = `/program/detail/${programId}`;
            }
        });
    }
// 페이징 업데이트
    function updatePagination(paging) {
        const pagination = $('.pagination');
        pagination.empty();

        if (!paging || !paging.totalPages) {
            return;
        }

        const makePageLink = (page, text, title = '') => {
            const isActive = page === paging.currentPage;
            const activeClass = isActive ? 'active' : '';
            return `<a href="#" class="page-link ${activeClass}" 
                   data-page="${page}" ${title ? `title="${title}"` : ''}>${text}</a>`;
        };

        const totalPages = paging.totalPages;
        const currentPage = paging.currentPage;
        const pageSize = 5;

        let startPage = Math.max(1, currentPage - Math.floor(pageSize / 2));
        let endPage = startPage + pageSize - 1;

        if (endPage > totalPages) {
            endPage = totalPages;
            startPage = Math.max(1, endPage - pageSize + 1);
        }

        if (currentPage > 1) {
            pagination.append(makePageLink(1, '<<', '처음'));
            pagination.append(makePageLink(currentPage - 1, '<', '이전'));
        }

        for (let i = startPage; i <= endPage; i++) {
            pagination.append(makePageLink(i, i));
        }

        if (currentPage < totalPages) {
            pagination.append(makePageLink(currentPage + 1, '>', '다음'));
            pagination.append(makePageLink(totalPages, '>>', '마지막'));
        }

        $('.page-link').on('click', function(e) {
            e.preventDefault();
            const page = $(this).data('page');
            loadPrograms(page);
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
    initializeFilterState();
    initializeFromURL();
    loadPrograms(1);
});