// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiidao.entity.access;

import org.gobiiproject.gobiidao.entity.core.EntityDao;
import org.gobiiproject.gobiidao.entity.pojos.Marker;


import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/24/2016.
 */
public interface MarkerEntityDao extends EntityDao<Marker> {
    Map<String, List<String>> getMarkers(List<Integer> markerIds );
}
