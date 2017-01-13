package org.gobiiproject.gobiidao.resultset.core;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import javax.persistence.StoredProcedureParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/18/2016.
 */
public class ParamExtractor {

    public static Map<String, Object> makeParamVals(Object dtoInstance) throws GobiiDaoException {

        Map<String, Object> returnVal = new HashMap<>();

        try {
            for (Method currentMethod : dtoInstance.getClass().getDeclaredMethods()) {

                GobiiEntityParam gobiiEntityParam = currentMethod.getAnnotation(GobiiEntityParam.class);
                if (null != gobiiEntityParam) {

                    String paramName = gobiiEntityParam.paramName();
                    Object paramVal = currentMethod.invoke(dtoInstance);
                    returnVal.put(paramName, paramVal);

                } // if the field is annotated as a param

            } // iterate all fields
        } catch(IllegalAccessException e) {
            throw new GobiiDaoException(e);
        } catch(InvocationTargetException e) {
            throw new GobiiDaoException(e);
        }

        return returnVal;

    } // makeParamVals()

} // ParamExtractor

