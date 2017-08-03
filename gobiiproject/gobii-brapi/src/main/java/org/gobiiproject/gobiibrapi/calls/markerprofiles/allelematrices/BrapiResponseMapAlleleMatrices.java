package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices;

import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearchItem;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapAlleleMatrices {

    @Autowired
    private ExperimentService experimentService = null;

    private List<BrapiResponseAlleleMatricesItem> getBrapiJsonResponseAlleleMatricesItems() {

        List<BrapiResponseAlleleMatricesItem> returnVal = new ArrayList<>();

        List<ExperimentDTO> experimentDTOS = experimentService.getAlleleMatrices(null);

        returnVal = getListBrapiResponseAlleleMatricesItem(experimentDTOS);

        return returnVal;
    }

    private List<BrapiResponseAlleleMatricesItem> getListBrapiResponseAlleleMatricesItem(List<ExperimentDTO> experimentDTOS) {

        List<BrapiResponseAlleleMatricesItem> returnVal = new ArrayList<>();

        for(ExperimentDTO experimentDTO : experimentDTOS) {

            List<DataSetDTO> currentDataSetList = experimentDTO.getDatasets();

            for(DataSetDTO dataSetDTO : currentDataSetList) {

                BrapiResponseAlleleMatricesItem brapiResponseAlleleMatricesItem = new BrapiResponseAlleleMatricesItem();
                brapiResponseAlleleMatricesItem.setName(experimentDTO.getExperimentName() + "-" +dataSetDTO.getName());
                brapiResponseAlleleMatricesItem.setLastUpdated(DateUtils.makeDateYYYYMMDD());
                brapiResponseAlleleMatricesItem.setMatrixDbId(dataSetDTO.getDataSetId().toString());
                brapiResponseAlleleMatricesItem.setStudyDbId(experimentDTO.getProjectId().toString());
                brapiResponseAlleleMatricesItem.setDescription("Dummy description " + dataSetDTO.getDataSetId().toString());
                returnVal.add(brapiResponseAlleleMatricesItem);

            }

        }

        return returnVal;


    }

    private List<BrapiResponseAlleleMatricesItem> getBrapiJsonResponseAlleleMatricesItemsByStudyDbId(Integer studyDbId) {

        List<BrapiResponseAlleleMatricesItem> returnVal = new ArrayList<>();

        List<ExperimentDTO> experimentDTOS = experimentService.getAlleleMatrices(studyDbId);

        returnVal = getListBrapiResponseAlleleMatricesItem(experimentDTOS);

        return returnVal;
    }



    public BrapiResponseAlleleMatrices getBrapiResponseAlleleMatricesItemsByStudyDbId(Integer studyDbId) {
        BrapiResponseAlleleMatrices returnVal = new BrapiResponseAlleleMatrices();

        List<BrapiResponseAlleleMatricesItem> searchItems = getBrapiJsonResponseAlleleMatricesItemsByStudyDbId(studyDbId);

        returnVal.setData(searchItems);

        return returnVal ;

    }

    public BrapiResponseAlleleMatrices getBrapiResponseAlleleMatrices() {
        BrapiResponseAlleleMatrices returnVal = new BrapiResponseAlleleMatrices();

        List<BrapiResponseAlleleMatricesItem> searchItems = getBrapiJsonResponseAlleleMatricesItems();

        returnVal.setData(searchItems);

        return returnVal ;

    }

}
