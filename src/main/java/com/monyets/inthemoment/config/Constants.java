package com.monyets.inthemoment.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String SETTING_TOKEN = "USER_TOKEN";

    public static final String DEFAULT_CHANNEL_ID = "C3NMEQF3Q";
    public static final String DEFAULT_USER_TOKEN = "xoxp-68007742017-146065674359-150641671345-80ec6f1bc8e2fa5cd3ccaa910e697a8c";
    public static final int MAX_MESSAGE_AGE = 3600000;

    public static final int TIME_ONE_HOUR = 3600 * 1000;

    private Constants() {
    }
}
