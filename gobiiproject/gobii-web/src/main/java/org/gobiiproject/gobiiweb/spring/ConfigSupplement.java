package org.gobiiproject.gobiiweb.spring;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiiweb.DataSourceSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 8/16/2016.
 */

@Configuration
public class ConfigSupplement {

    @Bean(name="dataSourceMulti")
    public DataSourceSelector dataSourceMulti() throws Exception {

        DataSourceSelector returnVal = new DataSourceSelector();

        ConfigSettings configSettings = new ConfigSettings();
        Map<Object,Object> targetDataSources = new HashMap<>();
        for (GobiiCropConfig currentGobiiCropConfig : configSettings.getActiveCropConfigs()) {

            DriverManagerDataSource currentDataSource = new DriverManagerDataSource();

            currentDataSource.setDriverClassName("org.postgresql.Driver");

            currentDataSource.setUrl(currentGobiiCropConfig
                    .getCropDbConfig(GobiiDbType.POSTGRESQL)
                    .getConnectionString());

            currentDataSource.setUsername(currentGobiiCropConfig
                    .getCropDbConfig(GobiiDbType.POSTGRESQL)
                    .getUserName());

            currentDataSource.setUsername(currentGobiiCropConfig
                    .getCropDbConfig(GobiiDbType.POSTGRESQL)
                    .getUserName());


            currentDataSource.setPassword(currentGobiiCropConfig
                    .getCropDbConfig(GobiiDbType.POSTGRESQL)
                    .getPassword());

            targetDataSources.put(currentGobiiCropConfig.getGobiiCropType(),currentDataSource);

        } // iterate crop configs

        returnVal.setTargetDataSources(targetDataSources);

        return returnVal;

    }
}
