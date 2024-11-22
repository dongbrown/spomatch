$(document).ready(function() {
    const programId = window.location.pathname.split('/').pop();

    // 조회수 증가
    $.post(`/program/api/view/${programId}`);

    // 찜하기 버튼 클릭
    $('#likeButton').on('click', function() {
        const userId = 'tempUser'; // 실제로는 로그인한 사용자 ID를 사용해야 함
        $.ajax({
            url: `/program/api/like/${programId}`,
            type: 'POST',
            data: { userId: userId },
            success: function(isLiked) {
                const message = isLiked ? '찜하기가 추가되었습니다.' : '찜하기가 취소되었습니다.';
                showAlert(message);
                updateLikeButton(isLiked);
            },
            error: function(xhr) {
                showAlert('찜하기 처리에 실패했습니다.');
            }
        });
    });

    // 공유하기 버튼 클릭
    $('#shareButton').on('click', function() {
        const url = window.location.href;
        navigator.clipboard.writeText(url).then(function() {
            showAlert('링크가 복사되었습니다.');
        }).catch(function() {
            showAlert('링크 복사에 실패했습니다.');
        });
    });

    // 찜하기 버튼 상태 업데이트
    function updateLikeButton(isLiked) {
        const $likeButton = $('#likeButton');
        if (isLiked) {
            $likeButton.addClass('active').text('찜하기 취소');
        } else {
            $likeButton.removeClass('active').text('찜하기');
        }
    }
});