package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * This class contains the properties necessary to configure a database.
 */
@Root
public class CropDbConfig {

    public CropDbConfig() {
    }

    public CropDbConfig(GobiiDbType gobiiDbType,
                        String host,
                        String dbName,
                        Integer port,
                        String userName,
                        String password,
                        boolean decrypt) {

        this.gobiiDbType = gobiiDbType;
        this.host = host;
        this.dbName = dbName;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.decrypt = decrypt;
    }

    @Element(required = false)
    private boolean decrypt = false;

    @Element(required = false)
    private GobiiDbType gobiiDbType = null;

    @Element(required = false)
    private String host = null;

    @Element(required = false)
    private String dbName = null;

    @Element(required = false)
    private Integer port = null;

    @Element(required = false)
    private String userName = null;

    @Element(required = false)
    private String password = null;

    public GobiiDbType getGobiiDbType() {
        return gobiiDbType;
    }

    public CropDbConfig setGobiiDbType(GobiiDbType gobiiDbType) {
        this.gobiiDbType = gobiiDbType;
        return this;
    }

    public String getHost() {
        return host;
    }

    public CropDbConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getDbName() {
        return dbName;
    }

    public CropDbConfig setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public CropDbConfig setPort(Integer port) {
        this.port = port;
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

    public CropDbConfig setUserName(String userName) {
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

    public CropDbConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConnectionString() {

        return ("jdbc:"
                + this.gobiiDbType.toString().toLowerCase()
                + "://"
                + this.host
                + ":"
                + this.port.toString()
                + "/"
                + this.dbName);
    }

    public boolean isDecrypt() {
        return decrypt;
    }

    public void setDecrypt(boolean decrypt) {
        this.decrypt = decrypt;
    }
}
