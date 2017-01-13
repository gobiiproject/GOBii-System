package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/29/2016.
 */
public interface DtoMapMarker {

    MarkerGroupDTO getMarkers(List<Integer> chromosomes );

}
