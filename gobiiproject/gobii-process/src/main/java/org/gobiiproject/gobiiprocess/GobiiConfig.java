package org.gobiiproject.gobiiprocess;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.CropDbConfig;
import org.gobiiproject.gobiimodel.types.GobiiAuthenticationType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Checks configurations of a Gobii instance for sanity.
 * Created by Phil on 6/24/2016.
 */
public class GobiiConfig {

    private static String NAME_COMMAND = "GobiiConfig";
    private static String TOMCAT_BASE_DIR = "wbase";
    private static String CONFIG_BASE_URL = "wurl";
    private static String CONFIG_MKDIRS = "wdirs";
    private static String COPY_WARS = "wcopy";
    private static String PROP_FILE_FQPN = "wfqpn";
    private static String PROP_FILE_PROPS_TO_XML = "wxml";
    private static String VALIDATE_CONFIGURATION = "validate";

    private static String CONFIG_ADD_ITEM = "a";
    private static String CONFIG_MARK_CROP_ACTIVE = "cA";
    private static String CONFIG_MARK_CROP_NOTACTIVE = "cD";
    private static String CONFIG_REMOVE_CROP = "cR";
    private static String CONFIG_GLOBAL_DEFAULT_CROP = "gD";
    private static String CONFIG_GLOBAL_FILESYS_ROOT = "gR";
    private static String CONFIG_GLOBAL_FILESYS_LOG = "gL";


    private static String CONFIG_SVR_GLOBAL_EMAIL = "stE"; // does not require -c
    private static String CONFIG_SVR_GLOBAL_EMAIL_TYPE = "stT"; // does not require -c
    private static String CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE = "stH"; // does not require -c
    private static String CONFIG_SVR_CROP_WEB = "stW";
    private static String CONFIG_SVR_CROP_POSTGRES = "stP";
    private static String CONFIG_SVR_CROP_MONET = "stM";


    private static String CONFIG_SVR_GLOBAL_AUTH_TYPE = "auT";
    private static String CONFIG_SVR_GLOBAL_LDAP_UDN = "ldUDN";
    private static String CONFIG_SVR_GLOBAL_LDAP_URL = "ldURL";
    private static String CONFIG_SVR_GLOBAL_LDAP_BUSR = "ldBUSR";
    private static String CONFIG_SVR_GLOBAL_LDAP_BPAS = "ldBPAS";
    private static String CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER = "ldraUSR";
    private static String CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD = "ldraPAS";

    private static String CONFIG_SVR_GLOBAL_LDAP_DECRYPT = "e";

    private static String CONFIG_TST_GLOBAL = "gt";
    private static String CONFIG_TST_GLOBAL_INTIAL_URL = "gtiu";
    private static String CONFIG_TST_GLOBAL_SSH_INTIAL_URL = "gtsu";
    private static String CONFIG_TST_GLOBAL_SSH_HOST = "gtsh";
    private static String CONFIG_TST_GLOBAL_SSH_PORT = "gtsp";
    private static String CONFIG_TST_GLOBAL_SSH_FLAG = "gtsf";
    private static String CONFIG_TST_GLOBAL_CONFIG_DIR_TEST = "gtcd";
    private static String CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM = "gtcs";
    private static String CONFIG_TST_GLOBAL_CONFIG_CROP_ID = "gtcr";
    private static String CONFIG_TST_GLOBAL_LDAP_USER = "gtldu";
    private static String CONFIG_TST_GLOBAL_LDAP_PASSWORD = "gtldp";


    private static String CONFIG_CROP_ID = "c";

    private static String CONFIG_SVR_OPTIONS_HOST = "soH";
    private static String CONFIG_SVR_OPTIONS_PORT = "soN";
    private static String CONFIG_SVR_OPTIONS_CONTEXT_ROOT = "soR";
    private static String CONFIG_SVR_OPTIONS_USER_NAME = "soU";
    private static String CONFIG_SVR_OPTIONS_PASSWORD = "soP";


    private static String WAR_FILES_DIR = "wars/";

    private static void printSeparator() {
        System.out.println("\n\n*******************************************************************");
    }

    private static void printField(String fieldName, String value) {
        System.out.println("******\t" + fieldName + ": " + value);
    }

    private static void setOption(Options options,
                                  String argument,
                                  boolean requiresValue,
                                  String helpText,
                                  String shortName) throws Exception {

        if (options.getOption(argument) != null) {
            throw new Exception("Option is already defined: " + argument);
        }

        //There does not appear to be a way to set argument name with the variants on addOption()
        options
                .addOption(argument, requiresValue, helpText)
                .getOption(argument)
                .setArgName(shortName);
    }

    /**
     * Main method of the configuration utility. This utility has a number of functions, all of which can be
     * seen in the help listing -- run the utility without options to get a help listing.
     * This utility and this utility alone should be used for creating configuration files for deployment
     * purposes. The -validate option should be used on files before attempting a deployment: it will
     * point out missing and, to some extent, invalid values in the file (it does _not_ test whether server
     * configurations are valid). The gobii-client project contains a unit test that demonstrates many (but
     * not all) of the configuration options. Note that some of the commandline items are global to a deployment
     * whilst others are specific to a crop.
     * <p>
     * The following example sequence of commands illustrates how to create and validate a complete configuration file.
     * <p>
     * # Set root gobii directory (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -gR "/shared_data/gobii/"
     * <p>
     * # Configure email server (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -stE  -soH smtp.gmail.com -soN 465 -soU user@gmail.com -soP password -stT SMTP -stH na
     * <p>
     * # Configure web server for crop DEV
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  DEV  -stW  -soH localhost -soN 8282 -soR /gobii-dev/
     * <p>
     * # Configure web server for crop TEST
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  TEST  -stW  -soH localhost -soN 8383 -soR /gobii-test/
     * <p>
     * # Configure PostGRES server for crop DEV
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  DEV  -stP
     * -soH localhost -soN 5432 -soU appuser -soP password -soR gobii_dev
     * <p>
     * # Configure MonetDB server for crop DEV
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  DEV -stM  -soH localhost -soN 5000 -soU appuser -soP appuser -soR gobii_dev
     * <p>
     * # Configure PostGRES server for crop TEST
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  TEST  -stP  -soH localhost -soN 5432 -soU appuser -soP appuser -soR gobii_test
     * <p>
     * # Configure MonetDB server for crop DEV
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  TEST -stM  -soH localhost -soN 5000 -soU appuser -soP appuser -soR gobii_test
     * <p>
     * # Configure integration testing (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -gt  -gtcd /gobii-config-test -gtcq /gobii-config-test/gobii-web.xml -gtcr  DEV  -gtcs  "java -jar gobiiconfig.jar"  -gtiu http://localhost:8282/gobii-dev -gtsf false -gtsh localhost -gtsp 8080 -gtsu http://localhost:8080/gobii-dev
     * <p>
     * # Set default crop to DEV (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -gD   DEV
     * <p>
     * # Set log file directory (global)
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -gL  /shared_data/gobii
     * <p>
     * # Mark crop TEST inactive
     * java -jar gobiiconfig.jar -a -wfqpn /gobii-config-test/gobii-web.xml -c  TEST  -cD
     * <p>
     * # Validate the file
     * java -jar gobiiconfig.jar -validate -wfqpn /gobii-config-test/gobii-web.xml
     *
     * @param args
     */
    public static void main(String[] args) {

        int exitCode = -1;

        // active?
        // remove?

        try {

            // define commandline options
            Options options = new Options();
            setOption(options, TOMCAT_BASE_DIR, true, "Tomcat base directory (e.g., /usr/local/tomcat7)", "tomcat base");
            setOption(options, CONFIG_BASE_URL, true, "url of GOBII web server for configuration verification", "base url");
            setOption(options, CONFIG_MKDIRS, false, "make gobii directories from root in the specified properties file (requires " + PROP_FILE_FQPN + ")", "make directories");
            setOption(options, COPY_WARS, false, "create war files for active crops from the specified war file (requires " + PROP_FILE_FQPN + ")", "copy wars");
            setOption(options, PROP_FILE_FQPN, true, "fqpn of gobii configuration file", "config fqpn");
            setOption(options, PROP_FILE_PROPS_TO_XML, false, "Convert existing gobii-properties file to xml (requires " + PROP_FILE_FQPN + ")", "convert to xml");
            setOption(options, CONFIG_ADD_ITEM, false, "Adds or updates the configuration value specified by one of the infrastructure parameters ("
                    + CONFIG_GLOBAL_FILESYS_ROOT + ", " + CONFIG_GLOBAL_DEFAULT_CROP + ") or parameters that require server option parameters ("
                    + CONFIG_SVR_GLOBAL_EMAIL + ", " + CONFIG_CROP_ID + ")", "add config item");

            setOption(options, CONFIG_REMOVE_CROP, true, "Removes the specified crop and related server specifications", "crop ID");
            setOption(options, CONFIG_MARK_CROP_ACTIVE, false, "Marks the specified crop active", "crop ID");
            setOption(options, CONFIG_MARK_CROP_NOTACTIVE, false, "Marks the specified crop inactive", "crop ID");

            setOption(options, CONFIG_GLOBAL_DEFAULT_CROP, true, "Default crop (global)", "crop id");
            setOption(options, CONFIG_GLOBAL_FILESYS_ROOT, true, "Absolute path to the gobii file system root (global)", "gobii root fqpn");
            setOption(options, CONFIG_GLOBAL_FILESYS_LOG, true, "Log file directory (global)", "log directory");

            setOption(options, CONFIG_CROP_ID, true, "Identifier of crop to add or modify; must be accompanied by a server specifier and its options", "crop ID");


            setOption(options, CONFIG_SVR_GLOBAL_EMAIL, false, "Server type: Email (not crop specific)", "server: email"); // does not require -c
            setOption(options, CONFIG_SVR_GLOBAL_EMAIL_TYPE, true, "Email server type", "server type"); // does not require -c
            setOption(options, CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE, true, "Email server hash type", "hash type"); // does not require -c
            setOption(options, CONFIG_SVR_CROP_WEB, false, "Server type: Web", "server: web");
            setOption(options, CONFIG_SVR_CROP_POSTGRES, false, "Server type: postgres", "server: pgsql");
            setOption(options, CONFIG_SVR_CROP_MONET, false, "Server type: Monet DB", "server: monet");


            setOption(options, CONFIG_SVR_GLOBAL_AUTH_TYPE, true, "Authentication type (LDAP | LDAP_CONNECT_WITH_MANAGER | ACTIVE_DIRECTORY | ACTIVE_DIRECTORY_CONNECT_WITH_MANAGER | TEST)", "authentication type");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_UDN, true, "LDAP User DN pattern (e.g., uid={0},ou=people) ", "User DN Pattern");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_URL, true, "Fully-qualified LDAP URL", "LDAP URL");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_BUSR, true, "User for authenticated LDAP search", "LDAP user");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_BPAS, true, "Password for authenticated LDAP search", "LDAP password");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER, true, "LDAP user as which background processes will run", "Background LDAP user");
            setOption(options, CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD, true, "LDAP password with which background processes authenticate", "Background LDAP password");

            setOption(options, CONFIG_SVR_GLOBAL_LDAP_DECRYPT, true, "Whether or not to decrypt ALL userids and passwords (true | false)", "decryption flag");

            setOption(options, CONFIG_SVR_OPTIONS_HOST, true, "Server option: hostname", "hostname");
            setOption(options, CONFIG_SVR_OPTIONS_PORT, true, "Server option: port number", "port number");
            setOption(options, CONFIG_SVR_OPTIONS_CONTEXT_ROOT, true, "Server option: context root ("
                    + CONFIG_SVR_CROP_WEB
                    + ") or database name ("
                    + CONFIG_SVR_CROP_POSTGRES + " and " + "("
                    + CONFIG_SVR_CROP_MONET
                    + ")", "context path");
            setOption(options, CONFIG_SVR_OPTIONS_USER_NAME, true, "Server option: Username", "user name");
            setOption(options, CONFIG_SVR_OPTIONS_PASSWORD, true, "Server option: Password", "password");


            setOption(options, CONFIG_TST_GLOBAL, false, "Configure test options", "test options");
            setOption(options, CONFIG_TST_GLOBAL_INTIAL_URL, true, "test option: intial server URL", "test url");
            setOption(options, CONFIG_TST_GLOBAL_SSH_INTIAL_URL, true, "test option: intial server URL for ssh", "ssh url");
            setOption(options, CONFIG_TST_GLOBAL_SSH_HOST, true, "test option: host for ssh", "ssh host");
            setOption(options, CONFIG_TST_GLOBAL_SSH_PORT, true, "test option: port for ssh", "ssh port");
            setOption(options, CONFIG_TST_GLOBAL_SSH_FLAG, true, "test option: flag to test SSH", "ssh flag");
            setOption(options, CONFIG_TST_GLOBAL_CONFIG_DIR_TEST, true, "directory for creating test configuration files", "test directory");
            setOption(options, CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM, true, "configuration utility command to which command args are appended", "config cmd");
            setOption(options, CONFIG_TST_GLOBAL_CONFIG_CROP_ID, true, "Crop to use for automated testing", "crop id");
            setOption(options, CONFIG_TST_GLOBAL_LDAP_USER, true, "LDAP user as which unit tests authenticate (if Authentication requires LDAP)", "LDAP test user");
            setOption(options, CONFIG_TST_GLOBAL_LDAP_PASSWORD, true, "LDAP password with which LDAP unit test user authenticates (if Authentication requires LDAP)", "LDAP test user password");

            setOption(options, VALIDATE_CONFIGURATION, false, "Verify that the specified configuration has all the values necessary for the system to function (does not test that the servers exist); requires " + PROP_FILE_FQPN, "validate");


            // parse our commandline
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            HelpFormatter helpFormatter = new HelpFormatter();

            if (commandLine.hasOption(TOMCAT_BASE_DIR)) {

                String tomcatBaseDirectory = commandLine.getOptionValue(TOMCAT_BASE_DIR);
                File tomcatDirectory = new File(tomcatBaseDirectory);

                if (tomcatDirectory.exists()) {

                    String configFileServerFqpn = tomcatBaseDirectory + "/conf/server.xml";

                    File configFileServer = new File(configFileServerFqpn);
                    if (configFileServer.exists()) {

                        GobiiConfig.printSeparator();
                        GobiiConfig.printField("Configuration Mode", "From tomcat server configuration");
                        GobiiConfig.printField("Tomcat file found", configFileServerFqpn);


                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document documentServer = documentBuilder.parse(new FileInputStream(configFileServer));


                        XPath xPathPropsLoc = XPathFactory.newInstance().newXPath();
                        NodeList nodesServer = (NodeList) xPathPropsLoc.evaluate("//Server/GlobalNamingResources/Environment[@name='gobiipropsloc']",
                                documentServer.getDocumentElement(), XPathConstants.NODESET);

                        Element locationElement = (Element) nodesServer.item(0);

                        if (null != locationElement) {

                            GobiiConfig.printField("Server configuration", "Proper node found");

                            String propertiesFileFqpn = locationElement.getAttribute("value");
                            File propertiesFile = new File(propertiesFileFqpn);
                            if (propertiesFile.exists()) {

                                GobiiConfig.printField("Local properties file", propertiesFileFqpn);

                                ConfigSettings configSettings = new ConfigSettings(propertiesFileFqpn);

                                String defaultCropType = configSettings.getDefaultGobiiCropType();
                                configSettings.setCurrentGobiiCropType(defaultCropType);

                                String configServerUrl = "http://"
                                        + configSettings.getCurrentCropConfig().getServiceDomain()
                                        + ":"
                                        + configSettings.getCurrentCropConfig().getServicePort().toString()
                                        + "/"
                                        + configSettings.getCurrentCropConfig().getServiceAppRoot();

                                String configFileContextFqpn = tomcatBaseDirectory + "/conf/context.xml";
                                File configFileContext = new File(configFileContextFqpn);
                                if (configFileContext.exists()) {

                                    GobiiConfig.printField("Tomcat file found", configFileContextFqpn);


                                    Document documentContext = documentBuilder.parse(new FileInputStream(configFileContext));
                                    XPath xPath = XPathFactory.newInstance().newXPath();
                                    NodeList nodesContext = (NodeList) xPath.evaluate("//Context/ResourceLink[@name='gobiipropsloc']",
                                            documentContext.getDocumentElement(), XPathConstants.NODESET);

                                    Element locationElementForLink = (Element) nodesContext.item(0);

                                    if (null != locationElementForLink) {
                                        GobiiConfig.printField("Context configuration", "Proper node found");
                                    } else {
                                        System.err.print("The configuration in server.xml does not define ResourceLink to the properties file: " + configFileServerFqpn);
                                    }

                                    ClientContext clientContext = configClientContext(configServerUrl);


                                    if (GobiiConfig.showServerInfo(clientContext)) {
                                        exitCode = 0;
                                    }


                                } else {
                                    System.err.println("Cannot find config file: : " + configFileContextFqpn);

                                }

                            } else {
                                System.err.println("The property file specified in "
                                        + configFileServerFqpn
                                        + " does not exist: "
                                        + propertiesFileFqpn);
                            }

                        } else {
                            System.err.println("The configuration does not define the properties file location: " + configFileServerFqpn);
                        }

                    } else {
                        System.err.println("Cannot find config file: : " + configFileServerFqpn);
                    }

                } else {
                    System.err.println("Specified tomcat base directory does not exist: " + tomcatBaseDirectory);
                }

            } else if (commandLine.hasOption(CONFIG_BASE_URL)) {

                String configUrl = commandLine.getOptionValue(CONFIG_BASE_URL);

                GobiiConfig.printSeparator();
                GobiiConfig.printField("Configuration Mode", "From url");

                ClientContext clientContext = configClientContext(configUrl);

                if (GobiiConfig.showServerInfo(clientContext)) {
                    exitCode = 0;
                }


            } else if (commandLine.hasOption(CONFIG_MKDIRS)
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);


                if (GobiiConfig.makeGobiiDirectories(propFileFqpn)) {
                    exitCode = 0;
                }


            } else if (commandLine.hasOption(COPY_WARS)
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);
                String warFileFqpn = commandLine.getOptionValue(COPY_WARS);


                if (GobiiConfig.copyWars(propFileFqpn, warFileFqpn)) {
                    exitCode = 0;
                }

            } else if (commandLine.hasOption(PROP_FILE_PROPS_TO_XML)
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);

                File propsFile = new File(propFileFqpn);
                if (propsFile.exists() && !propsFile.isDirectory()) {

                    exitCode = 0;

                } else {
                    System.err.println("Cannot find config file: : " + propFileFqpn);
                }

                // this should do the conversion automatically
                ConfigSettings configSettings = new ConfigSettings(propFileFqpn);
                configSettings.commit();

            } else if (commandLine.hasOption(CONFIG_ADD_ITEM)) {


                String propFileFqpn = null;
                if (commandLine.hasOption(PROP_FILE_FQPN)
                        && (null != (propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN)))) {


                    if (setGobiiConfiguration(propFileFqpn, options, commandLine)) {
                        exitCode = 0;
                    }

                } else {
                    System.err.println("Value is required: " + options.getOption(PROP_FILE_FQPN).getDescription());
                }

            } else if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_DECRYPT)
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                if (setDecryptionFlag(options, commandLine.getOptionValue(PROP_FILE_FQPN), commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_DECRYPT))) {
                    exitCode = 0;
                }

            } else if (commandLine.hasOption(VALIDATE_CONFIGURATION)) {


                String propFileFqpn = null;
                if (commandLine.hasOption(PROP_FILE_FQPN) &&
                        (null != (propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN)))) {
                    if (validateGobiiConfiguration(propFileFqpn, options, commandLine)) {
                        System.out.print("File " + propFileFqpn + " is valid.");
                        exitCode = 0;
                    }
                } else {
                    System.err.println("Value is required: " + options.getOption(PROP_FILE_FQPN).getDescription());
                }

            } else {
                helpFormatter.printHelp(NAME_COMMAND, options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.exit(exitCode);

    }// main()


    private static void writeConfigSettingsMessage(Options options,
                                                   String configFileFqpn,
                                                   List<String> configArgs,
                                                   List<String> configVals,
                                                   String cropId) throws Exception {

        if (configArgs.size() != configArgs.size()) {
            throw new Exception("The size of the options and values arrays do not match");
        }

        String contextMessage = "The following "
                + (LineUtils.isNullOrEmpty(cropId) ? "global " : "")
                + "options "
                + (LineUtils.isNullOrEmpty(cropId) ? "" : "for the " + cropId + " crop ")
                + "are being written to the the config file "
                + configFileFqpn
                + ": ";

        System.out.println(contextMessage);
        for (int idx = 0; idx < configArgs.size(); idx++) {
            String currentArg = configArgs.get(idx);
            String currentOption = options.getOption(currentArg).getArgName();
            String curentDescription = options.getOption(currentArg).getDescription();
            String currentValue = configVals.get(idx);
            System.out.println("-" + currentArg + " <" + currentOption + ">:\t\t" + currentValue + " (" + curentDescription + ")");
        }
    }


    private static ConfigSettings getConfigSettings(String propFileFqpn) {

        ConfigSettings returnVal = null;

        try {

            File file = new File(propFileFqpn);
            if (file.exists()) {

                returnVal = ConfigSettings.read(propFileFqpn);
            } else {
                returnVal = ConfigSettings.makeNew(propFileFqpn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnVal;

    }


    private static boolean setDecryptionFlag(Options options, String propFileFqpn, String flagAsString) {

        boolean returnVal = false;

        try {

            ConfigSettings configSettings = getConfigSettings(propFileFqpn);

            boolean decrypt = false;
            if (flagAsString.toLowerCase().equals("true")) {
                decrypt = true;
            }

            configSettings.setIsDecrypt(decrypt);
            configSettings.commit();
            returnVal = true;

            writeConfigSettingsMessage(options,
                    propFileFqpn,
                    Arrays.asList(CONFIG_SVR_GLOBAL_LDAP_DECRYPT),
                    Arrays.asList(decrypt ? "true" : "false"),
                    null);

        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;
        }

        return returnVal;
    }

    private static boolean setGobiiConfiguration(String propFileFqpn, Options options, CommandLine commandLine) {

        boolean returnVal = true;

        try {


            ConfigSettings configSettings = getConfigSettings(propFileFqpn);

            if (commandLine.hasOption(CONFIG_GLOBAL_DEFAULT_CROP)) {

                String defaultCrop = commandLine.getOptionValue(CONFIG_GLOBAL_DEFAULT_CROP);

                if (!configSettings.isCropDefined(defaultCrop)) {
                    configSettings.setCrop(defaultCrop, true, null, null, null);
                }

                configSettings.setDefaultGobiiCropType(defaultCrop);

                configSettings.commit();

                writeConfigSettingsMessage(options,
                        propFileFqpn,
                        Arrays.asList(CONFIG_GLOBAL_DEFAULT_CROP),
                        Arrays.asList(defaultCrop),
                        null);


            } else if (commandLine.hasOption(CONFIG_GLOBAL_FILESYS_ROOT)) {

                String fileSysRoot = commandLine.getOptionValue(CONFIG_GLOBAL_FILESYS_ROOT);

                configSettings.setFileSystemRoot(fileSysRoot);
                configSettings.commit();

                writeConfigSettingsMessage(options,
                        propFileFqpn,
                        Arrays.asList(CONFIG_GLOBAL_FILESYS_ROOT),
                        Arrays.asList(fileSysRoot),
                        null);


            } else if (commandLine.hasOption(CONFIG_GLOBAL_FILESYS_LOG)) {
                String fileSysLog = commandLine.getOptionValue(CONFIG_GLOBAL_FILESYS_LOG);
                configSettings.setFileSystemLog(fileSysLog);
                configSettings.commit();

                writeConfigSettingsMessage(options,
                        propFileFqpn,
                        Arrays.asList(CONFIG_GLOBAL_FILESYS_LOG),
                        Arrays.asList(fileSysLog),
                        null);

            } else if (commandLine.hasOption(CONFIG_MARK_CROP_ACTIVE) &&
                    commandLine.hasOption(CONFIG_CROP_ID)) {

                String cropId = commandLine.getOptionValue(CONFIG_CROP_ID);
                if (configSettings.isCropDefined(cropId)) {
                    CropConfig cropConfig = configSettings.getCropConfig(cropId);
                    cropConfig.setActive(true);
                } else {
                    returnVal = false;
                    System.err.println("The specified crop does not exist: " + cropId);
                }

                configSettings.commit();

            } else if (commandLine.hasOption(CONFIG_MARK_CROP_NOTACTIVE) &&
                    commandLine.hasOption(CONFIG_CROP_ID)) {

                String cropId = commandLine.getOptionValue(CONFIG_CROP_ID);
                if (configSettings.isCropDefined(cropId)) {
                    CropConfig cropConfig = configSettings.getCropConfig(cropId);
                    cropConfig.setActive(false);
                } else {
                    returnVal = false;
                    System.err.println("The specified crop does not exist: " + cropId);

                }

                configSettings.commit();

            } else if (commandLine.hasOption(CONFIG_TST_GLOBAL)) {

                List<String> argsSet = new ArrayList<>();
                List<String> valsSet = new ArrayList<>();

                String configFileFqpn = null;
                String configFileTestDirectory = null;
                String configUtilCommandlineStem = null;
                String initialConfigUrl = null;
                String initialConfigUrlForSshOverride = null;
                String sshOverrideHost = null;
                Integer sshOverridePort = null;
                String testCrop = null;
                String ldapTestUser = null;
                String ldapTestPassword = null;
                boolean isTestSsh = false;

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_INTIAL_URL)) {
                    initialConfigUrl = commandLine.getOptionValue(CONFIG_TST_GLOBAL_INTIAL_URL);
                    argsSet.add(CONFIG_TST_GLOBAL_INTIAL_URL);
                    valsSet.add(initialConfigUrl);
                    configSettings.getTestExecConfig().setInitialConfigUrl(initialConfigUrl);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_SSH_INTIAL_URL)) {
                    initialConfigUrlForSshOverride = commandLine.getOptionValue(CONFIG_TST_GLOBAL_SSH_INTIAL_URL);
                    argsSet.add(CONFIG_TST_GLOBAL_SSH_INTIAL_URL);
                    valsSet.add(initialConfigUrlForSshOverride);
                    configSettings.getTestExecConfig().setInitialConfigUrlForSshOverride(initialConfigUrlForSshOverride);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_SSH_HOST)) {
                    sshOverrideHost = commandLine.getOptionValue(CONFIG_TST_GLOBAL_SSH_HOST);
                    argsSet.add(CONFIG_TST_GLOBAL_SSH_HOST);
                    valsSet.add(sshOverrideHost);
                    configSettings.getTestExecConfig().setSshOverrideHost(sshOverrideHost);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_SSH_PORT)) {
                    String portString = commandLine.getOptionValue(CONFIG_TST_GLOBAL_SSH_PORT);
                    if (!NumberUtils.isNumber(portString)) {
                        throw new Exception("Specified value for "
                                + CONFIG_TST_GLOBAL_SSH_PORT
                                + "("
                                + options.getOption(CONFIG_TST_GLOBAL_SSH_PORT).getDescription()
                                + ") is not a valid integer: "
                                + portString);
                    }
                    sshOverridePort = Integer.parseInt(portString);
                    argsSet.add(CONFIG_TST_GLOBAL_SSH_PORT);
                    valsSet.add(sshOverridePort.toString());
                    configSettings.getTestExecConfig().setSshOverridePort(sshOverridePort);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_SSH_FLAG)) {
                    isTestSsh = commandLine.getOptionValue(CONFIG_TST_GLOBAL_SSH_FLAG).toLowerCase().equals("true") ? true : false;
                    argsSet.add(CONFIG_TST_GLOBAL_SSH_FLAG);
                    valsSet.add(isTestSsh ? "true" : "false");
                    configSettings.getTestExecConfig().setTestSsh(isTestSsh);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_CONFIG_DIR_TEST)) {
                    configFileTestDirectory = commandLine.getOptionValue(CONFIG_TST_GLOBAL_CONFIG_DIR_TEST);
                    argsSet.add(CONFIG_TST_GLOBAL_CONFIG_DIR_TEST);
                    valsSet.add(configFileTestDirectory);
                    configSettings.getTestExecConfig().setConfigFileTestDirectory(configFileTestDirectory);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM)) {
                    configUtilCommandlineStem = commandLine.getOptionValue(CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM);
                    argsSet.add(CONFIG_TST_GLOBAL_CONFIG_UTIL_CMD_STEM);
                    valsSet.add(configUtilCommandlineStem);
                    configSettings.getTestExecConfig().setConfigUtilCommandlineStem(configUtilCommandlineStem);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_CONFIG_CROP_ID)) {
                    testCrop = commandLine.getOptionValue(CONFIG_TST_GLOBAL_CONFIG_CROP_ID);
                    argsSet.add(CONFIG_TST_GLOBAL_CONFIG_CROP_ID);
                    valsSet.add(testCrop);
                    configSettings.getTestExecConfig().setTestCrop(testCrop);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_LDAP_USER)) {
                    ldapTestUser = commandLine.getOptionValue(CONFIG_TST_GLOBAL_LDAP_USER);
                    argsSet.add(CONFIG_TST_GLOBAL_LDAP_USER);
                    valsSet.add(ldapTestUser);
                    configSettings.getTestExecConfig().setLdapUserForUnitTest(ldapTestUser);
                }

                if (commandLine.hasOption(CONFIG_TST_GLOBAL_LDAP_PASSWORD)) {
                    ldapTestPassword = commandLine.getOptionValue(CONFIG_TST_GLOBAL_LDAP_PASSWORD);
                    argsSet.add(CONFIG_TST_GLOBAL_LDAP_PASSWORD);
                    valsSet.add(ldapTestPassword);
                    configSettings.getTestExecConfig().setLdapPasswordForUnitTest(ldapTestPassword);
                }


                configSettings.commit();

                writeConfigSettingsMessage(options,
                        propFileFqpn,
                        argsSet,
                        valsSet,
                        null);

            } else if (commandLine.hasOption(CONFIG_SVR_GLOBAL_AUTH_TYPE)) {

                List<String> argsSet = new ArrayList<>();
                List<String> valsSet = new ArrayList<>();

                String gobiiAuthenticationTypeRaw = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_AUTH_TYPE);
                GobiiAuthenticationType gobiiAuthenticationType = GobiiAuthenticationType.valueOf(gobiiAuthenticationTypeRaw);
                argsSet.add(CONFIG_SVR_GLOBAL_AUTH_TYPE);
                valsSet.add(gobiiAuthenticationTypeRaw);
                configSettings.setGobiiAuthenticationType(gobiiAuthenticationType);

                String ldapUserDnPattern = null;
                String ldapUrl = null;
                String ldapBindUser = null;
                String ldapBindPassword = null;
                String ldapRunAsUser = null;
                String ldapRunAsPassword = null;

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_UDN)) {
                    ldapUserDnPattern = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_UDN);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_UDN);
                    valsSet.add(ldapUserDnPattern);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_URL)) {
                    ldapUrl = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_URL);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_URL);
                    valsSet.add(ldapUrl);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_BUSR)) {
                    ldapBindUser = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_BUSR);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_BUSR);
                    valsSet.add(ldapBindUser);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_BPAS)) {
                    ldapBindPassword = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_BPAS);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_BPAS);
                    valsSet.add(ldapBindPassword);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER)) {
                    ldapRunAsUser = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_USER);
                    valsSet.add(ldapRunAsUser);
                }

                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD)) {
                    ldapRunAsPassword = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD);
                    argsSet.add(CONFIG_SVR_GLOBAL_LDAP_RUN_AS_PASSWORD);
                    valsSet.add(ldapRunAsPassword);
                }



                configSettings.setLdapUrl(ldapUrl);
                configSettings.setLdapUserDnPattern(ldapUserDnPattern);
                configSettings.setLdapBindUser(ldapBindUser);
                configSettings.setLdapBindPassword(ldapBindPassword);
                configSettings.setLdapUserForBackendProcs(ldapRunAsUser);
                configSettings.setLdapPasswordForBackendProcs(ldapRunAsPassword);
                configSettings.commit();

                writeConfigSettingsMessage(options,
                        propFileFqpn,
                        argsSet,
                        valsSet,
                        null);


            } else if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL) ||
                    commandLine.hasOption(CONFIG_CROP_ID)) {

                List<String> argsSet = new ArrayList<>();
                List<String> valsSet = new ArrayList<>();


                String svrHost = null;
                String svrUserName = null;
                String svrPassword = null;
                Integer svrPort = null;

                if (commandLine.hasOption(CONFIG_SVR_OPTIONS_HOST)) {
                    svrHost = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_HOST);
                    argsSet.add(CONFIG_SVR_OPTIONS_HOST);
                    valsSet.add(svrHost);
                }

                if (commandLine.hasOption(CONFIG_SVR_OPTIONS_USER_NAME)) {
                    svrUserName = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_USER_NAME);
                    argsSet.add(CONFIG_SVR_OPTIONS_USER_NAME);
                    valsSet.add(svrUserName);
                }

                if (commandLine.hasOption(CONFIG_SVR_OPTIONS_PASSWORD)) {
                    svrPassword = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_PASSWORD);
                    argsSet.add(CONFIG_SVR_OPTIONS_PASSWORD);
                    valsSet.add(svrPassword);
                }

                if (commandLine.hasOption(CONFIG_SVR_OPTIONS_PORT)) {

                    String svrPortStr = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_PORT);

                    if (NumberUtils.isNumber(svrPortStr)) {
                        svrPort = Integer.parseInt(svrPortStr);
                    } else {
                        throw new Exception("Option for port value (" + CONFIG_SVR_OPTIONS_PORT + ") is not an integer: " + svrPortStr);
                    }

                    argsSet.add(CONFIG_SVR_OPTIONS_PORT);
                    valsSet.add(svrPortStr);
                }


                if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL)) {

                    configSettings.setEmailSvrDomain(svrHost);
                    configSettings.setEmailSvrUser(svrUserName);
                    configSettings.setEmailSvrPassword(svrPassword);
                    configSettings.setEmailSvrPort(svrPort);

                    if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL_TYPE)) {
                        String emailSvrType = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_EMAIL_TYPE);
                        configSettings.setEmailSvrType(emailSvrType);
                        argsSet.add(CONFIG_SVR_GLOBAL_EMAIL_TYPE);
                        valsSet.add(emailSvrType);
                    }

                    if (commandLine.hasOption(CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE)) {
                        String hashhType = commandLine.getOptionValue(CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE);
                        configSettings.setEmailSvrHashType(hashhType);
                        argsSet.add(CONFIG_SVR_GLOBAL_EMAIL_HASHTYPE);
                        valsSet.add(hashhType);
                    }


                } else if (commandLine.hasOption(CONFIG_CROP_ID)) {

                    String cropId = commandLine.getOptionValue(CONFIG_CROP_ID);


                    if (!configSettings.isCropDefined(cropId)) {
                        configSettings.setCrop(cropId, true, null, null, null);
                    }

                    CropConfig cropConfig = configSettings.getCropConfig(cropId);

                    String contextRoot = null;
                    if (commandLine.hasOption(CONFIG_SVR_OPTIONS_CONTEXT_ROOT)) {
                        contextRoot = commandLine.getOptionValue(CONFIG_SVR_OPTIONS_CONTEXT_ROOT);
                        argsSet.add(CONFIG_SVR_OPTIONS_CONTEXT_ROOT);
                        valsSet.add(contextRoot);
                    }

                    if (commandLine.hasOption(CONFIG_SVR_CROP_WEB)) {

                        argsSet.add(CONFIG_SVR_CROP_WEB);
                        valsSet.add("");

                        cropConfig.setServiceDomain(svrHost);
                        cropConfig.setServicePort(svrPort);
                        cropConfig.setServiceAppRoot(contextRoot);

                    } else if (commandLine.hasOption(CONFIG_SVR_CROP_POSTGRES) ||
                            (commandLine.hasOption(CONFIG_SVR_CROP_MONET))) {

                        GobiiDbType gobiiDbType = GobiiDbType.UNKNOWN;
                        if (commandLine.hasOption(CONFIG_SVR_CROP_POSTGRES)) {
                            gobiiDbType = GobiiDbType.POSTGRESQL;
                            argsSet.add(CONFIG_SVR_CROP_POSTGRES);
                            valsSet.add("");
                        } else if (commandLine.hasOption(CONFIG_SVR_CROP_MONET)) {
                            gobiiDbType = GobiiDbType.MONETDB;
                            argsSet.add(CONFIG_SVR_CROP_MONET);
                            valsSet.add("");
                        }


                        cropConfig.setCropDbConfig(gobiiDbType,
                                svrHost,
                                contextRoot,
                                svrPort,
                                svrUserName,
                                svrPassword);

                    } else {
                        // do nothing: allow control to fall through to print help
                    }

                    writeConfigSettingsMessage(options,
                            propFileFqpn,
                            argsSet,
                            valsSet,
                            cropId);

                }

                configSettings.commit();


            } else if (commandLine.hasOption(CONFIG_REMOVE_CROP)) {

                String cropId = commandLine.getOptionValue(CONFIG_REMOVE_CROP);

                if (configSettings.isCropDefined(cropId)) {

                    configSettings.removeCrop(cropId);
                    configSettings.commit();
                } else {
                    System.err.println("The following crop does not exist: " + cropId);
                    returnVal = false;
                }

            } else {
                // do nothing: allow control to fall through to print help

            }

        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;
        }

        return returnVal;

    }

    private static boolean validateGobiiConfiguration(String propFileFqpn, Options options, CommandLine commandLine) {

        boolean returnVal = true;

        try {

            if (commandLine.hasOption(VALIDATE_CONFIGURATION) && commandLine.hasOption(PROP_FILE_FQPN)) {

                String configFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);
                ConfigSettings configSettings = new ConfigSettings(configFileFqpn);

                if (LineUtils.isNullOrEmpty(configSettings.getEmailSvrDomain())) {
                    System.err.println("An email server host is not defined");
                    returnVal = false;
                }

                if (configSettings.getEmailServerPort() == null) {
                    System.err.println("An email port is not defined");
                    returnVal = false;
                }

                if (LineUtils.isNullOrEmpty(configSettings.getEmailSvrUser())) {
                    System.err.println("An email server user id is not defined");
                    returnVal = false;
                }

                if (LineUtils.isNullOrEmpty(configSettings.getEmailSvrUser())) {
                    System.err.println("An email server password is not defined");
                    returnVal = false;
                }

                if (LineUtils.isNullOrEmpty(configSettings.getDefaultGobiiCropType())) {
                    System.err.println("A default crop type is not defined");
                    returnVal = false;
                }

                if (LineUtils.isNullOrEmpty(configSettings.getFileSystemRoot())) {
                    System.err.println("A file system root is not defined");
                    returnVal = false;
                } else {
                    File directoryToTest = new File(configSettings.getFileSystemRoot());
                    if (!directoryToTest.exists() || !directoryToTest.isDirectory()) {
                        System.err.println("The specified file system root does not exist or is not a directory: " + configSettings.getFileSystemRoot());
                        returnVal = false;
                    }
                }

                if (configSettings.getGobiiAuthenticationType() == null) {
                    System.err.println("An authentication type is not specified");
                    returnVal = false;
                }

                // for TEST authentication we use internal, in-memory users
                if (!configSettings.getGobiiAuthenticationType().equals(GobiiAuthenticationType.TEST)) {

                    if (LineUtils.isNullOrEmpty(configSettings.getLdapUserDnPattern())) {
                        System.err.println("The authentication type is "
                                + configSettings.getGobiiAuthenticationType().toString()
                                + " but a user dn pattern is not specified");
                        returnVal = false;
                    }

                    if (LineUtils.isNullOrEmpty(configSettings.getLdapUrl())) {
                        System.err.println("The authentication type is "
                                + configSettings.getGobiiAuthenticationType().toString()
                                + " but an ldap url is not specified");
                        returnVal = false;
                    }

                    if (configSettings.getGobiiAuthenticationType().equals(GobiiAuthenticationType.LDAP_CONNECT_WITH_MANAGER) ||
                            configSettings.getGobiiAuthenticationType().equals(GobiiAuthenticationType.ACTIVE_DIRECTORY_CONNECT_WITH_MANAGER)) {

                        if (LineUtils.isNullOrEmpty(configSettings.getLdapBindUser())) {
                            System.err.println("The authentication type is "
                                    + configSettings.getGobiiAuthenticationType().toString()
                                    + " but an ldap bind user is not specified");
                            returnVal = false;
                        }

                        if (LineUtils.isNullOrEmpty(configSettings.getLdapBindPassword())) {
                            System.err.println("The authentication type is "
                                    + configSettings.getGobiiAuthenticationType().toString()
                                    + " but an ldap bind password is not specified");
                            returnVal = false;
                        }

                    } // if the authentication type requires connection credentails

                } // if the authentication type requires url and user dn pattern


                if (LineUtils.isNullOrEmpty(configSettings.getFileSystemLog())) {
                    System.err.println("A file system log directory is not defined");
                    returnVal = false;
                } else {
                    File directoryToTest = new File(configSettings.getFileSystemLog());
                    if (!directoryToTest.exists() || !directoryToTest.isDirectory()) {
                        System.err.println("The specified file system log does not exist or is not a directory: " + configSettings.getFileSystemLog());
                        returnVal = false;
                    }
                }


                if (LineUtils.isNullOrEmpty(configSettings.getFileSysCropsParent())) {
                    System.err.println("A file system crop parent directory is not defined");
                    returnVal = false;
                } else {
                    File directoryToTest = new File(configSettings.getFileSysCropsParent());
                    if (!directoryToTest.exists() || !directoryToTest.isDirectory()) {
                        System.err.println("The specified file crop parent directory does not exist or is not a directory: " + configSettings.getFileSysCropsParent());
                        returnVal = false;
                    }
                }


                if (configSettings.getTestExecConfig() == null) {
                    System.err.println("No test exec configuration is defined");
                    returnVal = false;
                }

                if (LineUtils.isNullOrEmpty(configSettings.getTestExecConfig().getTestCrop())) {
                    System.err.println("A test crop id is not defined");
                    returnVal = false;
                } else {

                    String cropId = configSettings.getTestExecConfig().getTestCrop();
                    if (!configSettings.isCropDefined(cropId)) {
                        System.err.println("The test crop is not defined in the crop configurations: " + cropId);
                        returnVal = false;
                    }

                    if (configSettings
                            .getActiveCropConfigs()
                            .stream()
                            .filter(cropConfig -> cropConfig.getGobiiCropType().equals(cropId))
                            .count() != 1) {
                        System.err.println("The specified test crop config is not an active crop: " + cropId);
                        returnVal = false;
                    }
                }


                if (LineUtils.isNullOrEmpty(configSettings.getTestExecConfig().getInitialConfigUrl())) {
                    System.err.println("An initial configuration url for testing is not defined");
                    returnVal = false;
                }


                if (LineUtils.isNullOrEmpty(configSettings.getTestExecConfig().getConfigFileTestDirectory())) {
                    System.err.println("A a directory for test files is not defined");
                    returnVal = false;
                } else {
                    String testDirectoryPath = configSettings.getTestExecConfig().getConfigFileTestDirectory();
                    File testFilePath = new File(configSettings.getTestExecConfig().getConfigFileTestDirectory());
                    if (!testFilePath.exists()) {
                        System.err.println("The specified test file path does not exist: "
                                + testDirectoryPath);
                        returnVal = false;
                    }
                }


                if (LineUtils.isNullOrEmpty(configSettings.getTestExecConfig().getConfigUtilCommandlineStem())) {
                    System.err.println("The commandline stem of this utility for testing purposes is not defined");
                    returnVal = false;
                }


                if (configSettings.getActiveCropConfigs().size() < 1) {
                    System.err.println("No active crops are defined");
                    returnVal = false;
                }

                for (CropConfig currentCropConfig : configSettings.getActiveCropConfigs()) {


                    if (!currentCropConfig.getServiceAppRoot().toLowerCase().contains(currentCropConfig.getGobiiCropType())) {
                        System.err.println("The context root "
                                + currentCropConfig.getServiceAppRoot()
                                + " does not contain the crop ID "
                                + currentCropConfig.getGobiiCropType());
                        returnVal = false;
                    }

                    if (LineUtils.isNullOrEmpty(currentCropConfig.getGobiiCropType())) {
                        System.err.println("The crop type for the active crop  is not defined");
                        returnVal = false;
                    }


                    if (LineUtils.isNullOrEmpty(currentCropConfig.getServiceDomain())) {
                        System.err.println("The web server host for the active crop (" + currentCropConfig.getGobiiCropType() + ") is not defined");
                        returnVal = false;

                    }


                    if (LineUtils.isNullOrEmpty(currentCropConfig.getServiceAppRoot())) {
                        System.err.println("The web server context path for the active crop (" + currentCropConfig.getGobiiCropType() + ") is not defined");
                        returnVal = false;

                    }


                    if (currentCropConfig.getServicePort() == null) {
                        System.err.println("The web server port for the active crop (" + currentCropConfig.getGobiiCropType() + ") is not defined");
                        returnVal = false;

                    }

                    CropDbConfig cropDbConfigPostGres = currentCropConfig.getCropDbConfig(GobiiDbType.POSTGRESQL);
                    if (cropDbConfigPostGres == null) {
                        System.err.println("The postgresdb for the active crop (" + currentCropConfig.getGobiiCropType() + ") is not defined");
                        returnVal = false;
                    } else {
                        returnVal = returnVal && verifyDbConfig(cropDbConfigPostGres);
                    }

                    CropDbConfig cropDbConfigMonetDB = currentCropConfig.getCropDbConfig(GobiiDbType.MONETDB);
                    if (cropDbConfigMonetDB == null) {
                        System.err.println("The monetdb for the active crop (" + currentCropConfig.getGobiiCropType() + ") is not defined");
                        returnVal = false;
                    } else {
                        returnVal = returnVal && verifyDbConfig(cropDbConfigMonetDB);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;
        }

        return returnVal;

    } //


    private static boolean verifyDbConfig(CropDbConfig cropDbConfig) {

        boolean returnVal = true;

        if (LineUtils.isNullOrEmpty(cropDbConfig.getHost())) {
            System.err.println("The db config for " + cropDbConfig.getGobiiDbType().toString() + " does not define a host");
            returnVal = false;
        }


        if (cropDbConfig.getPort() == null) {
            System.err.println("The db config for " + cropDbConfig.getGobiiDbType().toString() + " does not define a port");
            returnVal = false;
        }


        if (LineUtils.isNullOrEmpty(cropDbConfig.getUserName())) {
            System.err.println("The db config for " + cropDbConfig.getGobiiDbType().toString() + " does not define a user name");
            returnVal = false;
        }


        if (LineUtils.isNullOrEmpty(cropDbConfig.getPassword())) {
            System.err.println("The db config for " + cropDbConfig.getGobiiDbType().toString() + " does not define a password");
            returnVal = false;
        }

        if (LineUtils.isNullOrEmpty(cropDbConfig.getDbName())) {
            System.err.println("The db config for " + cropDbConfig.getGobiiDbType().toString() + " does not define a database name");
            returnVal = false;
        }

        return returnVal;
    }

    private static ClientContext configClientContext(String configServerUrl) throws Exception {
        System.out.println();
        System.out.println();
        GobiiConfig.printSeparator();
        GobiiConfig.printField("Config request server", configServerUrl);

        System.out.println();
        System.out.println();
        GobiiConfig.printSeparator();

        return ClientContext.getInstance(configServerUrl, true);

    }

    private static boolean showServerInfo(ClientContext clientContext) throws Exception {

        boolean returnVal = true;

        // The logging framework emits debugging messages before it knows not to emit them.
        // Until we solve this problem, we we'll visually set those messages aside
        List<String> gobiiCropTypes = clientContext.getInstance(null, false).getCropTypeTypes();
        GobiiConfig.printSeparator();

        GobiiConfig.printField("Default crop", ClientContext.getInstance(null, false).getDefaultCropType().toString());

        for (String currentCropType : gobiiCropTypes) {

            ClientContext.getInstance(null, false).setCurrentClientCrop(currentCropType);

            GobiiConfig.printSeparator();
            GobiiConfig.printField("Crop Type", currentCropType.toString());
            GobiiConfig.printField("Host", ClientContext.getInstance(null, false).getCurrentCropDomain());
            GobiiConfig.printField("Port", ClientContext.getInstance(null, false).getCurrentCropPort().toString());
            GobiiConfig.printField("Context root", ClientContext.getInstance(null, false).getCurrentCropContextRoot());

            GobiiConfig.printField("Loader instructions directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INSTRUCTIONS));
            GobiiConfig.printField("User file upload directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileProcessDir.RAW_USER_FILES));
            GobiiConfig.printField("Digester output directory ", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES));
            GobiiConfig.printField("Extractor instructions directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS));

            //if(!LineUtils.isNullOrEmpty())

            // This ping thing gives out too much internal details about the server. Removing this for now.
//            SystemUsers systemUsers = new SystemUsers();
//            SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
//
//            if (ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword())) {
//
//                PingDTO pingDTORequest = new PingDTO();
//
//
//                //DtoRequestPing dtoRequestPing = new DtoRequestPing();
//                GobiiEnvelopeRestResource<PingDTO> gobiiEnvelopeRestResourcePingDTO = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
//                        .getUriFactory()
//                        .resourceColl(ServiceRequestId.URL_PING));
//
//                PayloadEnvelope<PingDTO> resultEnvelopePing = gobiiEnvelopeRestResourcePingDTO.post(PingDTO.class,
//                        new PayloadEnvelope<>(pingDTORequest, GobiiProcessType.CREATE));
//                //PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(new PayloadEnvelope<>(newContactDto, GobiiProcessType.CREATE));
//
//
//                Integer responseNum = 1;
//                if (resultEnvelopePing.getHeader().getStatus().isSucceeded()) {
//                    PingDTO pingDTOResponse = resultEnvelopePing.getPayload().getData().get(0);
//                    for (String currentResponse : pingDTOResponse.getPingResponses()) {
//                        GobiiConfig.printField("Ping response " + (responseNum++).toString(), currentResponse);
//                    }
//                } else {
//                    for (HeaderStatusMessage currentHeader : resultEnvelopePing.getHeader().getStatus().getStatusMessages()) {
//                        GobiiConfig.printField("Service error " + (responseNum++).toString(), currentHeader.getMessage());
//                        returnVal = false;
//                    }
//                }
//            } else {
//                System.err.println("Authentication to server for crop failed: " + currentCropType.toString());
//                returnVal = false;
//            }
        }

        return returnVal;
    }

    private static boolean makeGobiiDirectories(String propFileFqpn) {

        boolean returnVal = true;

        try {

            ConfigSettings configSettings = new ConfigSettings(propFileFqpn);
            if (configSettings.getActiveCropTypes().size() > 0) {
                for (String currentCrop : configSettings.getActiveCropTypes()) {

                    printSeparator();
                    printField("Checking directories for crop", currentCrop);


                    for (GobiiFileProcessDir currentRelativeDirectory : EnumSet.allOf(GobiiFileProcessDir.class)) {

                        String directoryToMake = configSettings.getProcessingPath(currentCrop, currentRelativeDirectory);


                        File currentFile = new File(directoryToMake);
                        if (!currentFile.exists()) {

                            printField("Creating new directory", directoryToMake);
                            if (!currentFile.mkdirs()) {
                                System.err.println("Unable to create directory " + directoryToMake);
                                returnVal = false;
                            }

                        } else {
                            printField("Checking permissions on existing directory", directoryToMake);

                        }

                        if (!currentFile.canRead() && !currentFile.setReadable(true, false)) {
                            System.err.println("Unable to set read permissions on directory " + directoryToMake);
                            returnVal = false;
                        }

                        if (!currentFile.canWrite() && !currentFile.setWritable(true, false)) {
                            System.err.println("Unable to set write permissions on directory " + directoryToMake);
                            returnVal = false;

                        }
                    } // iterate directories
                } // iterate crops
            } else {
                System.err.println("No directories were created because there are no active crops defined");
                returnVal = false;
            }


        } catch (Exception e) {

            e.printStackTrace();
            returnVal = false;

        }

        return returnVal;
    }

    private static boolean copyWars(String configFileFqpn, String warFileFqpn) {

        boolean returnVal = true;

        try {


            File sourceFile = new File(warFileFqpn);
            if (sourceFile.exists()) {

                ConfigSettings configSettings = new ConfigSettings(configFileFqpn);
                String warDestinationRoot = configSettings.getFileSystemRoot() + WAR_FILES_DIR;

                File destinationRootPath = new File(warDestinationRoot);
                if (!destinationRootPath.exists()) {
                    if (!destinationRootPath.mkdirs()) {
                        System.err.println("Unable to create war directory: " + warDestinationRoot);
                        returnVal = false;

                    }
                }

                if (!destinationRootPath.canRead() && !destinationRootPath.setReadable(true, false)) {

                    System.err.println("Unable to set read permissions on war directory: " + warDestinationRoot);
                    returnVal = false;

                }

                if (!destinationRootPath.canWrite() && !destinationRootPath.setWritable(true, false)) {

                    System.err.println("Unable to set write permissions on war directory: " + warDestinationRoot);
                    returnVal = false;

                }

                if (returnVal) {
                    for (CropConfig currentCrop : configSettings.getActiveCropConfigs()) {

                        String warDestinationFqpn = warDestinationRoot + "gobii-" + currentCrop
                                .getGobiiCropType()
                                .toString()
                                .toLowerCase()
                                + ".war";

                        File destinationFile = new File(warDestinationFqpn);

                        if (destinationFile.exists()) {
                            destinationFile.delete();
                        }

                        Files.copy(sourceFile.toPath(), destinationFile.toPath());
                        printField("Created war", warDestinationFqpn);

                    }
                }

            } else {
                System.err.println("Source war file does not exist: " + warFileFqpn);
                returnVal = false;

            }

        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;

        }

        return returnVal;

    }
}
