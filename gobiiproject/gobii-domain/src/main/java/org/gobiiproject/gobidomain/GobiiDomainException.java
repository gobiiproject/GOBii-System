package org.gobiiproject.gobidomain;

/**
 * Created by Phil on 4/7/2016.
 */
public class GobiiDomainException extends Exception {

    public GobiiDomainException(Exception e) {
        super(e);
    }

    public GobiiDomainException(String message) {
        super(message);
    }
}
