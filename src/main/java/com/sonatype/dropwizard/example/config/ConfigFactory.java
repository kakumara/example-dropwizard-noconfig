/*
 * Copyright (c) 2011-present Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://links.sonatype.com/products/clm/attributions.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.dropwizard.example.config;

public class ConfigFactory
{
  public static DbConfig getDbConfig() {
    DbConfig dbConfig = new DbConfig();
    dbConfig.url = Environment.getEnvironmentVariable("example.db.url").orElse("jdbc:h2:./target/example");
    dbConfig.driverClass = Environment.getEnvironmentVariable("example.db.driverClass").orElse("org.h2.Driver");
    dbConfig.user = Environment.getEnvironmentVariable("example.db.user").orElse("sa");
    dbConfig.password = Environment.getEnvironmentVariable("example.db.password").orElse("sa");
    return dbConfig;
  }
}
