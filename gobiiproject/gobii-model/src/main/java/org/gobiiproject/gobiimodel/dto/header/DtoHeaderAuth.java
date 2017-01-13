package org.gobiiproject.gobiimodel.dto.header;


import org.gobiiproject.gobiimodel.types.GobiiCropType;

import java.io.Serializable;

/**
 * Created by MrPhil on 6/18/2015.
 */
public class DtoHeaderAuth implements Serializable {


    private String userName;
    private String password;
    private String token;
    private GobiiCropType gobiiCropType;

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

    public GobiiCropType getGobiiCropType() {
        return gobiiCropType;
    }

    public void setGobiiCropType(GobiiCropType gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }



}//DtoHeaderResponse
