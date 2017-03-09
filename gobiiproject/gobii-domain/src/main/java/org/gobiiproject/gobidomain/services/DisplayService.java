// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.DisplayDTO;

import java.util.List;

/**
 * Created by Phil on 3/24/2016.
 */
public interface DisplayService {

    List<DisplayDTO> getDisplays() throws GobiiDomainException;
    DisplayDTO createDisplay(DisplayDTO displayDTO) throws GobiiDomainException;
    DisplayDTO replaceDisplay(Integer displayId, DisplayDTO displayDTO) throws GobiiDomainException;
    DisplayDTO getDisplayById(Integer displayId) throws GobiiDomainException;
}
