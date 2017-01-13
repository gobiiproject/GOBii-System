// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobiimodel.dto.container.*;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;


/**
 * Created by MrPhil on 7/6/2015.
 */
@Scope(value = "request")
@Controller
@RequestMapping("/load")
public class LoadController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LoadController.class);

    @Autowired
    private PingService pingService = null;


    @Autowired
    private ReferenceService referenceService = null;

    @Autowired
    private AnalysisService analysisService = null;

    @Autowired
    private ManifestService manifestService = null;

    @Autowired
    private MarkerGroupService markerGroupService = null;

    @Autowired
    private DisplayService displayService = null;
    
    @Autowired
    private CvService cvService = null;

    @Autowired
    private MapsetService mapsetService;

    @Autowired
    private ConfigSettingsService configSettingsService;

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @ResponseBody
    public PingDTO getPingResponse(@RequestBody PingDTO pingDTORequest) {

        PingDTO returnVal = new PingDTO();

        try {
            returnVal = pingService.getPings(pingDTORequest);
            String newResponseString = LineUtils.wrapLine("Loader controller responded");
            returnVal.getPingResponses().add(newResponseString);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public String authenticate(@RequestBody String noContentExpected) {

        String returnVal = null;
        try {
            returnVal = "Authenticated: " +  (new Date()).toString();
        } catch (Exception e) {
            String msg = e.getMessage();
            String tmp = msg;
//            throw (e);
        }

        return (returnVal);

    }//getByContentType()


    @RequestMapping(value = "/cv", method = RequestMethod.POST)
    @ResponseBody
    public CvDTO getPingResponse(@RequestBody CvDTO CvDTO) {

    	CvDTO returnVal = new CvDTO();

        try {
            returnVal = cvService.procesCv(CvDTO);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }


    @RequestMapping(value = "/reference", method = RequestMethod.POST)
    @ResponseBody
    public ReferenceDTO processReference(@RequestBody ReferenceDTO referenceDTO) {

        ReferenceDTO returnVal = new ReferenceDTO();
        //PingDTO pingDTORequest = new PingDTO();
        try {
            returnVal = referenceService.processReference(referenceDTO);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }//processReference


    @RequestMapping(value = "/manifest", method = RequestMethod.POST)
    @ResponseBody
    public ManifestDTO process(@RequestBody ManifestDTO manifestDTO) {

        ManifestDTO returnVal = new ManifestDTO();
        try {
            returnVal = manifestService.process(manifestDTO);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }//processManifest

    @RequestMapping(value = "/display", method = RequestMethod.POST)
    @ResponseBody
    public DisplayDTO processDisplay(@RequestBody DisplayDTO displayDTO) {

        DisplayDTO returnVal = new DisplayDTO();

        try {
            returnVal = displayService.process(displayDTO);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

//    @RequestMapping(value = "/dataset", method = RequestMethod.POST)
//    @ResponseBody
//    public DataSetDTO processDataset(@RequestBody DataSetDTO dataSetDTO) {
//
//        DataSetDTO returnVal = new DataSetDTO();
//
//        try {
//            returnVal = dataSetService.processDataSet(dataSetDTO);
//        } catch (Exception e) {
//
//            returnVal.getStatus().addException(e);
//            LOGGER.error(e.getMessage());
//        }
//
//        return (returnVal);
//
//    }

    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    @ResponseBody
    public AnalysisDTO processAnalysis(@RequestBody AnalysisDTO analysisDTO) {

        AnalysisDTO returnVal = new AnalysisDTO();

        try {
            returnVal = analysisService.getAnalysisDetails(analysisDTO);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/markergroup", method = RequestMethod.POST)
    @ResponseBody
    public MarkerGroupDTO process(@RequestBody MarkerGroupDTO markerGroupDTO) {

        MarkerGroupDTO returnVal = new MarkerGroupDTO();

        try {
            returnVal = markerGroupService.process(markerGroupDTO);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/mapset", method = RequestMethod.POST)
    @ResponseBody
    public MapsetDTO processMapset(@RequestBody MapsetDTO MapsetDTO) {

        MapsetDTO returnVal = new MapsetDTO();

        try {
            returnVal = mapsetService.processMapset(MapsetDTO);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }


}// LoadController
