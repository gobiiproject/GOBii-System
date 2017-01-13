package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapDisplay {
    DisplayDTO getDisplayDetails(DisplayDTO displayDTO) throws GobiiDtoMappingException;
    DisplayDTO createDisplay(DisplayDTO displayDTO) throws GobiiDtoMappingException;
    DisplayDTO updateDisplay(DisplayDTO displayDTO) throws GobiiDtoMappingException;
}
