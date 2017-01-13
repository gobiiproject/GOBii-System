package org.gobiiproject.gobiidao.entity.access.impl.mock;

import org.gobiiproject.gobiidao.entity.access.PingEntityDao;
import org.gobiiproject.gobiidao.entity.core.impl.DaoImplHibernate;
import org.gobiiproject.gobiidao.entity.pojos.Marker;
import org.gobiiproject.gobiimodel.utils.LineUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/4/2016.
 */
public class PingEntityDaoImplMock extends DaoImplHibernate<Marker> implements PingEntityDao {

    @Override
    public List<String> getPingResponses(List<String> pingRequests) {

        List<String> returnVal = new ArrayList<>();

        for(String currentString : pingRequests ) {

            String responseLine = LineUtils.wrapLine("DAO Layer responds: " + currentString);
            returnVal.add( responseLine);

        } // iterate ping requests

        return (returnVal);

    } // getPingResponses()

} // PingEntityDaoImplMock
