package org.gobiiproject.gobiimodel.dto.instructions.loader;

import org.gobiiproject.gobiimodel.types.GobiiFileType;

/**
 * Created by Phil on 4/12/2016.
 */
public class GobiiFile {



    String source = null;
    String destination = null;
    String delimiter = null;
    GobiiFileType gobiiFileType = null;
    private boolean isCreateSource = true;
    boolean requireDirectoriesToExist = false;


    public String getSource() {
        return source;
    }

    public GobiiFile setSource(String source) {
        this.source = source;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public GobiiFile setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public GobiiFile setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public GobiiFileType getGobiiFileType() {
        return gobiiFileType;
    }

    public GobiiFile setGobiiFileType(GobiiFileType gobiiFileType) {
        this.gobiiFileType = gobiiFileType;
        return this;
    }

    public boolean isCreateSource() {
        return isCreateSource;
    }

    public GobiiFile setCreateSource(boolean createSource) {
        isCreateSource = createSource;
        return this;
    }

    public boolean isRequireDirectoriesToExist() {
        return requireDirectoriesToExist;
    }

    public GobiiFile setRequireDirectoriesToExist(boolean requireDirectoriesToExist) {
        this.requireDirectoriesToExist = requireDirectoriesToExist;
        return this;
    }
}
