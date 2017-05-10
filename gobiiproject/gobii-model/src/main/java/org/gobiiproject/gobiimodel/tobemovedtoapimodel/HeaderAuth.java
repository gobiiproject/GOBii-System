package org.gobiiproject.gobiimodel.tobemovedtoapimodel;




import java.io.Serializable;

/**
 * Created by MrPhil on 6/18/2015.
 */
public class HeaderAuth implements Serializable {


    private String userName;
    private String password;
    private String token;
    private String gobiiCropType;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    public String getUserName() {
        return (this.userName);
    }

    
    public void setPassword(String password) {
        this.password = password;
    }

    
    public String getPassword() {
        return (this.password);
    }

    
    public void setToken(String token) {
        this.token = token;
    }

    
    public String getToken() {
        return (this.token);
    }

    public String getGobiiCropType() {
        return this.gobiiCropType;
    }

    public void setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }



}//Status
