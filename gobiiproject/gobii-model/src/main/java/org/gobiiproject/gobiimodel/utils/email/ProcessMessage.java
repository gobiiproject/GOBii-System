package org.gobiiproject.gobiimodel.utils.email;


import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.gobiiproject.gobiimodel.utils.HelperFunctions.getDurationReadable;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.sizeToReadable;

/*
 *  GOBII - Process mail message format.  (Hopefully to replace DigesterMessage.java)
 */
public class ProcessMessage extends MailMessage {
    private String statusLine;
    private String errorLine;
    private String tableLine;
    private String entityLine;
    private String identifierLine;
    private String pathsLine;
    File fPath;
    private String color;
    final String redColor = "#E74C3C";
    final String greenColor = "#2ECC71";
    final String tableLineWidth = "40";
    final String entityLineWidth = "40";
    final String identifierLineWidth = "40";
    final String pathsLineWidth = "65";
    List<HTMLTableEntity> entries=new ArrayList<>();
    List<HTMLTableEntity> identifiers=new ArrayList<>();
    List<HTMLTableEntity> entities=new ArrayList<>();
    List<HTMLTableEntity> paths=new ArrayList<>();
    
    
    /**
     * Sets the BODY of the mail message with TABLEs
     * @param jobName Name of the JOB ([GOBII - Extractor]: crop - extraction of "xxxx") 
     * @param shortError error message, 100 or less charectors
     * @param success If the job is success/failed (true/false)
     * @param longError Long error message
     * @return
     */
    public ProcessMessage setBody(String jobName, String type, long time, String shortError,boolean success, String longError){
        this.setStatus(success);
        this.setSubject(jobName+(success?" Success":" Failed"));
        this.errorLine=shortError;
        this.color = (success ? greenColor:redColor);
        if(!entries.isEmpty()) {
            tableLine = HTMLTableEntity.getHTMLTable(entries, tableLineWidth,"Table","Total in File", "Total Loaded","Total Existing","Total Invalid");
        }
        if(!identifiers.isEmpty()) {
            identifierLine = HTMLTableEntity.getHTMLTable(identifiers, identifierLineWidth,"Identifier Type","Name","ID");
        }
        if(!entities.isEmpty()) {
            entityLine = HTMLTableEntity.getHTMLTable(entities, entityLineWidth,"Type","Count");
        }
        if(!paths.isEmpty()) {
            pathsLine = HTMLTableEntity.getHTMLTable(paths, pathsLineWidth,"File Type","Path","Size");
        }

        String line="<br/>";
        StringBuilder body=new StringBuilder();
        body.append("<html><head><style>table{font-family:arial,sans-serif;border-collapse:collapse;width:60%;}th{background-color:" + color + ";border:1px solid #dddddd;text-align:left;padding:8px;}td{border:1px solid #dddddd;text-align:left;padding:8px;}tr:nth-child(even){background-color:lightblue;}</style></head><body>");

        if(type!=null){
            body.append("<font size = 4><b>"+type+"</b></font> (Duration: "+(time>=1000?time/1000+"secs":time+"ms")+")<br/><br/>");
        }
        else{
            body.append("<br/><br/>");
        }

        body.append(statusLine+line);
        if(errorLine!=null)body.append(errorLine+line);
        body.append(line);
        if(identifierLine!=null)body.append(identifierLine+line);
        if(entityLine!=null)body.append(entityLine+line);
        if(tableLine!=null)body.append(tableLine+line);
        if(pathsLine!=null)body.append(pathsLine+line);
        if(longError!=null)body.append(longError);
        body.append("</html>");
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
    public ProcessMessage addEntry(String tableName,String fileCount, String loadCount, String existCount, String invalidCount){
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
    public ProcessMessage addIdentifier(String type,String name, String id){
        if((name==null) && ((id==null || id.equals("null"))))return this;
        if(id==null){
            identifiers.add(new HTMLTableEntity(type,name,""));
        }
        else {
            identifiers.add(new HTMLTableEntity(type, name, id));
        }
         return this;
    }
    
    public ProcessMessage addIdentifier(String type, GobiiFilePropNameId identifier){
        if(identifier==null)return this;//Don't add a null ID to the table
        return addIdentifier(type,identifier.getName(),identifier.getId()+"");
    }
    
     /**
     * Add an entry to the Entity table (just with two strings)
     * @param type Type of entry (Platform, Dataset, etc.)
     * @param name Name of entry
     * @return this object
     */
    public ProcessMessage addEntity(String type,String name){
        if(name==null)return this;
        entities.add(new HTMLTableEntity(type,name));
        return this;
    }

    public ProcessMessage addEntity(String type, GobiiFilePropNameId entity){
        if(entity==null)return this;//Don't add a null ID to the table
        return addEntity(type,entity.getName()+"");
    }
    
    
    /**
     * Add item to the filepaths entry
     * @param type type of file
     * @param path filepath
     * @return this object
     */
    public ProcessMessage addPath(String type,String path){
    	if(new File(path).length() > 1){
    		paths.add(new HTMLTableEntity(type,path,sizeToReadable(new File(path).length())));
    	}
        return this;
    }

    /***
     * Get destination path for instruction file (done directory)
     * @param type type of file
     * @param path current path to get the file length
     * @param donePath Destination path (final path or instruction file)
     * @return
     */
    public ProcessMessage addPath(String type, String path, String donePath){
        String pathFinal = donePath + new File(path).getName();
        if(new File(path).length() > 1){
            paths.add(new HTMLTableEntity(type,pathFinal,sizeToReadable(new File(path).length())));
        }
        return this;
    }
    
    /**
     * Set status line in HTML format. format includes font size and color
     * @param status
     * @return
     */
    private ProcessMessage setStatus(boolean status) {
        statusLine = "Status: " + (status ?
                "<font color="+greenColor+" size=4><b>SUCCESS</b></font>" :
                "<font color="+redColor+" size=4><b>ERROR</b></font>");
        return this;
    }


}

