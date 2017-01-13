package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsDataSet extends SpDef {

    public SpInsDataSet() {

        super("{call createdataset(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

        this.addParamDef("name", String.class).setNullable(false);
        this.addParamDef("experimentId", Integer.class).setNullable(false);
        this.addParamDef("callingAnalysisId", Integer.class).setNullable(false);
        this.addParamDef("datasetAnalysIds", ArrayList.class).setNullable(true);
        this.addParamDef("dataTable", String.class).setNullable(true);
        this.addParamDef("dataFile", String.class).setNullable(true);
        this.addParamDef("qualityTable", String.class).setNullable(true);
        this.addParamDef("qualityFile", String.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(true);
        this.addParamDef("createdDate", java.util.Date.class).setNullable(true);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", java.util.Date.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(false);
        this.addParamDef("typeId", Integer.class).setNullable(true);

    } // ctor

//    public Integer projectId = null;
//    public Integer getEntityIdId() {
//        return projectId;
//    }


//    public void execute(Connection dbConnection) throws SQLException {
//
//        CallableStatement callableStatement = dbConnection.prepareCall("{call createproject(?,?,?,?,?,?,?,?,?)}");
//
//        callableStatement.setString(1,(String) parameters.get("projectName"));
//        callableStatement.setString(2,(String) parameters.get("projectCode"));
//        callableStatement.setString(3,(String) parameters.get("projectDescription"));
//        callableStatement.setInt(4,(Integer) parameters.get("piContact"));
//        callableStatement.setInt(5,(Integer) parameters.get("createdBy"));
//        callableStatement.setDate(6,(Date) parameters.get("createdDate"));
//        callableStatement.setInt(7,(Integer) parameters.get("modifiedby"));
//        callableStatement.setDate(8,(Date) parameters.get("modifiedDate"));
//        callableStatement.setInt(9,(Integer) parameters.get("projectStatus"));
//
//        callableStatement.registerOutParameter(10, Types.INTEGER);
//
//        projectId = callableStatement.executeUpdate();

//    } // execute()
}
