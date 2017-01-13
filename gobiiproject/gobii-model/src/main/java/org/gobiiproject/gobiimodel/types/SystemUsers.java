// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.types;

import java.util.*;

/**
 * Created by MrPhil on 7/3/2015.
 */

public class SystemUsers {


    private Map<String, SystemUserDetail> userDetails = new HashMap<>();

    public SystemUsers() {

        userDetails.put(SystemUserNames.USER_READER.toString(),
                new SystemUserDetail(SystemUserNames.USER_READER.toString(), "reader", "READER"));

        userDetails.put(SystemUserNames.USER_IMPORTER.toString(),
                new SystemUserDetail(SystemUserNames.USER_IMPORTER.toString(), "password2", "ADMIN"));

    } // ctor

    public SystemUserDetail getDetail(String userName) {

        return userDetails.get(userName);
    }

} // SystemUserDetails