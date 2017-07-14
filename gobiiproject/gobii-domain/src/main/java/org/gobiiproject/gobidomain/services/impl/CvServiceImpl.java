package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobiidtomapping.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 4/29/2016.
 */
public class CvServiceImpl implements CvService {
	Logger LOGGER = LoggerFactory.getLogger(CvServiceImpl.class);

    @Autowired
    DtoMapCv dtoMapCv = null;

	@Override
	public CvDTO createCv(CvDTO cvDTO) throws GobiiDomainException {

		CvDTO returnVal;

		try {

			returnVal = dtoMapCv.createCv(cvDTO);

			// When we have roles and permissions, this will be set programmatically
			returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
			returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.DELETE);
		} catch (Exception e) {

			LOGGER.error("Gobii service error", e);
			throw new GobiiDomainException(e);
		}

		return returnVal;
	}

	@Override
	public CvDTO replaceCv(Integer cvId, CvDTO cvDTO) throws GobiiDomainException {

		CvDTO returnVal;

		try {

			if(null == cvDTO.getCvId() ||
					cvDTO.getCvId().equals(cvId)) {

				CvDTO existingCvDTO = dtoMapCv.getCvDetails(cvId);
				if(null != existingCvDTO.getCvId() && existingCvDTO.getCvId().equals(cvId)) {

					returnVal = dtoMapCv.replaceCv(cvId, cvDTO);
					returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
					returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
					returnVal.getAllowedProcessTypes().add(GobiiProcessType.DELETE);

				} else {

					throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
							GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
							"The specified cvId ("
									+ cvId
									+ ") does not match an existing cv.");
				}
			} else {

				throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
						GobiiValidationStatusType.BAD_REQUEST,
						"The cvId specified in the dto ("
								+ cvDTO.getCvId()
								+ ") does not match the cvId passed as a parameter "
								+ "("
								+ cvId
								+ ")");
			}


		} catch (Exception e) {

			LOGGER.error("Gobii service error", e);
			throw new GobiiDomainException(e);
		}

		return returnVal;

	}

	@Override
	public CvDTO deleteCv(Integer cvId) throws GobiiDomainException {

		CvDTO returnVal;

		try {


				CvDTO existingCvDTO = dtoMapCv.getCvDetails(cvId);

				returnVal = dtoMapCv.deleteCv(existingCvDTO);
				returnVal.getAllowedProcessTypes().add(GobiiProcessType.NONE);

		} catch (Exception e) {

			LOGGER.error("Gobii service error", e);
			throw new GobiiDomainException(e);

		}

		return returnVal;

	}

	@Override
	public List<CvDTO> getCvs() throws GobiiDomainException {

		List<CvDTO> returnVal;

		returnVal = dtoMapCv.getCvs();
		for(CvDTO currentCvDTO : returnVal) {
			currentCvDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
			currentCvDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
            currentCvDTO.getAllowedProcessTypes().add(GobiiProcessType.DELETE);
		}

		if(null == returnVal) {
			returnVal = new ArrayList<>();
		}

		return returnVal;
	}

	@Override
	public List<CvDTO> getCvsByGroupName(String groupName) throws GobiiDtoMappingException {

		List<CvDTO> returnVal;

		returnVal = dtoMapCv.getCvsByGroupName(groupName);

		for (CvDTO currentCvDTO : returnVal) {

			currentCvDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
		}

		return returnVal;

	}

    @Override
    public CvDTO getCvById(Integer cvId) throws GobiiDomainException {

        CvDTO returnVal;

        returnVal = dtoMapCv.getCvDetails(cvId);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.DELETE);

        if (null == returnVal) {
            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The specified cvId ("
                            + cvId
                            + ") does not match an existing cv ");
        }

        return returnVal;
    }
}
