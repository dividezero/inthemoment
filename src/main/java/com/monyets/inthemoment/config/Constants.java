package com.monyets.inthemoment.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String DEFAULT_CHANNEL_ID = "C3NMEQF3Q";
    public static final String DEFAULT_USER_TOKEN = "xoxp-68007742017-146065674359-151992147046-2e2c7cfc6647e3df70dc805e765fdb8a";
    public static final int MAX_MESSAGE_AGE = 3600000;

    public static final int TIME_ONE_HOUR = 3600 * 1000;

    private Constants() {
    }
}
