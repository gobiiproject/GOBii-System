package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsManifestDao {


    ResultSet getManifestNames() throws GobiiDaoException;
    ResultSet getManifestDetailsByManifestId(Integer referenceId) throws GobiiDaoException;
    Integer createManifest( Map<String, Object> parameters) throws GobiiDaoException;
    void updateManifest(Map<String, Object> parameters) throws GobiiDaoException;
}
