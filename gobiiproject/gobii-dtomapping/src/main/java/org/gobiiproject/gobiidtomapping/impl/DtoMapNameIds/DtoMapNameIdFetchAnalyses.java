package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
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
public class DtoMapNameIdFetchAnalyses implements DtoMapNameIdFetch {

    @Autowired
    private RsAnalysisDao rsAnalysisDao = null;
    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdFetchAnalyses.class);


    @Override
    public GobiiEntityNameType getEntityTypeName() throws GobiiException {
        return GobiiEntityNameType.ANALYSES;
    }


    private List<NameIdDTO> getAllNameIdsForAnalysis() throws GobiiException {

        List<NameIdDTO> returnVal = new ArrayList<>();

        ResultSet resultSet = rsAnalysisDao.getAnalysisNames();
        List<NameIdDTO> listDTO = new ArrayList<>();

        try {

            while (resultSet.next()) {
                NameIdDTO nameIdDTO = new NameIdDTO();
                nameIdDTO.setId(resultSet.getInt("analysis_id"));
                nameIdDTO.setName(resultSet.getString("name"));
                returnVal.add(nameIdDTO);
            }

        } catch (SQLException e) {
            LOGGER.error("error retrieving analysis names", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    private List<NameIdDTO> getNameIdListForAnalysisNameByTypeId(Integer typeId) {

        List<NameIdDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsAnalysisDao.getAnalysisNamesByTypeId(typeId);

            NameIdDTO nameIdDTO;
            while (resultSet.next()) {
                nameIdDTO = new NameIdDTO();
                nameIdDTO.setId(resultSet.getInt("analysis_id"));
                nameIdDTO.setName(resultSet.getString("name"));
                returnVal.add(nameIdDTO);
            }


        } catch (SQLException e) {
            LOGGER.error("error retrieving analysis names", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    @Override
    public List<NameIdDTO> getNameIds(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        if (GobiiFilterType.NONE == dtoMapNameIdParams.getGobiiFilterType()) {
            returnVal = this.getAllNameIdsForAnalysis();
        } else {

            if (GobiiFilterType.BYTYPEID == dtoMapNameIdParams.getGobiiFilterType()) {

                returnVal = this.getNameIdListForAnalysisNameByTypeId(dtoMapNameIdParams.getFilterValueAsInteger());

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
