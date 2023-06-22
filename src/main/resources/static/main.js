// main.js

// 메인 페이지 로드 시 실행되는 함수
window.onload = function() {
    // 서비스 배너 로드 등 초기화 작업 수행
    loadServiceBanner();
    // 로그인 버튼 클릭 이벤트 설정
    document.getElementById('loginBtn').addEventListener('click', function() {
        // 로그인 페이지로 이동
        window.location.href = 'login.html';
    });
};
