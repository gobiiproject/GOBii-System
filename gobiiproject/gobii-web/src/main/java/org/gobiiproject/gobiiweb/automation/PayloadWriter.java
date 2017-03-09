package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Header;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.headerlesscontainer.DTOBase;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

/**
 * Created by Phil on 9/25/2016.
 */
//There fore, the generic type for this class extends DTOBase, so we are
//guaranteed it must have the getId() methd and the set of allowable actions
public class PayloadWriter<T extends DTOBase> {

    private final Class<T> dtoType;
    private HttpServletRequest httpServletRequest;
    private String gobiiWebVersion;

    public PayloadWriter(HttpServletRequest httpServletRequest,
                         Class<T> dtoType) throws Exception{
        this.dtoType = dtoType;
        this.httpServletRequest = httpServletRequest;

        this.gobiiWebVersion = GobiiVersionInfo.getVersion();
    }

    public void writeSingleItemForId(PayloadEnvelope<T> payloadEnvelope,
                                     RestUri restUri,
                                     T itemToWrite,
                                     String id) throws Exception {

        if ((null != itemToWrite) &&
                !LineUtils.isNullOrEmpty(id) &&
                (itemToWrite.getId() != null) &&
                (itemToWrite.getId() > 0)) {

            if (itemToWrite.getClass() == this.dtoType) {

                payloadEnvelope.getPayload().getData().add(itemToWrite);

//                String contextPath = this.httpServletRequest.getContextPath();
//                UriFactory uriFactory = new UriFactory(contextPath);
//                RestUri restUri = uriFactory.resourceByUriIdParam(serviceRequestId);
                restUri.setParamValue("id", id);
                //And hence we can create the link ehre

                String uri = restUri.makeUrl();
                Link link = new Link(uri, "Link to " + dtoType + ", id " + id);

                for (GobiiProcessType currentProcessType : itemToWrite.getAllowedProcessTypes()) {

                    switch (currentProcessType) {

                        case CREATE:
                            link.getMethods().add(RestMethodTypes.POST);
                            break;
                        case READ:
                            link.getMethods().add(RestMethodTypes.GET);
                            break;
                        case UPDATE:
                            link.getMethods().add(RestMethodTypes.PUT);
                            // add PATCH when we support that
                            break;
                        case DELETE:
                            link.getMethods().add(RestMethodTypes.DELETE);
                    }
                }


                payloadEnvelope.getHeader().setGobiiVersion(this.gobiiWebVersion);
                payloadEnvelope.getPayload().getLinkCollection().getLinksPerDataItem().add(link);


            } else {
                throw new GobiiWebException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The  data item type ("
                                + itemToWrite.getClass()
                                + ") does not match the intended type("
                                + this.dtoType
                                + ")");
            }
        }
    }

    public void writeSingleItemForDefaultId(PayloadEnvelope<T> payloadEnvelope,
                                            RestUri restUri,
                                            T itemToWrite) throws GobiiWebException, Exception {

        if ((null != itemToWrite) && (itemToWrite.getId() != null)) {
            String id = itemToWrite.getId().toString();

            this.writeSingleItemForId(payloadEnvelope,
                    restUri,
                    itemToWrite,
                    id);
        }


        payloadEnvelope.getHeader().setGobiiVersion(this.gobiiWebVersion);

    }

    public void writeList(PayloadEnvelope<T> payloadEnvelope,
                          RestUri restUri,
                          List<T> itemsToWrite) throws GobiiWebException, Exception {

        for (T currentItem : itemsToWrite) {
            this.writeSingleItemForDefaultId(payloadEnvelope, restUri, currentItem);
        }


        payloadEnvelope.getHeader().setGobiiVersion(this.gobiiWebVersion);
    }
}
