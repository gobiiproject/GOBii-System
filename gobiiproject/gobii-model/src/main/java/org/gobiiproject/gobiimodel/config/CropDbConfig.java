package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiDbType;

/**
 * Created by Phil on 5/18/2016.
 */
public class CropDbConfig {

    CropDbConfig(GobiiDbType gobiiDbType,
                 String host,
                 String dbName,
                 Integer port,
                 String userName,
                 String password) {

        this.gobiiDbType = gobiiDbType;
        this.host = host;
        this.dbName = dbName;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    private GobiiDbType gobiiDbType = null;
    private String host = null;
    private String dbName = null;
    private Integer port = null;
    private String userName = null;
    private String password = null;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
