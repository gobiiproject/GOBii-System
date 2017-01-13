package org.gobiiproject.gobiimodel.types;

/**
 * Created by Phil on 3/25/2016.
 */
public class SystemUserDetail {


    public SystemUserDetail(String userName, String password, String roles) {

        this.userName = userName;
        this.password = password;
        this.roles = roles;
    } // ctor

    private String userName;
    private String password;
    private String roles;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }
}
