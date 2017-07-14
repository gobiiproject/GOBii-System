package org.gobiiproject.gobiiclient.dtorequests.infrastructure;

import org.gobiiproject.gobiiclient.core.common.TestConfiguration;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.CropDbConfig;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.types.GobiiAuthenticationType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by Phil on 11/3/2016.
 */


public class TestGobiiConfig {


    private static String ecnryptionWarning = "If the key used by the Decrypter class has changed, the enrypted usernames/passwords will have to be regenerated";
    private static String FILE_PATH_DELIMETER = "/";
    private static TestExecConfig testExecConfig = null;


    @BeforeClass
    public static void setUpClass() throws Exception {
        testExecConfig = new TestConfiguration().getConfigSettings().getTestExecConfig();
    }

    private String makeCommandline(String arguments) {

        String returnVal;

        
        returnVal = testExecConfig.getConfigUtilCommandlineStem() + "/gobiiconfig.jar" + " " + arguments;

        return returnVal;
    }

    private String getTestFileDirectoryOfRecord() {
        return testExecConfig.getConfigFileTestDirectory();
    }

    private String makeTestFileFqpn(String testPurpose) {

        String randomSuffix = UUID.randomUUID().toString();
        String returnVal = testExecConfig.getConfigFileTestDirectory()
                + FILE_PATH_DELIMETER
                + "test_"
                + testPurpose
                + "_"
                + randomSuffix
                + ".xml";
        return returnVal;

    }

    @Test
    public void testCreateNewConfigFile() throws Exception {

        String testFileFqpn = makeTestFileFqpn("createnewconfig");

        String commandLine = makeCommandline("-a -wfqpn " + testFileFqpn + " -gR \"/mnt/lustre\"");
        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        File createdFile = new File(testFileFqpn);
        Assert.assertTrue("File should have been created but does not exist"
                        + testFileFqpn,
                createdFile.exists());

    } //

    @Test
    public void testSetFileSystemRoot() throws Exception {

        String testFileFqpn = makeTestFileFqpn("filesystemroot");

        String fsSystemRoot = "/nowhere/subnowhere";
        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gR "
                + fsSystemRoot);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        File createdFile = new File(testFileFqpn);
        Assert.assertTrue("File should have been created but does not exist"
                        + testFileFqpn,
                createdFile.exists());

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);
        Assert.assertTrue("The file system root value read from the file does not match the input",
                configSettings.getFileSystemRoot().equals(fsSystemRoot));
    }

    @Test
    public void testSetEmailServer() throws Exception {

        String testFileFqpn = makeTestFileFqpn("setemailoptions");

        String user = "user_" + UUID.randomUUID().toString();
        String password = "password_" + UUID.randomUUID().toString();
        String host = "host_" + UUID.randomUUID().toString();
        String type = "type_" + UUID.randomUUID().toString();
        String hash = "hash_" + UUID.randomUUID().toString();
        Integer port = 25;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -stE "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + user
                + " -soP "
                + password
                + " -stT "
                + type
                + " -stH "
                + hash);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        Assert.assertTrue("The host name does not match",
                configSettings.getEmailSvrDomain().equals(host));

        Assert.assertTrue("The port does not match",
                configSettings.getEmailServerPort().equals(port));

        Assert.assertTrue("The user name does not match",
                configSettings.getEmailSvrUser().equals(user));

        Assert.assertTrue("The password does not match",
                configSettings.getEmailSvrPassword().equals(password));

        Assert.assertTrue("The email type does not match",
                configSettings.getEmailSvrType().equals(type));

        Assert.assertTrue("The hash type does not match",
                configSettings.getEmailSvrHashType().equals(hash));

    }


    @Test
    public void testSetAuthenticationlServer() throws Exception {

        String testFileFqpn = makeTestFileFqpn("setauthoptions");

        String gobiiAuthenticationTypeRaw = GobiiAuthenticationType.ACTIVE_DIRECTORY.toString();
        String ldapUserDnPattern = "dn_pattern_" + UUID.randomUUID().toString();
        String ldapUrl = "url_" + UUID.randomUUID().toString();
        String ldapBindUser = "bind_user_" + UUID.randomUUID().toString();
        String ldapBindPassword = "bind_password_" + UUID.randomUUID().toString();
        String ldapUserForBackgroundProcess = "run_asUser" + UUID.randomUUID().toString();
        String ldapPasswordForBackgroundProcess = "run_asPassword" + UUID.randomUUID().toString();


        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -auT "
                + gobiiAuthenticationTypeRaw
                + " -ldUDN "
                + ldapUserDnPattern
                + " -ldURL "
                + ldapUrl
                + " -ldBUSR "
                + ldapBindUser
                + " -ldBPAS "
                + ldapBindPassword
                + " -ldraUSR "
                + ldapUserForBackgroundProcess
                + " -ldraPAS "
                + ldapPasswordForBackgroundProcess
        );

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        Assert.assertTrue("The authentication type does not match",
                configSettings.getGobiiAuthenticationType().equals(GobiiAuthenticationType.ACTIVE_DIRECTORY));

        Assert.assertTrue("The ldap user dn pattern does not match",
                configSettings.getLdapUserDnPattern().equals(ldapUserDnPattern));

        Assert.assertTrue("The ldap url does not match",
                configSettings.getLdapUrl().equals(ldapUrl));

        Assert.assertTrue("The ldap bind user does not match",
                configSettings.getLdapBindUser().equals(ldapBindUser));

        Assert.assertTrue("The ldap bind password does not match",
                configSettings.getLdapBindPassword().equals(ldapBindPassword));

        Assert.assertTrue("The ldap background process user does not match",
                configSettings.getLdapUserForBackendProcs().equals(ldapUserForBackgroundProcess));

        Assert.assertTrue("The ldap background process password does not match",
                configSettings.getLdapPasswordForBackendProcs().equals(ldapPasswordForBackgroundProcess));

    }


    @Test
    public void testSetAuthenticationlServerEnryption() throws Exception {

        String testFileFqpn = makeTestFileFqpn("setauthoptions");

        String gobiiAuthenticationTypeRaw = GobiiAuthenticationType.ACTIVE_DIRECTORY.toString();
        String ldapUserDnPattern = "dn_pattern_" + UUID.randomUUID().toString();
        String ldapUrl = "url_" + UUID.randomUUID().toString();

        // These have to have been set up with the encryption tool, which is held in an
        // undisclosed location. So these are effectively hard coded. Note that these were
        // created using a specific key, which is also not disclosed. If the key changes,
        // these tests will fail, and the plain and encrypted values being used will have to be
        // changed
        String ldapBindUserPlain = "arbitraryUserId01";
        String ldapBindUserEncrypted = "MMmn4rz4WqjfWew2+kkwss/PfLIeQf2jIyY8XvKh8So=";

        String ldapBindPasswordPlain = "arbitraryPassword01";
        String ldapBindPasswordEncrypted = "vxI/Bh2/YLytBxLpA5ZBrPY/wHrxcSBuIQxcw9sULbg=";

        String ldapUserForBackgroundProcessPlain = "arbitraryUserId02";
        String ldapUserForBackgroundProcessEncrypted = "MMmn4rz4WqjfWew2+kkwsgiEcEZHqa1Y3B9ejaKhtDU=";

        String ldapPasswordForBackgroundProcessPlain = "arbitraryPassword02";
        String ldapPasswordForBackgroundProcessEncrypted = "vxI/Bh2/YLytBxLpA5ZBrGQeX3soc1pml0zSZv9o/KA=";


        String commandLineToWriteLdapConfig = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -auT "
                + gobiiAuthenticationTypeRaw
                + " -ldUDN "
                + ldapUserDnPattern
                + " -ldURL "
                + ldapUrl
                + " -ldBUSR "
                + ldapBindUserEncrypted
                + " -ldBPAS "
                + ldapBindPasswordEncrypted
                + " -ldraUSR "
                + ldapUserForBackgroundProcessEncrypted
                + " -ldraPAS "
                + ldapPasswordForBackgroundProcessEncrypted
        );

        boolean commandLineToWriteLdapConfigSucceded = HelperFunctions.tryExec(commandLineToWriteLdapConfig, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLineToWriteLdapConfig, commandLineToWriteLdapConfigSucceded);


        String commandLineToWriteDecryptionOption = makeCommandline("-wfqpn "
                + testFileFqpn
                + " -e true");
        boolean commandlineToWriteEncryptionOptionSucceded = HelperFunctions.tryExec(commandLineToWriteDecryptionOption, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLineToWriteDecryptionOption, commandlineToWriteEncryptionOptionSucceded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        Assert.assertTrue("Encryption options in configuration settings is not set to true",
                configSettings.isDecrypt());


        Assert.assertTrue("The plain ldap bind user retrieved does not match the encrypted ldap user that was written: " + ecnryptionWarning,
                configSettings.getLdapBindUser().equals(ldapBindUserPlain));

        Assert.assertTrue("The plain ldap bind password retrieved does not match the encrypted ldap password that was written: " + ecnryptionWarning,
                configSettings.getLdapBindPassword().equals(ldapBindPasswordPlain));

        String ldapUserForBackendProcessRetrieved = configSettings.getLdapUserForBackendProcs(); 
        Assert.assertTrue("The plain background run as user retrieved does not match the encrypted background run as user that was written: " + ecnryptionWarning,
                ldapUserForBackendProcessRetrieved.equals(ldapUserForBackgroundProcessPlain));

        String ldapPasswordForBackendProcessRetrieved = configSettings.getLdapPasswordForBackendProcs();
        Assert.assertTrue("The plain ldap background run as password retrieved does not match the encrypted background run as password user that was written: " + ecnryptionWarning,
                ldapPasswordForBackendProcessRetrieved.equals(ldapPasswordForBackgroundProcessPlain));

    }

    @Test
    public void testSetCropWebServerOptions() throws Exception {

        String testFileFqpn = makeTestFileFqpn("cropwebserver");

        String cropId = "foocrop";
        String host = "host_" + UUID.randomUUID().toString();
        String contextPathWithoutTerminator = "context-" + UUID.randomUUID().toString();
        String contextPathWithTerminator = contextPathWithoutTerminator + LineUtils.PATH_TERMINATOR;
        Integer port = 8080;

//     * Set crop web options:  -a -wfqpn "c:\gobii-config-test\testconfig.xml" -c "barcrop" -stW -soH "foohost" -soN 8080 -soU "foo userr" -soP "foo password" -soR "foo-web"

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + " -stW "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soR "
                + contextPathWithoutTerminator);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        CropConfig cropConfig = configSettings.getCropConfig(cropId);
        Assert.assertNotNull("The crop was not created: " + cropId,
                cropConfig);

        Assert.assertTrue("The host name does not match",
                cropConfig.getServiceDomain().equals(host));

        Assert.assertTrue("The port does not match: should be "
                        + port.toString()
                        + "; got: "
                        + cropConfig.getServicePort(),
                cropConfig.getServicePort().equals(port));

        Assert.assertTrue("Crop is not set to active by default",
                cropConfig.isActive());

        Assert.assertTrue("The context path not match",
                cropConfig.getServiceAppRoot().equals(contextPathWithTerminator));

    }

    @Test
    public void testSetPostGresForCrop() throws Exception {
        //-a -wfqpn "c:\gobii-config-test\testconfig.xml" -c "barcrop" -stP -soH "foohost" -soN 5433 -soU "foo userr" -soP "foo password" -soR "foodb"

        String testFileFqpn = makeTestFileFqpn("croppgsql");

        String cropId = "foocrop";
        String user = "user_" + UUID.randomUUID().toString();
        String password = "password_" + UUID.randomUUID().toString();
        String host = "host_" + UUID.randomUUID().toString();
        String contextPath = "foodbname-" + UUID.randomUUID().toString();
        Integer port = 5063;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + " -stP "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + user
                + " -soP "
                + password
                + " -soR "
                + contextPath);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        CropDbConfig cropDbConfig = configSettings.getCropConfig(cropId).getCropDbConfig(GobiiDbType.POSTGRESQL);
        Assert.assertNotNull("The crop db config was not created: " + cropId,
                cropDbConfig);

        Assert.assertTrue("The host name does not match",
                cropDbConfig.getHost().equals(host));

        Assert.assertTrue("The port does not match: should be "
                        + port.toString()
                        + "; got: "
                        + cropDbConfig.getPort(),
                cropDbConfig.getPort().equals(port));

        Assert.assertTrue("The context path not match",
                cropDbConfig.getDbName().equals(contextPath));

        Assert.assertTrue("The user name does not match",
                cropDbConfig.getUserName().equals(user));

        Assert.assertTrue("The password does not match",
                cropDbConfig.getPassword().equals(password));
    }

    @Test
    public void testSetPostGresForCropEncrypted() throws Exception {
        //-a -wfqpn "c:\gobii-config-test\testconfig.xml" -c "barcrop" -stP -soH "foohost" -soN 5433 -soU "foo userr" -soP "foo passwordPlain" -soR "foodb"

        String testFileFqpn = makeTestFileFqpn("croppgsql");

        String cropId = "foocrop";
        String userPlain = "arbitraryUserId04";
        String passwordPlain = "arbitraryPassword04";
        String userEncrypted = "MMmn4rz4WqjfWew2+kkwssZ4jLl/ekRA0uI918BNVLQ=";
        String passwordEncrypted = "vxI/Bh2/YLytBxLpA5ZBrIFUJejBkcv9uJlg5FrQGRw=";
        String host = "host_" + UUID.randomUUID().toString();
        String contextPath = "foodbname-" + UUID.randomUUID().toString();
        Integer port = 5063;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + " -stP "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + userEncrypted
                + " -soP "
                + passwordEncrypted
                + " -soR "
                + contextPath);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        String commandLineToWriteDecryptionOption = makeCommandline("-wfqpn "
                + testFileFqpn
                + " -e true");
        boolean commandlineToWriteEncryptionOptionSucceded = HelperFunctions.tryExec(commandLineToWriteDecryptionOption, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLineToWriteDecryptionOption, commandlineToWriteEncryptionOptionSucceded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        CropDbConfig cropDbConfig = configSettings.getCropConfig(cropId).getCropDbConfig(GobiiDbType.POSTGRESQL);
        Assert.assertNotNull("The crop db config was not created: " + cropId,
                cropDbConfig);

        Assert.assertTrue("The host name does not match",
                cropDbConfig.getHost().equals(host));

        Assert.assertTrue("The port does not match: should be "
                        + port.toString()
                        + "; got: "
                        + cropDbConfig.getPort(),
                cropDbConfig.getPort().equals(port));

        Assert.assertTrue("The context path not match",
                cropDbConfig.getDbName().equals(contextPath));

        Assert.assertTrue("The plain user retrieved name does not match the enrypted user written: " + ecnryptionWarning,
                cropDbConfig.getUserName().equals(userPlain));

        Assert.assertTrue("The plain password retrieved name does not match the enrypted password written: " + ecnryptionWarning,
                cropDbConfig.getPassword().equals(passwordPlain));
    }


    @Test
    public void testSetMonetGresForCrop() throws Exception {
        String testFileFqpn = makeTestFileFqpn("cropmonet");

        String cropId = "barcrop";
        String user = "user_" + UUID.randomUUID().toString();
        String password = "password_" + UUID.randomUUID().toString();
        String host = "host_" + UUID.randomUUID().toString();
        String contextPath = "foodbname-" + UUID.randomUUID().toString();
        Integer port = 5063;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + " -stM "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + user
                + " -soP "
                + password
                + " -soR "
                + contextPath);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        CropDbConfig cropDbConfig = configSettings.getCropConfig(cropId).getCropDbConfig(GobiiDbType.MONETDB);
        Assert.assertNotNull("The crop db config was not created: " + cropId,
                cropDbConfig);

        Assert.assertTrue("The host name does not match",
                cropDbConfig.getHost().equals(host));

        Assert.assertTrue("The port does not match: should be "
                        + port.toString()
                        + "; got: "
                        + cropDbConfig.getPort(),
                cropDbConfig.getPort().equals(port));

        Assert.assertTrue("The context path not match",
                cropDbConfig.getDbName().equals(contextPath));

        Assert.assertTrue("The user name does not match",
                cropDbConfig.getUserName().equals(user));

        Assert.assertTrue("The password does not match",
                cropDbConfig.getPassword().equals(password));
    }


    @Test
    public void testSetMonetGresForCropEncrypted() throws Exception {
        String testFileFqpn = makeTestFileFqpn("cropmonet");

        String cropId = "barcrop";
        String userPlain = "arbitraryUserId05";
        String passwordPlain = "arbitraryPassword05";
        String userEncrypted = "MMmn4rz4WqjfWew2+kkwstTa47exiUVJDw0pL5wcwr8=";
        String passwordEncrypted = "vxI/Bh2/YLytBxLpA5ZBrBnxWbwoLeJ6Sv4nM3n14J0=";
        String host = "host_" + UUID.randomUUID().toString();
        String contextPath = "foodbname-" + UUID.randomUUID().toString();
        Integer port = 5063;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + " -stM "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + userEncrypted
                + " -soP "
                + passwordEncrypted
                + " -soR "
                + contextPath);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        String commandLineToWriteDecryptionOption = makeCommandline("-wfqpn "
                + testFileFqpn
                + " -e true");
        boolean commandlineToWriteEncryptionOptionSucceded = HelperFunctions.tryExec(commandLineToWriteDecryptionOption, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLineToWriteDecryptionOption, commandlineToWriteEncryptionOptionSucceded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        CropDbConfig cropDbConfig = configSettings.getCropConfig(cropId).getCropDbConfig(GobiiDbType.MONETDB);
        Assert.assertNotNull("The crop db config was not created: " + cropId,
                cropDbConfig);

        Assert.assertTrue("The host name does not match",
                cropDbConfig.getHost().equals(host));

        Assert.assertTrue("The port does not match: should be "
                        + port.toString()
                        + "; got: "
                        + cropDbConfig.getPort(),
                cropDbConfig.getPort().equals(port));

        Assert.assertTrue("The context path not match",
                cropDbConfig.getDbName().equals(contextPath));

        Assert.assertTrue("The retrieved user name does not match the encrypted user name: " + ecnryptionWarning,
                cropDbConfig.getUserName().equals(userPlain));

        Assert.assertTrue("The retrieved password does not match the encrypted password: " + ecnryptionWarning,
                cropDbConfig.getPassword().equals(passwordPlain));
    }


    @Test
    public void testSetTestOptions() throws Exception {

        String testFileFqpn = makeTestFileFqpn("testvals");

        String configFileTestDirectory = "test_dir_" + UUID.randomUUID().toString();
        String configUtilCommandlineStem = "comandstem_" + UUID.randomUUID().toString();
        String initialConfigUrl = "configurl_" + UUID.randomUUID().toString();
        String initialConfigUrlForSshOverride = "urlssh" + UUID.randomUUID().toString();
        String sshOverrideHost = "hostssh_" + UUID.randomUUID().toString();
        Integer sshOverridePort = 5;
        String testCrop = "testcrop_" + UUID.randomUUID().toString();
        boolean isTestSsh = false;
        String ldapUserForUnitTest = "ldapUnitTestUser_" + UUID.randomUUID().toString();
        String ldapPasswordForUnitTest = "ldapUnitTestPassword_" + UUID.randomUUID().toString();


        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gt "
                + " -gtcd "
                + configFileTestDirectory
                + " -gtcr "
                + testCrop
                + " -gtcs "
                + configUtilCommandlineStem
                + " -gtiu "
                + initialConfigUrl
                + " -gtsf "
                + (isTestSsh ? "true" : "false")
                + " -gtsh "
                + sshOverrideHost
                + " -gtsp "
                + sshOverridePort
                + " -gtsu "
                + initialConfigUrlForSshOverride
                + " -gtldu "
                + ldapUserForUnitTest
                + " -gtldp "
                + ldapPasswordForUnitTest
        );


        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);


        Assert.assertTrue("Config test value does not match: test directory", configSettings.getTestExecConfig().getConfigFileTestDirectory().equals(configFileTestDirectory));
        Assert.assertTrue("Config test value does not match: commandline stem", configSettings.getTestExecConfig().getConfigUtilCommandlineStem().equals(configUtilCommandlineStem));
        Assert.assertTrue("Config test value does not match: config URL", configSettings.getTestExecConfig().getInitialConfigUrl().equals(initialConfigUrl));
        Assert.assertTrue("Config test value does not match: ssh override URL", configSettings.getTestExecConfig().getInitialConfigUrlForSshOverride().equals(initialConfigUrlForSshOverride));
        Assert.assertTrue("Config test value does not match: ssh override host", configSettings.getTestExecConfig().getSshOverrideHost().equals(sshOverrideHost));
        Assert.assertTrue("Config test value does not match: ssh override port", configSettings.getTestExecConfig().getSshOverridePort().equals(sshOverridePort));
        Assert.assertTrue("Config test value does not match: test crop", configSettings.getTestExecConfig().getTestCrop().equals(testCrop));
        Assert.assertTrue("Config test value does not match: test flag", configSettings.getTestExecConfig().isTestSsh() == isTestSsh);
        Assert.assertTrue("Config test value does not match: ldap user", configSettings.getTestExecConfig().getLdapUserForUnitTest().equals(ldapUserForUnitTest));
        Assert.assertTrue("Config test value does not match: ldap password", configSettings.getTestExecConfig().getLdapPasswordForUnitTest().equals(ldapPasswordForUnitTest));
        Assert.assertTrue("Config test value does not match: ldap password", configSettings.getTestExecConfig().getLdapPasswordForUnitTest().equals(ldapPasswordForUnitTest));
    }

    @Test
    public void testSetTestOptionsWithEncryption() throws Exception {

        String testFileFqpn = makeTestFileFqpn("testvals");

        String configFileTestDirectory = "test_dir_" + UUID.randomUUID().toString();
        String configUtilCommandlineStem = "comandstem_" + UUID.randomUUID().toString();
        String initialConfigUrl = "configurl_" + UUID.randomUUID().toString();
        String initialConfigUrlForSshOverride = "urlssh" + UUID.randomUUID().toString();
        String sshOverrideHost = "hostssh_" + UUID.randomUUID().toString();
        Integer sshOverridePort = 5;
        String testCrop = "testcrop_" + UUID.randomUUID().toString();
        boolean isTestSsh = false;

        String ldapBindUserPlain = "arbitraryUserId03";
        String ldapBindUserEncrypted = "MMmn4rz4WqjfWew2+kkwsg9Ncz04FPQGLxOvXdvjSfk=";

        String ldapBindPasswordPlain = "arbitraryPassword03";
        String ldapBindPasswordEncrypted = "vxI/Bh2/YLytBxLpA5ZBrN/+l5ltxdJe1ohiILwvYBA=";


        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gt "
                + " -gtcd "
                + configFileTestDirectory
                + " -gtcr "
                + testCrop
                + " -gtcs "
                + configUtilCommandlineStem
                + " -gtiu "
                + initialConfigUrl
                + " -gtsf "
                + (isTestSsh ? "true" : "false")
                + " -gtsh "
                + sshOverrideHost
                + " -gtsp "
                + sshOverridePort
                + " -gtsu "
                + initialConfigUrlForSshOverride
                + " -gtldu "
                + ldapBindUserEncrypted
                + " -gtldp "
                + ldapBindPasswordEncrypted
        );


        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        String commandLineToWriteDecryptionOption = makeCommandline("-wfqpn "
                + testFileFqpn
                + " -e true");

        boolean commandlineToWriteEncryptionOptionSucceded = HelperFunctions.tryExec(commandLineToWriteDecryptionOption, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLineToWriteDecryptionOption, commandlineToWriteEncryptionOptionSucceded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);


        Assert.assertTrue("Config test value does not match: test directory", configSettings.getTestExecConfig().getConfigFileTestDirectory().equals(configFileTestDirectory));
        Assert.assertTrue("Config test value does not match: commandline stem", configSettings.getTestExecConfig().getConfigUtilCommandlineStem().equals(configUtilCommandlineStem));
        Assert.assertTrue("Config test value does not match: config URL", configSettings.getTestExecConfig().getInitialConfigUrl().equals(initialConfigUrl));
        Assert.assertTrue("Config test value does not match: ssh override URL", configSettings.getTestExecConfig().getInitialConfigUrlForSshOverride().equals(initialConfigUrlForSshOverride));
        Assert.assertTrue("Config test value does not match: ssh override host", configSettings.getTestExecConfig().getSshOverrideHost().equals(sshOverrideHost));
        Assert.assertTrue("Config test value does not match: ssh override port", configSettings.getTestExecConfig().getSshOverridePort().equals(sshOverridePort));
        Assert.assertTrue("Config test value does not match: test crop", configSettings.getTestExecConfig().getTestCrop().equals(testCrop));
        Assert.assertTrue("Config test value does not match: test flag", configSettings.getTestExecConfig().isTestSsh() == isTestSsh);
        Assert.assertTrue("Plain ldap unit test user retrieved does not match the encrypted user: " + ecnryptionWarning,
                configSettings.getTestExecConfig().getLdapUserForUnitTest().equals(ldapBindUserPlain));
        Assert.assertTrue("Plain ldap unit test password retrieved does not match the encrypted password: " + ecnryptionWarning,
                configSettings.getTestExecConfig().getLdapPasswordForUnitTest().equals(ldapBindPasswordPlain));
    }

    @Test
    public void testCreateDirectories() throws Exception {

        String testFileFqpn = makeTestFileFqpn("createdirs");

        String testRootDirectory = getTestFileDirectoryOfRecord() + FILE_PATH_DELIMETER + UUID.randomUUID().toString();

        String createConfigCommand = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gR "
                + testRootDirectory);

        boolean succeeded = HelperFunctions.tryExec(createConfigCommand, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + createConfigCommand, succeeded);

        createCrops(testFileFqpn, Arrays.asList("dev", "test", "extra"));

        String createDirectoriesCommand = makeCommandline(" -wfqpn "
                + testFileFqpn
                + " -wdirs ");


        succeeded = HelperFunctions.tryExec(createDirectoriesCommand, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + createDirectoriesCommand, succeeded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);
        for (CropConfig currentCropConfig : configSettings.getActiveCropConfigs()) {

            for (GobiiFileProcessDir currentRelativeDirectory : EnumSet.allOf(GobiiFileProcessDir.class)) {

                String currentCropDir = configSettings.getProcessingPath(currentCropConfig.getGobiiCropType(), currentRelativeDirectory);
                File file = new File(currentCropDir);
                Assert.assertTrue("Crop directory was not created: " + currentCropDir, file.exists());
                Assert.assertTrue("Crop fqpn was not created as a directory: " + currentCropDir, file.isDirectory());
            }
        }

    }

    private boolean createCrops(String testFileFqpn, List<String> cropIds) throws Exception {

        boolean addCropSucceeded = false;

        for (String currentCropId : cropIds) {


            String host = "host_" + UUID.randomUUID().toString();
            String contextPath = "context-" + UUID.randomUUID().toString();
            Integer port = 8080;

            String commandLineForCurrentCrop = makeCommandline("-a -wfqpn "
                    + testFileFqpn
                    + " -c "
                    + currentCropId
                    + " -stW "
                    + " -soH "
                    + host
                    + " -soN "
                    + port.toString()
                    + " -soR "
                    + contextPath);

            addCropSucceeded = HelperFunctions.tryExec(commandLineForCurrentCrop, testFileFqpn + ".out", testFileFqpn + ".err");
            Assert.assertTrue("Command failed: " + commandLineForCurrentCrop, addCropSucceeded);

        } // iterate crops to create

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);
        for (String currentCropId : cropIds) {
            addCropSucceeded = (configSettings.getCropConfig(currentCropId) != null);
            Assert.assertTrue("The crop was not created: " + currentCropId,
                    addCropSucceeded);
        }

        return addCropSucceeded;
    }

    private boolean configureWebServer(String testFileFqpn,
                                       String cropId,
                                       String host,
                                       String contextPath,
                                       Integer port) {

        boolean returnVal = false;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + " -stW "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soR "
                + contextPath);

        returnVal = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, returnVal);


        return returnVal;
    }

    private boolean configureDataBase(String testFileFqpn,
                                      String cropId,
                                      GobiiDbType gobiiDbType,
                                      String host,
                                      String databaseName,
                                      Integer port,
                                      String user,
                                      String password) {

        boolean returnVal;


        String serverType = gobiiDbType == GobiiDbType.POSTGRESQL ? " -stP " : "-stM ";

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + serverType
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + user
                + " -soP "
                + password
                + " -soR "
                + databaseName);

        returnVal = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, returnVal);

        return returnVal;

    }

    @Ignore // fails on SYS_INT
    public void testSetCropActive() throws Exception {

        String testFileFqpn = makeTestFileFqpn("setcropactive");

        createCrops(testFileFqpn, Arrays.asList("dev", "test", "extra"));

        String commandSetTestActive = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + " test "
                + " -cA ");

        boolean succeeded = HelperFunctions.tryExec(commandSetTestActive, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandSetTestActive, succeeded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);
        Assert.assertTrue("The TEST Crop was not marked active", configSettings.getCropConfig("test").isActive());


        String commandSetTestNotActive = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + "test "
                + " -cD ");

        succeeded = HelperFunctions.tryExec(commandSetTestNotActive, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandSetTestNotActive, succeeded);

        configSettings = new ConfigSettings(testFileFqpn);
        Assert.assertFalse("The test Crop was not marked inactive", configSettings.getCropConfig("test").isActive());

    }

    @Ignore // fails on SYS_INT
    public void removeCrop() throws Exception {

        String testFileFqpn = makeTestFileFqpn("removecrop");

        createCrops(testFileFqpn, Arrays.asList("dev", "test", "extra"));

        String cropToRemove = "test";

        String commandRemoveCrop = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -cR "
                + " " + cropToRemove + " ");

        boolean succeeded = HelperFunctions.tryExec(commandRemoveCrop, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandRemoveCrop, succeeded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        Assert.assertFalse("The crop was not removed: " + cropToRemove,
                configSettings.isCropDefined(cropToRemove));
    }

    @Ignore // fails on SYS_INT
    public void testSetDefaultCrop() throws Exception {

        String testFileFqpn = makeTestFileFqpn("defaultcrop");

        createCrops(testFileFqpn, Arrays.asList("dev", "test", "extra"));

        String defaultCrop = "dev";

        String setDefaultCrop = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gD "
                + " " + defaultCrop + " ");

        boolean succeeded = HelperFunctions.tryExec(setDefaultCrop, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + setDefaultCrop, succeeded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        Assert.assertTrue("The crop was not set as the default: " + defaultCrop,
                configSettings.getDefaultGobiiCropType().equals(defaultCrop));
    }


    @Ignore// fails on SYS_INT
    public void testSetLogFileLocation() throws Exception {

        String testFileFqpn = makeTestFileFqpn("logfilelocation");

        String logFileLocation = "log_" + UUID.randomUUID().toString();

        String setLogFileLocation = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gL "
                + " " + logFileLocation + " ");

        boolean succeeded = HelperFunctions.tryExec(setLogFileLocation, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + setLogFileLocation, succeeded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        Assert.assertTrue("The log file location was not set: " + logFileLocation,
                configSettings.getFileSystemLog().equals(logFileLocation));
    }


    /**
     * This method creates a complete configuration file. It passes the verificaiton test in GobiiConfig.
     * It has also been verified that with this configuraiton, the web server will start and unit tests will
     * run. It has not yet been tested with the Digestor and Extractor
     */
    @Ignore // fails on SYS_INT
    public void makeValidConfigFile() throws Exception {

        String testFileFqpn = makeTestFileFqpn("makecompleteconfig");


        // SET FILE SYSTEM ROOT ****************************
        String commandLine = makeCommandline("-a -wfqpn " + testFileFqpn + " -gR \"/shared_data/gobii/\"");
        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        // SET EMAIL OPTIONS **********************************
        String user = "example@gmail.com";
        String password = "password";
        String host = "smtp.gmail.com";
        String type = "SMTP";
        String hash = "na";
        Integer port = 465;

        commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -stE "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + user
                + " -soP "
                + password
                + " -stT "
                + type
                + " -stH "
                + hash);

        succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        // CONFIGURE THE VARIOUS SERVERS *******************************
        String cropIdDev = " dev ";
        String cropidTest = " test ";

        configureWebServer(testFileFqpn,
                cropIdDev,
                "localhost",
                "/gobii-dev/",
                8282);
        configureWebServer(testFileFqpn,
                cropidTest,
                "localhost",
                "/gobii-test/",
                8383);


        configureDataBase(testFileFqpn,
                cropIdDev,
                GobiiDbType.POSTGRESQL,
                "localhost",
                "gobii_dev",
                5432,
                "appuser",
                "password");

        configureDataBase(testFileFqpn,
                cropIdDev,
                GobiiDbType.MONETDB,
                "localhost",
                "gobii_dev",
                5000,
                "appuser",
                "appuser");

        configureDataBase(testFileFqpn,
                cropidTest,
                GobiiDbType.POSTGRESQL,
                "localhost",
                "gobii_test",
                5432,
                "appuser",
                "appuser");

        configureDataBase(testFileFqpn,
                cropidTest,
                GobiiDbType.MONETDB,
                "localhost",
                "gobii_test",
                5000,
                "appuser",
                "appuser");


        // CONFIGURE TEST SETTINGS **********************************

        String configFileTestDirectory = "/gobii-config-test";
        String configUtilCommandlineStem = " \"java -jar C:\\phil-source\\IntelliJ\\gobiiproject\\gobii-process\\target\\gobiiconfig.jar\" ";
        String initialConfigUrl = "http://localhost:8282/gobii-dev";
        String initialConfigUrlForSshOverride = "http://localhost:8080/gobii-dev";
        String sshOverrideHost = "localhost";
        Integer sshOverridePort = 8080;

        boolean isTestSsh = false;

        commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gt "
                + " -gtcd "
                + configFileTestDirectory
                + " -gtcr "
                + cropIdDev // SIC
                + " -gtcs "
                + configUtilCommandlineStem
                + " -gtiu "
                + initialConfigUrl
                + " -gtsf "
                + (isTestSsh ? "true" : "false")
                + " -gtsh "
                + sshOverrideHost
                + " -gtsp "
                + sshOverridePort
                + " -gtsu "
                + initialConfigUrlForSshOverride);


        succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        // ************************ SET DEFAULT CROP TYPE
        commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gD "
                + " " + cropIdDev + " ");

        succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        // ******************** SET LOG FILE LOCATION
        String logFileLocation = "/shared_data/gobii";

        commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gL "
                + " " + logFileLocation + " ");

        succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        // *********************** MARK TEST INSTANCE NOT ACTIVE
        String commandline = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropidTest
                + " -cD ");

        succeeded = HelperFunctions.tryExec(commandline, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandline, succeeded);


        // ************************ VALIDATE WHAT WE CREATED
        commandLine = makeCommandline("-validate -wfqpn " + testFileFqpn);
        succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

    }

}
