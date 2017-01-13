package org.gobiiproject.gobiimodel.types;

/**
 * Created by Phil on 9/25/2016.
 */
public enum GobiiValidationStatusType {
    NONE,
    UNKNOWN,
    VALIDATION_COMPOUND_UNIQUE,
    VALIDATION_NOT_UNIQUE,
    NONEXISTENT_FK_ENTITY,
    BAD_REQUEST,
    MISSING_REQUIRED_VALUE,
    ENTITY_DOES_NOT_EXIST,
    ENTITY_ALREADY_EXISTS,
}
