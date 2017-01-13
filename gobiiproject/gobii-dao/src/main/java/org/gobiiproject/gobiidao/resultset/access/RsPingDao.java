package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 4/27/2016.
 */
public interface RsPingDao {


    Map<String,String> getPingResponses(List<String> pingRequests) throws GobiiDaoException;

}
