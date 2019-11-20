package com.stefansator.mensaplan;

public class UserSession {
    public static int SESSION_TOKEN = 0;

    public static void setSessionToken(int sessionToken) {
        SESSION_TOKEN = sessionToken;
    }

    public static int getSessionToken() {
        return SESSION_TOKEN;
    }

    public static void endSession() {
        SESSION_TOKEN = 0;
    }
}
