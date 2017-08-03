package org.gobiiproject.gobiiclient.core.gobii;

import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.utils.LineUtils;


/**
 * Created by Phil on 5/13/2016.
 */
public class GobiiClientContextAuth {

    private static TestExecConfig testExecConfig = null;

    public static TestExecConfig getTestExecConfig() throws Exception{
        if (testExecConfig == null) {
            testExecConfig = (new GobiiTestConfiguration()).getConfigSettings().getTestExecConfig();
        }

        return testExecConfig;
    }


    private static boolean authenticate(TestExecConfig testExecConfig, String cropId ) throws Exception {

        getTestExecConfig();

        // this method assumes we've already initialized the context with the server URL
        String clientCrop = null;
        if(LineUtils.isNullOrEmpty( cropId )) {

            clientCrop = testExecConfig.getTestCrop();
        } else {
            clientCrop = cropId;

        }


        String testUserName = testExecConfig.getLdapUserForUnitTest();
        String testPassword = testExecConfig.getLdapPasswordForUnitTest();

        return GobiiClientContext.getInstance(null, false).login(clientCrop, testUserName, testPassword);
    }

    public static boolean authenticate() throws Exception {
        return  authenticate(null);
    }

    public static boolean authenticate(String cropId ) throws Exception {

        GobiiClientContext.resetConfiguration();

        String initialConfigUrl = null;
        // clear the current context so that we start from scratch populating server configs
        
        if (!getTestExecConfig().isTestSsh()) {

            initialConfigUrl = getTestExecConfig().getInitialConfigUrl();
        } else {
            if (null == getTestExecConfig().getSshOverrideHost()
                    || null == getTestExecConfig().getSshOverridePort()) {
                throw new Exception("Cannot test SSH override without host and port");
            }

            initialConfigUrl = getTestExecConfig().getInitialConfigUrlForSshOverride();
            GobiiClientContext.setSshOverride(getTestExecConfig().getSshOverrideHost(),
                    getTestExecConfig().getSshOverridePort());
        }


        // in a real client, the user would supply the complete url (including protocol an dport) at startup.
        // the URL we are constructing here should look like what the
        // end-user will specify (for example, http://biotech.cornell.edu:8080/gobii-rice)
        // the url _must_ include the protocol and port and context path -- everything
        // as if you were navigating to that path in a web browser


        //String gobiiCropTypeDefault = GobiiClientContext.getInstance(initialConfigUrl, true).getDefaultCropType();
        GobiiClientContext.getInstance(initialConfigUrl, true);

        return GobiiClientContextAuth.authenticate(getTestExecConfig(),cropId);
    }

    // not implemented yet
    public static boolean deAuthenticate() throws Exception {
        GobiiClientContext.resetConfiguration();
        return true;
    }
}
