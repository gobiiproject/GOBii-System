package org.gobiiproject.gobiimodel.config;

import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.utils.LineUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Phil on 5/5/2016.
 */
public class ConfigSettings {


    private final String PROP_NAME_WEB_SVR_DEFAULT_CROP = "websvr.defaultcrop";

    private final String PROP_NAME_MAIL_SVR_TYPE = "mailsvr.type";
    private final String PROP_NAME_MAIL_SVR_DOMAIN = "mailsvr.domain";
    private final String PROP_NAME_MAIL_SVR_PORT = "mailsvr.port";
    private final String PROP_NAME_MAIL_SVR_USER = "mailsvr.user";
    private final String PROP_NAME_MAIL_SVR_HASHTYPE = "mailsvr.hashtype";
    private final String PROP_NAME_MAIL_SVR_PWD = "mailsvr.pwd";

    private final String PROP_NAME_IFL_INTEGRITY_CHECK = "ifl.integritycheck";

    private final String PROP_NAME_FILE_SYSTEM_ROOT = "filesys.root";

    private final String DB_PREFX = "db.";
    private final String DB_SUFFIX_HOST = "host";
    private final String DB_SUFFIX_PORT = "port";
    private final String DB_SUFFIX_DBNAME = "dbname";
    private final String DB_SUFFIX_USER = "username";
    private final String DB_SUFFIX_PASSWORD = "password";


    private final String CROP_SUFFIX_SERVICE_DOMAIN = "servicedomain";
    private final String CROP_SUFFIX_SERVICE_APPROOT = "serviceapproot";
    private final String CROP_SUFFIX_SERVICE_PORT = "serviceport";
    private final String CROP_SUFFIX_USER_FILE_LOCLOCATION = "usrfloc";
    private final String CROP_SUFFIX_LOADR_FILE_LOCATION = "ldrfloc";
    private final String CROP_SUFFIX_EXTRACTOR_FILE_LOCATION = "extrfloc";
    private final String CROP_SUFFIX_EXTRACTOR_FILE_OUTPUT = "extrout";
    private final String CROP_SUFFIX_INTERMEDIATE_FILE_LOCATION = "intfloc";
    private final String CROP_SUFFIX_INTERMEDIATE_FILE_ACTIVE = "active";

    private final String CROP_PREFIX = "crops.";
    private final String CROP_PREFIX_DEV = CROP_PREFIX + "dev.";
    private final String CROP_PREFIX_TEST = CROP_PREFIX + "test.";
    private final String CROP_PREFIX_CHICKPEA = CROP_PREFIX + "chickpea.";
    private final String CROP_PREFIX_MAIZE = CROP_PREFIX + "maize.";
    private final String CROP_PREFIX_RICE = CROP_PREFIX + "rice.";
    private final String CROP_PREFIX_SORGHUM = CROP_PREFIX + "sorghum.";
    private final String CROP_PREFIX_WHEAT = CROP_PREFIX + "wheat.";


    private String[] cropPrefixes = {
            CROP_PREFIX_DEV,
            CROP_PREFIX_TEST,
            CROP_PREFIX_CHICKPEA,
            CROP_PREFIX_MAIZE,
            CROP_PREFIX_RICE,
            CROP_PREFIX_SORGHUM,
            CROP_PREFIX_WHEAT
    };


    private Map<GobiiCropType, CropConfig> cropConfigs = new HashMap<>();

    private GobiiCropType currentGobiiCropType = GobiiCropType.TEST;
    private GobiiCropType defaultGobiiCropType = GobiiCropType.TEST; // default crop

    private String emailSvrType;
    private String emailSvrDomain;
    private String emailSvrUser;
    private String emailSvrHashType;
    private String emailSvrPassword;
    private Integer emailServerPort = 0;
    private boolean iflIntegrityCheck = false;
    private String fileSystemRoot;


    private ConfigFileReader configReader = null;

    public ConfigSettings() throws Exception {
        this(null);
    }

    public ConfigSettings(String fqpn) throws Exception {

        this.configReader = new ConfigFileReader(fqpn);

        String currentPrefix = null;

        String candidateCropName = configReader.getPropValue(PROP_NAME_WEB_SVR_DEFAULT_CROP).toUpperCase();
        if (!LineUtils.isNullOrEmpty(candidateCropName)) {
            if (0 == Arrays.asList(GobiiCropType.values())
                    .stream()
                    .filter(c -> c.toString().toUpperCase().equals(candidateCropName))
                    .count()) {
                throw new Exception("The configuration file specifies an instance type that does not correspond to a crop type: " + candidateCropName);
            }

            defaultGobiiCropType = GobiiCropType.valueOf(candidateCropName);
            currentGobiiCropType = defaultGobiiCropType;
        } else {
            throw new Exception("The configuration does not specify a default crop");
        }

        emailSvrType = configReader.getPropValue(PROP_NAME_MAIL_SVR_TYPE);
        emailSvrDomain = configReader.getPropValue(PROP_NAME_MAIL_SVR_DOMAIN);

        if (null != configReader.getPropValue(PROP_NAME_MAIL_SVR_PORT) && NumberUtils.isNumber(configReader.getPropValue(PROP_NAME_MAIL_SVR_PORT))) {
            emailServerPort = Integer.parseInt(configReader.getPropValue(PROP_NAME_MAIL_SVR_PORT));
        }

        emailSvrUser = configReader.getPropValue(PROP_NAME_MAIL_SVR_USER);
        emailSvrHashType = configReader.getPropValue(PROP_NAME_MAIL_SVR_HASHTYPE);
        emailSvrPassword = configReader.getPropValue(PROP_NAME_MAIL_SVR_PWD);
        iflIntegrityCheck = configReader.getPropValue(PROP_NAME_IFL_INTEGRITY_CHECK).equals("true");

        fileSystemRoot = configReader.getPropValue(PROP_NAME_FILE_SYSTEM_ROOT);


        for (int idx = 0; idx < cropPrefixes.length; idx++) {
            currentPrefix = cropPrefixes[idx];

            final String cropTypeFromProp = currentPrefix
                    .replace(CROP_PREFIX, "")
                    .replace(".", "")
                    .toUpperCase();


            if (0 == Arrays.asList(GobiiCropType.values())
                    .stream()
                    .filter(c -> c.toString().toUpperCase().equals(cropTypeFromProp.toUpperCase()))
                    .count()) {
                throw new Exception("The configuration file specifies a non-existent crop type: " + cropTypeFromProp);
            }

            GobiiCropType currentGobiiCropType = GobiiCropType.valueOf(cropTypeFromProp.toUpperCase());


            String serviceDomain = configReader.getPropValue(currentPrefix + CROP_SUFFIX_SERVICE_DOMAIN);
            String serviceAppRoot = configReader.getPropValue(currentPrefix + CROP_SUFFIX_SERVICE_APPROOT);
            Integer servicePort = Integer.parseInt(configReader.getPropValue(currentPrefix + CROP_SUFFIX_SERVICE_PORT));
            String userFilesLocation = configReader.getPropValue(currentPrefix + CROP_SUFFIX_USER_FILE_LOCLOCATION);
            String loaderFilesLocation = configReader.getPropValue(currentPrefix + CROP_SUFFIX_LOADR_FILE_LOCATION);
            String extractorFilesLocation = configReader.getPropValue(currentPrefix + CROP_SUFFIX_EXTRACTOR_FILE_LOCATION);
            String extractorFilesOutputLocation = configReader.getPropValue(currentPrefix + CROP_SUFFIX_EXTRACTOR_FILE_OUTPUT);
            String intermediateFilesLocation = configReader.getPropValue(currentPrefix + CROP_SUFFIX_INTERMEDIATE_FILE_LOCATION);
            String isActiveString = configReader.getPropValue(currentPrefix + CROP_SUFFIX_INTERMEDIATE_FILE_ACTIVE);
            boolean isActive = isActiveString.toLowerCase().equals("true");

            CropConfig currentCropConfig = new CropConfig(currentGobiiCropType,
                    serviceDomain,
                    serviceAppRoot,
                    servicePort,
                    loaderFilesLocation,
                    extractorFilesLocation,
                    extractorFilesOutputLocation,
                    userFilesLocation,
                    intermediateFilesLocation,
                    isActive);

            //crops.rice.db.monetdb.password=appuser
            for (GobiiDbType currentDbType : GobiiDbType.values()) {

                String currentDbTypeSegment = currentDbType.toString().toLowerCase() + ".";
                String currentDbPrefix = currentPrefix + DB_PREFX + currentDbTypeSegment;
                String currentHost = configReader.getPropValue(currentDbPrefix + DB_SUFFIX_HOST);
                String currentDbName = configReader.getPropValue(currentDbPrefix + DB_SUFFIX_DBNAME);
                Integer currentPort = Integer.parseInt(configReader.getPropValue(currentDbPrefix + DB_SUFFIX_PORT));
                String currentUserName = configReader.getPropValue(currentDbPrefix + DB_SUFFIX_USER);
                String currentPassword = configReader.getPropValue(currentDbPrefix + DB_SUFFIX_PASSWORD);

                CropDbConfig currentCropDbConfig = new CropDbConfig(
                        currentDbType,
                        currentHost,
                        currentDbName,
                        currentPort,
                        currentUserName,
                        currentPassword
                );

                currentCropConfig.addCropDbConfig(currentDbType, currentCropDbConfig);
            }


            cropConfigs.put(currentGobiiCropType, currentCropConfig);

        }

        if ( 0 == this.getActiveCropConfigs()
                .stream()
                .filter(c -> c.getGobiiCropType().equals(this.getDefaultGobiiCropType()))
                .collect(Collectors.toList())
            .size() ) {
            throw(new Exception("The server for the default crop type " + this.getDefaultGobiiCropType().toString() + " is not marked active!" ));
        }

    } // ctor


    public CropConfig getCropConfig(GobiiCropType gobiiCropType) {

        CropConfig returnVal = null;
        returnVal = cropConfigs.get(gobiiCropType);

        return returnVal;
    }

    public List<CropConfig> getActiveCropConfigs() {
        return cropConfigs
                .values()
                .stream()
                .filter(c -> c.isActive())
                .collect(Collectors.toList());
    }

    public CropConfig getCurrentCropConfig() {
        return getCropConfig(getCurrentGobiiCropType());
    }


    public void setCurrentGobiiCropType(GobiiCropType currentGobiiCropType) {
        this.currentGobiiCropType = currentGobiiCropType;

    }

    public GobiiCropType getCurrentGobiiCropType() {
        return currentGobiiCropType;
    }

    public GobiiCropType getDefaultGobiiCropType() {
        return defaultGobiiCropType;
    }



    public void setDefaultGobiiCropType(GobiiCropType defaultGobiiCropType) {
        this.defaultGobiiCropType = defaultGobiiCropType;
    }

    public String getEmailSvrPassword() {
        return emailSvrPassword;
    }

    public String getEmailSvrHashType() {
        return emailSvrHashType;
    }

    public String getEmailSvrUser() {
        return emailSvrUser;
    }

    public String getEmailSvrDomain() {
        return emailSvrDomain;
    }

    public String getEmailSvrType() {
        return emailSvrType;
    }

    public Integer getEmailServerPort() {
        return emailServerPort;
    }

    public void setEmailServerPort(Integer emailServerPort) {
        this.emailServerPort = emailServerPort;
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
}
