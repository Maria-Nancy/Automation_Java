package com.test;

public class EmailConfig {
    private static final String EMAIL_USERNAME_ENV = "EMAIL_USERNAME";
    private static final String EMAIL_PASSWORD_ENV = "EMAIL_PASSWORD";

    public static String getEmailUsername() {
        String username = System.getenv(EMAIL_USERNAME_ENV);
        if (username == null || username.isEmpty()) {
            throw new IllegalStateException("Email username not set in environment variables. Please set " + EMAIL_USERNAME_ENV);
        }
        return username;
    }

    public static String getEmailPassword() {
        String password = System.getenv(EMAIL_PASSWORD_ENV);
        if (password == null || password.isEmpty()) {
            throw new IllegalStateException("Email password not set in environment variables. Please set " + EMAIL_PASSWORD_ENV);
        }
        return password;
    }
}