package org.gobiiproject.gobiimodel.utils;

import javax.sound.sampled.Line;

/**
 * Created by Phil on 5/24/2016.
 */
public class ExceptionUtils {

    public static String makeMessageFromException(Exception e) {

        String returnVal = "<no exception data";

        if (null != e) {


            String message = null;
            if (false == e.getClass().equals(NullPointerException.class)) {

                if (false == LineUtils.isNullOrEmpty(e.getMessage())) {
                    message = e.getMessage();
                }

            } else {
                message = "NullPointerException";
            }

            String stackTrace = null;
            if (null != e.getStackTrace()) {

                stackTrace = stringIfyStacktrace(e.getStackTrace());
            }

            String causeMessage = null;
            String causeStackTrace = null;
            if (null != e.getCause()) {

                if (false == LineUtils.isNullOrEmpty(e.getCause().getMessage())) {
                    causeMessage = e.getCause().getMessage();
                }

                if (null != e.getCause().getStackTrace()) {
                    causeStackTrace = stringIfyStacktrace(e.getCause().getStackTrace());

                }
            }

            returnVal = "MESSAGE: " + (null != message ? message : "<no message>") + ";\n "
                    + "EXCEPTION STACK: " + (null != stackTrace ? stackTrace : "<no stack trace>") + ";\n "
                    + "CAUSED BY: " + (null != causeMessage ? causeMessage : "<no cause message>") + ";\n "
                    + "CAUSED BY STACK: " + (null != causeStackTrace ? causeStackTrace : "<no cause stack trace>");
        }

        return returnVal;
    }

    public static String stringIfyStacktrace(StackTraceElement[] elements) {
        String returnVal = "<no stack trace>";

        if (null != elements) {
            for (int idx = 0; idx < elements.length; idx++) {
                StackTraceElement currentElement = elements[idx];
                returnVal += currentElement.getClassName()
                        + "::"
                        + currentElement.getMethodName()
                        + " (" + currentElement.getLineNumber() + ")"
                        + ";\n";
            }
        }

        return returnVal;
    }
}
