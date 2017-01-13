package org.gobiiproject.gobiidao.resultset.core;


import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 4/18/2016.
 */
public class SpRunnerCallable implements Work {

    Logger LOGGER = LoggerFactory.getLogger(SpRunnerCallable.class);


    @PersistenceContext
    protected EntityManager em;

    private SpDef spDef = null;
    private Map<String, Object> paramVals = null;

    List<String> errors = new ArrayList<>();
    Integer result = null;

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorString() {
        String returnVal = "";

        for (String currentMessage : errors) {
            returnVal += currentMessage + "; ";
        }

        return returnVal;
    }

    public Integer getResult() {
        return result;
    }

    public boolean run(SpDef spDef, Map<String, Object> paramVals) {

        boolean returnVal = true;
        errors.clear();

//        if (spDef.getSpParamDefs().size() == paramVals.size()) {

            try {
                this.spDef = spDef;
                this.paramVals = paramVals;

                // first do validation checking
                List<SpParamDef> paramDefs = spDef.getSpParamDefs();
                for (int idx = 0; idx < paramDefs.size() && returnVal; idx++) {

                    SpParamDef currentParamDef = paramDefs.get(idx);
                    String currentParamName = currentParamDef.getParamName();

                    if (paramVals.containsKey(currentParamName)) {

                        Object currentParamVal = paramVals.get(currentParamName);
                        if (null == currentParamVal && !currentParamDef.isNullable()) {
                            errors.add("Parameter is not allowed to be null : " + currentParamName);
                        } else if (null != currentParamVal && !currentParamVal.getClass().equals(currentParamDef.getParamType())) {
                            errors.add("Parameter " + currentParamName + " should be of type " + currentParamDef.getParamType() + "; ");
                        } else {
                            if (null != currentParamVal) {
                                currentParamDef.setCurrentValue(currentParamVal);
                            } else {
                                currentParamDef.setCurrentValue(currentParamDef.getDefaultValue());
                            }
                        }

                    } else {
                        returnVal = false;
                        String message = "There is no value param entry for parameter " + currentParamName + ";";
                        errors.add(message);
                        LOGGER.error(message);
                    }

                } // iterate param defs


                returnVal = errors.size() == 0;

                if (returnVal) {

                    try {
                        Session session = (Session) em.getDelegate();
                        session.doWork(this);

                    } catch (Exception e) {
                        returnVal = false;
                        String message = "Exception executing stored proc " + spDef.getCallString();
                        errors.add(message + ": " + e.getMessage() + "; cased by: " + e.getCause());
                        LOGGER.error(message, e);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Erro running stored procedure", e);
                throw e;
            } finally {
                this.spDef = null;
                this.paramVals = null;
            }
//        } else {
//            returnVal = false;
//            String message = "Param def size and input parameters size do not match";
//            errors.add(message);
//            LOGGER.error(message);
//        }

        return returnVal;

    } // run()


    @Override
    public void execute(Connection connection) throws SQLException {


        CallableStatement callableStatement = connection.prepareCall(spDef.getCallString());

        List<SpParamDef> paramDefs = spDef.getSpParamDefs();

        for (SpParamDef currentParamDef : paramDefs) {

            Integer currentParamIndex = currentParamDef.getOrderIdx();
            String currentParamName = currentParamDef.getParamName();
            Type currentParamType = currentParamDef.getParamType();
            Object currentParamValue = currentParamDef.getCurrentValue();

            try {
                if (currentParamType.equals(String.class)) {
                    if (null != currentParamValue) {
                        callableStatement.setString(currentParamIndex, (String) currentParamValue);
                    } else {
                        callableStatement.setNull(currentParamIndex, Types.VARCHAR);
                    }
                } else if (currentParamType.equals(Integer.class)) {
                    if (null != currentParamValue) {
                        callableStatement.setInt(currentParamIndex, (Integer) currentParamValue);
                    } else {
                        callableStatement.setNull(currentParamIndex, Types.INTEGER);
                    }
                } else if (currentParamType.equals(Date.class)) {
                    if (null != currentParamValue) {

                        Date javaDateValue = (Date) currentParamValue;
                        LocalDate localDate = javaDateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
                        callableStatement.setDate(currentParamIndex, sqlDate);
                    } else {
                        callableStatement.setNull(currentParamIndex, Types.DATE);
                    }
                } else if (currentParamType.equals(ArrayList.class)) {
                    if (null != currentParamValue) {

                        List<Integer> list = (List<Integer>) currentParamValue;
                        Integer[] intArray = new Integer[list.size()];
                        intArray = list.toArray(intArray);
                        Array sqlArray = connection.createArrayOf("integer", intArray);
                        callableStatement.setArray(currentParamIndex, sqlArray);
                    } else {
                        callableStatement.setNull(currentParamIndex, Types.ARRAY);
                    }
                } else {
                    throw new SQLException("Unsupported param type: " + Type.class.toString());
                }

            } catch (Exception e) {
                String message = "Error executing stored procedure " + spDef.getCallString() + " with " +
                        "Param Name: " + currentParamName + "; " +
                        "Param Value: " + currentParamValue + "; " +
                        "Param Type: " + currentParamType.toString() + "; ";
                throw new SQLException(message);
            }
        }


        Integer resultOutParamIdx = paramDefs.size();

        if (spDef.isReturnsKey()) {
            callableStatement.registerOutParameter(resultOutParamIdx, Types.INTEGER);
        }

        callableStatement.executeUpdate();

        if (spDef.isReturnsKey()) {
            result = callableStatement.getInt(resultOutParamIdx);
        }


    } // execute
}
