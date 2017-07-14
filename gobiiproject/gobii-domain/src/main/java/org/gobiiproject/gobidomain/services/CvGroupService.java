package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvGroupDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;

import java.util.List;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public interface CvGroupService {

    List<CvDTO> getCvsForGroup(Integer groupId) throws GobiiDomainException;
    List<CvGroupDTO> getCvsForType(GobiiCvGroupType groupType) throws GobiiDomainException;

}
