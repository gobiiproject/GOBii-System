package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 12/18/2016.
 */
public class BrapiResponseMapObservationVariables {


    private List<BrapiResponseObservationVariablesDetail> getObservationVariableDetails(Integer studyDbId ) throws Exception {

        List<BrapiResponseObservationVariablesDetail> returnVal = new ArrayList<>();

        returnVal.add(new BrapiResponseObservationVariablesDetail(new ObservationScale("testid1","testNameOne"),
                "testdbid1",
                "testvariable1",
                new ObservationMethhod("testdbid","testmethodname"),
                new ObservationTrait("testtraitname","testdbid")));

        returnVal.add(new BrapiResponseObservationVariablesDetail(new ObservationScale("testid2","testNameOTwo"),
                "testdbid2",
                "testvariable2",
                new ObservationMethhod("testdbid2","testmethodname2"),
                new ObservationTrait("testtraitname2","testdbid2")));

        return returnVal;
    }

    public BrapiResponseObservationVariablesMaster gerObservationVariablesByStudyId(Integer studyDbId ) throws Exception {

        BrapiResponseObservationVariablesMaster returnVal = new BrapiResponseObservationVariablesMaster();

        returnVal.setStudyDbId(studyDbId);
        returnVal.setTrialName("trialname");

        returnVal.setData(this.getObservationVariableDetails(studyDbId));

        return returnVal;
    }
}
