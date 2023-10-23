package com.wooreal.gravitygather.dto.user;

public class EmailVerificationResult {
    private boolean result;

    // 생성자
    private EmailVerificationResult(boolean result) {
        this.result = result;
    }

    // of 메소드
    public static EmailVerificationResult of(boolean result) {
        return new EmailVerificationResult(result);
    }

    // getter 메소드
    public boolean isResult() {
        return this.result;
    }
}