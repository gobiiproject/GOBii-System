package org.gobiiproject.gobiimodel.dto.instructions.loader;

import org.gobiiproject.gobiimodel.types.DataSetOrientationType;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;

/**
 * Created by Phil on 4/12/2016.
 */
public class GobiiFileColumn {


    private GobiiColumnType gobiiColumnType = null;
    private Integer rCoord = null;
    private Integer cCoord = null;
    private String name = null;
    private String filterFrom = null;
    private String filterTo = null;
    private String findText = null;
    private String replaceText = null;
    private String constantValue = null;
    private String index = null;
    private boolean subcolumn = false;
    private String subcolumnDelimiter = null;

    private DataSetType dataSetType = DataSetType.IUPAC;
    private DataSetOrientationType dataSetOrientationType = DataSetOrientationType.MARKER_FAST;


    private String metaDataId = null;

    public GobiiColumnType getGobiiColumnType() {
        return gobiiColumnType;
    }

    public GobiiFileColumn setGobiiColumnType(GobiiColumnType gobiiColumnType) {
        this.gobiiColumnType = gobiiColumnType;
        return this;
    }

    public Integer getrCoord() {
        return rCoord;
    }

    public GobiiFileColumn setRCoord(Integer rCoord) {
        this.rCoord = rCoord;
        return this;
    }

    public Integer getcCoord() {
        return cCoord;
    }

    public GobiiFileColumn setCCoord(Integer cCoord) {
        this.cCoord = cCoord;
        return this;
    }

    public String getName() {
        return name;
    }

    public GobiiFileColumn setName(String name) {
        this.name = name;
        return this;
    }

    public String getFilterFrom() {
        return filterFrom;
    }

    public GobiiFileColumn setFilterFrom(String filterFrom) {
        this.filterFrom = filterFrom;
        return this;
    }

    public String getFilterTo() {
        return filterTo;
    }

    public GobiiFileColumn setFilterTo(String filterTo) {
        this.filterTo = filterTo;
        return this;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public GobiiFileColumn setConstantValue(String constantValue) {
        this.constantValue = constantValue;
        return this;
    }

    public String getMetaDataId() {
        return metaDataId;
    }

    public GobiiFileColumn setMetaDataId(String metaDataId) {
        this.metaDataId = metaDataId;
        return this;
    }


    public String getIndex() {
        return index;
    }

    public GobiiFileColumn setIndex(String index) {
        this.index = index;
        return this;
    }

    public boolean isSubcolumn() {
        return subcolumn;
    }

    public GobiiFileColumn setSubcolumn(boolean subcolumn) {
        this.subcolumn = subcolumn;
        return this;
    }

    public String getSubcolumnDelimiter() {
        return subcolumnDelimiter;
    }

    public GobiiFileColumn setSubcolumnDelimiter(String subcolumnDelimiter) {
        this.subcolumnDelimiter = subcolumnDelimiter;
        return this;
    }

    public String getFindText() {
        return findText;
    }

    public GobiiFileColumn setFindText(String findText) {
        this.findText = findText;
        return this;
    }

    public String getReplaceText() {
        return replaceText;
    }

    public GobiiFileColumn setReplaceText(String replaceText) {
        this.replaceText = replaceText;
        return this;
    }

    public DataSetType getDataSetType() {
        return dataSetType;
    }

    public GobiiFileColumn setDataSetType(DataSetType dataSetType) {
        this.dataSetType = dataSetType;
        return this;
    }

    public DataSetOrientationType getDataSetOrientationType() {
        return dataSetOrientationType;
    }

    public GobiiFileColumn setDataSetOrientationType(DataSetOrientationType dataSetOrientationType) {
        this.dataSetOrientationType = dataSetOrientationType;
        return this;
    }
}
