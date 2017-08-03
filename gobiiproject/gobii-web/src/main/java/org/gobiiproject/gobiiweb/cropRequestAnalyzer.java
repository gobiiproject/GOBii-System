package org.gobiiproject.gobiiweb;

import org.gobiiproject.gobiimodel.config.ConfigSettings;

import org.gobiiproject.gobiimodel.config.GobiiCropConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Phil on 5/25/2016.
 */
public class CropRequestAnalyzer {

    private static Logger LOGGER = LoggerFactory.getLogger(CropRequestAnalyzer.class);
    private static ConfigSettings CONFIG_SETTINGS = new ConfigSettings();


    /***
     * Given a uri (for example, /gobii-maize/gobii/v1/contacts):
     *  1) Find the crop configuration that has gobii-maize as its context path;
     *  2) Set the cropId to the cropId for that context path
     * @param httpRequest
     * @return
     * @throws Exception
     */
    private static String getCropTypeFromUri(HttpServletRequest httpRequest) throws Exception {

        String returnVal = null;
        String errorMessage = null;

        if (null != httpRequest) {

            String requestUrl = httpRequest.getRequestURI();

            for (int idx = 0;
                 (idx < CONFIG_SETTINGS.getActiveCropConfigs().size()) && (returnVal == null);
                 idx++) {

                GobiiCropConfig currentCropConfig = CONFIG_SETTINGS.getActiveCropConfigs().get(idx);

                String rawContextPath = currentCropConfig
                        .getContextPath();

                // double check that context paths are unique
                if (CONFIG_SETTINGS
                        .getActiveCropConfigs()
                        .stream()
                        .filter(cc -> {
                            return cc.getContextPath().equals(rawContextPath);
                        })
                        .count() < 2) {

                    String contextPathWithoutSlashes = rawContextPath
                            .replace("/", "");


                    String candidateSegment = requestUrl
                            .replace("/", "")
                            .substring(0, contextPathWithoutSlashes.length());

                    if (candidateSegment.toLowerCase().equals(contextPathWithoutSlashes.toLowerCase())) {

                        returnVal = currentCropConfig.getGobiiCropType();
                    }


                } else {

                    errorMessage = "The context path is repeated in the configuration; context paths must be unique: " + rawContextPath;

                }
            } // iterate configurations

            // this is the condition if we simply couldn't find a config
            if( errorMessage == null && returnVal == null ) {
                errorMessage = "There is no active crop with the context path of th specified URL; ";
            }

            if (returnVal == null) {

                errorMessage += "; the cropId corresponding to the context path of the request url could not be set: " + requestUrl;

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
            // we used to have a "default" crop for this purpose but that approach
            // led to some really bad bugs.
            if (CONFIG_SETTINGS.getActiveCropTypes().size() <= 0) {
                String message = "Unable to initialize: there are no active crop types in the configuration";
                LOGGER.error(message);
                throw new Exception(message);
            }

            // so we just randomly use the first active crop in the list
            // again, this should only occur upon initialization, when ther eis no
            // http context; when there is, we will get the cropId from the url
            // (GobiiConfig's validator enforces that all cropIds must be contained
            // within all server context paths)
            returnVal = CONFIG_SETTINGS.getActiveCropTypes().get(0);
        }

        return returnVal;

    }


    public static String getGobiiCropType(HttpServletRequest httpRequest) throws Exception {

        String returnVal = CropRequestAnalyzer.getCropTypeFromUri(httpRequest);

        if (null == returnVal) {
            String message = "Unable to determine crop type from response or uri";
            LOGGER.error(message);
            throw new Exception(message);
        }

        return returnVal;
    }
}
