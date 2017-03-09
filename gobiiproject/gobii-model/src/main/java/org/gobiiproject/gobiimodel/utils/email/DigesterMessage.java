package org.gobiiproject.gobiimodel.utils.email;

import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Digest-specific mail message format
 */
public class DigesterMessage extends MailMessage {
    String statusLine;
    String errorLine;
    String tableLine;
    String identifierLine;
    String pathsLine;
    List<HTMLTableEntity> entries=new ArrayList<>();
    List<HTMLTableEntity> identifiers=new ArrayList<>();
    List<HTMLTableEntity> paths=new ArrayList<>();

    /**
     * Sets the body of the DigesterMessage to the table format.
     * Note: call addIdentifier, addEntry, and addPath before this to populate those fields.
     * @param jobName Name of the job being processed
     * @param shortError Short (100 character or less) error message to pass back in case of error (Or NULL)
     * @param success If the job was successful (true/false)
     * @param longError Long error message, HTML formatting allowed
     * @return this object
     */
    public DigesterMessage setBody(String jobName, String shortError,boolean success, String longError){
        this.setStatus(success);
        this.setSubject("Job "+jobName+(success?" Success":" Error"));
        this.errorLine=shortError;
        if(!entries.isEmpty()) {
            tableLine = HTMLTableEntity.getHTMLTable(entries,"Table","Total in File", "Total Loaded","Total Existing","Total Invalid");
        }
        if(!identifiers.isEmpty()) {
            identifierLine = HTMLTableEntity.getHTMLTable(identifiers,"Identifier Type","Name","ID");
        }
        if(!paths.isEmpty()) {
            pathsLine = HTMLTableEntity.getHTMLTable(paths,"File Type","Path");
        }

        String line="<br/>";
        StringBuilder body=new StringBuilder();
        body.append(statusLine+line);
        if(errorLine!=null)body.append(errorLine+line);
        body.append(line);
        if(identifierLine!=null)body.append(identifierLine+line);
        if(tableLine!=null)body.append(tableLine+line);
        if(pathsLine!=null)body.append(pathsLine+line);
        if(longError!=null)body.append(longError);
        this.setBody(body.toString());
        return this;
    }

    /**
     * Add an entry to the intermediate File table
     * @param tableName Name of table
     * @param fileCount Unique entry count in file
     * @param loadCount Count of loaded entries
     * @param existCount Count of duplicate entries
     * @return this object
     */
    public DigesterMessage addEntry(String tableName,String fileCount, String loadCount, String existCount, String invalidCount){
        entries.add(new HTMLTableEntity(tableName,fileCount,loadCount,existCount, invalidCount));
        return this;
    }

    /**
     * Add an entry to the Identifiers table
     * @param type Type of entry (Platform, Dataset, etc.)
     * @param name Name of entry
     * @param id Identifier of entry
     * @return this object
     */
    public DigesterMessage addIdentifier(String type,String name, String id){
        if((name==null) && ((id==null || id.equals("null"))))return this;
        identifiers.add(new HTMLTableEntity(type,name,id));
        return this;
    }
    public DigesterMessage addIdentifier(String type, GobiiFilePropNameId identifier){
        if(identifier==null)return this;//Don't add a null ID to the table
        return addIdentifier(type,identifier.getName(),identifier.getId()+"");
    }

    /**
     * Add item to the filepaths entry
     * @param type type of file
     * @param path filepath
     * @return this object
     */
    public DigesterMessage addPath(String type,String path){
        paths.add(new HTMLTableEntity(type,path));
        return this;
    }
    private DigesterMessage setStatus(boolean status) {
        statusLine = "Status: " + (status ?
                "<b>Success</b>" :
                "<b color='red'>Error</b>");
        return this;
    }
}