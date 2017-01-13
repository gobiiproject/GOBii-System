package org.gobiiproject.gobiiclient.core;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class DtoRequestProcessor<T extends DtoMetaData> {

    Logger LOGGER = LoggerFactory.getLogger(DtoRequestProcessor.class);

    public T process(T dtoToProcess, Class<T> DtoType,
                     ControllerType controllerType,
                     ServiceRequestId requestId) throws Exception {

        String token = ClientContext.getInstance(null, false).getUserToken();
        String serviceDomain = ClientContext.getInstance(null, false).getCurrentCropDomain();
        Integer servicePort = ClientContext.getInstance(null, false).getCurrentCropPort();

        return this.process(dtoToProcess,
                DtoType,
                controllerType,
                requestId,
                serviceDomain,
                servicePort,
                token);

    }

    public T process(T dtoToProcess,
                     Class<T> DtoType,
                     ControllerType controllerType,
                     ServiceRequestId requestId,
                     String host,
                     Integer port,
                     String token) throws Exception {

        T returnVal = null;

        TypedRestRequest<T> typedRestRequest= new TypedRestRequest<>(host, port, DtoType);


        if (null == token || token.isEmpty()) {
            throw (new Exception("there is no user token; user must log in"));
        }

        String url = Urls.getRequestUrl(controllerType,
                requestId);

        returnVal = typedRestRequest.getTypedHtppResponseForDto(url,
                dtoToProcess,
                token);

        return returnVal;

    }

}
