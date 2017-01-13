package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobiidtomapping.*;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Angel on 4/29/2016.
 */
public class CvServiceImpl implements CvService {
	Logger LOGGER = LoggerFactory.getLogger(CvServiceImpl.class);

    @Autowired
    DtoMapCv dtoMapCv = null;


	@Override
	public CvDTO procesCv(CvDTO cvDTO) {

		CvDTO returnVal = new CvDTO();

		try {
			switch (cvDTO.getGobiiProcessType()) {
				case READ:
					returnVal = dtoMapCv.getCvDetails(cvDTO);
					break;

				case CREATE:
					returnVal = dtoMapCv.createCv(cvDTO);
					break;

				case UPDATE:
					returnVal = dtoMapCv.updateCv(cvDTO);
					break;

				case DELETE:
					returnVal = dtoMapCv.deleteCv(cvDTO);
					break;

				default:
					returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
							GobiiValidationStatusType.BAD_REQUEST,
							"Unsupported proces Cv type " + cvDTO.getGobiiProcessType().toString());

			}

		} catch (Exception e) {

			returnVal.getStatus().addException(e);
			LOGGER.error("Gobii service error", e);
		}

		return returnVal;
	}
}
