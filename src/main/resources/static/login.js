// login.js

// 확인 버튼 클릭 이벤트 설정
document.getElementById('confirmBtn').addEventListener('click', function() {
    // 로그인 로직 수행
    // 입력된 이메일과 비밀번호를 가져와서 처리
    var email = document.getElementById('emailInput').value;
    var password = document.getElementById('passwordInput').value;
    // 로그인 처리 후 페이지 이동
    login(email, password);
});

// 로그인 함수
function login(email, password) {
    // 로그인 로직 작성
    // 예를 들어, 서버와 통신하여 인증 처리 등을 수행
    // 성공 시 페이지 이동
    window.location.href = 'dashboard.html';
}
