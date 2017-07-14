package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

/**
 * Created by Phil on 9/25/2016.
 */
public class PayloadReader<T> {


    private final Class<T> dtoType;
    public PayloadReader(Class<T> dtoType) {
        this.dtoType = dtoType;
    }

    public T extractSingleItem(PayloadEnvelope<T> payloadEnvelope) throws GobiiWebException {

        T returnVal;

        if (null != payloadEnvelope) {

            if (null != payloadEnvelope.getPayload()) {

                if (null != payloadEnvelope.getPayload().getData()) {

                    if (1 == payloadEnvelope.getPayload().getData().size()) {

                        if( payloadEnvelope.getPayload().getData().get(0).getClass() == this.dtoType) {
                            returnVal = payloadEnvelope.getPayload().getData().get(0);
                        } else {
                            throw new GobiiWebException(GobiiStatusLevel.VALIDATION,
                                    GobiiValidationStatusType.BAD_REQUEST,
                                    "The enclosed payload data item type ("
                                            + payloadEnvelope.getPayload().getData().get(0).getClass()
                                            +") does not match the intended type("
                                            + this.dtoType
                                            +")");
                        }

                    } else {
                        throw new GobiiWebException(GobiiStatusLevel.VALIDATION,
                                GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                "Request payload data does not contain exactly one item");
                    }

                } else {
                    throw new GobiiWebException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "Request payload does not contain a data collection");

                }

            } else {
                throw new GobiiWebException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                        "Request payload envelope does not contain a payload member");
            }


        } else {
            throw new GobiiWebException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                    "Request payload envelope is null");
        }

        return returnVal;
    }

}
