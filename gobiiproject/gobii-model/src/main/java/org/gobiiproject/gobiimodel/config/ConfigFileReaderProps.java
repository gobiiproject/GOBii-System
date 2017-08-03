package org.gobiiproject.gobiimodel.config;

import org.apache.commons.lang.math.NumberUtils;

import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.utils.LineUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * This class creates a ConfigValues instance from a legacy .properties file
 */
public class ConfigFileReaderProps {


    private final String PROP_NAME_WEB_SVR_DEFAULT_CROP = "websvr.defaultcrop";

    private final String PROP_NAME_MAIL_SVR_TYPE = "mailsvr.type";
    private final String PROP_NAME_MAIL_SVR_DOMAIN = "mailsvr.domain";
    private final String PROP_NAME_MAIL_SVR_PORT = "mailsvr.port";
    private final String PROP_NAME_MAIL_SVR_USER = "mailsvr.user";
    private final String PROP_NAME_MAIL_SVR_HASHTYPE = "mailsvr.hashtype";
    private final String PROP_NAME_MAIL_SVR_PWD = "mailsvr.pwd";

    private final String PROP_NAME_IFL_INTEGRITY_CHECK = "ifl.integritycheck";

    private final String PROP_NAME_FILE_SYSTEM_ROOT = "filesys.root";
    private final String PROP_NAME_FILE_SYSTEM_LOG = "filesys.log";

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


    private String fqpn = null;

    public ConfigFileReaderProps(String fqpn) {

        this.fqpn = fqpn;

    }


    private Properties webProperties = null;

    private Properties getWebProperties() throws Exception {

        if (null == webProperties) {

            String configFileWebPath = null;
            if (null != this.fqpn) {
                configFileWebPath = this.fqpn;

            } else {
                throw new Exception("Configuration file location is null");
            }

            if (!LineUtils.isNullOrEmpty(configFileWebPath)) {

                InputStream configFileWebStream = new FileInputStream(configFileWebPath);
                if (null != configFileWebStream) {

                    webProperties = new Properties();
                    webProperties.load(configFileWebStream);

                } else {
                    throw new Exception("Unable to create input stream for config file: " + configFileWebPath);
                }

            } else {
                throw new Exception("JNDI lookup on prop file location is null or empty");
            }

        }

        return webProperties;

    } // getWebProperties()

    public String getPropValue(String propName) throws Exception {

        String returnVal = getWebProperties().getProperty(propName);
        if (null == returnVal) {
            returnVal = ""; //prevent NPEs
        }

        return returnVal;

    } // getPropValue()

    public ConfigValues makeConfigValues() throws Exception {

        ConfigValues returnVal = new ConfigValues();

        String currentPrefix = null;

        returnVal.setEmailSvrType(this.getPropValue(PROP_NAME_MAIL_SVR_TYPE));
        returnVal.setEmailSvrDomain(this.getPropValue(PROP_NAME_MAIL_SVR_DOMAIN));

        if (null != this.getPropValue(PROP_NAME_MAIL_SVR_PORT) && NumberUtils.isNumber(this.getPropValue(PROP_NAME_MAIL_SVR_PORT))) {
            returnVal.setEmailSvrPort(Integer.parseInt(this.getPropValue(PROP_NAME_MAIL_SVR_PORT)));
        }

        returnVal.setEmailSvrUser(this.getPropValue(PROP_NAME_MAIL_SVR_USER));
        returnVal.setEmailSvrHashType(this.getPropValue(PROP_NAME_MAIL_SVR_HASHTYPE));
        returnVal.setEmailSvrPassword(this.getPropValue(PROP_NAME_MAIL_SVR_PWD));
        returnVal.setIflIntegrityCheck(this.getPropValue(PROP_NAME_IFL_INTEGRITY_CHECK).equals("true"));

        returnVal.setFileSystemRoot(this.getPropValue(PROP_NAME_FILE_SYSTEM_ROOT));
        returnVal.setFileSystemLog(this.getPropValue(PROP_NAME_FILE_SYSTEM_LOG));


        //List<GobiiCropConfig> cropConfigsToSerialize = new ArrayList<>();
        Map<String, GobiiCropConfig> cropConfigs = new HashMap<>();
        for (int idx = 0; idx < cropPrefixes.length; idx++) {

            currentPrefix = cropPrefixes[idx];

            final String cropTypeFromProp = currentPrefix
                    .replace(CROP_PREFIX, "")
                    .replace(".", "")
                    .toLowerCase();


            String currentGobiiCropType = cropTypeFromProp.toLowerCase();


            String serviceDomain = this.getPropValue(currentPrefix + CROP_SUFFIX_SERVICE_DOMAIN);
            String serviceAppRoot = this.getPropValue(currentPrefix + CROP_SUFFIX_SERVICE_APPROOT);
            Integer servicePort = Integer.parseInt(this.getPropValue(currentPrefix + CROP_SUFFIX_SERVICE_PORT));
            String isActiveString = this.getPropValue(currentPrefix + CROP_SUFFIX_INTERMEDIATE_FILE_ACTIVE);
            boolean isActive = isActiveString.toLowerCase().equals("true");

            GobiiCropConfig currentGobiiCropConfig = new GobiiCropConfig(currentGobiiCropType,
                    serviceDomain,
                    serviceAppRoot,
                    servicePort,
                    isActive,
                    false);

            for (GobiiDbType currentDbType : GobiiDbType.values()) {

                if (currentDbType != GobiiDbType.UNKNOWN) {

                    String currentDbTypeSegment = currentDbType.toString().toLowerCase() + ".";
                    String currentDbPrefix = currentPrefix + DB_PREFX + currentDbTypeSegment;
                    String currentHost = this.getPropValue(currentDbPrefix + DB_SUFFIX_HOST);
                    String currentDbName = this.getPropValue(currentDbPrefix + DB_SUFFIX_DBNAME);
                    Integer currentPort = Integer.parseInt(this.getPropValue(currentDbPrefix + DB_SUFFIX_PORT));
                    String currentUserName = this.getPropValue(currentDbPrefix + DB_SUFFIX_USER);
                    String currentPassword = this.getPropValue(currentDbPrefix + DB_SUFFIX_PASSWORD);

                    GobiiCropDbConfig currentGobiiCropDbConfig = new GobiiCropDbConfig(
                            currentDbType,
                            currentHost,
                            currentDbName,
                            currentPort,
                            currentUserName,
                            currentPassword,
                            false
                    );

                    currentGobiiCropConfig.addCropDbConfig(currentDbType, currentGobiiCropDbConfig);
                }

                cropConfigs.put(currentGobiiCropType, currentGobiiCropConfig);
            }

        } // iterate crop configs

        returnVal.setCropConfigs(cropConfigs);

        return returnVal;

    } // makeConfigValues()()

} // ConfigFileReaderProps
