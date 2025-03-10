package pl.edu.dik.tks.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoConfig {

    @Value("${mongodb.connection.uri}")
    private String mongoConnectionUri;

    @Bean
    public ConnectionString mongoConnectionUri() {
        return new ConnectionString(mongoConnectionUri);
    }

    @Value("${mongodb.database.name}")
    private String mongoDatabaseName;

    @Bean
    public String mongoDatabaseName() {
        return mongoDatabaseName;
    }

    // TODO sprawdzic czy try catch jest potrzebny
    // Config according to https://www.mongodb.com/developer/languages/java/java-mapping-pojos/
    //                     https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/data-formats/document-data-format-pojo/
    //               UUID->https://www.baeldung.com/java-mongodb-uuid
    @Bean(destroyMethod = "close")
    public MongoClient mongoConnection() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                .register(UUID.class)
                .automatic(true)
                .build();

        CodecRegistry pojoCodecRegistry = fromRegistries(
                getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider)
        );

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)  //TODO: teoretycznie powinno brac z configa ??
                .applyConnectionString(mongoConnectionUri())
                .codecRegistry(pojoCodecRegistry)
                .build();

        return MongoClients.create(clientSettings);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoConnection) {
        return mongoConnection.getDatabase(mongoDatabaseName());
    }

}
