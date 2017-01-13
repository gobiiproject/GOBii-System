package org.gobiiproject.gobiidtomapping.core;

import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames.PROPPCOLARAMNAME_PROP_ID;

/**
 * Created by Phil on 5/4/2016.
 */
public class EntityProperties {


    public static List<EntityPropertyDTO> resultSetToProperties(Integer parentEntityId,
                                                                ResultSet propertyResultSet) throws SQLException {

        List<EntityPropertyDTO> returnVal = new ArrayList<>();

        while (propertyResultSet.next()) {
            String propertyName = propertyResultSet.getString(EntityPropertyParamNames.PROPPCOLARAMNAME_NAME);
            String propertyValue = propertyResultSet.getString(EntityPropertyParamNames.PROPPCOLARAMNAME_VALUE);

            //we dont' always get back a property id
            Integer propertyId = null;
            ResultSetMetaData resultSetMetaData = propertyResultSet.getMetaData();
            int columns = resultSetMetaData.getColumnCount();
            for (int x = 1; x <= columns; x++) {
                if (EntityPropertyParamNames.PROPPCOLARAMNAME_PROP_ID.equals(resultSetMetaData.getColumnName(x))) {
                    propertyId = propertyResultSet.getInt(EntityPropertyParamNames.PROPPCOLARAMNAME_PROP_ID);
                }
            }

            EntityPropertyDTO currentProjectProperty =
                    new EntityPropertyDTO(parentEntityId,
                            propertyId,
                            propertyName,
                            propertyValue);
            returnVal.add(currentProjectProperty);
        }

        return returnVal;

    } // resultSetToProperties


    public static Map<String, Object> propertiesToParams(Integer entityId, EntityPropertyDTO entityProperty) {

        Map<String, Object> returnVal = new HashMap<>();

        returnVal.put(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, entityId);
        returnVal.put(EntityPropertyParamNames.PROPPCOLARAMNAME_NAME, entityProperty.getPropertyName());
        returnVal.put(EntityPropertyParamNames.PROPPCOLARAMNAME_VALUE, entityProperty.getPropertyValue());


        return returnVal;

    }
}
