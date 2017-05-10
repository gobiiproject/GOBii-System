package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.security.Decrypter;
import org.simpleframework.xml.Element;

/**
 * This class encapsualtes values that are necessry for the integration test framework.
 */
public class TestExecConfig {

    @Element(required = false)
    private String testCrop = "DEV";

    @Element(required = false)
    private String initialConfigUrl = "http://localhost:8282/gobii-dev";

    @Element(required = false)
    private String initialConfigUrlForSshOverride = "http://localhost:8080/gobii-dev";

    @Element(required = false)
    private String sshOverrideHost = "localhost";

    @Element(required = false)
    private Integer sshOverridePort = 8080;

    @Element(required = false)
    private boolean isTestSsh = false;

    @Element(required = false)
    private String configFileTestDirectory = "/gobii-config-test";

    @Element(required = false)
    private String configUtilCommandlineStem = "java -jar C:\\phil-source\\IntelliJ\\gobiiproject\\gobii-process\\target\\gobiiconfig.jar";

    @Element(required = false)
    private String ldapUserForUnitTest;

    @Element(required = false)
    private String ldapPasswordForUnitTest;

    @Element(required = false)
    private boolean decrypt = false;

    public String getTestCrop() {
        return testCrop;
    }

    public void setTestCrop(String testCrop) {
        this.testCrop = testCrop;
    }

    public String getInitialConfigUrl() {
        return initialConfigUrl;
    }

    public TestExecConfig setInitialConfigUrl(String initialConfigUrl) {
        this.initialConfigUrl = initialConfigUrl;
        return this;
    }

    public String getInitialConfigUrlForSshOverride() {
        return initialConfigUrlForSshOverride;
    }

    public TestExecConfig setInitialConfigUrlForSshOverride(String initialConfigUrlForSshOverride) {
        this.initialConfigUrlForSshOverride = initialConfigUrlForSshOverride;
        return this;
    }

    public String getSshOverrideHost() {
        return sshOverrideHost;
    }

    public TestExecConfig setSshOverrideHost(String sshOverrideHost) {
        this.sshOverrideHost = sshOverrideHost;
        return this;
    }

    public Integer getSshOverridePort() {
        return sshOverridePort;
    }

    public TestExecConfig setSshOverridePort(Integer sshOverridePort) {
        this.sshOverridePort = sshOverridePort;
        return this;
    }

    public boolean isTestSsh() {
        return isTestSsh;
    }

    public TestExecConfig setTestSsh(boolean testSsh) {
        isTestSsh = testSsh;
        return this;
    }

    public String getConfigFileTestDirectory() {
        return configFileTestDirectory;
    }

    public TestExecConfig setConfigFileTestDirectory(String configFileTestDirectory) {
        this.configFileTestDirectory = configFileTestDirectory;
        return this;
    }

    public String getConfigUtilCommandlineStem() {
        return configUtilCommandlineStem;
    }

    public TestExecConfig setConfigUtilCommandlineStem(String configUtilCommandlineStem) {
        this.configUtilCommandlineStem = configUtilCommandlineStem;
        return this;
    }

    public String getLdapUserForUnitTest() {
        
        String returnVal;

        if (this.decrypt) {
            returnVal = Decrypter.decrypt(this.ldapUserForUnitTest, null);
        } else {
            returnVal = this.ldapUserForUnitTest;
        }

        return returnVal;
    }

    public void setLdapUserForUnitTest(String ldapUserForUnitTest) {
        this.ldapUserForUnitTest = ldapUserForUnitTest;
    }

    public String getLdapPasswordForUnitTest() {

        String returnVal;

        if (this.decrypt) {
            returnVal = Decrypter.decrypt(this.ldapPasswordForUnitTest, null);
        } else {
            returnVal = this.ldapPasswordForUnitTest;
        }

        return returnVal;
    }

    public void setLdapPasswordForUnitTest(String ldapPasswordForUnitTest) {
        this.ldapPasswordForUnitTest = ldapPasswordForUnitTest;
    }

    public boolean isDecrypt() {
        return decrypt;
    }

    public void setDecrypt(boolean decrypt) {
        this.decrypt = decrypt;
    }
}
