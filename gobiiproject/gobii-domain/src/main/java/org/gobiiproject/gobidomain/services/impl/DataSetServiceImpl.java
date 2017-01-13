package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class DataSetServiceImpl implements DataSetService {

    Logger LOGGER = LoggerFactory.getLogger(DataSetServiceImpl.class);


    @Autowired
    DtoMapDataSet dtoMapDataSet = null;

    @Override
    public List<DataSetDTO> getDataSets() throws GobiiDomainException {

        List<DataSetDTO> returnVal;

        try {
            returnVal = dtoMapDataSet.getDataSets();

            for (DataSetDTO currentDataSetDTO : returnVal) {
                currentDataSetDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
                currentDataSetDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
            }


            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public DataSetDTO getDataSetById(Integer dataSetId) {

        DataSetDTO returnVal;

        try {
            returnVal = dtoMapDataSet.getDataSetDetails(dataSetId);

            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified dataSetId ("
                                + dataSetId
                                + ") does not match an existing dataSet ");
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public DataSetDTO createDataSet(DataSetDTO dataSetDTO) throws GobiiDomainException {

        DataSetDTO returnVal;

        dataSetDTO.setCreatedDate(new Date());
        dataSetDTO.setModifiedDate(new Date());
        returnVal = dtoMapDataSet.createDataSet(dataSetDTO);

        // When we have roles and permissions, this will be set programmatically
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        return returnVal;
    }

    @Override
    public DataSetDTO replaceDataSet(Integer dataSetId, DataSetDTO dataSetDTO) throws GobiiDomainException {
        DataSetDTO returnVal;

        try {

            if (null == dataSetDTO.getDataSetId() ||
                    dataSetDTO.getDataSetId().equals(dataSetId)) {


                DataSetDTO existingDataSetDTO = dtoMapDataSet.getDataSetDetails(dataSetId);

                if (null != existingDataSetDTO.getDataSetId() && existingDataSetDTO.getDataSetId().equals(dataSetId)) {


                    dataSetDTO.setModifiedDate(new Date());
                    returnVal = dtoMapDataSet.replaceDataSet(dataSetId, dataSetDTO);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified dataSetId ("
                                    + dataSetId
                                    + ") does not match an existing dataSet ");
                }

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The dataSetId specified in the dto ("
                                + dataSetDTO.getDataSetId()
                                + ") does not match the dataSetId passed as a parameter "
                                + "("
                                + dataSetId
                                + ")");

            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }


        return returnVal;
    }
}
