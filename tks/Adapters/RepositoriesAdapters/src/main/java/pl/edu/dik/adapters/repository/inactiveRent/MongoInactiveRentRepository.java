package pl.edu.dik.adapters.repository.inactiveRent;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.edu.dik.adapters.model.rent.RentEnt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class MongoInactiveRentRepository implements InactiveRentRepository {

    private final MongoCollection<RentEnt> inactiveRentMongoCollection;

    public MongoInactiveRentRepository(MongoDatabase database, MongoClient mongoClient) {
        this.inactiveRentMongoCollection = database.getCollection("inactiveRent", RentEnt.class);
    }

    @Override
    public Optional<RentEnt> findById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(inactiveRentMongoCollection.find(filter).first());
    }

    @Override
    public List<RentEnt> findAll() {
        return StreamSupport.stream(inactiveRentMongoCollection.find().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentEnt> getRentsByAccountId(UUID clientId) {
        Bson filter = Filters.eq("account._id", clientId);
        return StreamSupport.stream(inactiveRentMongoCollection.find(filter).spliterator(), false)
                .collect(Collectors.toList());
    }
}
