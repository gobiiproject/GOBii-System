package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsReferenceDao {


    ResultSet getReferenceNames() throws GobiiDaoException;
    ResultSet getReferenceDetailsByReferenceId(Integer referenceId) throws GobiiDaoException;
    Integer createReference( Map<String, Object> parameters) throws GobiiDaoException;
    void updateReference(Map<String, Object> parameters) throws GobiiDaoException;
}
