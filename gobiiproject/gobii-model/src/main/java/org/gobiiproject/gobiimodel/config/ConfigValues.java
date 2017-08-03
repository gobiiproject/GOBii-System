package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.GobiiAuthenticationType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * This class is essentially a POJO for the configuration data. It has a small
 * amount of logic for retrieval of directories. IN particuarl, given a GobiiFileProcessDir
 * value, it will provide the appropriate path for specific file processing locations. The properties
 * of this class are annotated with the simpleframework XML annotations for the purpose of
 * serialization. Aside from the collection of GobiiCropConfig instances, the proeprties of this class
 * are global to the configuration. There will be one GobiiCropConfig instance for every crop supported
 * by a given deployment.
 */
class ConfigValues {

    @Element(required = false)
    private TestExecConfig testExecConfig = new TestExecConfig();

    @Element(required=false)
    private ServerConfigKDC serverConfigKDC = new ServerConfigKDC();


    @ElementMap(required = false)
    private Map<String, GobiiCropConfig> cropConfigs = new LinkedHashMap<>();

    @ElementMap(required = false)
    private Map<GobiiFileProcessDir, String> relativePaths = new EnumMap<GobiiFileProcessDir, String>(GobiiFileProcessDir.class) {{

        // these defaults should generally not be changed
        // note that they will be appended to the crops root directory
        put(GobiiFileProcessDir.RAW_USER_FILES, "files/");
        put(GobiiFileProcessDir.HDF5_FILES, "hdf5/");
        put(GobiiFileProcessDir.LOADER_INSTRUCTIONS, "loader/instructions/");
        put(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES, "loader/digest/");
        put(GobiiFileProcessDir.LOADER_INPROGRESS_FILES, "loader/inprogress/");
        put(GobiiFileProcessDir.LOADER_DONE, "loader/done/");
        put(GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS, "extractor/instructions/");
        put(GobiiFileProcessDir.EXTRACTOR_INPROGRESS, "extractor/inprogress/");
        put(GobiiFileProcessDir.EXTRACTOR_DONE, "extractor/done/");
        put(GobiiFileProcessDir.EXTRACTOR_OUTPUT, "extractor/output/");
        put(GobiiFileProcessDir.QC_OUTPUT, "loader/qc/");

    }};

    private String currentGobiiCropType;

    @Element(required = false)
    private String emailSvrType;

    @Element(required = false)
    private String emailSvrDomain;

    @Element(required = false)
    private String emailSvrUser;

    @Element(required = false)
    private String emailSvrHashType;

    @Element(required = false)
    private String emailSvrPassword;

    @Element(required = false)
    private Integer emailServerPort = 0;

    @Element(required = false)
    private GobiiAuthenticationType gobiiAuthenticationType = GobiiAuthenticationType.TEST;

    @Element(required = false)
    private String ldapUserDnPattern;

    @Element(required = false)
    private String ldapUrl;

    @Element(required = false)
    private String ldapBindUser;

    @Element(required = false)
    private String ldapBindPassword;

    @Element(required = false)
    private boolean iflIntegrityCheck = false;

    @Element(required = false)
    private String fileSystemRoot;

    @Element(required = false)
    private String fileSysCropsParent = "crops/";

    @Element(required = false)
    private String fileSystemLog;

    @Element(required = false)
    private boolean isDecrypt = false;

    @Element(required = false)
    private String ldapUserForBackendProcs;

    @Element(required = false)
    private String ldapPasswordForBackendProcs;

    @Element(required = false)
    private boolean isAuthenticateBrapi = true;

    public TestExecConfig getTestExecConfig() {
        return testExecConfig;
    }

    public ServerConfigKDC getKDCConfig() {
        return serverConfigKDC;
    }

    public void setTestExecConfig(TestExecConfig testExecConfig) {
        this.testExecConfig = testExecConfig;
    }

    public boolean isCropDefined(String gobiiCropType) {
        return this.getCropConfigs().containsKey(gobiiCropType);
    }

    public GobiiCropConfig getCropConfig(String gobiiCropType) throws Exception {

        GobiiCropConfig returnVal = null;

        if (!getCropConfigs().containsKey(gobiiCropType)) {
            throw new Exception("There is no configuration defined for crop " + gobiiCropType);
        }

        returnVal = getCropConfigs().get(gobiiCropType);

        return returnVal;
    }


    public String getProcessingPath(String cropType, GobiiFileProcessDir gobiiFileProcessDir) throws Exception {

        String returnVal;

        if (!cropConfigs.containsKey(cropType)) {
            throw new Exception("Unknown crop type: " + cropType);
        }

        String cropRoot = this.getFileSysCropsParent();
        String crop = LineUtils.terminateDirectoryPath(cropType);
        String relativePath = LineUtils.terminateDirectoryPath(relativePaths.get(gobiiFileProcessDir));

        returnVal = cropRoot + crop + relativePath;

        returnVal = returnVal.toLowerCase();

        return returnVal;
    } //

    public List<GobiiCropConfig> getActiveCropConfigs() throws Exception {


        return getCropConfigs()
                .values()
                .stream()
                .filter(c -> c.isActive())
                .collect(Collectors.toList());
    }

    public GobiiCropConfig getCurrentCropConfig() throws Exception {
        return getCropConfig(getCurrentGobiiCropType());
    }


    public void setCurrentGobiiCropType(String currentGobiiCropType) {
        this.currentGobiiCropType = currentGobiiCropType;

    }

    public String getCurrentGobiiCropType() {
        return currentGobiiCropType;
    }


    public Map<String, GobiiCropConfig> getCropConfigs() {

        return this.cropConfigs;

    }

    public void setCropConfigs(Map<String, GobiiCropConfig> cropConfigs) {

        for (Map.Entry<String, GobiiCropConfig> entry : cropConfigs.entrySet()) {
            String lowerCaseCropType = entry.getValue().getGobiiCropType();
            entry.getValue().setGobiiCropType(lowerCaseCropType);
            this.cropConfigs.put(lowerCaseCropType, entry.getValue());
        }
    }

    public void setCrop(String gobiiCropType,
                        boolean isActive,
                        String serviceDomain,
                        String serviceAppRoot,
                        Integer servicePort) throws Exception {

        gobiiCropType = gobiiCropType.toLowerCase();

        GobiiCropConfig gobiiCropConfig;
        if (this.isCropDefined(gobiiCropType)) {
            gobiiCropConfig = this.getCropConfig(gobiiCropType);
        } else {
            gobiiCropConfig = new GobiiCropConfig();
            this.cropConfigs.put(gobiiCropType, gobiiCropConfig);
        }

        gobiiCropConfig
                .setGobiiCropType(gobiiCropType)
                .setActive(isActive)
                .setHost(serviceDomain)
                .setContextPath(serviceAppRoot)
                .setPort(servicePort);
    }

    public void removeCrop(String cropId) throws Exception {

        if (!cropConfigs.containsKey(cropId)) {
            throw new Exception("The specified crop cannot be removed because it does not exist: " + cropId);
        }


        if ((!LineUtils.isNullOrEmpty(getTestExecConfig().getTestCrop())) &&
                getTestExecConfig().getTestCrop().equals(cropId)) {

            throw new Exception("Unable to remove crop " + cropId + " because it is the crop used for testing");
        }

        cropConfigs.remove(cropId);

    }


    public String getEmailSvrType() {
        return emailSvrType;
    }

    public void setEmailSvrType(String emailSvrType) {
        this.emailSvrType = emailSvrType;
    }

    public String getEmailSvrDomain() {
        return emailSvrDomain;
    }

    public void setEmailSvrDomain(String emailSvrDomain) {
        this.emailSvrDomain = emailSvrDomain;
    }

    public String getEmailSvrUser() {
        return emailSvrUser;
    }

    public void setEmailSvrUser(String emailSvrUser) {
        this.emailSvrUser = emailSvrUser;
    }

    public String getEmailSvrHashType() {
        return emailSvrHashType;
    }

    public void setEmailSvrHashType(String emailSvrHashType) {
        this.emailSvrHashType = emailSvrHashType;
    }

    public String getEmailSvrPassword() {
        return emailSvrPassword;
    }

    public void setEmailSvrPassword(String emailSvrPassword) {
        this.emailSvrPassword = emailSvrPassword;
    }

    public Integer getEmailServerPort() {
        return emailServerPort;
    }

    public void setEmailSvrPort(Integer emailServerPort) {
        this.emailServerPort = emailServerPort;
    }

    public GobiiAuthenticationType getGobiiAuthenticationType() {
        return gobiiAuthenticationType;
    }

    public void setGobiiAuthenticationType(GobiiAuthenticationType gobiiAuthenticationType) {
        this.gobiiAuthenticationType = gobiiAuthenticationType;
    }

    public String getLdapUserDnPattern() {
        return ldapUserDnPattern;
    }

    public void setLdapUserDnPattern(String ldapUserDnPattern) {
        this.ldapUserDnPattern = ldapUserDnPattern;
    }

    public String getLdapUrl() {
        return ldapUrl;
    }

    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }

    public String getLdapBindUser() {

        String returnVal;

        if (this.isDecrypt) {

            returnVal = Decrypter.decrypt(this.ldapBindUser, null);

        } else {

            returnVal = this.ldapBindUser;

        }

        return returnVal;
    }

    public void setLdapBindUser(String ldapBindUser) {
        this.ldapBindUser = ldapBindUser;
    }

    public String getLdapBindPassword() {

        String returnVal;

        if (this.isDecrypt) {

            returnVal = Decrypter.decrypt(this.ldapBindPassword, null);

        } else {

            returnVal = this.ldapBindPassword;

        }

        return returnVal;

    }

    public void setLdapBindPassword(String ldapBindPassword) {
        this.ldapBindPassword = ldapBindPassword;
    }

    public boolean isIflIntegrityCheck() {
        return iflIntegrityCheck;
    }

    public void setIflIntegrityCheck(boolean iflIntegrityCheck) {
        this.iflIntegrityCheck = iflIntegrityCheck;
    }

    public String getFileSystemRoot() {
        return fileSystemRoot;
    }

    public void setFileSystemRoot(String fileSystemRoot) {
        this.fileSystemRoot = fileSystemRoot;
    }

    public String getFileSystemLog() {
        return fileSystemLog;
    }

    public void setFileSystemLog(String fileSystemLog) {
        this.fileSystemLog = fileSystemLog;
    }

    public String getFileSysCropsParent() {

        String returnVal = LineUtils.terminateDirectoryPath(this.fileSystemRoot);
        returnVal += LineUtils.terminateDirectoryPath(this.fileSysCropsParent);
        return returnVal;
    }

    public void setFileSysCropsParent(String fileSysCropsParent) {
        this.fileSysCropsParent = fileSysCropsParent;
    }

    public boolean isDecrypt() {
        return isDecrypt;
    }

    // note that we set isDecrypt as a global property in ConfigValues and in \
    // each db config because the child db config objects don't have access to the
    // parent property. Moreover, in all cases, we only decrypt in the getters.
    // This is vital because there are multiple times that the config utility's options
    // are set and if we decrypt when we set the values, the decrypt flag would have to have
    // been set at the beginning of setting the config options, which is not a constraint we want
    // to impose.
    public void setDecrypt(boolean isDecrypt) {

        this.isDecrypt = isDecrypt;
        this.testExecConfig.setDecrypt(isDecrypt);

        for (GobiiCropConfig currentGobiiCropConfig : this.cropConfigs.values()) {

            for (GobiiCropDbConfig currentGobiiCropDbConfig : currentGobiiCropConfig.getCropConfigs()) {
                currentGobiiCropDbConfig.setDecrypt(isDecrypt);
            }
        }
    }

    public String getLdapUserForBackendProcs() {

        String returnVal;

        if (this.isDecrypt) {
            returnVal = Decrypter.decrypt(ldapUserForBackendProcs, null);
        } else {
            returnVal = ldapUserForBackendProcs;
        }

        return returnVal;
    }

    public void setLdapUserForBackendProcs(String ldapUserForBackendProcs) {
        this.ldapUserForBackendProcs = ldapUserForBackendProcs;
    }

    public String getLdapPasswordForBackendProcs() {
        String returnVal;

        if (this.isDecrypt) {
            returnVal = Decrypter.decrypt(ldapPasswordForBackendProcs, null);
        } else {
            returnVal = ldapPasswordForBackendProcs;
        }

        return returnVal;
    }

    public void setLdapPasswordForBackendProcs(String ldapPasswordForBackendProcs) {
        this.ldapPasswordForBackendProcs = ldapPasswordForBackendProcs;
    }

    public boolean isAuthenticateBrapi() {
        return isAuthenticateBrapi;
    }

    public void setAuthenticateBrapi(boolean authenticateBrapi) {
        isAuthenticateBrapi = authenticateBrapi;
    }
}
