package org.gobiiproject.gobidomain.services;

import java.util.List;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;

/**
 * Created by Phil on 4/28/2016.
 * Modified by AVB on 9/29/2016.
 */
public interface MapsetService {

    MapsetDTO processMapset(MapsetDTO MapsetDTO);

    List<MapsetDTO> getAllMapsetNames() throws GobiiDomainException;

}
