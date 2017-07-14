package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.DisplayDTO;

import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapDisplay {

    DisplayDTO getDisplayDetails(Integer displayId) throws GobiiDtoMappingException;
    DisplayDTO createDisplay(DisplayDTO displayDTO) throws GobiiDtoMappingException;
    DisplayDTO replaceDisplay(Integer displayId, DisplayDTO displayDTO) throws GobiiDtoMappingException;
    List<DisplayDTO> getDisplays() throws GobiiDtoMappingException;
}
