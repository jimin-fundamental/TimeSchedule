// main.js

// 메인 페이지 로드 시 실행되는 함수
window.onload = function() {
    // 서비스 배너 로드 등 초기화 작업 수행
    loadServiceBanner();
    // 로그인 버튼 클릭 이벤트 설정
    const loginButton = document.querySelector('.login-button');
    loginButton.addEventListener('click', function() {
        // 로그인 페이지로 이동
        window.location.href = 'page2.html';
    });
};

// 서비스 배너 로드 함수
function loadServiceBanner() {
    // 서비스 배너 로드 로직 작성
    // 예를 들어, AJAX 요청을 통해 서버에서 배너 데이터를 가져와서 화면에 표시하는 등의 처리
    // 아래는 예시로 배너를 콘솔에 출력하는 코드입니다.
    console.log('Service Banner Loaded');
}
