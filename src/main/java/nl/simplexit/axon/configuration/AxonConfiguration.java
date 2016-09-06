package nl.simplexit.axon.configuration;


import com.mongodb.MongoClient;
import nl.simplexit.axon.model.Account;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;
import org.axonframework.mongo3.eventstore.DefaultMongoTemplate;
import org.axonframework.mongo3.eventstore.MongoEventStore;
import org.axonframework.mongo3.eventstore.MongoTemplate;
import org.axonframework.serializer.json.JacksonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
//@PropertySource(value = "classpath:application.yml")
//@/EnableElasticsearchRepositories(basePackages = "nl.vnuvacaturemedia.ndsm.address.repository")
public class AxonConfiguration {

  @Autowired
  public MongoClient mongo;

  @Value("${spring.application.databaseName}")
  private String databaseName;

  @Value("${spring.application.eventsCollectionName}")
  private String eventsCollectionName;

  @Value("${spring.application.snapshotCollectionName}")
  private String snapshotCollectionName;

  @Bean
  JacksonSerializer axonJsonSerializer() {
    return new JacksonSerializer();
  }

  @Bean
  public SimpleCommandBus commandBus() {
    SimpleCommandBus simpleCommandBus = new SimpleCommandBus();
    return simpleCommandBus;
  }

  /**
   * The simple command bus, an implementation of an EventBus
   * mostly appropriate in a single JVM, single threaded use case.
   * @return the {@link SimpleEventBus}
   */
  @Bean
  public SimpleEventBus eventBus() {
    return new SimpleEventBus();
  }

  @Bean
  public DefaultCommandGateway commandGateway() {
    return new DefaultCommandGateway(commandBus());
  }

  @Bean(name = "axonMongoTemplate")
  MongoTemplate axonMongoTemplate() {
    MongoTemplate template = new DefaultMongoTemplate(mongo,
            databaseName, eventsCollectionName, snapshotCollectionName);
    return template;
  }

  /**
   * An event sourcing implementation needs a place to store events. i.e. The event Store.
   * In our use case we will be storing our events in a file system, so we configure
   * the FileSystemEventStore as our EventStore implementation
   *
   * It should be noted that Axon allows storing the events
   * in other persistent mechanism...jdbc, jpa etc
   *
   * @return the {@link EventStore}
   */
  @Bean
  public EventStore eventStore() {
    //EventStore eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File("./events")));
    MongoEventStore eventStore = new MongoEventStore(axonJsonSerializer(), axonMongoTemplate());

    return eventStore;
  }

  /**
   * Our aggregate root is now created from stream of events and not from a representation in a persistent mechanism,
   * thus we need a repository that can handle the retrieving of our aggregate root from the stream of events.
   *
   * We configure the EventSourcingRepository which does exactly this. We supply it with the event store
   * @return {@link EventSourcingRepository}
   */
  @Bean
  public EventSourcingRepository eventSourcingRepository() {
    EventSourcingRepository eventSourcingRepository = new EventSourcingRepository(Account.class, eventStore());
    eventSourcingRepository.setEventBus(eventBus());
    return eventSourcingRepository;
  }
}
