package com.tangerineteam.gyoolworksap.common;

public enum ResponseMassege {
    BAD_CREDENTIALS("아이디 또는 비밀번호가 틀렸습니다.")
    ,TOKEN_EXPIRED("access 토큰만료")
    ,LOGGED_OUT("logout")
    ,TOKEN_INVALID("유효하지 않은 토큰");

    private final String message;

    ResponseMassege(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
