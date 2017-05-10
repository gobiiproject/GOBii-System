package org.gobiiproject.gobiimodel.types;

/**
 * Created by Phil on 9/25/2016.
 */
public enum GobiiCvGroupType {

    GROUP_TYPE_UNKNOWN(0),
    GROUP_TYPE_SYSTEM(1),
    GROUP_TYPE_USER(2);

    Integer groupType;

    GobiiCvGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public Integer getGroupTypeId() {
        return groupType;
    }

    public static GobiiCvGroupType fromInt(Integer intParam) {

        GobiiCvGroupType returnVal = GobiiCvGroupType.GROUP_TYPE_UNKNOWN;

        if( intParam.equals(GROUP_TYPE_SYSTEM.getGroupTypeId())) {
            returnVal = GROUP_TYPE_SYSTEM;
        } else if( intParam.equals(GROUP_TYPE_USER.getGroupTypeId())) {
            returnVal = GROUP_TYPE_USER;
        }

        return returnVal;
    }
}
