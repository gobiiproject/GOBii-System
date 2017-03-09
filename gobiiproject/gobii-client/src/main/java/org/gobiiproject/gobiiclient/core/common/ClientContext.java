package org.gobiiproject.gobiiclient.core.common;

import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiiclient.core.gobii.GobiiPayloadResponse;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 5/13/2016.
 */
public final class ClientContext {


    private static Logger LOGGER = LoggerFactory.getLogger(ClientContext.class);

    // configure as a singleton
    // this may not be effective if more thn one classloader is used
    private static ClientContext clientContext = null;
    private static String sshOverrideHost = null;
    private static Integer sshOverridePort = null;

    public static void setSshOverride(String sshOverrideHost, Integer sshOverridePort) throws Exception {

        if (null == sshOverrideHost) {
            throw new Exception("SSH port and host must be specified");
        }

        if (null == sshOverridePort) {
            throw new Exception("SSH host and port must be specified");
        }

        ClientContext.sshOverrideHost = sshOverrideHost;
        ClientContext.sshOverridePort = sshOverridePort;
    }


    public static boolean isInitialized() {
        return null != clientContext;
    }

    public synchronized static void resetConfiguration() {

        clientContext = null;
        sshOverrideHost = null;
        sshOverridePort = null;
    }


    public synchronized static ClientContext getInstance(ConfigSettings configSettings,
                                                         String cropType) throws Exception {

        if (null == clientContext) {

            if (null == configSettings) {
                throw new Exception("Client context cannot be null!");
            }

            clientContext = new ClientContext();
            clientContext.fileSystemRoot = configSettings.getFileSystemRoot();

            if (LineUtils.isNullOrEmpty(cropType)) {
                clientContext.defaultGobiiCropType = configSettings.getDefaultGobiiCropType();
                clientContext.currentGobiiCropType = clientContext.defaultGobiiCropType;
            } else {
                clientContext.defaultGobiiCropType = cropType;
                clientContext.currentGobiiCropType = cropType;
            }

            for (CropConfig currentCropConfig : configSettings.getActiveCropConfigs()) {

                ServerConfig currentServerConfig = new ServerConfig(currentCropConfig,
                        configSettings.getProcessingPath(currentCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS),
                        configSettings.getProcessingPath(currentCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.LOADER_INSTRUCTIONS),
                        configSettings.getProcessingPath(currentCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES),
                        configSettings.getProcessingPath(currentCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.RAW_USER_FILES)
                );

                clientContext.serverConfigs.put(currentCropConfig.getGobiiCropType(),
                        currentServerConfig);
            }
        }

        return clientContext;

    }// getInstance


    public synchronized static ClientContext getInstance(String gobiiURL, boolean initConfigFromServer) throws Exception {

        if (null == clientContext) {

            if (initConfigFromServer) {


                if (!LineUtils.isNullOrEmpty(gobiiURL)) {

                    if ('/' != gobiiURL.charAt(gobiiURL.length() - 1)) {
                        gobiiURL = gobiiURL + '/';
                    }

                    URL url = null;
                    try {
                        url = new URL(gobiiURL);
                    } catch (Exception e) {
                        throw new Exception("Error retrieving server configuration due to invalid url: "
                                + e.getMessage()
                                + "; url must be in this format: http://host:port/context-root");
                    }

                    clientContext = (new ClientContext()).makeFromServer(url);

                } else {
                    throw new Exception("initConfigFromServer is specfied, but the gobiiUrl parameter is null or empty");
                }

            } else {

                throw new Exception("Client configuration must be initialized from a web server url or fqpn to gobii properties file");
            }

            clientContext.gobiiCropTypes.addAll(clientContext
                    .serverConfigs
                    .keySet());
        }

        return clientContext;
    }

    private ClientContext makeFromServer(URL url) throws Exception {

        ClientContext returnVal = new ClientContext();

        String host = url.getHost();
        String context = url.getPath();
        Integer port = url.getPort();


        if (LineUtils.isNullOrEmpty(host)) {
            throw new Exception("The specified URL does not contain a host name: " + url.toString());
        }

        if (LineUtils.isNullOrEmpty(context)) {
            throw new Exception("The specified URL does not specify the context path for the Gobii server : " + url.toString());
        }

        if (port <= 0) {
            throw new Exception("The specified URL does not contain a valid port id: " + url.toString());
        }

        // first authenticate
        // you can't use login() from here -- it assumes that ClientContext has already been constructed
        String authPath = ServiceRequestId.URL_AUTH.getRequestUrl(context, ControllerType.GOBII);
        HttpCore httpCore = new HttpCore(host, port, null);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        returnVal.userToken = httpCore.getTokenForUser(authPath, userDetail.getUserName(), userDetail.getPassword());

        // now get the settings
        String settingsPath = ServiceRequestId.URL_CONFIGSETTINGS.getRequestUrl(context,ControllerType.GOBII);

        RestUri configSettingsUri = new UriFactory(null).RestUriFromUri(settingsPath);
        HttpMethodResult httpMethodResult = httpCore.get(configSettingsUri, returnVal.userToken);
        GobiiPayloadResponse<ConfigSettingsDTO> gobiiPayloadResponse = new GobiiPayloadResponse<>(configSettingsUri);

        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = gobiiPayloadResponse.getPayloadFromResponse(ConfigSettingsDTO.class,
                RestMethodTypes.GET,
                HttpStatus.SC_OK,
                httpMethodResult);

        if (resultEnvelope.getHeader().getStatus().isSucceeded()) {

            ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);
            returnVal.defaultGobiiCropType = configSettingsDTOResponse.getDefaultCrop();
            returnVal.serverConfigs = configSettingsDTOResponse.getServerConfigs();

        } else {
            throw new Exception("Unable to get server configuration from "
                    + settingsPath
                    + "; the following error occurred: "
                    + resultEnvelope.getHeader().getStatus().getStatusMessages().get(0).getMessage());
        }

        return returnVal;
    }


    private ClientContext() throws Exception {

    }

    public enum ProcessMode {Asynch, Block}

    String fileSystemRoot;

    private Map<String, ServerConfig> serverConfigs = new HashMap<>();


    private ProcessMode processMode = ProcessMode.Asynch;
    private String userToken = null;

    String currentGobiiCropType;

    String defaultGobiiCropType;
    List<String> gobiiCropTypes = new ArrayList<>();


    private ServerConfig getServerConfig() throws Exception {

        ServerConfig returnVal;

        String cropType;
        if (!LineUtils.isNullOrEmpty(currentGobiiCropType)) {
            cropType = this.currentGobiiCropType;
        } else {
            cropType = this.defaultGobiiCropType;
        }

        returnVal = this.getServerConfigByCropType(cropType);

        return returnVal;
    }

    public UriFactory getUriFactory() throws Exception {

        String contextPath = this.getServerConfig().getContextRoot();
        return new UriFactory(contextPath);
    }

    public UriFactory getUriFactory(ControllerType controllerType) throws Exception {

        String contextPath = this.getServerConfig().getContextRoot();
        return new UriFactory(contextPath, controllerType);
    }


    public String getCurrentCropDomain() throws Exception {

        String returnVal;

        if (null == sshOverrideHost) {
            returnVal = this.getServerConfig().getDomain();
        } else {
            returnVal = sshOverrideHost;
        }

        return returnVal;
    }

    public String getCurrentCropContextRoot() throws Exception {
        return this.getServerConfigByCropType(this.currentGobiiCropType).getContextRoot();
    }

    public String getCropContextRoot(String cropType) throws Exception {

        String returnVal;

        returnVal = this.getServerConfigByCropType(cropType).getContextRoot();

        return returnVal;
    }


    public Integer getCurrentCropPort() throws Exception {

        Integer returnVal;

        if (null == sshOverridePort) {

            returnVal = this.getServerConfigByCropType(this.currentGobiiCropType).getPort();
        } else {
            returnVal = sshOverridePort;
        }

        return returnVal;
    }


    public List<String> getCropTypeTypes() {
        return gobiiCropTypes;
    }


    public String getCurrentClientCropType() {
        return this.currentGobiiCropType;
    }

    public void setCurrentClientCrop(String currentClientCrop) {
        this.currentGobiiCropType = currentClientCrop;
    }

    public String getDefaultCropType() {
        return this.defaultGobiiCropType;
    }

    public String getFileLocationOfCurrenCrop(GobiiFileProcessDir gobiiFileProcessDir) throws Exception {
        return this.getServerConfigByCropType(this.currentGobiiCropType)
                .getFileLocations()
                .get(gobiiFileProcessDir);
    }

    public boolean login(String userName, String password) throws Exception {
        boolean returnVal = true;

        try {
            String authUrl = ServiceRequestId.URL_AUTH
                    .getRequestUrl(this.getCurrentCropContextRoot(),
                    ControllerType.GOBII);

            HttpCore httpCore = new HttpCore(this.getCurrentCropDomain(),
                    this.getCurrentCropPort(),
                    this.getCurrentCropContextRoot());

            userToken = httpCore.getTokenForUser(authUrl, userName, password);
        } catch (Exception e) {
            LOGGER.error("Authenticating", e);
            throw new Exception(e);
        }

        return returnVal;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getFileSystemRoot() {
        return fileSystemRoot;
    }

    private ServerConfig getServerConfigByCropType(String cropType) throws Exception {

        ServerConfig returnVal;

        if (!this.serverConfigs.containsKey(cropType)) {
            throw new Exception("No server configuration is defined for crop: " + cropType);
        }

        returnVal = this.serverConfigs.get(cropType);

        return returnVal;
    }
}
