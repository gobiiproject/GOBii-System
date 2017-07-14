package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
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
public class DtoMapNameIdFetchPlatforms implements DtoMapNameIdFetch {

    @Autowired
    private RsPlatformDao rsPlatformDao = null;


    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdFetchPlatforms.class);


    @Override
    public GobiiEntityNameType getEntityTypeName() throws GobiiException {
        return GobiiEntityNameType.PLATFORMS;
    }


    private NameIdDTO makeNameIdDtoForPlatform(ResultSet resultSet) throws SQLException {

        NameIdDTO returnVal = new NameIdDTO();

        returnVal.setId(resultSet.getInt("platform_id"));
        returnVal.setName(resultSet.getString("name"));

        return returnVal;
    }

    private List<NameIdDTO> getPlatformNames() throws GobiiException {

        List<NameIdDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsPlatformDao.getPlatformNames();
            List<NameIdDTO> listDTO = new ArrayList<>();

            while (resultSet.next()) {
                returnVal.add(this.makeNameIdDtoForPlatform(resultSet));
            }



        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    private List<NameIdDTO> getPlatformNamesByTypeId(Integer platformTypeId) throws GobiiException{

        List<NameIdDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsPlatformDao.getPlatformNamesByTypeId(platformTypeId);


            while (resultSet.next()) {
                returnVal.add(this.makeNameIdDtoForPlatform(resultSet));
            }



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
            returnVal = this.getPlatformNames();
        } else {

            if (GobiiFilterType.BYTYPEID == dtoMapNameIdParams.getGobiiFilterType()) {

                returnVal = this.getPlatformNamesByTypeId(dtoMapNameIdParams.getFilterValueAsInteger());

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
