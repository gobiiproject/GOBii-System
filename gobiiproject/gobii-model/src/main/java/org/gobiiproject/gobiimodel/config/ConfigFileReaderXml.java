package org.gobiiproject.gobiimodel.config;


import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

/**
 * This class handles serialization of the modern configuration file format,
 * which is XML.
 */
public class ConfigFileReaderXml {

    public void write(ConfigValues configValues, String fileName) throws Exception {

        Serializer serializer = new Persister();

        File file = new File(fileName);
        if( ! file.exists()) {
            file.createNewFile();
        }

        serializer.write(configValues, file);



    } // ConfigFileReaderProps

    public ConfigValues read(String fileName) throws Exception {

        ConfigValues returnVal = null;

        Serializer serializer = new Persister();
        File file = new File(fileName);

        // third parameter false causes serializer to ignore elements in the
        // file that do not have a corresponding proeprty in te pojo.
        returnVal = serializer.read(ConfigValues.class, file,false);

        return returnVal;


    }
}
