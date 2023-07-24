$(document).ready(function () {
    start(); // start 함수를 호출합니다.
});
document.addEventListener("DOMContentLoaded", function () {
    start();
});

function start() {
    const auth = getToken();
    console.log("auth=", auth);
    if (auth === '') {
        document.getElementById('login-text').style.display = 'block';
        document.getElementById('signup-text').style.display = 'block';
        document.getElementById('logout-text').style.display = 'none';
        document.getElementById('mypage-text').style.display = 'none';
    } else {
        document.getElementById('login-text').style.display = 'none';
        document.getElementById('signup-text').style.display = 'none';
        document.getElementById('logout-text').style.display = 'block';
        document.getElementById('mypage-text').style.display = 'block';
        const postBoxes = document.getElementsByClassName('postbox');
        for (let i = 0; i < postBoxes.length; i++) {
            postBoxes[i].style.display = 'block';
        }
    }
}


function getToken() {
    let auth = getCookie('Authorization');
    if (auth === undefined || auth == null) {
        return '';
    }
    return auth;
}

function getCookie(cookieName) {
    var name = cookieName + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(';');
    for (var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i];
        while (cookie.charAt(0) == ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name) == 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}

//로그아웃
function logout() {
    let authCookie = getCookie('Authorization');
    console.log(authCookie);
    $.ajax({
        url: 'http://localhost:8080/tdl/user/logout',
        type: 'POST',
        headers: {
            'Authorization': authCookie
        },
        processData: false,
        contentType: false,
        success: function(response) {
            Cookies.remove('Authorization', {path: '/'}); // 쿠키삭제
            // 요청 성공 시 처리할 로직
            alert("로그아웃 성공 !");

            window.location.reload();

        },
        error: function(xhr, status, error) {
            console.log(xhr);
            alert("로그아웃 실패 !");
        }
    });
}