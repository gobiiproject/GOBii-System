package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;

import java.util.List;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public interface DtoMapCvGroup {

    List<CvDTO> getCvsForGroup(Integer groupId) throws GobiiDtoMappingException;

    Integer getGroupTypeForGroupId(Integer groupId) throws GobiiDtoMappingException;

}
