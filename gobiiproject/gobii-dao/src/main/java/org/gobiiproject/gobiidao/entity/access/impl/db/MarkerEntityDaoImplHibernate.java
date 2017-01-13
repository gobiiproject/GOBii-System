// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************

package org.gobiiproject.gobiidao.entity.access.impl.db;


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
public class MarkerEntityDaoImplHibernate extends DaoImplHibernate<Marker> implements MarkerEntityDao {

    @Override
    public Map<String, List<String>> getMarkers(List<Integer> markerIds ) {

        // Ths is completely the wrong way to do this with JPA/Hibernate
        // It is just a hello world to getting data out of the database
        List<Marker> markers = new ArrayList<>();
        for( Integer currentMarkerId : markerIds  ) {

            Marker currentMarker = this.find(currentMarkerId);
            markers.add(currentMarker);
        }

        Map<String,List<String>> returnVal = new HashMap<>();

        List<String> arrayList = new ArrayList<>();
        arrayList.add("marker1");
        arrayList.add("marker2");

        returnVal.put("Group 1", arrayList);
        returnVal.put("Group 2", arrayList);

        return returnVal;
    } // getMarkers()

}
