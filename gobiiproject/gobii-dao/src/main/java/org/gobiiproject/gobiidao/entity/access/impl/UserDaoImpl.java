// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiidao.entity.access.impl;

import org.gobiiproject.gobiidao.entity.access.UserDao;
import org.gobiiproject.gobiimodel.entity.User;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.springframework.stereotype.Component;

/**
 * This is domain DAO to entityaccess users. Kinda fake here.
 */
@Component
public class UserDaoImpl implements UserDao {

    public User getByLogin(String login) {

        User returnVal = null;

        SystemUsers systemUsers = new SystemUsers();

        SystemUserDetail userDetail = systemUsers.getDetail(login);

        // we are in the process of migrating to the system user map
        if (null != userDetail) {
            returnVal = new User(userDetail.getUserName(), userDetail.getUserName(), userDetail.getPassword(), userDetail.getRoles());
        } else {

            // parameter order for user: String login, String name, String password, String... roles
            switch (login) {
                case "admin":
                    returnVal = new User("admin", "Administrator", "admin", "ADMIN");
                    break;
                case "special":
                    returnVal = new User("special", "Special Expert", "special", "ROLE_SPECIAL");
                    break;
                case "user1":
                    returnVal = new User("user1", "User Uno", "user1");
                    break;
                case "Aladdin":
                    returnVal = new User("Aladdin", "Aladdin", "open sesame");
                    break;
                case "lokesh":
                    returnVal = new User("lokesh", "lockesh", "password1", "NOBODY");
                    break;
                case "reader_guy":
                    returnVal = new User("reader_guy", "can only read", "password1", "READER");
                    break;
                default:
                    returnVal = null;
            }
        }

        return (returnVal);
    }
}

/*
*   <org.gobiiproject.gobidomain.security:authentication-manager alias="authenticationManager">
        <org.gobiiproject.gobidomain.security:authentication-provider>
            <org.gobiiproject.gobidomain.security:user-service>
                <org.gobiiproject.gobidomain.security:user name="lokesh" password="password1" authorities="NOBODY" />
                <org.gobiiproject.gobidomain.security:user name="reader_guy" password="password1" authorities="READER" />
                <org.gobiiproject.gobidomain.security:user name="admin" password="password2" authorities="ADMIN" />
                <org.gobiiproject.gobidomain.security:user name="USER_IMPORTER" password="password2" authorities="ADMIN" />
            </org.gobiiproject.gobidomain.security:user-service>
        </org.gobiiproject.gobidomain.security:authentication-provider>
    </org.gobiiproject.gobidomain.security:authentication-manager>
* */