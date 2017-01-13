package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsOrganizationDao;
import org.gobiiproject.gobiidao.resultset.access.RsOrganizationDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapOrganization;
import org.gobiiproject.gobiidtomapping.DtoMapOrganization;
import org.gobiiproject.gobiidtomapping.DtoMapProtocol;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.core.EntityProperties;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class DtoMapOrganizationImpl implements DtoMapOrganization {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapOrganizationImpl.class);

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Autowired
    private RsOrganizationDao rsOrganizationDao;

    @Autowired
    private DtoMapProtocol dtoMapProtocol;

    @SuppressWarnings("unchecked")
    @Override
    public List<OrganizationDTO> getOrganizations() throws GobiiDtoMappingException {

        List<OrganizationDTO> returnVal = new ArrayList<OrganizationDTO>();

        try {

            returnVal = (List<OrganizationDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_ORGANIZATION_ALL,null);

            for( OrganizationDTO currentOrganizationDto : returnVal ) {
                this.dtoMapProtocol.addVendorProtocolsToOrganization(currentOrganizationDto);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public OrganizationDTO getOrganizationDetails(Integer organizationId) throws GobiiDtoMappingException {

        OrganizationDTO returnVal = new OrganizationDTO();

        try {

            ResultSet resultSet = rsOrganizationDao.getOrganizationDetailsByOrganizationId(organizationId);

            if (resultSet.next()) {

                // apply organization values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
                this.dtoMapProtocol.addVendorProtocolsToOrganization(returnVal);
            } // if result set has a row

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    } // getOrganizationDetails()

    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) throws GobiiDtoMappingException {
        OrganizationDTO returnVal = organizationDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(organizationDTO);
            Integer organizationId = rsOrganizationDao.createOrganization(parameters);
            returnVal.setOrganizationId(organizationId);

        } catch (Exception e) {

            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    @Override
    public OrganizationDTO replaceOrganization(Integer organizationId, OrganizationDTO organizationDTO) throws GobiiDtoMappingException {

        OrganizationDTO returnVal = organizationDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("organizationId", organizationId);
            rsOrganizationDao.updateOrganization(parameters);

        } catch (Exception e) {

            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }
}
