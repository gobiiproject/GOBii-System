package org.gobiiproject.gobiidao.resultset.core;

import java.lang.reflect.Type;

/**
 * Created by Phil on 4/18/2016.
 */
public class SpParamDef {

    private Integer orderIdx;
    private String paramName;
    private Type paramType;
    private Object currentValue;
    private Object defaultValue;
    private boolean isNullable;
    private String fkTargetTable;
    private String fkTargetColumn;

    public SpParamDef(String paramName, Type paramType ) {
        this.paramName = paramName;
        this.paramType = paramType;
    }

    public String getParamName() {
        return paramName;
    }

    public Type getParamType() {
        return paramType;
    }

    public Integer getOrderIdx() {
        return orderIdx;
    }

    public SpParamDef setOrderIdx(Integer orderIdx) {
        this.orderIdx = orderIdx;
        return this;
    }

    public Object getCurrentValue() {
        return currentValue;
    }

    public SpParamDef setCurrentValue(Object currentValue) {
        this.currentValue = currentValue;
        return this;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public SpParamDef setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public SpParamDef setNullable(boolean nullable) {
        this.isNullable = nullable;
        return this;
    }

    public String getFkTargetTable() {
        return fkTargetTable;
    }

    public SpParamDef setFkTargetTable(String fkTargetTable) {
        this.fkTargetTable = fkTargetTable;
        return this;
    }

    public String getFkTargetColumn() {
        return fkTargetColumn;
    }

    public SpParamDef setFkTargetColumn(String fkTargetColumn) {
        this.fkTargetColumn = fkTargetColumn;
        return this;
    }


}
