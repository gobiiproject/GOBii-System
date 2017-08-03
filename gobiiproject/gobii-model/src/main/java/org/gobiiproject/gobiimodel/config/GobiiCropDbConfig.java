package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * This class contains the properties necessary to configure a database.
 */
@Root
public class GobiiCropDbConfig extends ServerBase {

    public GobiiCropDbConfig() {
    }

    public GobiiCropDbConfig(GobiiDbType gobiiDbType,
                             String host,
                             String dbName,
                             Integer port,
                             String userName,
                             String password,
                             boolean decrypt) {

        super(host,dbName,port,true);
        this.gobiiDbType = gobiiDbType;
        this.userName = userName;
        this.password = password;
        this.decrypt = decrypt;

    }

    @Element(required = false)
    private boolean decrypt = false;

    @Element(required = false)
    private GobiiDbType gobiiDbType = null;

    @Element(required = false)
    private String userName = null;

    @Element(required = false)
    private String password = null;

    public GobiiDbType getGobiiDbType() {
        return gobiiDbType;
    }

    public GobiiCropDbConfig setGobiiDbType(GobiiDbType gobiiDbType) {
        this.gobiiDbType = gobiiDbType;
        return this;
    }

    public String getHost() {
        return super.getHost();
    }

    public GobiiCropDbConfig setHost(String host) {
        super.setHost(host);
        return this;
    }

    public String getContextPath() {
        return super.getContextPath(false);
    }

    public GobiiCropDbConfig setContextPath(String dbName) {
        super.setContextPath(dbName);
        return this;
    }

    public Integer getPort() {
        return super.getPort();
    }

    public GobiiCropDbConfig setPort(Integer port) {
        super.setPort(port);
        return this;
    }

    public String getUserName() {

        String returnVal = null;

        if (this.decrypt) {
            returnVal = Decrypter.decrypt(this.userName, null);
        } else {
            returnVal = this.userName;
        }

        return returnVal;
    }

    public GobiiCropDbConfig setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {

        String returnVal = null;

        if (this.decrypt) {
            returnVal = Decrypter.decrypt(this.password, null);
        } else {
            returnVal = this.password;
        }

        return returnVal;
    }

    public GobiiCropDbConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConnectionString() {

        String returnVal = "jdbc:"
                + this.gobiiDbType.toString().toLowerCase()
                + "://"
                + super.getHost()
                + ":"
                + super.getPort()
                + "/"
                + this.getContextPath();

        return (returnVal);
    }

    public boolean isDecrypt() {
        return decrypt;
    }

    public void setDecrypt(boolean decrypt) {
        this.decrypt = decrypt;
    }
}
