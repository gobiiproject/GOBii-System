package org.gobiiproject.gobiidao.resultset.core;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Phil on 4/18/2016.
 */
public class ResultColumnApplicator {

    private static Logger LOGGER = LoggerFactory.getLogger(ResultColumnApplicator.class);

    public static void applyColumnValues(ResultSet resultSet, Object dtoInstance) throws GobiiDaoException {

        Map<String, Object> returnVal = new HashMap<>();

        String currentColumnName = null;
        String currentParameterName = null;
        Type currentColumnType = null;
        try {
            for (Method currentMethod : dtoInstance.getClass().getDeclaredMethods()) {

                GobiiEntityColumn gobiiEntityColumn = currentMethod.getAnnotation(GobiiEntityColumn.class);

                if (null != gobiiEntityColumn) {

                    currentColumnName = gobiiEntityColumn.columnName();
                    Type[] methodParameterTypes = currentMethod.getParameterTypes();
                    if (methodParameterTypes.length == 1) {

                        currentColumnType = methodParameterTypes[0];
                        currentParameterName = currentMethod.getParameters()[0].getName();
                        Object currentColumnValue = resultSet.getObject(currentColumnName);


                        if (currentColumnType.equals(String.class)) {

                            String currentStringValue = (String) currentColumnValue;
                            currentMethod.invoke(dtoInstance, currentStringValue);

                        } else if (currentColumnType.equals(Integer.class)) {

                            Integer currentIntegerValue = (Integer) currentColumnValue;
                            currentMethod.invoke(dtoInstance, currentIntegerValue);

                        } else if (currentColumnType.equals(Date.class)) {

                            Date currentDateValue = (Date) currentColumnValue;
                            currentMethod.invoke(dtoInstance, currentDateValue);

                        } else if (currentColumnType.equals(List.class)) {

                            List<Integer> intList = new ArrayList<>();
                            Array sqlArray = resultSet.getArray(currentColumnName);

                            if (null != sqlArray) {

                                Integer[] integerList = (Integer[]) sqlArray.getArray();
                                for (int idx = 0; idx < integerList.length; idx++) {
                                    Integer currentIntValue = integerList[idx];
                                    intList.add(currentIntValue);

                                } // iterate values
                            }

                            currentMethod.invoke(dtoInstance, intList);

                        } else {
                            throw new SQLException("Unsupported param type for method " + currentMethod.getName() + ", parameter " + currentParameterName + ": " + currentColumnType);
                        }


                    } else {
                        throw new GobiiDaoException("Annotated setter method " + currentMethod.getName() + " does not have exactly one parameter");
                    }

                }  // if the field is annotated as a column

            } // iterate all fields

        } catch (Exception e) {
            String message = "error applying value of column "
                    + currentColumnName
                    + " to setter labeleled as "
                    + currentParameterName
                    + " in class  "
                    + dtoInstance.getClass()
                    + ": "
                    + e.getMessage();
            if (null != e.getCause()) {
                message += " caused by: " + e.getCause();
            }

            LOGGER.error(message, e);
            throw new GobiiDaoException(message);
        }

    } // applyColumnValues()

} // ParamExtractor

