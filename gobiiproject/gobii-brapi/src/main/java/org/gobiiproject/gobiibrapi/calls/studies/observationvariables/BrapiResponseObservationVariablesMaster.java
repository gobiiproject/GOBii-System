package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

import org.gobiiproject.gobiibrapi.core.derived.BrapiListResult;

/**
 * Created by Phil on 12/18/2016.
 */
public class BrapiResponseObservationVariablesMaster extends BrapiListResult<BrapiResponseObservationVariablesDetail> {


    public BrapiResponseObservationVariablesMaster() {
        super(BrapiResponseObservationVariablesDetail.class);
    }



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
