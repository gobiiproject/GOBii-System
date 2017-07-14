package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIdFetch;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 10/16/2016.
 */
public class DtoMapNameIdFetchDataSets implements DtoMapNameIdFetch {

    @Autowired
    private RsDataSetDao rsDataSetDao = null;

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdFetchDataSets.class);


    @Override
    public GobiiEntityNameType getEntityTypeName() throws GobiiException {
        return GobiiEntityNameType.DATASETS;
    }


    private List makeMapOfDataSetNames(ResultSet resultSet) throws SQLException {

        List<NameIdDTO> returnVal = new ArrayList<>();

        NameIdDTO nameIdDTO;
        while (resultSet.next()) {
            nameIdDTO = new NameIdDTO();
            nameIdDTO.setId(resultSet.getInt("dataset_id"));
            nameIdDTO.setName(resultSet.getString("name"));

            if (resultSet.wasNull()) {
                nameIdDTO.setName("<no name>");
            }
            returnVal.add(nameIdDTO);
        }

        return returnVal;
    }


    private List<NameIdDTO> getDatasetNames() throws GobiiException {

        List<NameIdDTO> returnVal;

        try {

            ResultSet resultSet = rsDataSetDao.getDatasetNames();

            returnVal = makeMapOfDataSetNames(resultSet);


        } catch (SQLException e) {
            LOGGER.error("Error retrieving dataset", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    private List<NameIdDTO> getNameIdListForDataSetByExperimentId(Integer experimentId) throws GobiiException {

        List<NameIdDTO> returnVal;

        try {

            ResultSet resultSet = rsDataSetDao.getDatasetNamesByExperimentId(experimentId);
            returnVal = makeMapOfDataSetNames(resultSet);



        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);

        }

        return returnVal;

    }


    @Override
    public List<NameIdDTO> getNameIds(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        if (GobiiFilterType.NONE == dtoMapNameIdParams.getGobiiFilterType()) {
            returnVal = this.getDatasetNames();
        } else {

            if (GobiiFilterType.BYTYPEID == dtoMapNameIdParams.getGobiiFilterType()) {

                returnVal = this.getNameIdListForDataSetByExperimentId(dtoMapNameIdParams.getFilterValueAsInteger());

            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Unsupported filter type for "
                                + this.getEntityTypeName().toString().toLowerCase()
                                + ": " + dtoMapNameIdParams.getGobiiFilterType());
            }
        }

        return returnVal;
    }
}
