// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;

/**
 * Created by Angel on 4/29/2016.
 */
public interface CvService {

    CvDTO procesCv(CvDTO cvDTO);
}
