package org.gobiiproject.gobiiapimodel.restresources;

import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by Phil on 10/18/2016.
 */
public class EntityNameConverter {

    public static RestUri toServiceRequestId(String contextRoot,
                                             GobiiEntityNameType gobiiEntityNameType)
            throws GobiiException {


        RestUri returnVal;
        try {
            switch (gobiiEntityNameType) {

                case ANALYSES:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_ANALYSIS);
                    break;

                case CONTACTS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_CONTACTS);
                    break;

                case DATASETS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_DATASETS);
                    break;

                case CVTERMS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_CV);
                    break;

                case CVGROUPS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_CV);
                    break;

                case PROJECTS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_PROJECTS);
                    break;

                case ORGANIZATIONS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_ORGANIZATION);
                    break;

                case PLATFORMS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_PLATFORM);
                    break;

                case MANIFESTS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_MANIFEST);
                    break;

                case MAPSETS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_MAPSET);
                    break;

                case MARKERGROUPS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_MARKERGROUP);
                    break;

                case EXPERIMENTS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_EXPERIMENTS);
                    break;

                case REFERENCES:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_REFERENCE);
                    break;

                case ROLES:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_ROLES);
                    break;

                case PROTOCOLS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_PROTOCOL);
                    break;

                case VENDORS_PROTOCOLS:
                    returnVal = UriFactory.resourceByUriIdParam(contextRoot,
                            ServiceRequestId.URL_PROTOCOL)
                            .appendSegment(ServiceRequestId.URL_VENDORS);
                    break;

                default:
                    throw new GobiiException("Unknown GobiiEntityTypeName: " + gobiiEntityNameType.toString());

            }
        } catch (Exception e) {
            throw new GobiiException(e);
        }

        return returnVal;
    }
}
