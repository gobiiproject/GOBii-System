package org.gobiiproject.gobiiweb;

import org.gobiiproject.gobiimodel.config.ConfigSettings;

import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/25/2016.
 */
public class CropRequestAnalyzer {

    private static Logger LOGGER = LoggerFactory.getLogger(CropRequestAnalyzer.class);
    private static ConfigSettings CONFIG_SETTINGS = new ConfigSettings();


    private static HttpServletRequest getRequest() {

        HttpServletRequest returnVal = null;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (null != requestAttributes && requestAttributes instanceof ServletRequestAttributes) {
            returnVal = ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        return returnVal;
    }


    private static String getCropTypeFromHeaders(HttpServletRequest httpRequest) throws Exception {

        String returnVal = null;

        String errorMessage = null;

        if (null != httpRequest) {

            returnVal = httpRequest.getHeader(GobiiHttpHeaderNames.HEADER_GOBII_CROP);

            if (LineUtils.isNullOrEmpty(returnVal)) {

                // this is not an exception because if we didn't get the crop ID from
                // the header we'll infer it from uri
                LOGGER.error("Request did not include the response "
                        + GobiiHttpHeaderNames.HEADER_GOBII_CROP);
            }

        } else {
            throw new Exception("Unable to retreive servlet request for crop type analysis from response");

        }

        return returnVal;

    }

    public static String getDefaultCropType() {

        String returnVal = null;

        try {
            returnVal = CONFIG_SETTINGS.getDefaultGobiiCropType();

        } catch (Exception e) {
            LOGGER.error("Error retrieving config settings to find default crop", e);
        }

        return returnVal;
    }


    private static String getCropTypeFromUri(HttpServletRequest httpRequest) throws Exception {

        String returnVal = null;

        String errorMessage = null;

        if (null != httpRequest) {
            String requestUrl = httpRequest.getRequestURI();

            List<String> matchedCrops =
                    CONFIG_SETTINGS
                            .getActiveCropTypes()
                            .stream()
                            .filter(c -> requestUrl.toLowerCase().contains(c.toString().toLowerCase()))
                            .collect(Collectors.toList());

            if (matchedCrops.size() > 0) {

                if (1 == matchedCrops.size()) {
                    returnVal = matchedCrops.get(0);
                } else {
                    errorMessage = "The current url ("
                            + requestUrl
                            + ") matched more than one one crop; the service app root must contain only one crop ID";
                }

            } else {

                errorMessage = "The current url ("
                        + requestUrl
                        + ") did not match any crops; ; service app root must contain one, and only one, crop ID";
            }

        } else {
            errorMessage = "Unable to retreive servlet request for crop type analysis";
        }

        if (errorMessage != null) {
            LOGGER.error(errorMessage);
            throw new Exception(errorMessage);
        }

        return returnVal;
    }

    public static String getGobiiCropType() throws Exception {

        String returnVal = null;

        HttpServletRequest httpRequest = null;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (null != requestAttributes && requestAttributes instanceof ServletRequestAttributes) {
            httpRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        if (httpRequest != null) {
            returnVal = CropRequestAnalyzer.getGobiiCropType(httpRequest);
        } else {
            // this will be the case when the server is initializing
            returnVal = CropRequestAnalyzer.getDefaultCropType();
        }

        return returnVal;

    }


    public static String getGobiiCropType(HttpServletRequest httpRequest) throws Exception {

        String returnVal = CropRequestAnalyzer.getCropTypeFromHeaders(httpRequest);

        if (null == returnVal) {

            returnVal = CropRequestAnalyzer.getCropTypeFromUri(httpRequest);

            if (null == returnVal) {

                returnVal = CropRequestAnalyzer.getDefaultCropType();

                if (null == returnVal) {

                    LOGGER.error("Unable to determine crop type from response or uri; setting crop type to "
                            + returnVal
                            + " database connectioins will be made accordingly");
                }
            }
        }

        return returnVal;
    }
}
