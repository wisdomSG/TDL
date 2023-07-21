


function start() {
    const auth = getToken();
    console.log("auth=", auth);
    if (auth === '') {

        $('#login-text').show();
        $('#signup-text').show();
    } else {
        $('#logout-text').show();
        $('#mypage-text').show();
        $('.postbox').show();
    }
}


function getToken() {
    let auth = Cookies.get('Authorization');
    if (auth === undefined || auth == null) {
        return '';
    }
    return auth;
}

