package org.gobiiproject.gobiiprocess.digester.HelperFunctions;

import org.gobiiproject.gobiimodel.utils.FileSystemInterface;

/**
 * Allows transformations to be called on a file which may move the file's location. Provides a from and to destination.
 * If 'returnFile' is called returns the file with a move command, else if 'finalTransform' is called, returns the file
 * as part of the transformation to the base location.
 *
 * This allows a file to go through multiple transformations in place.
 *
 * @author jdl232
 */
public class SequenceInPlaceTransform {
    private String baseFileLocation;
    private String errorPath;
    private int incrementNumber=0;
    public SequenceInPlaceTransform(String baseFileLocation, String errorPath){
        this.baseFileLocation=baseFileLocation;
        this.errorPath=errorPath;
    }

    /**
     * Returns the current position of the file (it's location) based on the increment number.
     * (if base file is /josh/text.txt and increment is 2, returns /josh/text.txt.2
     * @return current position of the file
     */
    private String currentFilePosition(){
        if(incrementNumber==0) return baseFileLocation;
        else return baseFileLocation+"."+incrementNumber;
    }

    /**
     * Runs the transformation on the file, supplying a new file location based on the naming scheme:
     * file
     * file.1
     * file.2
     * file.3
     * @param transformation Transformation to occur
     */
    public void transform(MobileTransform transformation){
        String lastLocation= currentFilePosition();
        incrementNumber++;
        String nextLocation= currentFilePosition();
        transform(transformation,lastLocation,nextLocation, errorPath);
    }

    /**
     * Combination of transform() and returnFile(), saves on a move.
     * @param transformation see transform
     */
    public void finalTransform(MobileTransform transformation){
        if(incrementNumber==0)throw new RuntimeException("Invalid call to SeqnenceInPlaceTransform.finalTransform");
        transform(transformation, currentFilePosition(),baseFileLocation, errorPath);
        incrementNumber=0;
    }

    private static void transform(MobileTransform transformation, String fromFile, String toFile, String errorPath){
        transformation.transform(fromFile,toFile,errorPath);
        FileSystemInterface.rmIfExist(fromFile);
    }

    /**
     * Returns file to the base location if it has moved
     */
    public void returnFile(){
        if(incrementNumber==0)return;
        FileSystemInterface.mv(currentFilePosition(),baseFileLocation);
        FileSystemInterface.rmIfExist(currentFilePosition());
        incrementNumber=0;
    }
}
