// signup.js

// 가입 완료 버튼 활성화 여부 체크
function checkSignupForm() {
    var password = document.getElementById('passwordInput').value;
    var confirmPassword = document.getElementById('confirmPasswordInput').value;
    var submitBtn = document.getElementById('submitBtn');

    if (password === confirmPassword) {
        submitBtn.disabled = false;
    } else {
        submitBtn.disabled = true;
    }
}

// 가입 완료 버튼 클릭 이벤트 설정
document.getElementById('submitBtn').addEventListener('click', function() {
    // 회원가입 로직 수행
    // 입력된 이메일, 비밀번호 등을 가져와서 처리
    var email = document.getElementById('emailInput').value;
    var password = document.getElementById('passwordInput').value;
    // 회원가입 처리 후 페이지 이동
    signup(email, password);
});

// 회원가입 함수
function signup(email, password) {
    // 회원가입 로직 작성
    // 예를 들어, 서버에 회원 정보를 저장하는 등의 처리
    // 성공 시 페이지 이동
    window.location.href = 'dashboard.html';
}
