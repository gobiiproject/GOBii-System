package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseDataList;

/**
 * Created by Phil on 12/18/2016.
 */
public class BrapiResponseObservationVariablesMaster extends BrapiResponseDataList<BrapiResponseObservationVariablesDetail> {


    public BrapiResponseObservationVariablesMaster() {}



    Integer studyDbId;
    String trialName;

    public Integer getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(Integer studyDbId) {
        this.studyDbId = studyDbId;
    }

    public String getTrialName() {
        return trialName;
    }

    public void setTrialName(String trialName) {
        this.trialName = trialName;
    }
}
