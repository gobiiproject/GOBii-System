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

        String userReaderName = "READER";
        userDetails.put(userReaderName ,
                new SystemUserDetail(userReaderName , "password", "READER"));

        String userImporterName = "IMPORTER";
        userDetails.put(userImporterName,
                new SystemUserDetail(userImporterName, "password", "ADMIN"));

    } // ctor

    public SystemUserDetail getDetail(String userName) {

        return userDetails.get(userName);
    }

} // SystemUserDetails