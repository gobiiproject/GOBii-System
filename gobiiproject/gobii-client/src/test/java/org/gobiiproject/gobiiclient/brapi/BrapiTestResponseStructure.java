package org.gobiiproject.gobiiclient.brapi;

import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.junit.Assert;

/**
 * Created by Phil on 12/16/2016.
 */
public class BrapiTestResponseStructure {


    public static void validatateBrapiResponseStructure(BrapiMetaData brapiMetaData) {

        Assert.assertNotNull(brapiMetaData);
        Assert.assertNotNull(brapiMetaData.getDatafiles());
        Assert.assertNotNull(brapiMetaData.getPagination());
        Assert.assertNotNull(brapiMetaData.getStatus());

    }

}
