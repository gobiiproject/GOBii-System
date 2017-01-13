package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;

/**
 * Created by Phil on 4/18/2016.
 */
public class TestUtils {

    public static boolean  checkAndPrintHeaderMessages(DtoMetaData dtoMetaData ) {

        boolean returnVal = false;

        if (!dtoMetaData.getDtoHeaderResponse().isSucceeded()) {
            returnVal = true;
            System.out.println();
            System.out.println("*** Header errors: ");
            for (HeaderStatusMessage currentStatusMesage : dtoMetaData.getDtoHeaderResponse().getStatusMessages()) {
                System.out.println(currentStatusMesage.getMessage());
            }
        }

        return returnVal;
    }
}
