package org.gobiiproject.gobiiclient.gobii.Helpers;

import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This class serves as a central repository for PK values for specific entities.
 * Unit tests often need to create entities that have foreign keys to other entities.
 * So, when a unit test requires the PK value of another entity for an FK, rather
 * than creating the FK entity on its own, it can
 * use this class (though the getInstance() method) to get the PK if it already exists.
 * Aside from conenience, this mechanism has the added advantage of reducing database thrash.
 * The GobiiEntityNameType enum is used to identify the entities. A list of PK values for each entity is stored
 * because more than one such value may be needed. Of course, in order for the PK values
 * to be available, they must be added to the collection with addPkVal().
 */
public class GlobalPkValues {

    private static GlobalPkValues instance = null;

    protected GlobalPkValues() {
        // Exists only to defeat instantiation.
    }

    public static GlobalPkValues getInstance() {
        if (instance == null) {
            instance = new GlobalPkValues();
        }
        return instance;
    }

    Map<GobiiEntityNameType, List<Integer>> pkMap = new EnumMap<>(GobiiEntityNameType.class);

    public Integer getPkValCount(GobiiEntityNameType gobiiEntityNameType) {

        Integer returnVal = 0;

        if (pkMap.containsKey(gobiiEntityNameType)) {
            returnVal = pkMap.get(gobiiEntityNameType).size();
        }

        return returnVal;
    }

    public void resetPkVals(GobiiEntityNameType gobiiEntityNameType ) throws Exception {
        if( this.pkMap.containsKey(gobiiEntityNameType)) {
            this.pkMap.remove(gobiiEntityNameType);
        }
    }

    public List<Integer> getPkVals(GobiiEntityNameType gobiiEntityNameType) throws Exception {

        List<Integer> returnVal = null;

        if (!pkMap.containsKey(gobiiEntityNameType)) {
            throw new Exception("There are no PKs for entity: " + gobiiEntityNameType.toString());
        }

        if (pkMap.containsKey(gobiiEntityNameType)) {
            returnVal = pkMap.get(gobiiEntityNameType);
        }

        return returnVal;
    }

    public Integer getAPkVal(GobiiEntityNameType gobiiEntityNameType) {

        Integer returnVal = null;

        if (pkMap.containsKey(gobiiEntityNameType)
                && pkMap.get(gobiiEntityNameType).size() > 0) {
            returnVal = pkMap.get(gobiiEntityNameType).get(0); // get arbitrary value for now
        }

        return returnVal;
    }

    public Integer addPkVal(GobiiEntityNameType gobiiEntityNameType, Integer pkVal) {

        if (!pkMap.containsKey(gobiiEntityNameType)) {
            pkMap.put(gobiiEntityNameType, new ArrayList<>());
        }

        if (!pkMap.get(gobiiEntityNameType).contains(pkVal)) {
            pkMap.get(gobiiEntityNameType).add(pkVal);
        }

        return pkMap.size();
    }
}
