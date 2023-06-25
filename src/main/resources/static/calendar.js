// calendar.js

// 날짜 칸 선택 이벤트 설정
var dateCells = document.getElementsByClassName('dateCell');
Array.from(dateCells).forEach(function(cell) {
    cell.addEventListener('click', function() {
        // 선택된 날짜에 해당하는 스케줄 페이지로 이동
        var date = cell.innerText;
        window.location.href = 'schedule.html?date=' + date;
    });
});
