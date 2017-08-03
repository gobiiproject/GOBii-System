package org.gobiiproject.gobiiclient.core.gobii;

import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.HttpCore;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.ServerCapabilityType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * This class encapsulates the configuration data and http component that Java clients
 * will require in order to connect to gobii web services. In the nominal case, configuration
 * data are retrieved from  web services via a resource that does not require authentication, whilst all
 * other gobii web service calls require authentication. In the marked case, a ConfigSettings instance is
 * provided to the client context, requiring that a configuration file be accessible to the caller
 * on its local file system. Each of these approaches has its own getInstance() method.
 * The local configuration method is used by the Digester/Extractor processes and loaderUI.
 * Once a getInstance() method is called, the client context is populated with a list of configuration
 * settings indexed by cropId. The cropId uniquely identifies a GOBII server with respect to its destination
 * address. In a prior version of this class, the user could arbitrarily determine the "current" crop
 * by setting the crop ID. Moreover, there was also the concept of a default crop. This flexibility
 * led to some significant and hard to trace bugs, where a request for a crop would correctly go to
 * that crops context path (e.g., gobii-dev) but to the port of a different instance. In the new
 * dispensation, there are two, and only two ways, in which the "current" crop ID can be set:
 * 1) Through the getInstance() method that takes a ConfigurationSettings instance: in these cases,
 *    we want to do the authentication in the getInstance() method so that our Java clients needn't
 *    be troubled by that detail. Accordingly, that version of getInstance() does a login to the
 *    server corresponding to the specified crop;
 * 2) Through the login() method: for all other clients, the flow of control is:
 *    a) Use the getInstance() method that takes a configuration URL to populate the configuration list;
 *    b) The user selects a desired crop from the list;
 *    c) The user logs in with the login() method, specifying the selected crop and credentials.
 *
 * Thus, when a different crop is desired, the user will have to authenticate specifically to the server
 * for that crop, and in this case the HttpCore instance (which knows the domain and port of the server
 * along with the authentication token) is reset. Corresponding to this change is a new approach to
 * database connection selection on the server. In the previous dispensation, it was possible for a
 * client to authenticate to a particular server instance, but specify an arbitrary crop database
 * by specifying the cropId in the HTTP headers. The server would use the header-specified cropId
 * to select the database connection corresponding to that crop's database. This approach, again,
 * led to some difficult bugs. In the new dispensation, a server will use only the database connection
 * for the server as it is identified by the context path. That is to say, when the server gets a request,
 * it will look up the server instance from configuration by the context path that the server identifies
 * in the request URL. The cropId for the server instance identified by these means is then use to
 * select the database instance for that crop. In other words, a context path (e.g., gobii-maize) is now
 * exclusively identified with the database for that configuration's database. Client no longer includes
 * a cropId in the HTTP request headers. However, the server continues to return the identified cropId
 * in the response headers for informational purposes.
 *
 */
public final class GobiiClientContext {


    private static Logger LOGGER = LoggerFactory.getLogger(GobiiClientContext.class);

    // configure as a singleton
    // this may not be effective if more thn one classloader is used
    private static GobiiClientContext gobiiClientContext = null;
    private static String sshOverrideHost = null;
    private static Integer sshOverridePort = null;

    public static void setSshOverride(String sshOverrideHost, Integer sshOverridePort) throws Exception {

        if (null == sshOverrideHost) {
            throw new Exception("SSH port and host must be specified");
        }

        if (null == sshOverridePort) {
            throw new Exception("SSH host and port must be specified");
        }

        GobiiClientContext.sshOverrideHost = sshOverrideHost;
        GobiiClientContext.sshOverridePort = sshOverridePort;
    }


    public static boolean isInitialized() {
        return null != gobiiClientContext;
    }

    public synchronized static void resetConfiguration() {

        gobiiClientContext = null;
        sshOverrideHost = null;
        sshOverridePort = null;

    }


    public synchronized static GobiiClientContext getInstance(ConfigSettings configSettings,
                                                              String cropId, GobiiAutoLoginType gobiiAutoLoginType) throws Exception {

        if (null == gobiiClientContext) {

            if (LineUtils.isNullOrEmpty(cropId)) {
                throw new Exception("Crop ID must not be null");
            }

            if (null == configSettings) {
                throw new Exception("Client context cannot be null!");
            }

            gobiiClientContext = new GobiiClientContext();
            gobiiClientContext.cropId = cropId;
            gobiiClientContext.fileSystemRoot = configSettings.getFileSystemRoot();
            gobiiClientContext.serverCapabilities = configSettings.getServerCapabilities();

            for (GobiiCropConfig currentGobiiCropConfig : configSettings.getActiveCropConfigs()) {

                ServerConfig currentServerConfig = new ServerConfig(currentGobiiCropConfig,
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS),
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.LOADER_INSTRUCTIONS),
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES),
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.RAW_USER_FILES)
                );

                gobiiClientContext.serverConfigs.put(currentGobiiCropConfig.getGobiiCropType(),
                        currentServerConfig);
            }

            gobiiClientContext.gobiiCropTypes.addAll(gobiiClientContext
                    .serverConfigs
                    .keySet());

            if (gobiiAutoLoginType != GobiiAutoLoginType.UNKNOWN) {

                String userName = null;
                String password = null;

                if (gobiiAutoLoginType == GobiiAutoLoginType.USER_RUN_AS) {

                    userName = configSettings.getLdapUserForBackendProcs();
                    password = configSettings.getLdapPasswordForBackendProcs();

                } else if (gobiiAutoLoginType == GobiiAutoLoginType.USER_TEST) {
                    userName = configSettings.getTestExecConfig().getLdapUserForUnitTest();
                    password = configSettings.getTestExecConfig().getLdapPasswordForUnitTest();
                } else {
                    throw new Exception("Unknown gobiiAutoLoginType: " + gobiiAutoLoginType.toString());
                }

                if (!LineUtils.isNullOrEmpty(userName) && !LineUtils.isNullOrEmpty(password)) {
                    if (!gobiiClientContext.login(cropId, userName, password)) {
                        throw new Exception("Login with auth type "
                                + gobiiAutoLoginType.toString()
                                + " failed: "
                                + gobiiClientContext.getLoginFailure());
                    }
                }
            }
        }

        return gobiiClientContext;

    }// getInstance


    public synchronized static GobiiClientContext getInstance(String gobiiURL, boolean initConfigFromServer) throws Exception {

        if (null == gobiiClientContext) {

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

                    gobiiClientContext = (new GobiiClientContext()).initServerConfigsFromAnonymousAccess(url);

                } else {
                    throw new Exception("initConfigFromServer is specfied, but the gobiiUrl parameter is null or empty");
                }

            } else {

                throw new Exception("Client configuration must be initialized from a web server url or fqpn to gobii properties file");
            }

            gobiiClientContext.gobiiCropTypes.addAll(gobiiClientContext
                    .serverConfigs
                    .keySet());
        }

        return gobiiClientContext;
    }

    private GobiiClientContext initServerConfigsFromAnonymousAccess(URL url) throws Exception {

        GobiiClientContext returnVal = new GobiiClientContext();

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

        // The /configsettings resource does not require authentication
        // this should be the only case in which we don't provide a crop ID
        HttpCore httpCore = new HttpCore(host, port);
        String settingsPath = GobiiServiceRequestId.URL_CONFIGSETTINGS.getRequestUrl(context, GobiiControllerType.GOBII.getControllerPath());

        RestUri configSettingsUri = new GobiiUriFactory(null).RestUriFromUri(settingsPath);
        HttpMethodResult httpMethodResult = httpCore.get(configSettingsUri);

        GobiiPayloadResponse<ConfigSettingsDTO> gobiiPayloadResponse = new GobiiPayloadResponse<>(configSettingsUri);
        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = gobiiPayloadResponse.getPayloadFromResponse(ConfigSettingsDTO.class,
                RestMethodTypes.GET,
                HttpStatus.SC_OK,
                httpMethodResult);

        if (resultEnvelope.getHeader().getStatus().isSucceeded()) {

            ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);
            returnVal.serverConfigs = configSettingsDTOResponse.getServerConfigs();
            returnVal.serverCapabilities = configSettingsDTOResponse.getServerCapabilities();

        } else {
            throw new Exception("Unable to get server configuration from "
                    + settingsPath
                    + "; the following error occurred: "
                    + resultEnvelope.getHeader().getStatus().getStatusMessages().get(0).getMessage());
        }

        return returnVal;
    }


    private GobiiClientContext() throws Exception {

    }

    public enum ProcessMode {Asynch, Block}

    String fileSystemRoot;

    private Map<String, ServerConfig> serverConfigs = new HashMap<>();
    private Map<ServerCapabilityType, Boolean> serverCapabilities = new HashMap<>();



    String cropId;

    List<String> gobiiCropTypes = new ArrayList<>();


    /***
     * Gets the ServerConfig for the current cropId
     * @return
     * @throws Exception
     */
    private ServerConfig getServerConfig() throws Exception {

        ServerConfig returnVal;
        returnVal = this.getServerConfig(this.cropId);
        return returnVal;
    }

    /***
     * This method only returns a ServerConfig, if it exists. It does not change the state of this instance of
     * GobiiClientContext with respect to the current cropId. In other words, when you call this method, you get
     * the ServerConfig, but the context and the HttpCore that it encapsulates still reference the server for the
     * cropId with which login() was called.
     * @param cropId: The ID of the crop per the configuration file
     * @return
     * @throws Exception if cropoId is null or is not a crop defined in the configuration
     */
    public ServerConfig getServerConfig(String cropId) throws Exception {

        ServerConfig returnVal;

        if (LineUtils.isNullOrEmpty(cropId)) {
            throw new Exception("Unable to get server config: A cropID was never set for this context");
        }

        if (!this.serverConfigs.containsKey(cropId)) {
            throw new Exception("No server configuration is defined for crop: " + this.cropId);
        }

        returnVal = this.serverConfigs.get(cropId);

        return returnVal;

    }

    public Map<ServerCapabilityType, Boolean> getServerCapabilities() {
        return serverCapabilities;
    }

    public void setServerCapabilities(Map<ServerCapabilityType, Boolean> serverCapabilities) {
        this.serverCapabilities = serverCapabilities;
    }

    public GobiiUriFactory getUriFactory() throws Exception {

        String contextPath = this.getServerConfig().getContextRoot();
        return new GobiiUriFactory(contextPath);
    }

    public GobiiUriFactory getUriFactory(GobiiControllerType gobiiControllerType) throws Exception {

        String contextPath = this.getServerConfig().getContextRoot();
        return new GobiiUriFactory(contextPath, gobiiControllerType);
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
        return this.getServerConfig().getContextRoot();
    }

    public String getCropContextRoot(String cropType) throws Exception {

        String returnVal;

        returnVal = this.getServerConfig().getContextRoot();

        return returnVal;
    }


    public Integer getCurrentCropPort() throws Exception {

        Integer returnVal;

        if (null == sshOverridePort) {

            returnVal = this.getServerConfig().getPort();
        } else {
            returnVal = sshOverridePort;
        }

        return returnVal;
    }


    public List<String> getCropTypeTypes() {
        return gobiiCropTypes;
    }


    public String getCurrentClientCropType() {
        return this.cropId;
    }

    public String getFileLocationOfCurrenCrop(GobiiFileProcessDir gobiiFileProcessDir) throws Exception {
        return this.getServerConfig()
                .getFileLocations()
                .get(gobiiFileProcessDir);
    }

//    public void setCurrentCropId(String cropId) throws Exception {
//
//        if (!this.gobiiCropTypes.contains(cropId)) {
//            throw new Exception("The specified cropId does not exist");
//        }
//
//        this.cropId = cropId;
//    }


    private HttpCore httpCore = null;

    public HttpCore getHttp() throws Exception {

        if (this.httpCore == null) {
            throw new Exception("The http instance has not been initialized: you must call login() ");
        }

        return this.httpCore;
    }


    public String loginFailure;

    public String getLoginFailure() {
        return loginFailure;
    }

    /**
     * Authenticates the user and sets the token for subseqeunt requests. If the return
     * value is false, getLoginFailure() will indicate the reason that the login() failed.
     *
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public boolean login(String cropId, String userName, String password) throws Exception {

        boolean returnVal = false;

        if (LineUtils.isNullOrEmpty(cropId)) {
            throw new Exception("CropId must not be null");
        }

        if (!this.gobiiCropTypes.contains(cropId)) {
            throw new Exception("The requested crop does not exist in the configuration: " + cropId);
        }

        this.cropId = cropId;

        try {
            String authUrl = GobiiServiceRequestId.URL_AUTH
                    .getRequestUrl(this.getCurrentCropContextRoot(),
                            GobiiControllerType.GOBII.getControllerPath());

            RestUri authUri = this.getUriFactory().RestUriFromUri(authUrl);

            this.httpCore = new HttpCore(this.getCurrentCropDomain(),
                    this.getCurrentCropPort());


            HttpMethodResult httpMethodResult = this.getHttp().authenticateWithUser(authUri, userName, password);

            if (httpMethodResult.getResponseCode() == HttpStatus.SC_OK) {
                returnVal = true;

            } else {
                this.loginFailure = httpMethodResult.getResponseCode()
                        + ": ";

                if (!LineUtils.isNullOrEmpty(httpMethodResult.getMessage())) {
                    this.loginFailure += httpMethodResult.getMessage();
                } else {
                    this.loginFailure += httpMethodResult.getReasonPhrase();
                }
            }
        } catch (Exception e) {
            LOGGER.error("Authenticating", e);
            throw new Exception(e);
        }

        return returnVal;
    }

    public String getUserToken() throws Exception {
        return this.getHttp().getToken();
    }

    public String setUserToken(String token) throws Exception {
        return this.getHttp().setToken(token);
    }

    public String getFileSystemRoot() {
        return fileSystemRoot;
    }


}
