package org.gobiiproject.gobiimodel.utils.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class created in laziness. Accepts arbitrary length 'value' arguments and creates HTML tables out of them.
 * Note: Oddity in getHTMLTable - names of fields aren't stored in the HTMLTableEntity class, so you have to specify the
 * column names in getHTMLTable. Saves me some coding, but may be a pitfall since String... args may be empty. (Which throws a runtimeexception)
 */
public class HTMLTableEntity {
    List<String> fields=new ArrayList<>();

    /**
     * Creates an HTMLTableEntity, specifying a row in an HTML table. Pass parameters in order as table elements.
     * @param values Ordered List of Values for the entity to possess
     */
    public HTMLTableEntity(String... values){
        fields= Arrays.asList(values);
    }

    /**
     * Returns header row <including <b>th</b> columns.
     * @param fieldNames List of table header names
     * @return valid table starting row elements
     */
    private static String getHTMLTableStart(String width, String... fieldNames){
        if(fieldNames.length==0){
            throw new RuntimeException("Invalid Table Construction");
        }
        StringBuilder sb=new StringBuilder();
        sb.append("<table style=width:"+width+"%, border=\"1\"><tr>");
        for(String name:fieldNames) {
            sb.append("<th align=\"left\">").append(name).append("</th>");//Left align to better align tables
        }
        return sb.append("</tr>").toString();
    }
    /**
     * The end of the table. (Just slashTable)
     */
    private static String getHTMLTableEnd(){
        return "</table>";
    }
    /**
     * Magic. Returns a row containing every field, in order.
     */
    private String getHTMLTableRow(){
        StringBuilder sb=new StringBuilder();
        sb.append("<tr>");
        for(String value:fields) {
            sb.append("<td>").append(value).append("</td>");
        }
        return sb.append("</tr>").toString();
    }

    /**
     * Creates a valid HTML table given labels and 'HTMLTableEntity' objects.
     * @param contents HTMLTableEntity objects, one for each row.
     * @param labels String labels for each value, in order.
     * @return Valid HTML table based on the entities and labels
     */
    public static String getHTMLTable(List<HTMLTableEntity> contents,String width, String... labels){
        StringBuilder sb = new StringBuilder();
       sb.append(getHTMLTableStart(width, labels));
       for(HTMLTableEntity content:contents){
           sb.append(content.getHTMLTableRow());
       }
       sb.append(getHTMLTableEnd());
       return sb.toString();
   }
}
