package org.gobiiproject.gobiiprocess.extractor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiiprocess.ProcessGlobalConfigs;

public class ExtractorGlobalConfigs extends ProcessGlobalConfigs{
    private ExtractorGlobalConfigs(){}


    /**
     * Adds options to an Options object which will be read in 'setFromFlags'.
     */
    public static void addOptions(Options o)  {
        ProcessGlobalConfigs.addOptions(o);
    }
    public static void setFromFlags(CommandLine cli){
        ProcessGlobalConfigs.setFromFlags(cli);
     }

}