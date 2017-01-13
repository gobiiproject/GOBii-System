// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;

import java.util.List;

/**
 * Created by Angel on 4/19/2016.
 */
public interface ExperimentService {

    ExperimentDTO createExperiment(ExperimentDTO experimentDTO) throws GobiiDomainException;
    ExperimentDTO replaceExperiment(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDomainException;
    List<ExperimentDTO> getExperiments() throws GobiiDomainException;
    ExperimentDTO getExperimentById(Integer experimentId) throws GobiiDomainException;


}
