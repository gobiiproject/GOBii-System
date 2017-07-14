package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
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
public class DtoMapNameIdFetchCvTerms implements DtoMapNameIdFetch {

    @Autowired
    private RsCvDao rsCvDao = null;

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdFetchCvTerms.class);


    @Override
    public GobiiEntityNameType getEntityTypeName() {
        return GobiiEntityNameType.CVTERMS;
    }

    private NameIdDTO makeCvNameId(ResultSet resultSet) throws SQLException   {
        NameIdDTO returnVal = new NameIdDTO();
        returnVal.setId(resultSet.getInt("cv_id"));
        returnVal.setName(resultSet.getString("term").toString());
        return returnVal;

    }

    private List<NameIdDTO> getCvTermsForGroup(String cvGroupName) throws GobiiException {

        List<NameIdDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsCvDao.getCvTermsByGroup(cvGroupName);

            while (resultSet.next()) {
                returnVal.add(makeCvNameId(resultSet));
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    } // getCvTermsForGroup()

    private List<NameIdDTO> getCvTerms() throws GobiiException {

        List<NameIdDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsCvDao.getCvNames();


            while (resultSet.next()) {
                returnVal.add(makeCvNameId(resultSet));
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
            returnVal = this.getCvTerms();
        } else {

            if (GobiiFilterType.BYTYPENAME == dtoMapNameIdParams.getGobiiFilterType()) {

                returnVal = this.getCvTermsForGroup(dtoMapNameIdParams.getFilterValueAsString());

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
