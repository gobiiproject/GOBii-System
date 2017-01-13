package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;


/**
 * Created by Phil on 5/13/2016.
 */
public class Authenticator {

    private static TestExecConfig testExecConfig = null;

    private static TestExecConfig getTestExecConfig() throws Exception{
        if (testExecConfig == null) {
            testExecConfig = (new TestConfiguration()).getConfigSettings().getTestExecConfig();
        }

        return testExecConfig;
    }


    public static boolean authenticate(String gobiiCropType) throws Exception {

        getTestExecConfig();

        // this method assumes we've already initialized the context with the server URL
        ClientContext.getInstance(null, false).setCurrentClientCrop(gobiiCropType);
        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());

        return ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword());
    }

    public static boolean authenticate() throws Exception {

        ClientContext.resetConfiguration();

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
            ClientContext.setSshOverride(getTestExecConfig().getSshOverrideHost(),
                    getTestExecConfig().getSshOverridePort());
        }


        // in a real client, the user would supply the complete url (including protocol an dport) at startup.
        // the URL we are constructing here should look like what the
        // end-user will specify (for example, http://biotech.cornell.edu:8080/gobii-rice)
        // the url _must_ include the protocol and port and context path -- everything
        // as if you were navigating to that path in a web browser


        //String gobiiCropTypeDefault = ClientContext.getInstance(initialConfigUrl, true).getDefaultCropType();
        ClientContext.getInstance(initialConfigUrl, true);
        String gobiiTestCrop = getTestExecConfig().getTestCrop();
        return Authenticator.authenticate(gobiiTestCrop);
    }

    // not implemented yet
    public static boolean deAuthenticate() throws Exception {
        ClientContext.resetConfiguration();
        return true;
    }
}
