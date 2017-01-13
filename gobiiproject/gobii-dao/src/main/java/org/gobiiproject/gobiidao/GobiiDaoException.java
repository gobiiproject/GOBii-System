package org.gobiiproject.gobiidao;

/**
 * Created by Phil on 4/7/2016.
 */
public class GobiiDaoException extends Exception {

    public GobiiDaoException(String message) {
        super(message);
    }

    public GobiiDaoException(Exception e) {
        super(e);
    }
}
