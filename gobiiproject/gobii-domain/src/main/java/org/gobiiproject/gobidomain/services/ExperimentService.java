// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;

/**
 * Created by Angel on 4/19/2016.
 */
public interface ExperimentService {

    ExperimentDTO processExperiment(ExperimentDTO experimentDTO);

}
