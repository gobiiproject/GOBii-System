package org.gobiiproject.gobiimodel.types;

/**
 * Created by Phil on 5/18/2016.
 */
public enum GobiiAutoLoginType {
    // order is meaningful -- do not change it
    // Amongst other issues, this is how the enum
    // is set up on the web client side. So if this enum changes,
    // it must change on the web client side
    USER_RUN_AS,
    USER_TEST,
    UNKNOWN
}
