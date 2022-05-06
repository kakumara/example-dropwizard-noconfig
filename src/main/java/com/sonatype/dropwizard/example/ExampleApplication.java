/*
 * Copyright (c) 2011-present Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://links.sonatype.com/products/clm/attributions.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.dropwizard.example;

import java.sql.Connection;

import com.sonatype.dropwizard.example.dao.PersonDAO;
import com.sonatype.dropwizard.example.model.Person;
import com.sonatype.dropwizard.example.resource.PersonResource;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleApplication
    extends Application<MainConfig>
{
  private static final Logger log = LoggerFactory.getLogger(ExampleApplication.class);

  private final HibernateBundle<MainConfig> hibernateBundle =
      new HibernateBundle<>(Person.class)
      {
        @Override
        public DataSourceFactory getDataSourceFactory(MainConfig configuration) {
          return configuration.getDataSourceFactory();
        }
      };

  @Override
  public void initialize(Bootstrap<MainConfig> bootstrap) {
    // Enable variable substitution with environment variables
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(
            bootstrap.getConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor(true)
        )
    );

    bootstrap.addBundle(new MigrationsBundle<>()
    {
      @Override
      public DataSourceFactory getDataSourceFactory(MainConfig configuration) {
        return configuration.getDataSourceFactory();
      }
    });
    bootstrap.addBundle(hibernateBundle);
  }

  public static void main(String[] args) throws Exception {
    new ExampleApplication().run(args);
  }

  @Override
  public void run(MainConfig configuration, Environment environment) {
    migrate(configuration, environment);
    final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
    environment.jersey().register(new PersonResource(dao));
  }

  private void migrate(MainConfig configuration, Environment environment) {
    log.info("Running schema migration");
    ManagedDataSource dataSource = createMigrationDataSource(configuration, environment);

    try (Connection connection = dataSource.getConnection()) {
      JdbcConnection conn = new JdbcConnection(connection);

      Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(conn);
      Liquibase liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), database);
      liquibase.update("");

      log.info("Migration completed!");
    }
    catch (Exception ex) {
      throw new IllegalStateException("Unable to migrate database", ex);
    }
    finally {
      try {
        dataSource.stop();
      }
      catch (Exception ex) {
        log.error("Unable to stop data source used to execute schema migration", ex);
      }
    }
  }

  private ManagedDataSource createMigrationDataSource(MainConfig configuration, Environment environment) {
    DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();

    return dataSourceFactory.build(environment.metrics(), "migration-ds");
  }
}
