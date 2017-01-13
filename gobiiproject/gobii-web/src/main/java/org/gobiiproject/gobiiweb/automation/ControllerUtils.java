package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Header;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Phil on 9/23/2016.
 */
public class ControllerUtils {

    public static void setHeaderResponse(Header header,
                                         HttpServletResponse response,
                                         HttpStatus successResponseCode,
                                         HttpStatus failureResponseCode) {

        if (header.getStatus().isSucceeded()) {
            response.setStatus(successResponseCode.value());
        } else {
            response.setStatus(failureResponseCode.value());
        }

    }

}
