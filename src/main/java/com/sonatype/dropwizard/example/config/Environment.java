/*
 * Copyright (c) 2011-present Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://links.sonatype.com/products/clm/attributions.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.dropwizard.example.config;

import java.util.Optional;

public class Environment
{
  public static Optional<String> getEnvironmentVariable(String variableName) {
    return Optional.ofNullable(System.getenv(variableName));
  }
}
