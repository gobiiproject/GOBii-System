package org.gobiiproject.gobiiapimodel.restresources.gobii;

import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by Phil on 10/18/2016.
 */
public class GobiiEntityNameConverter {

    public static RestUri toServiceRequestId(String contextRoot,
                                             GobiiEntityNameType gobiiEntityNameType)
            throws GobiiException {


        RestUri returnVal;
        try {
            switch (gobiiEntityNameType) {

                case ANALYSES:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_ANALYSIS);
                    break;

                case CONTACTS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_CONTACTS);
                    break;

                case DATASETS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_DATASETS);
                    break;

                case CVTERMS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_CV);
                    break;

                case CVGROUPS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_CV);
                    break;

                case PROJECTS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_PROJECTS);
                    break;

                case ORGANIZATIONS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_ORGANIZATION);
                    break;

                case PLATFORMS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_PLATFORM);
                    break;

                case MANIFESTS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_MANIFEST);
                    break;

                case MAPSETS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_MAPSET);
                    break;

                case MARKERGROUPS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_MARKERGROUP);
                    break;

                case EXPERIMENTS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_EXPERIMENTS);
                    break;

                case REFERENCES:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_REFERENCE);
                    break;

                case ROLES:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_ROLES);
                    break;

                case PROTOCOLS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_PROTOCOL);
                    break;

                case VENDORS_PROTOCOLS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_PROTOCOL)
                            .appendSegment(GobiiServiceRequestId.URL_VENDORS);
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
