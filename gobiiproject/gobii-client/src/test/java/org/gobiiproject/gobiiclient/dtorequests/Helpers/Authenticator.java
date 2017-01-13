package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;



/**
 * Created by Phil on 5/13/2016.
 */
public class Authenticator {


    public static boolean authenticate(GobiiCropType gobiiCropType) throws Exception {

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
        if (!TestValues.TEST_SSH) {

            initialConfigUrl = TestValues.INTITIAL_CONFIG_URL;
        } else {
            if (null == TestValues.SSH_OVERRIDE_HOST || null == TestValues.SSH_OVERRIDE_PORT) {
                throw new Exception("Cannot test SSH override without host and port");
            }

            initialConfigUrl = TestValues.SSH_OVERRKDE_INTITIAL_CONFIG_URL;
            ClientContext.setSshOverride(TestValues.SSH_OVERRIDE_HOST, TestValues.SSH_OVERRIDE_PORT);
        }


        // in a real client, the user would supply the complete url (including protocol an dport) at startup.
        // the URL we are constructing here should look like what the
        // end-user will specify (for example, http://biotech.cornell.edu:8080/gobii-rice)
        // the url _must_ include the protocol and port and context path -- everything
        // as if you were navigating to that path in a web browser


        GobiiCropType gobiiCropTypeDefault = ClientContext.getInstance(initialConfigUrl, true).getDefaultCropType();
        return Authenticator.authenticate(gobiiCropTypeDefault);
    }

    // not implemented yet
    public static boolean deAuthenticate() throws Exception {
        ClientContext.resetConfiguration();
        return true;
    }
}
