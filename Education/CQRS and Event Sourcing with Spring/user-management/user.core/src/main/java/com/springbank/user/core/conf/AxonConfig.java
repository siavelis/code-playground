package com.springbank.user.core.conf;

import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import java.util.Collections;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoFactory;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoSettingsFactory;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

  @Value("${spring.data.mongodb.host:127.0.0.1}")
  private String mongoHost;

  @Value("${spring.data.mongodb.port:27018}")
  private int mongoPort;

  @Value("${spring.data.mongodb.database:user}")
  private String mongoDatabase;

  @Bean
  public MongoClient mongoClient() {
    var mongoSettingFactory = new MongoSettingsFactory();
    mongoSettingFactory.setMongoAddresses(Collections.singletonList(new ServerAddress(mongoHost, mongoPort)));

    var mongoFactory = new MongoFactory();
    mongoFactory.setMongoClientSettings(mongoSettingFactory.createMongoClientSettings());

    return mongoFactory.createMongo();
  }

  @Bean
  public MongoTemplate axonMongoTemplate() {
    return DefaultMongoTemplate.builder()
        .mongoDatabase(mongoClient(), mongoDatabase)
        .build();
  }

  @Bean
  public TokenStore tokenStore(Serializer serializer) {
    return MongoTokenStore.builder()
        .mongoTemplate(axonMongoTemplate())
        .serializer(serializer)
        .build();
  }

  @Bean
  public EventStorageEngine eventStorageEngine(MongoClient client) {
    return MongoEventStorageEngine.builder()
        .mongoTemplate(DefaultMongoTemplate.builder()
            .mongoDatabase(client)
            .build())
        .build();
  }

  @Bean
  EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
    return EmbeddedEventStore.builder()
        .storageEngine(storageEngine)
        .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
        .build();
  }

}
