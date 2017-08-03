package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseAlleleMatricesItem {


    private String name;
    private String matrixDbId;
    private String description;
    private String lastUpdated;
    private String studyDbId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatrixDbId() {
        return matrixDbId;
    }

    public void setMatrixDbId(String matrixDbId) {
        this.matrixDbId = matrixDbId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }
}
