/*
 *  Copyright (C) 2014 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 * 
 *  GPLv3 + Classpath exception
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.geosolutions.geofence.config;

import it.geosolutions.geofence.GeofenceAccessManager;
import it.geosolutions.geofence.cache.CacheConfiguration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.util.logging.Logging;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class GeoFenceConfigurationManager {

    private static final Logger LOGGER = Logging.getLogger(GeofenceAccessManager.class);

    private GeoFencePropertyPlaceholderConfigurer configurer;

    private GeoFenceConfiguration geofenceConfiguration;
    private CacheConfiguration cacheConfiguration;


    public GeoFenceConfiguration getConfiguration() {
        return geofenceConfiguration;
    }

    /**
     * Updates the configuration.
     *
     * @param configuration
     */
    public void setConfiguration(GeoFenceConfiguration configuration) {

        this.geofenceConfiguration = configuration;

        LOGGER.log(Level.INFO,
                "GeoFence configuration: instance name is {0}",
                configuration.getInstanceName());
    }

    public CacheConfiguration getCacheConfiguration() {
        return cacheConfiguration;
    }

    public void setCacheConfiguration(CacheConfiguration cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
    }


    public void storeConfiguration() throws IOException {
        File configurationFile =  configurer.getConfigFile();

        FileUtils.forceMkdir(configurationFile.getParentFile());

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(configurationFile));

            writer.write("### GeoFence Module configuration file\n");
            writer.write("### \n");
            writer.write("### GeoServer will read this file at boot time.\n");
            writer.write("### This file may be automatically regenerated by GeoServer, so any changes beside the property values may be lost.\n\n");


            saveConfiguration(writer, geofenceConfiguration);
            saveConfiguration(writer, cacheConfiguration);

        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * Saves current configuration to disk.
     */
    protected void saveConfiguration(Writer writer, GeoFenceConfiguration configuration)
            throws IOException {

        writer.write("### GeoFence main configuration\n\n");

        saveConfig(writer, "instanceName", configuration.getInstanceName());
        saveConfig(writer, "servicesUrl", configuration.getServicesUrl());
        saveConfig(writer, "allowRemoteAndInlineLayers", configuration.isAllowRemoteAndInlineLayers());
        saveConfig(writer, "allowDynamicStyles", configuration.isAllowDynamicStyles());
        saveConfig(writer, "grantWriteToWorkspacesToAuthenticatedUsers", configuration.isGrantWriteToWorkspacesToAuthenticatedUsers());
        saveConfig(writer, "useRolesToFilter", configuration.isUseRolesToFilter());
        saveConfig(writer, "acceptedRoles", configuration.getAcceptedRoles());
    }

    protected void saveConfig(Writer writer, String name, Object value) throws IOException {
        writer.write(name + "=" + String.valueOf(value) + "\n");
    }

    public void saveConfiguration(Writer writer, CacheConfiguration params) throws IOException {

        writer.write("\n\n### Cache configuration\n\n");

        saveConfig(writer, "cacheSize",    params.getSize());
        saveConfig(writer, "cacheRefresh", params.getRefreshMilliSec());
        saveConfig(writer, "cacheExpire",  params.getExpireMilliSec());
    }


   /**
    * Returns a copy of the configuration.
    * @return
    */

    public void setConfigurer(GeoFencePropertyPlaceholderConfigurer configurer) {
        this.configurer = configurer;
    }

}
