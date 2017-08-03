package org.gobiiproject.gobiiclient.gobii.Helpers;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.junit.Assert;
import org.junit.Ignore;

import java.io.File;
import java.util.UUID;

/**
 * Created by Phil on 10/31/2016.
 */
public class TestConfigurationSettings {


    private String testDirectory = "/tmp_gobii_test/";

    @Ignore // fails on SYS_INT
    public void configSettingsReadWrite() throws Exception {

        File testDir = new File(testDirectory);

        Assert.assertTrue("The configurationt est directory does not exist",
                testDir.exists());

        String newConfigFileNameFqpn = testDirectory + "test_config_" + UUID.randomUUID().toString() + ".xml";

        ConfigSettings.makeNew(newConfigFileNameFqpn);

        File createdConfigFile = new File(newConfigFileNameFqpn);

        Assert.assertTrue("The specified configuration file was not created",
                createdConfigFile.exists());

        String fileSysRootVal = "some-arbitrary-value";
        ConfigSettings configSettings = ConfigSettings.read(newConfigFileNameFqpn);
        configSettings.setFileSystemRoot(fileSysRootVal);
        configSettings.commit();


        ConfigSettings configSettingsSecondReference = ConfigSettings.read(newConfigFileNameFqpn);
        Assert.assertTrue("The configuration file does not contain the value that should have been set",
                configSettingsSecondReference.getFileSystemRoot().equals(fileSysRootVal));
        //** clean up after ourselves

        createdConfigFile.delete();


    }
}
