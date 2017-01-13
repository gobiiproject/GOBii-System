package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobiidtomapping.*;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
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
			switch (cvDTO.getProcessType()) {
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
					returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
							DtoHeaderResponse.ValidationStatusType.BAD_REQUEST,
							"Unsupported proces Cv type " + cvDTO.getProcessType().toString());

			}

		} catch (Exception e) {

			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error("Gobii service error", e);
		}

		return returnVal;
	}
}
