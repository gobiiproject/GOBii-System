package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Phil on 4/21/2016.
 */
public class DataSetServiceImpl implements DataSetService {

    Logger LOGGER = LoggerFactory.getLogger(DataSetServiceImpl.class);


    @Autowired
    DtoMapDataSet dtoMapDataSet = null;

    @Override
    public DataSetDTO processDataSet(DataSetDTO datasetDTO) {

        DataSetDTO returnVal = new DataSetDTO();

        try {

            switch (datasetDTO.getProcessType()) {
                case READ:
                    returnVal = dtoMapDataSet.getDataSetDetails(datasetDTO);
                    break;

                case CREATE:
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    returnVal = dtoMapDataSet.createDataset(datasetDTO);
                    break;

                case UPDATE:
                    returnVal.setModifiedDate(new Date());
                    returnVal = dtoMapDataSet.updateDataset(datasetDTO);
                    break;

                default:
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.BAD_REQUEST,
                            "Unsupported proces type " + datasetDTO.getProcessType().toString());

            }

        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}
