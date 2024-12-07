$(document).ready(function() {
    $.ajax({
        url: '/program/api/programs/recommended',
        type: 'GET',
        success: function(programs) {
            const $container = $('#popularPrograms');
            if (!programs || programs.length === 0) {
                console.log('인기 프로그램이 없습니다.');
                return;
            }
            programs.forEach(function(program) {
                const $program = $(`
                   <div class="popular-program bg-gray-50 p-4 rounded-lg transition-all hover:shadow-md cursor-pointer" 
                        onclick="location.href='/program/detail/${program.id}'">
                       <h3 class="font-bold text-lg text-gray-800 mb-2">${program.programName || '제목 없음'}</h3>
                       <p class="text-sm text-gray-600 mb-2">${program.facilityName || '시설명 없음'}</p>
                       <p class="text-sm text-gray-600 mb-2">${program.cityName || ''} ${program.districtName || ''}</p>
                       <div class="flex justify-between items-center text-sm">
                           <span class="text-blue-600">${program.programPrice ? `${program.programPrice}원` : '가격 정보 없음'}</span>
                           <span class="bg-blue-100 text-blue-800 px-2 py-1 rounded">
                               ${(new Date(program.programEndDate).getTime() >= Date.now()) ? '진행중' : '마감'}
                           </span>
                       </div>
                   </div>
               `);
                $container.append($program);
                console.log($program);
            });
        },
        error: function(xhr) {
            console.error('인기 프로그램 로드 실패:', xhr.status, xhr.responseText);
        }
    });
});
