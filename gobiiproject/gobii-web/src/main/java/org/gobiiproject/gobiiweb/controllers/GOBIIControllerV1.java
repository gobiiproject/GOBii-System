// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiEntityNameConverter;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.*;
import org.gobiiproject.gobiiapimodel.payload.HeaderAuth;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.automation.ControllerUtils;
import org.gobiiproject.gobiiweb.automation.GobiiVersionInfo;
import org.gobiiproject.gobiiweb.automation.PayloadReader;
import org.gobiiproject.gobiiweb.automation.PayloadWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by MrPhil on 7/6/2015.
 */
@Scope(value = "request")
@Controller
@RequestMapping(GobiiControllerType.SERVICE_PATH_GOBII)
public class GOBIIControllerV1 {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GOBIIControllerV1.class);

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
    private ExtractorInstructionFilesService extractorInstructionFilesService = null;

    @Autowired
    private LoaderFilesService loaderFilesService = null;

    @Autowired
    private DisplayService displayService = null;

    @Autowired
    private CvService cvService = null;

    @Autowired
    private CvGroupService cvGroupService = null;

    @Autowired
    private MarkerService markerService = null;

    @Autowired
    private DataSetService dataSetService = null;

    @Autowired
    private PlatformService platformService = null;

    @Autowired
    private MapsetService mapsetService = null;

    @Autowired
    private ConfigSettingsService configSettingsService;

    @Autowired
    private ProtocolService protocolService = null;

    @Autowired
    private FilesService fileService = null;

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<PingDTO> getPingResponse(@RequestBody PayloadEnvelope<PingDTO> pingDTOPayloadEnvelope) {

        PayloadEnvelope<PingDTO> returnVal = new PayloadEnvelope<PingDTO>();

        try {

            PayloadReader<PingDTO> payloadReader = new PayloadReader<>(PingDTO.class);
            PingDTO pingDTORequest = payloadReader.extractSingleItem(pingDTOPayloadEnvelope);

            PingDTO pingDTOResponse = pingService.getPings(pingDTORequest);
            String newResponseString = LineUtils.wrapLine("Loader controller responded");
            pingDTOResponse.getPingResponses().add(newResponseString);

            // add gobii version
            returnVal.getHeader().setGobiiVersion(GobiiVersionInfo.getVersion());

            returnVal.getPayload().getData().add(pingDTOResponse);
        } catch (GobiiException e) {

            returnVal.getHeader().getStatus().addException(e);

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }


        return (returnVal);

    }//getPingResponse()

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public String authenticate(@RequestBody String noContentExpected,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        String returnVal = null;
        try {
            returnVal = "Authenticated: " + (new Date()).toString();

            PayloadWriter<AuthDTO> payloadWriter = new PayloadWriter<>(request, response,
                    AuthDTO.class);

            HeaderAuth dtoHeaderAuth = new HeaderAuth();
            payloadWriter.setAuthHeader(dtoHeaderAuth, response);
            ObjectMapper objectMapper = new ObjectMapper();
            String dtoHeaderAuthString = objectMapper.writeValueAsString(dtoHeaderAuth);
            returnVal = dtoHeaderAuthString;

        } catch (Exception e) {
            String msg = e.getMessage();
            String tmp = msg;
            try {
                throw (e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return (returnVal);

    }

    @RequestMapping(value = "/configsettings", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ConfigSettingsDTO> getConfigSettings(
            HttpServletRequest request,
            HttpServletResponse response) {

        PayloadEnvelope<ConfigSettingsDTO> returnVal = new PayloadEnvelope<>();
        try {

            ConfigSettingsDTO configSettingsDTO = configSettingsService.getConfigSettings();
            if (null != configSettingsDTO) {

                PayloadWriter<ConfigSettingsDTO> payloadWriter = new PayloadWriter<>(request, response,
                        ConfigSettingsDTO.class);

                payloadWriter.writeSingleItemForDefaultId(returnVal,
                        GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                                GobiiServiceRequestId.URL_CONFIGSETTINGS),
                        configSettingsDTO);

            } else {
                returnVal.getHeader().getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Unable to retrieve config settings");
            }

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** ANALYSIS METHODS
    // *********************************************
    @RequestMapping(value = "/analyses", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<AnalysisDTO> createAnalysis(@RequestBody PayloadEnvelope<AnalysisDTO> payloadEnvelope,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<AnalysisDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<AnalysisDTO> payloadReader = new PayloadReader<>(AnalysisDTO.class);
            AnalysisDTO analysisDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            AnalysisDTO analysisDTONew = analysisService.createAnalysis(analysisDTOToCreate);

            PayloadWriter<AnalysisDTO> payloadWriter = new PayloadWriter<>(request, response,
                    AnalysisDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ANALYSIS),
                    analysisDTONew);
        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }


    @RequestMapping(value = "/analyses/{analysisId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<AnalysisDTO> replaceAnalysis(@RequestBody PayloadEnvelope<AnalysisDTO> payloadEnvelope,
                                                        @PathVariable Integer analysisId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        PayloadEnvelope<AnalysisDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<AnalysisDTO> payloadReader = new PayloadReader<>(AnalysisDTO.class);
            AnalysisDTO analysisDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            AnalysisDTO analysisDTOReplaced = analysisService.replaceAnalysis(analysisId, analysisDTOToReplace);

            PayloadWriter<AnalysisDTO> payloadWriter = new PayloadWriter<>(request, response,
                    AnalysisDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ANALYSIS),
                    analysisDTOReplaced);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/analyses", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<AnalysisDTO> getAnalyses(HttpServletRequest request,
                                                    HttpServletResponse response) {

        PayloadEnvelope<AnalysisDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<AnalysisDTO> analysisDTOs = analysisService.getAnalyses();

            PayloadWriter<AnalysisDTO> payloadWriter = new PayloadWriter<>(request, response,
                    AnalysisDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ANALYSIS),
                    analysisDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/analyses/{analysisId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<AnalysisDTO> getAnalysisById(@PathVariable Integer analysisId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        PayloadEnvelope<AnalysisDTO> returnVal = new PayloadEnvelope<>();

        try {

            AnalysisDTO analysisDTO = analysisService.getAnalysisById(analysisId);

            PayloadWriter<AnalysisDTO> payloadWriter = new PayloadWriter<>(request, response,
                    AnalysisDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ANALYSIS),
                    analysisDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** CONTACT METHODS
    // *********************************************
    @RequestMapping(value = "/contacts", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> createContact(@RequestBody PayloadEnvelope<ContactDTO> payloadEnvelope,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<ContactDTO> payloadReader = new PayloadReader<>(ContactDTO.class);
            ContactDTO contactDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            ContactDTO contactDTONew = contactService.createContact(contactDTOToCreate);

            PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ContactDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_CONTACTS),
                    contactDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/contacts/{contactId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> replaceContact(@RequestBody PayloadEnvelope<ContactDTO> payloadEnvelope,
                                                      @PathVariable Integer contactId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ContactDTO> payloadReader = new PayloadReader<>(ContactDTO.class);
            ContactDTO contactDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            ContactDTO contactDTOReplaced = contactService.replaceContact(contactId, contactDTOToReplace);

            PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ContactDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_CONTACTS),
                    contactDTOReplaced);


        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/contacts/{contactId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContactsById(@PathVariable Integer contactId,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            ContactDTO contactDTO = contactService.getContactById(contactId);
            if (null != contactDTO) {

                PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request, response,
                        ContactDTO.class);

                payloadWriter.writeSingleItemForDefaultId(returnVal,
                        GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                                GobiiServiceRequestId.URL_CONTACTS),
                        contactDTO);

            } else {
                returnVal.getHeader().getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Unable to retrieve a contact with contactId " + contactId);
            }

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContacts(HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<ContactDTO> payloadReader = new PayloadReader<>(ContactDTO.class);
            List<ContactDTO> platformDTOs = contactService.getContacts();

            PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ContactDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_CONTACTS),
                    platformDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    //Technically, this regex specifies an email address format, and it actually works.
    //However, when you execute this, you get back an error "The resource identified by this request is only
    // capable of generating responses with characteristics not acceptable according to the request "accept" headers."
    // In other words, the email address is telling the server that you're asking for some other format
    // So for email based searches, you'll have to use the request parameter version
    @RequestMapping(value = "/contacts/{email:[a-zA-Z-]+@[a-zA-Z-]+.[a-zA-Z-]+}",
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContactsByEmail(@PathVariable String email,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            returnVal.getHeader().getStatus().addStatusMessage(GobiiStatusLevel.ERROR, "Method not implemented");

//            ContactDTO contactRequestDTO = new ContactDTO();
//            contactRequestDTO.setContactId(1);
            //contactRequestDTO.setEmail(email);
            //returnVal = contactService.processDml(new PayloadEnvelope<>(contactRequestDTO, GobiiProcessType.READ));

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // Example: http://localhost:8282/gobii-dev/gobii/v1/contact-search?email=foo&lastName=bar&firstName=snot
    // all parameters must be present, but they don't all neeed a value
    @RequestMapping(value = "/contact-search",
            params = {"email", "lastName", "firstName", "userName"},
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContactsBySearch(@RequestParam("email") String email,
                                                           @RequestParam("lastName") String lastName,
                                                           @RequestParam("firstName") String firstName,
                                                           @RequestParam("userName") String userName,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {
            ContactDTO contactDTO = null;

            PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ContactDTO.class);

            if (!LineUtils.isNullOrEmpty(email)) {
                contactDTO = contactService.getContactByEmail(email);
            } else if (!LineUtils.isNullOrEmpty(userName)) {
                contactDTO = contactService.getContactByUserName(userName);
            } else {
                returnVal.getHeader().getStatus().addException(new GobiiException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "search request must provide email or userName"));
            }


            if (contactDTO != null) {
                payloadWriter.writeSingleItemForDefaultId(returnVal,
                        GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                                GobiiServiceRequestId.URL_CONTACTS),
                        contactDTO);
            }

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** CV METHODS
    // *********************************************
    @RequestMapping(value = "/cvs", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<CvDTO> createCv(@RequestBody PayloadEnvelope<CvDTO> payloadEnvelope,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {

        PayloadEnvelope<CvDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<CvDTO> payloadReader = new PayloadReader<>(CvDTO.class);
            CvDTO cvDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            CvDTO cvDTONew = cvService.createCv(cvDTOToCreate);

            PayloadWriter<CvDTO> payloadWriter = new PayloadWriter<>(request, response,
                    CvDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_CV),
                    cvDTONew);
        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }


    @RequestMapping(value = "/cvs/{cvId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<CvDTO> replaceCv(@RequestBody PayloadEnvelope<CvDTO> payloadEnvelope,
                                            @PathVariable Integer cvId,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {

        PayloadEnvelope<CvDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<CvDTO> payloadReader = new PayloadReader<>(CvDTO.class);
            CvDTO cvDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            CvDTO cvDTOReplaced = cvService.replaceCv(cvId, cvDTOToReplace);

            PayloadWriter<CvDTO> payloadWriter = new PayloadWriter<>(request, response,
                    CvDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_CV),
                    cvDTOReplaced);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/cvs", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<CvDTO> getCvs(HttpServletRequest request,
                                         HttpServletResponse response) {

        PayloadEnvelope<CvDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<CvDTO> cvDTOs = cvService.getCvs();

            PayloadWriter<CvDTO> payloadWriter = new PayloadWriter<>(request, response,
                    CvDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_CV),
                    cvDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/cvs/{cvId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<CvDTO> getCvById(@PathVariable Integer cvId,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {

        PayloadEnvelope<CvDTO> returnVal = new PayloadEnvelope<>();

        try {

            CvDTO cvDTO = cvService.getCvById(cvId);

            PayloadWriter<CvDTO> payloadWriter = new PayloadWriter<>(request, response,
                    CvDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_CV),
                    cvDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/cvs/{cvId:[\\d]+}", method = RequestMethod.DELETE)
    @ResponseBody
    public PayloadEnvelope<CvDTO> deleteCv(@PathVariable Integer cvId,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {

        PayloadEnvelope<CvDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<CvDTO> payloadReader = new PayloadReader<>(CvDTO.class);

            CvDTO cvDTODeleted = cvService.deleteCv(cvId);

            PayloadWriter<CvDTO> payloadWriter = new PayloadWriter<>(request, response,
                    CvDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_CV),
                    cvDTODeleted);

        } catch (GobiiException e) {

            returnVal.getHeader().getStatus().addException(e);

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);

        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/cvs/{groupName:[a-zA-Z_]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<CvDTO> getCvById(@PathVariable("groupName") String groupName,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {

        PayloadEnvelope<CvDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<CvDTO> cvDTOs = cvService.getCvsByGroupName(groupName);

            PayloadWriter<CvDTO> payloadWriter = new PayloadWriter<>(request, response,
                    CvDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_CV),
                    cvDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** CVGROUP METHODS
    // *********************************************
    @RequestMapping(value = "/cvgroups/{cvGroupId}/cvs", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<CvDTO> getCvsForCvGroup(@PathVariable Integer cvGroupId,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<CvDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<CvDTO> cvDTOS = cvGroupService.getCvsForGroup(cvGroupId);

            PayloadWriter<CvDTO> payloadWriter = new PayloadWriter<>(request, response,
                    CvDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceColl(request.getContextPath(),
                            GobiiServiceRequestId.URL_CV)
                            .addUriParam("id"),
                    cvDTOS);

        } catch (GobiiException e) {

            returnVal.getHeader().getStatus().addException(e);

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);

        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/cvgroups/{cvGroupTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<CvGroupDTO> getCvGroupsByType(@PathVariable Integer cvGroupTypeId,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {

        PayloadEnvelope<CvGroupDTO> returnVal = new PayloadEnvelope<>();

        try {

            GobiiCvGroupType gobiiCvGroupType = GobiiCvGroupType.fromInt(cvGroupTypeId);

            if (gobiiCvGroupType != GobiiCvGroupType.GROUP_TYPE_UNKNOWN) {

                List<CvGroupDTO> cvGroupDTOS = cvGroupService.getCvsForType(gobiiCvGroupType);

                PayloadWriter<CvGroupDTO> payloadWriter = new PayloadWriter<>(request, response,
                        CvGroupDTO.class);

                // we don't have a GET for a single cvGrouop, and probably don't need one
                // so  our links will just be the same URL as we got
                payloadWriter.writeList(returnVal,
                        GobiiUriFactory.resourceColl(request.getContextPath(),
                                GobiiServiceRequestId.URL_CVGROUP)
                                .addUriParam("id"),
                        cvGroupDTOS);
            } else {
                returnVal.getHeader().getStatus().addException(new Exception("Unknown group type: "
                        + cvGroupTypeId.toString()));
            }

        } catch (GobiiException e) {

            returnVal.getHeader().getStatus().addException(e);

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);

        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    // *********************************************
    // *************************** DATASET METHODS
    // *********************************************
    @RequestMapping(value = "/datasets", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<DataSetDTO> createDataSet(@RequestBody PayloadEnvelope<DataSetDTO> payloadEnvelope,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<DataSetDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<DataSetDTO> payloadReader = new PayloadReader<>(DataSetDTO.class);
            DataSetDTO dataSetDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            DataSetDTO dataSetDTONew = dataSetService.createDataSet(dataSetDTOToCreate);


            PayloadWriter<DataSetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    DataSetDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DATASETS),
                    dataSetDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/datasets/{dataSetId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<DataSetDTO> replaceDataSet(@RequestBody PayloadEnvelope<DataSetDTO> payloadEnvelope,
                                                      @PathVariable Integer dataSetId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<DataSetDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<DataSetDTO> payloadReader = new PayloadReader<>(DataSetDTO.class);
            DataSetDTO dataSetDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            DataSetDTO dataSetDTOReplaced = dataSetService.replaceDataSet(dataSetId, dataSetDTOToReplace);


            PayloadWriter<DataSetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    DataSetDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DATASETS),
                    dataSetDTOReplaced);
//

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/datasets", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<DataSetDTO> getDataSets(HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<DataSetDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<DataSetDTO> payloadReader = new PayloadReader<>(DataSetDTO.class);
            List<DataSetDTO> dataSetDTOs = dataSetService.getDataSets();

            PayloadWriter<DataSetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    DataSetDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DATASETS),
                    dataSetDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/datasets/{dataSetId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<DataSetDTO> getDataSetsById(@PathVariable Integer dataSetId,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<DataSetDTO> returnVal = new PayloadEnvelope<>();
        try {

            DataSetDTO dataSetDTO = dataSetService.getDataSetById(dataSetId);

            PayloadWriter<DataSetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    DataSetDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DATASETS),
                    dataSetDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/datasets/types", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<NameIdDTO> getDataSetsTypes(HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<NameIdDTO> returnVal = new PayloadEnvelope<>();
        try {

            GobiiEntityNameType gobiiEntityNameType = GobiiEntityNameType.CVTERMS;
            GobiiFilterType gobiiFilterType = GobiiFilterType.BYTYPENAME;

            DtoMapNameIdParams dtoMapNameIdParams = new DtoMapNameIdParams(gobiiEntityNameType, gobiiFilterType, "dataset_type");

            List<NameIdDTO> nameIdDTOList = nameIdListService.getNameIdList(dtoMapNameIdParams);

            PayloadWriter<NameIdDTO> payloadWriter = new PayloadWriter<>(request, response,
                    NameIdDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DATASETTYPES),
                    nameIdDTOList);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/datasets/types/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<DataSetDTO> getDataSetsByTypeId(@PathVariable Integer id,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {

        PayloadEnvelope<DataSetDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<DataSetDTO> dataSetDTOS = dataSetService.getDataSetsByTypeId(id);

            PayloadWriter<DataSetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    DataSetDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DATASETS),
                    dataSetDTOS);


        } catch (GobiiException e) {

            returnVal.getHeader().getStatus().addException(e);

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);

        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** DISPLAY METHODS
    // *********************************************
    @RequestMapping(value = "/displays", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<DisplayDTO> createDisplay(@RequestBody PayloadEnvelope<DisplayDTO> payloadEnvelope,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<DisplayDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<DisplayDTO> payloadReader = new PayloadReader<>(DisplayDTO.class);
            DisplayDTO displayDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            DisplayDTO displayDTONew = displayService.createDisplay(displayDTOToCreate);

            PayloadWriter<DisplayDTO> payloadWriter = new PayloadWriter<>(request, response,
                    DisplayDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DISPLAY),
                    displayDTONew);
        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }


    @RequestMapping(value = "/displays/{displayId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<DisplayDTO> replaceDisplay(@RequestBody PayloadEnvelope<DisplayDTO> payloadEnvelope,
                                                      @PathVariable Integer displayId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<DisplayDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<DisplayDTO> payloadReader = new PayloadReader<>(DisplayDTO.class);
            DisplayDTO displayDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            DisplayDTO displayDTOReplaced = displayService.replaceDisplay(displayId, displayDTOToReplace);

            PayloadWriter<DisplayDTO> payloadWriter = new PayloadWriter<>(request, response,
                    DisplayDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DISPLAY),
                    displayDTOReplaced);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/displays", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<DisplayDTO> getDisplays(HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<DisplayDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<DisplayDTO> displayDTOS = displayService.getDisplays();

            PayloadWriter<DisplayDTO> payloadWriter = new PayloadWriter<>(request, response,
                    DisplayDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DISPLAY),
                    displayDTOS);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/displays/{displayId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<DisplayDTO> getDisplayById(@PathVariable Integer displayId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<DisplayDTO> returnVal = new PayloadEnvelope<>();

        try {

            DisplayDTO displayDTO = displayService.getDisplayById(displayId);

            PayloadWriter<DisplayDTO> payloadWriter = new PayloadWriter<>(request, response,
                    DisplayDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_DISPLAY),
                    displayDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** LOADER INSTRUCTION METHODS
    // *********************************************

    @RequestMapping(value = "/instructions/loader", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<LoaderInstructionFilesDTO> createLoaderInstruction(@RequestBody PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope,
                                                                              HttpServletRequest request,
                                                                              HttpServletResponse response) {

        PayloadEnvelope<LoaderInstructionFilesDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<LoaderInstructionFilesDTO> payloadReader = new PayloadReader<>(LoaderInstructionFilesDTO.class);
            LoaderInstructionFilesDTO loaderInstructionFilesDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            LoaderInstructionFilesDTO loaderInstructionFilesDTONew = loaderInstructionFilesService.createInstruction(cropType, loaderInstructionFilesDTOToCreate);

            PayloadWriter<LoaderInstructionFilesDTO> payloadWriter = new PayloadWriter<>(request, response,
                    LoaderInstructionFilesDTO.class);

            payloadWriter.writeSingleItemForId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS),
                    loaderInstructionFilesDTONew,
                    loaderInstructionFilesDTONew.getInstructionFileName());

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/instructions/loader/{instructionFileName}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<LoaderInstructionFilesDTO> getLoaderInstruction(@PathVariable("instructionFileName") String instructionFileName,
                                                                           HttpServletRequest request,
                                                                           HttpServletResponse response) {

        PayloadEnvelope<LoaderInstructionFilesDTO> returnVal = new PayloadEnvelope<>();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            LoaderInstructionFilesDTO loaderInstructionFilesDTO = loaderInstructionFilesService.getInstruction(cropType, instructionFileName);

            PayloadWriter<LoaderInstructionFilesDTO> payloadWriter = new PayloadWriter<>(request, response,
                    LoaderInstructionFilesDTO.class);

            payloadWriter.writeSingleItemForId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS),
                    loaderInstructionFilesDTO,
                    loaderInstructionFilesDTO.getInstructionFileName());

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** EXTRACTOR INSTRUCTION METHODS
    // *********************************************

    @RequestMapping(value = "/instructions/extractor", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ExtractorInstructionFilesDTO> createExtractorInstruction(@RequestBody PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope,
                                                                                    HttpServletRequest request,
                                                                                    HttpServletResponse response) {

        PayloadEnvelope<ExtractorInstructionFilesDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<ExtractorInstructionFilesDTO> payloadReader = new PayloadReader<>(ExtractorInstructionFilesDTO.class);
            ExtractorInstructionFilesDTO extractorInstructionFilesDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);
            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService.createInstruction(cropType, extractorInstructionFilesDTOToCreate);


            PayloadWriter<ExtractorInstructionFilesDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ExtractorInstructionFilesDTO.class);

            payloadWriter.writeSingleItemForId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_FILE_EXTRACTOR_STATUS),
                    extractorInstructionFilesDTONew,
                    extractorInstructionFilesDTONew.getJobId());

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/instructions/extractor/status/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ExtractorInstructionFilesDTO> getExtractorInstructionStatus(@PathVariable("id") String jobId,
                                                                                       HttpServletRequest request,
                                                                                       HttpServletResponse response) {

        PayloadEnvelope<ExtractorInstructionFilesDTO> returnVal = new PayloadEnvelope<>();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            ExtractorInstructionFilesDTO extractorInstructionFilesDTO = extractorInstructionFilesService.getStatus(cropType, jobId);


            PayloadWriter<ExtractorInstructionFilesDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ExtractorInstructionFilesDTO.class);

            payloadWriter.writeSingleItemForId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_FILE_EXTRACTOR_STATUS),
                    extractorInstructionFilesDTO,
                    extractorInstructionFilesDTO.getJobId());

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** MANIFEST METHODS
    // *********************************************
    @RequestMapping(value = "/manifests", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ManifestDTO> createManifest(@RequestBody PayloadEnvelope<ManifestDTO> payloadEnvelope,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<ManifestDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ManifestDTO> payloadReader = new PayloadReader<>(ManifestDTO.class);
            ManifestDTO manifestDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            ManifestDTO manifestDTONew = manifestService.createManifest(manifestDTOToCreate);

            PayloadWriter<ManifestDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ManifestDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MANIFEST),
                    manifestDTONew);
        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/manifests/{manifestId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ManifestDTO> replaceManifest(@RequestBody PayloadEnvelope<ManifestDTO> payloadEnvelope,
                                                        @PathVariable Integer manifestId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        PayloadEnvelope<ManifestDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ManifestDTO> payloadReader = new PayloadReader<>(ManifestDTO.class);
            ManifestDTO manifestDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            ManifestDTO manifestDTOReplaced = manifestService.replaceManifest(manifestId, manifestDTOToReplace);

            PayloadWriter<ManifestDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ManifestDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MANIFEST),
                    manifestDTOReplaced);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }


    @RequestMapping(value = "/manifests", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ManifestDTO> getManifests(HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<ManifestDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<ManifestDTO> manifestDTOS = manifestService.getManifests();

            PayloadWriter<ManifestDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ManifestDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MANIFEST),
                    manifestDTOS);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/manifests/{manifestId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ManifestDTO> getManifestById(@PathVariable Integer manifestId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        PayloadEnvelope<ManifestDTO> returnVal = new PayloadEnvelope<>();

        try {

            ManifestDTO manifestDTO = manifestService.getManifestById(manifestId);

            PayloadWriter<ManifestDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ManifestDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MANIFEST),
                    manifestDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** MARKER METHODS
    // *********************************************
    @RequestMapping(value = "/markers", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<MarkerDTO> createMarker(@RequestBody PayloadEnvelope<MarkerDTO> payloadEnvelope,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<MarkerDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<MarkerDTO> payloadReader = new PayloadReader<>(MarkerDTO.class);
            MarkerDTO markerDtoToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            MarkerDTO dataSetDTONew = markerService.createMarker(markerDtoToCreate);


            PayloadWriter<MarkerDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MarkerDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MARKERS),
                    dataSetDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/markers/{markerId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<MarkerDTO> replaceMarker(@RequestBody PayloadEnvelope<MarkerDTO> payloadEnvelope,
                                                    @PathVariable Integer markerId,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {

        PayloadEnvelope<MarkerDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<MarkerDTO> payloadReader = new PayloadReader<>(MarkerDTO.class);
            MarkerDTO markerDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            MarkerDTO dataSetDTOReplaced = markerService.replaceMarker(markerId, markerDTOToReplace);


            PayloadWriter<MarkerDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MarkerDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MARKERS),
                    dataSetDTOReplaced);
//

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/markers", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<MarkerDTO> getMarkers(HttpServletRequest request,
                                                 HttpServletResponse response) {

        PayloadEnvelope<MarkerDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<MarkerDTO> payloadReader = new PayloadReader<>(MarkerDTO.class);
            List<MarkerDTO> markerDTOs = markerService.getMarkers();

            PayloadWriter<MarkerDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MarkerDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MARKERS),
                    markerDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/markers/{markerId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<MarkerDTO> getMarkerById(@PathVariable Integer markerId,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {

        PayloadEnvelope<MarkerDTO> returnVal = new PayloadEnvelope<>();
        try {

            MarkerDTO dataSetDTO = markerService.getMarkerById(markerId);

            PayloadWriter<MarkerDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MarkerDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MARKERS),
                    dataSetDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/marker-search",
            params = {"name"},
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<MarkerDTO> getMarkerByName(@RequestParam("name") String name,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<MarkerDTO> returnVal = new PayloadEnvelope<>();
        try {

            List<MarkerDTO> markersByName = markerService.getMarkersByName(name);

            PayloadWriter<MarkerDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MarkerDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MARKERS),
                    markersByName);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** NameIDList
    // *********************************************
    @RequestMapping(value = "/names/{entity}",
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<NameIdDTO> getNames(@PathVariable("entity") String entity,
                                               @RequestParam(value = "filterType", required = false) String filterType,
                                               @RequestParam(value = "filterValue", required = false) String filterValue,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {

        PayloadEnvelope<NameIdDTO> returnVal = new PayloadEnvelope<>();
        try {

            // We are getting raw string parameters from the uri and query parameters;
            // here is the place to validate the types before sending the parameters on the service layer,
            // which should only be dealing with GOBII native natives.
            //
            // **************** Get entity type
            GobiiEntityNameType gobiiEntityNameType;
            try {
                gobiiEntityNameType = GobiiEntityNameType.valueOf(entity.toUpperCase());
            } catch (IllegalArgumentException e) {

                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Unsupported entity for list request: " + entity);
            }


            // **************** If a filter was specified, convert it as well
            GobiiFilterType gobiiFilterType = GobiiFilterType.NONE;
            Object typedFilterValue = filterValue;
            if (!LineUtils.isNullOrEmpty(filterType)) {
                try {
                    gobiiFilterType = GobiiFilterType.valueOf(filterType.toUpperCase());
                } catch (IllegalArgumentException e) {

                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.NONE,
                            "Unsupported filter for list request: "
                                    + filterType
                                    + " for entity "
                                    + gobiiEntityNameType);
                }

                if (!LineUtils.isNullOrEmpty(filterValue)) {

                    if (GobiiFilterType.BYTYPEID == gobiiFilterType) {
                        if (NumberUtils.isNumber(filterValue)) {
                            typedFilterValue = Integer.valueOf(filterValue);
                        } else {
                            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                    GobiiValidationStatusType.NONE,
                                    "Value for "
                                            + filterType
                                            + " value is not a number: "
                                            + filterValue
                                            + " for entity "
                                            + gobiiEntityNameType);
                        }

                    } else if (GobiiFilterType.BYTYPENAME == gobiiFilterType) {
                        // there is nothing to test here -- the string could be anything
                        // add additional validation tests for other filter types

                    } else {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.NONE,
                                "Unable to do type checking on filter value for filter type "
                                        + filterType
                                        + " with value "
                                        + filterValue
                                        + " for entity "
                                        + gobiiEntityNameType);
                    }

                } else {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.NONE,
                            "A value was not supplied for filter: "
                                    + filterType
                                    + " for entity "
                                    + gobiiEntityNameType);
                }
            }


            DtoMapNameIdParams dtoMapNameIdParams = new DtoMapNameIdParams(gobiiEntityNameType, gobiiFilterType, typedFilterValue);

            List<NameIdDTO> nameIdList = nameIdListService.getNameIdList(dtoMapNameIdParams);

            PayloadWriter<NameIdDTO> payloadWriter = new PayloadWriter<>(request, response, NameIdDTO.class);
            payloadWriter.writeList(returnVal,
                    GobiiEntityNameConverter.toServiceRequestId(request.getContextPath(),
                            gobiiEntityNameType),
                    nameIdList);

            String cropType = returnVal.getHeader().getCropType();

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** ORGANIZATION METHODS
    // *********************************************

    @RequestMapping(value = "/organizations", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> createOrganization(@RequestBody PayloadEnvelope<OrganizationDTO> payloadEnvelope,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            OrganizationDTO organizationDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            OrganizationDTO OrganizationDTONew = organizationService.createOrganization(organizationDTOToCreate);

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request, response,
                    OrganizationDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ORGANIZATION),
                    OrganizationDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/organizations/{organizationId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> replaceOrganization(@RequestBody PayloadEnvelope<OrganizationDTO> payloadEnvelope,
                                                                @PathVariable Integer organizationId,
                                                                HttpServletRequest request,
                                                                HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            OrganizationDTO organizationDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            OrganizationDTO organizationDTOReplaced = organizationService.replaceOrganization(organizationId, organizationDTOToReplace);

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request, response,
                    OrganizationDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ORGANIZATION),
                    organizationDTOReplaced);


        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/organizations", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> getOrganizations(HttpServletRequest request,
                                                             HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            List<OrganizationDTO> organizationDTOs = organizationService.getOrganizations();

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request, response,
                    OrganizationDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ORGANIZATION),
                    organizationDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/organizations/{organizationId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> getOrganizationsById(@PathVariable Integer organizationId,
                                                                 HttpServletRequest request,
                                                                 HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            OrganizationDTO organizationDTO = organizationService.getOrganizationById(organizationId);

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request, response,
                    OrganizationDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ORGANIZATION),
                    organizationDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** MAPSET METHODS
    // *********************************************
    /*
    * NOTE: this implementation is incorrect: it is using getAllmapsetNames;
    * There needs to be a getAllMapset() method added. For now, the funcitonality
    * Provided by the LoadControlle remains in place and the client side tets have
    * not been modified. This funcitonality will have to be built out later.
    * Also note that the resource name /maps is correct but does not match
    * what is being used in ResourceBuilder on the client side*/
    @RequestMapping(value = "/maps", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<MapsetDTO> getMaps(HttpServletRequest request,
                                              HttpServletResponse response) {

        PayloadEnvelope<MapsetDTO> returnVal = new PayloadEnvelope<>();
        try {

            List<MapsetDTO> mapsetDTOs = mapsetService.getAllMapsetNames();

            PayloadWriter<MapsetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MapsetDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MAPSET),
                    mapsetDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** PLATFORM METHODS
    // *********************************************

    @RequestMapping(value = "/platforms", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<PlatformDTO> createPlatform(@RequestBody PayloadEnvelope<PlatformDTO> payloadEnvelope,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<PlatformDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<PlatformDTO> payloadReader = new PayloadReader<>(PlatformDTO.class);
            PlatformDTO platformDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            PlatformDTO platformDTONew = platformService.createPlatform(platformDTOToCreate);

            PayloadWriter<PlatformDTO> payloadWriter = new PayloadWriter<>(request, response,
                    PlatformDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PLATFORM),
                    platformDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/platforms/{platformId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<PlatformDTO> replacePlatform(@RequestBody PayloadEnvelope<PlatformDTO> payloadEnvelope,
                                                        @PathVariable Integer platformId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        PayloadEnvelope<PlatformDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<PlatformDTO> payloadReader = new PayloadReader<>(PlatformDTO.class);
            PlatformDTO platformDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            PlatformDTO platformDTOReplaced = platformService.replacePlatform(platformId, platformDTOToReplace);

            PayloadWriter<PlatformDTO> payloadWriter = new PayloadWriter<>(request, response,
                    PlatformDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PLATFORM),
                    platformDTOReplaced);


        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/platforms", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<PlatformDTO> getPlatforms(HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<PlatformDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<PlatformDTO> payloadReader = new PayloadReader<>(PlatformDTO.class);
            List<PlatformDTO> platformDTOs = platformService.getPlatforms();

            PayloadWriter<PlatformDTO> payloadWriter = new PayloadWriter<>(request, response,
                    PlatformDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PLATFORM),
                    platformDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/platforms/{platformId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<PlatformDTO> getPlatformsById(@PathVariable Integer platformId,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {

        PayloadEnvelope<PlatformDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<PlatformDTO> payloadReader = new PayloadReader<>(PlatformDTO.class);
            PlatformDTO platformDTO = platformService.getPlatformById(platformId);

            PayloadWriter<PlatformDTO> payloadWriter = new PayloadWriter<>(request, response,
                    PlatformDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PLATFORM),
                    platformDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** PROJECT METHODS
    // *********************************************
    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ProjectDTO> createProject(@RequestBody PayloadEnvelope<ProjectDTO> payloadEnvelope,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<ProjectDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<ProjectDTO> payloadReader = new PayloadReader<>(ProjectDTO.class);
            ProjectDTO projectDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            ProjectDTO projectDTONew = projectService.createProject(projectDTOToCreate);


            PayloadWriter<ProjectDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ProjectDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROJECTS),
                    projectDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/projects/{projectId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ProjectDTO> replaceProject(@RequestBody PayloadEnvelope<ProjectDTO> payloadEnvelope,
                                                      @PathVariable Integer projectId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<ProjectDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ProjectDTO> payloadReader = new PayloadReader<>(ProjectDTO.class);
            ProjectDTO projectDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            ProjectDTO projectDTOReplaced = projectService.replaceProject(projectId, projectDTOToReplace);


            PayloadWriter<ProjectDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ProjectDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROJECTS),
                    projectDTOReplaced);
//

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ProjectDTO> getProjects(HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<ProjectDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<ProjectDTO> payloadReader = new PayloadReader<>(ProjectDTO.class);
            List<ProjectDTO> projectDTOs = projectService.getProjects();

            PayloadWriter<ProjectDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ProjectDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROJECTS),
                    projectDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/projects/{projectId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ProjectDTO> getProjectsById(@PathVariable Integer projectId,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<ProjectDTO> returnVal = new PayloadEnvelope<>();
        try {

            ProjectDTO projectDTO = projectService.getProjectById(projectId);

            PayloadWriter<ProjectDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ProjectDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROJECTS),
                    projectDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** EXPERIMENT METHODS
    // *********************************************
    @RequestMapping(value = "/experiments", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ExperimentDTO> createExperiment(@RequestBody PayloadEnvelope<ExperimentDTO> payloadEnvelope,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {

        PayloadEnvelope<ExperimentDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<ExperimentDTO> payloadReader = new PayloadReader<>(ExperimentDTO.class);
            ExperimentDTO exprimentDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            ExperimentDTO exprimentDTONew = experimentService.createExperiment(exprimentDTOToCreate);

            PayloadWriter<ExperimentDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ExperimentDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_EXPERIMENTS),
                    exprimentDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/experiments/{experimentId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ExperimentDTO> replaceExperiment(@RequestBody PayloadEnvelope<ExperimentDTO> payloadEnvelope,
                                                            @PathVariable Integer experimentId,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response) {

        PayloadEnvelope<ExperimentDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ExperimentDTO> payloadReader = new PayloadReader<>(ExperimentDTO.class);
            ExperimentDTO exprimentDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            ExperimentDTO exprimentDTOReplaced = experimentService.replaceExperiment(experimentId, exprimentDTOToReplace);


            PayloadWriter<ExperimentDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ExperimentDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_EXPERIMENTS),
                    exprimentDTOReplaced);


        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/experiments", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ExperimentDTO> getExperiments(HttpServletRequest request,
                                                         HttpServletResponse response) {

        PayloadEnvelope<ExperimentDTO> returnVal = new PayloadEnvelope<>();
        try {

            List<ExperimentDTO> experimentDTOs = experimentService.getExperiments();


            PayloadWriter<ExperimentDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ExperimentDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_EXPERIMENTS),
                    experimentDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/experiments/{experimentId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ExperimentDTO> getExperimentsById(@PathVariable Integer experimentId,
                                                             HttpServletRequest request,
                                                             HttpServletResponse response) {

        PayloadEnvelope<ExperimentDTO> returnVal = new PayloadEnvelope<>();
        try {

            ExperimentDTO experimentDTO = experimentService.getExperimentById(experimentId);

            PayloadWriter<ExperimentDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ExperimentDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_EXPERIMENTS),
                    experimentDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** PROTOCOL METHODS
    // *********************************************
    @RequestMapping(value = "/protocols", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ProtocolDTO> createProtocol(@RequestBody PayloadEnvelope<ProtocolDTO> payloadEnvelope,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<ProtocolDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<ProtocolDTO> payloadReader = new PayloadReader<>(ProtocolDTO.class);
            ProtocolDTO protocolDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            ProtocolDTO protocolDTONew = protocolService.createProtocol(protocolDTOToCreate);

            PayloadWriter<ProtocolDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ProtocolDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROTOCOL),
                    protocolDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/protocols/{protocolId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ProtocolDTO> replaceProtocol(@RequestBody PayloadEnvelope<ProtocolDTO> payloadEnvelope,
                                                        @PathVariable Integer protocolId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        PayloadEnvelope<ProtocolDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ProtocolDTO> payloadReader = new PayloadReader<>(ProtocolDTO.class);
            ProtocolDTO protocolDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            ProtocolDTO protocolDTOReplaced = protocolService.replaceProtocol(protocolId, protocolDTOToReplace);

            PayloadWriter<ProtocolDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ProtocolDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROTOCOL),
                    protocolDTOReplaced);
        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/protocols/{protocolId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ProtocolDTO> replaceProtocol(@PathVariable Integer protocolId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        PayloadEnvelope<ProtocolDTO> returnVal = new PayloadEnvelope<>();

        try {


            ProtocolDTO protocolDTO = protocolService.getProtocolById(protocolId);

            PayloadWriter<ProtocolDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ProtocolDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROTOCOL),
                    protocolDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/protocols", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ProtocolDTO> getProtocols(HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<ProtocolDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<ProtocolDTO> payloadReader = new PayloadReader<>(ProtocolDTO.class);
            List<ProtocolDTO> ProtocolDTOs = protocolService.getProtocols();

            PayloadWriter<ProtocolDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ProtocolDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROTOCOL),
                    ProtocolDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/protocols/{protocolId:[\\d]+}/vendors", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> addVendorToProtocol(@RequestBody PayloadEnvelope<OrganizationDTO> payloadEnvelope,
                                                                @PathVariable Integer protocolId,
                                                                HttpServletRequest request,
                                                                HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            OrganizationDTO organizationDTO = payloadReader.extractSingleItem(payloadEnvelope);

            OrganizationDTO protocolDTOAssociated = protocolService.addVendotrToProtocol(protocolId, organizationDTO);

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request, response,
                    OrganizationDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ORGANIZATION),
                    protocolDTOAssociated);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/protocols/{protocolId:[\\d]+}/vendors", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> updateOrReplaceVendorProtocol(@RequestBody PayloadEnvelope<OrganizationDTO> payloadEnvelope,
                                                                          @PathVariable Integer protocolId,
                                                                          HttpServletRequest request,
                                                                          HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            OrganizationDTO organizationDTO = payloadReader.extractSingleItem(payloadEnvelope);

            OrganizationDTO protocolDTOAssociated = protocolService.updateOrReplaceVendotrToProtocol(protocolId, organizationDTO);

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request, response,
                    OrganizationDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_ORGANIZATION),
                    protocolDTOAssociated);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/protocols/{protocolId:[\\d]+}/vendors", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> getVendorsForProtocol(@PathVariable Integer protocolId,
                                                                  HttpServletRequest request,
                                                                  HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();

        try {


            List<OrganizationDTO> organizationDTOs = this.protocolService.getVendorsForProtocolByProtocolId(protocolId);

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request, response,
                    OrganizationDTO.class);


            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceColl(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROTOCOL)
                            .addUriParam("protocolId")
                            .setParamValue("protocolId", protocolId.toString())
                            .appendSegment(GobiiServiceRequestId.URL_VENDORS)
                            .addUriParam("id"), // <-- this is the one that PayloadWriter will set based on the list
                    organizationDTOs);

        } catch (GobiiException e) {

            returnVal.getHeader().getStatus().addException(e);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/experiments/{experimentId:[\\d]+}/protocols", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ProtocolDTO> getProtocolByExperimentId(@PathVariable Integer experimentId,
                                                                  HttpServletRequest request,
                                                                  HttpServletResponse response) {

        PayloadEnvelope<ProtocolDTO> returnVal = new PayloadEnvelope<>();
        try {

            ProtocolDTO protocolDTO = protocolService.getProtocolsByExperimentId(experimentId);

            PayloadWriter<ProtocolDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ProtocolDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_PROTOCOL),
                    protocolDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** FILE PREVIEW METHODS
    // *********************************************


    @RequestMapping(value = "/files/loader/{directoryName}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<LoaderFilePreviewDTO> createLoaderFileDirectory(@PathVariable("directoryName") String directoryName,
                                                                           @RequestBody PayloadEnvelope<LoaderFilePreviewDTO> payloadEnvelope,
                                                                           HttpServletRequest request,
                                                                           HttpServletResponse response) {

        PayloadEnvelope<LoaderFilePreviewDTO> returnVal = new PayloadEnvelope<>();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            LoaderFilePreviewDTO loaderFilePreviewDTO = loaderFilesService.makeDirectory(cropType, directoryName);
            PayloadWriter<LoaderFilePreviewDTO> payloadWriter = new PayloadWriter<>(request, response,
                    LoaderFilePreviewDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_FILE_LOAD),
                    loaderFilePreviewDTO
            );

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/files/loader/{directoryName}",
            params = {"fileFormat"},
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<LoaderFilePreviewDTO> getFilePreviewBySearch(@PathVariable("directoryName") String directoryName,
                                                                        @RequestParam(value = "fileFormat", required = false) String fileFormat,
                                                                        HttpServletRequest request,
                                                                        HttpServletResponse response) {

        PayloadEnvelope<LoaderFilePreviewDTO> returnVal = new PayloadEnvelope<>();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            LoaderFilePreviewDTO loaderFilePreviewDTO = loaderFilesService.getPreview(cropType, directoryName, fileFormat);
            PayloadWriter<LoaderFilePreviewDTO> payloadWriter = new PayloadWriter<>(request, response,
                    LoaderFilePreviewDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_FILE_LOAD),
                    loaderFilePreviewDTO
            );

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    // *********************************************
    // *************************** MAPSETS METHODS
    // *********************************************

    @RequestMapping(value = "/mapsets", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<MapsetDTO> createMapset(@RequestBody PayloadEnvelope<MapsetDTO> payloadEnvelope,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<MapsetDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<MapsetDTO> payloadReader = new PayloadReader<>(MapsetDTO.class);
            MapsetDTO mapsetDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            MapsetDTO mapsetDTONew = mapsetService.createMapset(mapsetDTOToCreate);

            PayloadWriter<MapsetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MapsetDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MAPSET),
                    mapsetDTONew);
        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/mapsets/{mapsetId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<MapsetDTO> replaceMapset(@RequestBody PayloadEnvelope<MapsetDTO> payloadEnvelope,
                                                    @PathVariable Integer mapsetId,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {

        PayloadEnvelope<MapsetDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<MapsetDTO> payloadReader = new PayloadReader<>(MapsetDTO.class);
            MapsetDTO mapsetDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            MapsetDTO mapsetDTOReplaced = mapsetService.replaceMapset(mapsetId, mapsetDTOToReplace);

            PayloadWriter<MapsetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MapsetDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MAPSET),
                    mapsetDTOReplaced);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/mapsets", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<MapsetDTO> getMapsets(HttpServletRequest request,
                                                 HttpServletResponse response) {

        PayloadEnvelope<MapsetDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<MapsetDTO> mapsetDTOs = mapsetService.getMapsets();

            PayloadWriter<MapsetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MapsetDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MAPSET),
                    mapsetDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/mapsets/{mapsetId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<MapsetDTO> getMapsetById(@PathVariable Integer mapsetId,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {

        PayloadEnvelope<MapsetDTO> returnVal = new PayloadEnvelope<>();

        try {

            MapsetDTO mapsetDTO = mapsetService.getMapsetById(mapsetId);

            PayloadWriter<MapsetDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MapsetDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MAPSET),
                    mapsetDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** MARKERGROUP METHODS
    // *********************************************

    @RequestMapping(value = "/markergroups", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<MarkerGroupDTO> createMarkerGroup(@RequestBody PayloadEnvelope<MarkerGroupDTO> payloadEnvelope,
                                                             HttpServletRequest request,
                                                             HttpServletResponse response) {

        PayloadEnvelope<MarkerGroupDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<MarkerGroupDTO> payloadReader = new PayloadReader<>(MarkerGroupDTO.class);
            MarkerGroupDTO markerGroupDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            MarkerGroupDTO markerGroupDTONew = markerGroupService.createMarkerGroup(markerGroupDTOToCreate);

            PayloadWriter<MarkerGroupDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MarkerGroupDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MARKERGROUP),
                    markerGroupDTONew);
        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/markergroups/{markerGroupId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<MarkerGroupDTO> replaceMarkerGroup(@RequestBody PayloadEnvelope<MarkerGroupDTO> payloadEnvelope,
                                                              @PathVariable Integer markerGroupId,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) {

        PayloadEnvelope<MarkerGroupDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<MarkerGroupDTO> payloadReader = new PayloadReader<>(MarkerGroupDTO.class);
            MarkerGroupDTO markerGroupDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            MarkerGroupDTO markerGroupDTOReplaced = markerGroupService.replaceMarkerGroup(markerGroupId, markerGroupDTOToReplace);

            PayloadWriter<MarkerGroupDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MarkerGroupDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MARKERGROUP),
                    markerGroupDTOReplaced);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/markergroups", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<MarkerGroupDTO> getMarkerGroups(HttpServletRequest request,
                                                           HttpServletResponse response) {

        PayloadEnvelope<MarkerGroupDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<MarkerGroupDTO> markerGroupDTOs = markerGroupService.getMarkerGroups();

            PayloadWriter<MarkerGroupDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MarkerGroupDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MARKERGROUP),
                    markerGroupDTOs);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/markergroups/{markerGroupId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<MarkerGroupDTO> getMarkerGroupById(@PathVariable Integer markerGroupId,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) {

        PayloadEnvelope<MarkerGroupDTO> returnVal = new PayloadEnvelope<>();

        try {

            MarkerGroupDTO markerGroupDTO = markerGroupService.getMarkerGroupById(markerGroupId);

            PayloadWriter<MarkerGroupDTO> payloadWriter = new PayloadWriter<>(request, response,
                    MarkerGroupDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_MARKERGROUP),
                    markerGroupDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** REFERENCE METHODS
    // *********************************************

    @RequestMapping(value = "/references", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ReferenceDTO> createReference(@RequestBody PayloadEnvelope<ReferenceDTO> payloadEnvelope,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {

        PayloadEnvelope<ReferenceDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ReferenceDTO> payloadReader = new PayloadReader<>(ReferenceDTO.class);
            ReferenceDTO referenceDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            ReferenceDTO referenceDTONew = referenceService.createReference(referenceDTOToCreate);

            PayloadWriter<ReferenceDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ReferenceDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_REFERENCE),
                    referenceDTONew);
        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/references/{referenceId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ReferenceDTO> replaceReference(@RequestBody PayloadEnvelope<ReferenceDTO> payloadEnvelope,
                                                          @PathVariable Integer referenceId,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {

        PayloadEnvelope<ReferenceDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ReferenceDTO> payloadReader = new PayloadReader<>(ReferenceDTO.class);
            ReferenceDTO referenceDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            ReferenceDTO referenceDTOReplaced = referenceService.replaceReference(referenceId, referenceDTOToReplace);

            PayloadWriter<ReferenceDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ReferenceDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_REFERENCE),
                    referenceDTOReplaced);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/references", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ReferenceDTO> getReferences(HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<ReferenceDTO> returnVal = new PayloadEnvelope<>();

        try {

            List<ReferenceDTO> referenceDTOS = referenceService.getReferences();

            PayloadWriter<ReferenceDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ReferenceDTO.class);

            payloadWriter.writeList(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_REFERENCE),
                    referenceDTOS);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);
    }

    @RequestMapping(value = "/references/{referenceId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ReferenceDTO> getReferenceById(@PathVariable Integer referenceId,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {

        PayloadEnvelope<ReferenceDTO> returnVal = new PayloadEnvelope<>();

        try {

            ReferenceDTO referenceDTO = referenceService.getReferenceById(referenceId);

            PayloadWriter<ReferenceDTO> payloadWriter = new PayloadWriter<>(request, response,
                    ReferenceDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    GobiiUriFactory.resourceByUriIdParam(request.getContextPath(),
                            GobiiServiceRequestId.URL_REFERENCE),
                    referenceDTO);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** FILE UPLOAD/DOWNLOAD
    // *********************************************
    @RequestMapping(value = "/files/{gobiiJobId}/{destinationType}",
            params = {"fileName"},
            method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFileHandler(@PathVariable("gobiiJobId") String gobiiJobId,
                             @PathVariable("destinationType") String destinationType,
                             @RequestParam("fileName") String fileName,
                             @RequestParam("file") MultipartFile file,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception{

        String name = file.getName();


        //we aren't using jobId here yet. For some destination types it will be required
        //for example, if we wanted to put files into the extractor/output directory, we would need
        //to use the jobid. But we don't suppor that use case yet.

        Enumeration<String> headers = request.getHeaders("Content-Disposition");

        if (!file.isEmpty()) {
            try {

                byte[] byteArray = file.getBytes();

                String cropType = CropRequestAnalyzer.getGobiiCropType(request);
                GobiiFileProcessDir gobiiFileProcessDir = GobiiFileProcessDir.valueOf(destinationType);

                this.fileService
                        .writeFile(cropType,
                                gobiiJobId,
                                fileName,
                                gobiiFileProcessDir,
                                byteArray);

            } catch (Exception e) {
                ControllerUtils.writeRawResponse(response,
                        HttpServletResponse.SC_NOT_ACCEPTABLE,
                        e.getMessage());
                LOGGER.error("Error uploading file",e);
            }

        } else {

            String message = "You failed to upload because the file was empty.";
            ControllerUtils.writeRawResponse(response,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    message);
            LOGGER.error("Error uploading file",message);

        }

        // this method has to return _something_ in order for a content-type to be set in the response (this makes
        // our client framework happy)
        return "";
    }

    @RequestMapping(value = "/files/{gobiiJobId}/{destinationType}",
            method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFileHandler(@PathVariable("gobiiJobId") String gobiiJobId,
                                                                   @PathVariable("destinationType") String destinationType,
                                                                   @RequestParam("fileName") String fileName,
                                                                   HttpServletRequest request,
                                                                   HttpServletResponse response) throws Exception {

        ResponseEntity<InputStreamResource> returnVal = null;
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            GobiiFileProcessDir gobiiFileProcessDir = GobiiFileProcessDir.valueOf(destinationType.toUpperCase());
            File file = this.fileService.readFile(cropType, gobiiJobId, fileName, gobiiFileProcessDir);

            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setContentLength(file.length());
            respHeaders.setContentDispositionFormData("attachment", fileName);

            InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
            returnVal = new ResponseEntity<>(inputStreamResource, respHeaders, HttpStatus.OK);

        } catch (Exception e) {

            ControllerUtils.writeRawResponse(response,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    e.getMessage());
            LOGGER.error("Error downloading file", e);

        }

        return returnVal;

    }


}// GOBIIController
