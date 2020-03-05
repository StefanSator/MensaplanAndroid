package com.stefansator.mensaplan;

/**
 * Class holding information about the current User Session.
 * @author stefansator
 * @version 1.0
 */
public class UserSession {
    /** Session Token of the user. Needed for identification of the user to the backend service. */
    public static int SESSION_TOKEN = 0;

    /**
     * Set Session Token.
     * @param sessionToken Session Token to set.
     */
    public static void setSessionToken(int sessionToken) {
        SESSION_TOKEN = sessionToken;
    }

    /**
     * Get Session Token.
     * @return int Session Token.
     */
    public static int getSessionToken() {
        return SESSION_TOKEN;
    }

    /**
     * End Session of User.
     */
    public static void endSession() {
        SESSION_TOKEN = 0;
    }
}
