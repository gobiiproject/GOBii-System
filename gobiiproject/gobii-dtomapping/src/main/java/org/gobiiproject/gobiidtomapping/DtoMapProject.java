package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapProject {
    ProjectDTO getProjectDetail(ProjectDTO projectDTO) throws GobiiDtoMappingException;
    ProjectDTO createProject(ProjectDTO projectDTO) throws GobiiDtoMappingException;
    ProjectDTO updateProject(ProjectDTO projectDTO) throws GobiiDtoMappingException;
}
