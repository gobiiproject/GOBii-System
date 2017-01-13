package org.gobiiproject.gobiimodel.dto.instructions.loader;

import org.gobiiproject.gobiimodel.types.GobiiFileType;

/**
 * Container for file-system specific information used in conversation with the server.
 * Created by Phil on 4/12/2016.
 */
public class GobiiFile {
    //Directory specifying the source (input) files
    String source = null;
    //Directory specifying the destination (output) files
    String destination = null;
    //File system supported delimiter (Not widely respected)
    String delimiter = null;
    //Source file type
    GobiiFileType gobiiFileType = null;
    //Only creates the source directory if this is set to true (always creates the destination directory)
    private boolean isCreateSource = true;
    //If true, fail with an error if the source and destination directories do not exist
    boolean requireDirectoriesToExist = false;

    //Protip from Josh: Leave isCreateSource and requireDirectoriesToExist alone unless you know what you're doing.

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
