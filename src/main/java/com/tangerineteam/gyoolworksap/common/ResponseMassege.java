package com.tangerineteam.gyoolworksap.common;

public enum ResponseMassege {
    BAD_CREDENTIALS("아이디 또는 비밀번호가 틀렸습니다.");


    private final String message;

    ResponseMassege(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
