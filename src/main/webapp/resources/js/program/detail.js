$(document).ready(function() {
    const programId = window.location.pathname.split('/').pop();

    // 조회수 증가
    $.post(`/program/api/view/${programId}`);

    // 찜하기 버튼 클릭
    $('#likeButton').on('click', function() {
        $.ajax({
            url: `/program/api/like/${programId}`,
            type: 'POST',
            success: function(response) {
                updateLikeButton(response);
            },
            error: function(xhr) {
                if (xhr.status === 401) { // Unauthorized
                    const response = JSON.parse(xhr.responseText);
                    showConfirmDialog(
                        response.message,
                        function() {
                            window.location.href = response.redirectUrl;
                        }
                    );
                } else {
                    showAlert('찜하기 처리에 실패했습니다.');
                }
            }
        });
    });

    // 공유하기 버튼 클릭
    $('#shareButton').on('click', function() {
        const url = window.location.href;
        navigator.clipboard.writeText(url)
            .then(function() {
                showAlert('링크가 복사되었습니다.');
            })
            .catch(function() {
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

    // 알림 다이얼로그 표시
    function showAlert(message) {
        alert(message);
    }

    // 확인 다이얼로그 표시
    function showConfirmDialog(message, callback) {
        if (confirm(message)) {
            callback();
        }
    }
});