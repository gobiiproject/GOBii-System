package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiimodel.config.GobiiCropDbConfig;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.sql.*;

/**
 * Created by jdl232 on 6/21/2017.
 */
public class SimplePostgresConnector {

    public SimplePostgresConnector(GobiiCropDbConfig config){
        this.dbConn=getDataSource(config);
    }

    private Connection dbConn=null;
    public static Connection getDataSource(GobiiCropDbConfig config){
        Connection conn = null;
        try{
            conn=DriverManager.getConnection(config.getConnectionString(),config.getUserName(),config.getPassword());
        }catch(SQLException e){
            ErrorLogger.logError("SimplePostgresConnector","Failed creating postgres connection",e);
        }
        return conn;
    }

    /**
     * Use if you want a query that returns true on any result, or false on empty result.
     * @param query Query to execute, as a string
     * @return True or False
     * @throws SQLException Often
     */
    public boolean boolQuery(String query) throws SQLException{
        Statement s = dbConn.createStatement();
        boolean ret = s.execute(query);
        s.close();
        return ret;
    }

    /**
     * Calls boolQuery on 'Select 1 from {TABLE} where {ENTITY} = {NAME} LIMIT 1
     * returns true if there were any results
     * @param table table name
     * @param entity column name in the table
     * @param name object's identifier in the column named in entity
     * @return True or False
     * @throws SQLException Often
     */
    public boolean hasEntry(String table, String entity, String name) throws SQLException{
        String statement="SELECT 1 from "+table+" WHERE "+entity+" = '"+ name + "' LIMIT 1";
        return boolQuery(statement);
    }

    public boolean hasMarkerInPlatform(String name, int platform) {
        String statement="SELECT 1 from marker WHERE name = '"+ name + "' and platform_id = "+platform+" LIMIT 1";
        try {
            return boolQuery(statement);
        }catch(SQLException e){ErrorLogger.logError("Postgres Connector",e);}
        return false;
    }

    public boolean hasMarker(String markerName){
        try {
            return hasEntry("marker", "name", markerName);
        }catch(SQLException e){
            ErrorLogger.logError("Postgres Connector",e);
        }
        return false;
    }


    public boolean hasDNARuninExperiment(String name, int experiment) {
        String statement="SELECT 1 from dnarun WHERE name = '"+ name + "' and experiment_id = "+experiment+" LIMIT 1";
        try {
            return boolQuery(statement);
        }catch(SQLException e){ErrorLogger.logError("Postgres Connector",e);}
        return false;
    }

    public boolean hasCVEntry(String cvGroupName, String cvName) throws SQLException{
        String statement="SELECT 1 from cv join cvgroup b on cv.cvgroup_id = b.cvgroup_id where b.name = '"+cvGroupName+"' and cv.term = '"+cvName+"' LIMIT 1";
        return boolQuery(statement);
    }
    public boolean hasGermplasmType(String germplasmType) {
        try{
            return hasCVEntry("germplasm_type",germplasmType);
        }catch(SQLException e){
            ErrorLogger.logError("Postgres Connector",e);
        }
        return false;
    }
    public boolean hasGermplasmSpecies(String germplasmSpecies) {
        try{
            return hasCVEntry("germplasm_species",germplasmSpecies);
        }catch(SQLException e){
            ErrorLogger.logError("Postgres Connector",e);
        }
        return false;
    }

    public boolean close(){
        try {
            dbConn.close();
        } catch (SQLException e) {
            ErrorLogger.logError("SimplePostgresConnector","Error closing",e);
            return false;
        }
        dbConn=null;
        return true;
    }

}
