// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiidao.entity.access.impl.mock;

import org.gobiiproject.gobiidao.entity.access.MarkerEntityDao;
import org.gobiiproject.gobiidao.entity.core.impl.DaoImplHibernate;
import org.gobiiproject.gobiidao.entity.pojos.Marker;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/24/2016.
 */
public class MarkerEntityDaoImplMock extends DaoImplHibernate<Marker> implements MarkerEntityDao {

    @Override
    public Map<String, List<String>> getMarkers(List<Integer> markerIds) {

        Map<String, List<String>> returnVal = new HashMap<>();
        List<String> testList1 = new ArrayList<>();
        testList1.add("marker a");
        testList1.add("marker b");
        returnVal.put("marker group 1", testList1);
        returnVal.put("marker group 2", testList1);

        return (returnVal);

    } // getMarkers()


} // MarkerEntityDaoImplMock
