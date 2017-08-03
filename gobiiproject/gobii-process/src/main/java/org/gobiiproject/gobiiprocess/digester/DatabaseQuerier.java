package org.gobiiproject.gobiiprocess.digester;

import org.gobiiproject.gobiimodel.config.GobiiCropDbConfig;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.SimplePostgresConnector;

import java.io.*;

/**
 * A series of simple existance queries
 */
public class DatabaseQuerier {
    SimplePostgresConnector connector;
    public DatabaseQuerier(GobiiCropDbConfig conf){
        connector=new SimplePostgresConnector(conf);
    }
    public boolean checkMarkerExistence(File markerFile){
        ColReader reader=new ColReader(markerFile,"marker");
        while(reader.hasNext()){
            String marker=reader.next();
            if(!connector.hasMarker(marker)){
                ErrorLogger.logError("Validation","Marker "+marker+" does not exist in database");
                return false;
            }
        }
        return true;
    }
    public boolean checkGermplasmTypeExistence(File germplasmFile){
        ColReader reader=new ColReader(germplasmFile,"germplasm_type");
        while(reader.hasNext()){
            String germplasm=reader.next();
            if(!connector.hasGermplasmType(germplasm)){
                ErrorLogger.logError("Validation","Germplasm Type "+germplasm+" does not exist in database");
                return false;
            }
        }
        return true;
    }
    public boolean checkGermplasmSpeciesExistence(File germplasmFile){
        ColReader reader=new ColReader(germplasmFile,"germplasm_species");
        while(reader.hasNext()){
            String germplasm=reader.next();
            if(!connector.hasGermplasmSpecies(germplasm)){
                ErrorLogger.logError("Validation","Germplasm Species "+germplasm+" does not exist in database");
                return false;
            }
        }
        return true;
    }
    public boolean checkDNARunInExperiment(File dnarunFile, int experiment){
        ColReader reader=new ColReader(dnarunFile,"dnarun_name");
        while(reader.hasNext()){
            String dnarun=reader.next();
            if(!connector.hasDNARuninExperiment(dnarun,experiment)){
                ErrorLogger.logError("Validation","DNARun "+dnarun+" does not exist in experiment "+experiment);
                return false;
            }
        }
        return true;
    }
    public boolean checkMarkerInPlatform(File markerFile, int platform){
        ColReader reader=new ColReader(markerFile,"marker_name");
        while(reader.hasNext()){
            String marker=reader.next();
            if(!connector.hasDNARuninExperiment(marker,platform)){
                ErrorLogger.logError("Validation","Marker "+marker+" does not exist in platform "+platform);
                return false;
            }
        }
        return true;
    }

    /**
     * Call when you're done with this.
     */
    public void close(){
        connector.close();
    }
}
class ColReader{
    BufferedReader reader;
    String colName;
    int colLoc;
    String val;
    ColReader(File inFile, String colName){
        try {
            reader=new BufferedReader(new FileReader(inFile));
            String header=reader.readLine();
            String[] parts = header.split("\t");
            for(int i=0; i < parts.length;i++){
                if(parts[i].toLowerCase().equals(colName.toLowerCase())){
                    colLoc=i;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            ErrorLogger.logError("Postgres Query","Failed to find file", e);
        } catch (IOException e) {
            ErrorLogger.logError("Postgres Query","Error reading file", e);
        }
        next();
    }
    public String next(){
        String ret=val;
        val=null;
        try{
            val=reader.readLine().split("\t")[colLoc];
        } catch(Exception e){/*Don't care*/}
        return ret;
    }
    public boolean hasNext(){
        return val!=null;
    }
}
