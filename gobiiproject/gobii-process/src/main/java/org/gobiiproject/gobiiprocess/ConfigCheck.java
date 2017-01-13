package org.gobiiproject.gobiiprocess;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestPing;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiFileLocationType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 6/24/2016.
 */
public class ConfigCheck {

    private static String NAME_COMMAND = "ConfigCheck";
    private static String TOMCAT_BASE_DIR = "tbase";
    private static String CONFIG_BASE_URL = "burl";
    private static String CONFIG_MKDIRS = "mdirs";
    private static String COPY_WARS = "wcopy";
    private static String PROP_FILE_FQPN = "pfqpn";

    private static String WAR_FILES_DIR = "wars/";

    private static List<String> directoriesRelativeToEachCrop = Arrays.asList(
            WAR_FILES_DIR,
            "extractor/inprogress/",
            "extractor/instructions/",
            "extractor/output/flapjack/",
            "extractor/output/hapmap/",
            "files/",
            "hdf5/",
            "loader/digest/",
            "loader/inprogress/",
            "loader/instructions/"
    );


    private static void printSeparator() {
        System.out.println("\n\n*******************************************************************");
    }

    private static void printField(String fieldName, String value) {
        System.out.println("******\t" + fieldName + ": " + value);
    }

    public static void main(String[] args) {

        int exitCode = -1;

        try {

            // define commandline options
            Options options = new Options();
            options.addOption(TOMCAT_BASE_DIR, true, "Tomcat base directory (e.g., /usr/local/tomcat7)");
            options.addOption(CONFIG_BASE_URL, true, "url of server from which to get initial config settings");
            options.addOption(CONFIG_MKDIRS, false, "make gobii directories from root in the specified properties file (requires " + PROP_FILE_FQPN + ")");
            options.addOption(COPY_WARS, true, "create war files for active crops from the specified war file (requires " + PROP_FILE_FQPN + ")");
            options.addOption(PROP_FILE_FQPN, true, "fqpn of gobii properties file");

            // parse our commandline
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            HelpFormatter formatter = new HelpFormatter();

            if (commandLine.hasOption(TOMCAT_BASE_DIR)) {

                String tomcatBaseDirectory = commandLine.getOptionValue(TOMCAT_BASE_DIR);
                File tomcatDirectory = new File(tomcatBaseDirectory);

                if (tomcatDirectory.exists()) {

                    String configFileServerFqpn = tomcatBaseDirectory + "/conf/server.xml";

                    File configFileServer = new File(configFileServerFqpn);
                    if (configFileServer.exists()) {

                        ConfigCheck.printSeparator();
                        ConfigCheck.printField("Configuration Mode", "From tomcat server configuration");
                        ConfigCheck.printField("Tomcat file found", configFileServerFqpn);


                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document documentServer = documentBuilder.parse(new FileInputStream(configFileServer));


                        XPath xPathPropsLoc = XPathFactory.newInstance().newXPath();
                        NodeList nodesServer = (NodeList) xPathPropsLoc.evaluate("//Server/GlobalNamingResources/Environment[@name='gobiipropsloc']",
                                documentServer.getDocumentElement(), XPathConstants.NODESET);

                        Element locationElement = (Element) nodesServer.item(0);

                        if (null != locationElement) {

                            ConfigCheck.printField("Server configuration", "Proper node found");

                            String propertiesFileFqpn = locationElement.getAttribute("value");
                            File propertiesFile = new File(propertiesFileFqpn);
                            if (propertiesFile.exists()) {

                                ConfigCheck.printField("Local properties file", propertiesFileFqpn);

                                ConfigSettings configSettings = new ConfigSettings(propertiesFileFqpn);

                                GobiiCropType defaultCropType = configSettings.getDefaultGobiiCropType();
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

                                    ConfigCheck.printField("Tomcat file found", configFileContextFqpn);


                                    Document documentContext = documentBuilder.parse(new FileInputStream(configFileContext));
                                    XPath xPath = XPathFactory.newInstance().newXPath();
                                    NodeList nodesContext = (NodeList) xPath.evaluate("//Context/ResourceLink[@name='gobiipropsloc']",
                                            documentContext.getDocumentElement(), XPathConstants.NODESET);

                                    Element locationElementForLink = (Element) nodesContext.item(0);

                                    if (null != locationElementForLink) {
                                        ConfigCheck.printField("Context configuration", "Proper node found");
                                    } else {
                                        System.err.print("The configuration in server.xml does not define ResourceLink to the properties file: " + configFileServerFqpn);
                                    }

                                    ClientContext clientContext = configClientContext(configServerUrl);


                                    if( ConfigCheck.showServerInfo(clientContext) ) {
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

                ConfigCheck.printSeparator();
                ConfigCheck.printField("Configuration Mode", "From url");

                ClientContext clientContext = configClientContext(configUrl);

                if( ConfigCheck.showServerInfo(clientContext)) {
                    exitCode = 0;
                }


            } else if (commandLine.hasOption(CONFIG_MKDIRS)
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);


                if( ConfigCheck.makeGobiiDirectories(propFileFqpn) ) {
                    exitCode = 0;
                }


            } else if (commandLine.hasOption(COPY_WARS)
                    && commandLine.hasOption(PROP_FILE_FQPN)) {

                String propFileFqpn = commandLine.getOptionValue(PROP_FILE_FQPN);
                String warFileFqpn = commandLine.getOptionValue(COPY_WARS);


                if( ConfigCheck.copyWars(propFileFqpn, warFileFqpn) ) {
                    exitCode = 0;
                }

            } else {
                formatter.printHelp(NAME_COMMAND, options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.exit(exitCode);

    }// main()

    private static ClientContext configClientContext(String configServerUrl) throws Exception {
        System.out.println();
        System.out.println();
        ConfigCheck.printSeparator();
        ConfigCheck.printField("Config request server", configServerUrl);

        System.out.println();
        System.out.println();
        ConfigCheck.printSeparator();

        return ClientContext.getInstance(configServerUrl, true);

    }

    private static boolean showServerInfo(ClientContext clientContext) throws Exception {

        boolean returnVal = true;

        // The logging framework emits debugging messages before it knows not to emit them.
        // Until we solve this problem, we we'll visually set those messages aside
        List<GobiiCropType> gobiiCropTypes = clientContext.getInstance(null, false).getCropTypeTypes();
        ConfigCheck.printSeparator();

        ConfigCheck.printField("Default crop", ClientContext.getInstance(null, false).getDefaultCropType().toString());

        for (GobiiCropType currentCropType : gobiiCropTypes) {

            ClientContext.getInstance(null, false).setCurrentClientCrop(currentCropType);

            ConfigCheck.printSeparator();
            ConfigCheck.printField("Crop Type", currentCropType.toString());
            ConfigCheck.printField("Host", ClientContext.getInstance(null, false).getCurrentCropDomain());
            ConfigCheck.printField("Port", ClientContext.getInstance(null, false).getCurrentCropPort().toString());
            ConfigCheck.printField("Context root", ClientContext.getInstance(null, false).getCurrentCropContextRoot());

            ConfigCheck.printField("Loader instructions directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.LOADERINSTRUCTION_FILES));
            ConfigCheck.printField("User file upload directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.RAWUSER_FILES));
            ConfigCheck.printField("Digester output directory ", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.INTERMEDIATE_FILES));
            ConfigCheck.printField("Extractor instructions directory", ClientContext.getInstance(null, false)
                    .getFileLocationOfCurrenCrop(GobiiFileLocationType.EXTRACTORINSTRUCTION_FILES));

            //if(!LineUtils.isNullOrEmpty())

            SystemUsers systemUsers = new SystemUsers();
            SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());

            if (ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword())) {

                PingDTO pingDTORequest = new PingDTO();
                pingDTORequest.setControllerType(ControllerType.LOADER);

                DtoRequestPing dtoRequestPing = new DtoRequestPing();
                PingDTO pingDTOResponse = dtoRequestPing.process(pingDTORequest);

                Integer responseNum = 1;
                if (pingDTOResponse.getDtoHeaderResponse().isSucceeded()) {
                    for (String currentResponse : pingDTOResponse.getPingResponses()) {
                        ConfigCheck.printField("Ping response " + (responseNum++).toString(), currentResponse);
                    }
                } else {
                    for (HeaderStatusMessage currentHeader : pingDTOResponse.getDtoHeaderResponse().getStatusMessages()) {
                        ConfigCheck.printField("Service error " + (responseNum++).toString(), currentHeader.getMessage());
                        returnVal = false;
                    }
                }
            } else {
                System.err.println("Authentication to server for crop failed: " + currentCropType.toString());
                returnVal = false;
            }
        }

        return returnVal;
    }

    private static boolean makeGobiiDirectories(String propFileFqpn) {

        boolean returnVal = true;

        try {

            ConfigSettings configSettings = new ConfigSettings(propFileFqpn);
            String gobiiRoot = configSettings.getFileSystemRoot();
            for (CropConfig currentCrop : configSettings.getActiveCropConfigs()) {

                printSeparator();
                printField("Checking directories for crop", currentCrop.getGobiiCropType().toString());

                for (String currentDirectory : directoriesRelativeToEachCrop) {


                    String directoryToMake = gobiiRoot + currentCrop
                            .getGobiiCropType()
                            .toString()
                            .toLowerCase()
                            + "/" + currentDirectory;
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
