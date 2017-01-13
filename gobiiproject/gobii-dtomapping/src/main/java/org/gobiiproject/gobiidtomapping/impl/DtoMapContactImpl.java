package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
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
public class DtoMapContactImpl implements DtoMapContact {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapContactImpl.class);


    @Autowired
    private RsContactDao rsContactDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;


    @SuppressWarnings("unchecked")
    @Override
    public List<ContactDTO> getContacts() throws GobiiDtoMappingException {

        List<ContactDTO> returnVal = new ArrayList<ContactDTO>();

        try {

            returnVal = (List<ContactDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_CONTACT_ALL, null);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Transactional
    @Override
    public ContactDTO getContactDetails(Integer contactId) throws Exception {

        ContactDTO returnVal = new ContactDTO();

        try {

            ResultSet resultSet = rsContactDao.getContactDetailsByContactId(contactId);

            if (resultSet.next()) {

                // apply contact values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet


        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw e;
        }

        return returnVal;
    }

    @Transactional
    @Override
    public ContactDTO getContactByEmail(String email) throws Exception {

        ContactDTO returnVal = new ContactDTO();

        try {

            ResultSet resultSet = rsContactDao.getContactDetailsByEmail(email);

            if (resultSet.next()) {

                // apply contact values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet


        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw e;
        }

        return returnVal;
    }

    @Override
    public ContactDTO createContact(ContactDTO contactDTO) throws Exception {

        ContactDTO returnVal = contactDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            Integer contactId = rsContactDao.createContact(parameters);
            returnVal.setContactId(contactId);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw e;
        }

        return returnVal;
    }

    @Override
    public ContactDTO replaceContact(Integer contactId, ContactDTO contactDTO) throws Exception {

        ContactDTO returnVal = contactDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("contactId", contactId);
            rsContactDao.updateContact(parameters);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw e;
        }

        return returnVal;
    }
}
