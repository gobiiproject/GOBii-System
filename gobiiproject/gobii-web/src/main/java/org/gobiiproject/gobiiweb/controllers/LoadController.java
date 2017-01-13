// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobiimodel.dto.container.*;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
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
    private ProjectService projectService = null;

    @Autowired
    private ContactService contactService = null;

    @Autowired
    private ReferenceService referenceService = null;

    @Autowired
    private AnalysisService analysisService = null;

    @Autowired
    private ManifestService manifestService = null;

    @Autowired
    private MarkerGroupService markerGroupService = null;

    @Autowired
    private OrganizationService organizationService = null;

    @Autowired
    private ExperimentService experimentService = null;

    @Autowired
    private NameIdListService nameIdListService = null;

    @Autowired
    private LoaderInstructionFilesService loaderInstructionFilesService = null;

    @Autowired
    private DisplayService displayService = null;
    
    @Autowired
    private CvService cvService = null;

    @Autowired
    private DataSetService dataSetService = null;

    @Autowired
    private PlatformService platformService = null;

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

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }//getPingResponse()

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public String authenticate(@RequestBody String noContentExpected) {

        String returnVal = null;
        try {
            returnVal = "Authenticated: " +  (new Date()).toString();
        } catch (Exception e) {
            String msg = e.getMessage();
            String tmp = msg;
            throw (e);
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

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }//getPingResponse()

    @RequestMapping(value = "/experiment", method = RequestMethod.POST)
    @ResponseBody
    public ExperimentDTO getPingResponse(@RequestBody ExperimentDTO experimentDTO) {

        ExperimentDTO returnVal = null;

        try {
            returnVal = experimentService.processExperiment(experimentDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }//getPingResponse()
    
    @RequestMapping(value = "/project", method = RequestMethod.POST)
    @ResponseBody
    public ProjectDTO processProject(@RequestBody ProjectDTO projectDTO) {

        ProjectDTO returnVal = null;

        try {
            returnVal = projectService.processProject(projectDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }//getPingResponse()

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    @ResponseBody
    public ContactDTO processContact(@RequestBody ContactDTO contactDTO) {

        ContactDTO returnVal = new ContactDTO();
        //PingDTO pingDTORequest = new PingDTO();
        try {
            returnVal = contactService.processContact(contactDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }//processCOntact


    @RequestMapping(value = "/reference", method = RequestMethod.POST)
    @ResponseBody
    public ReferenceDTO processReference(@RequestBody ReferenceDTO referenceDTO) {

        ReferenceDTO returnVal = new ReferenceDTO();
        //PingDTO pingDTORequest = new PingDTO();
        try {
            returnVal = referenceService.processReference(referenceDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
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

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }//processManifest

    @RequestMapping(value = "/organization", method = RequestMethod.POST)
    @ResponseBody
    public OrganizationDTO process(@RequestBody OrganizationDTO organizationDTO) {

        OrganizationDTO returnVal = new OrganizationDTO();
        try {
            returnVal = organizationService.process(organizationDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }//processOrganization

    @RequestMapping(value = "/nameidlist", method = RequestMethod.POST)
    @ResponseBody
    public NameIdListDTO getNameIdList(@RequestBody NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = nameIdListDTO;

        try {
            returnVal = nameIdListService.getNameIdList(nameIdListDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/instructions", method = RequestMethod.POST)
    @ResponseBody
    public LoaderInstructionFilesDTO processInstructions(@RequestBody LoaderInstructionFilesDTO loaderInstructionFilesDTO) {

        LoaderInstructionFilesDTO returnVal = new LoaderInstructionFilesDTO();

        try {
            returnVal = loaderInstructionFilesService.processLoaderFileInstructions(loaderInstructionFilesDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/display", method = RequestMethod.POST)
    @ResponseBody
    public DisplayDTO processDisplay(@RequestBody DisplayDTO displayDTO) {

        DisplayDTO returnVal = new DisplayDTO();

        try {
            returnVal = displayService.process(displayDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/dataset", method = RequestMethod.POST)
    @ResponseBody
    public DataSetDTO processDataset(@RequestBody DataSetDTO dataSetDTO) {

        DataSetDTO returnVal = new DataSetDTO();

        try {
            returnVal = dataSetService.processDataSet(dataSetDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    @ResponseBody
    public AnalysisDTO processAnalysis(@RequestBody AnalysisDTO analysisDTO) {

        AnalysisDTO returnVal = new AnalysisDTO();

        try {
            returnVal = analysisService.getAnalysisDetails(analysisDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
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

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/platform", method = RequestMethod.POST)
    @ResponseBody
    public PlatformDTO processPlatform(@RequestBody PlatformDTO platformDTO) {

        PlatformDTO returnVal = new PlatformDTO();

        try {
            returnVal = platformService.processPlatform(platformDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
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

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }
    @RequestMapping(value = "/configsettings", method = RequestMethod.POST)
    @ResponseBody
    public ConfigSettingsDTO process(@RequestBody ConfigSettingsDTO configSettingsDTO) {

        ConfigSettingsDTO returnVal = new ConfigSettingsDTO();

        try {
            returnVal = configSettingsService.process(configSettingsDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }


}// LoadController
