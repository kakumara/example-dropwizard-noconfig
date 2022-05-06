/*
 * Copyright (c) 2011-present Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://links.sonatype.com/products/clm/attributions.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.dropwizard.example;

import com.sonatype.dropwizard.example.config.ConfigFactory;
import com.sonatype.dropwizard.example.config.DbConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainConfig
    extends Configuration
{
  @NotNull
  private DataSourceFactory database = newDataSourceFactory();

  private DataSourceFactory newDataSourceFactory() {
    DbConfig dbConfig = ConfigFactory.getDbConfig();
    DataSourceFactory dataSourceFactory = new DataSourceFactory();
    dataSourceFactory.setDriverClass(dbConfig.driverClass);
    dataSourceFactory.setUrl(dbConfig.url);
    dataSourceFactory.setUser(dbConfig.user);
    dataSourceFactory.setPassword(dbConfig.password);
    return dataSourceFactory;
  }

  public DataSourceFactory getDataSourceFactory() {
    return database;
  }
}
