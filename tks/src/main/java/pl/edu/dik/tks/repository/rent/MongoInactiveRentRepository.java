package pl.edu.dik.tks.repository.rent;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.edu.dik.tks.model.rent.Rent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class MongoInactiveRentRepository implements InactiveRentRepository {

    private final MongoCollection<Rent> inactiveRentMongoCollection;

    public MongoInactiveRentRepository(MongoDatabase database, MongoClient mongoClient) {
        this.inactiveRentMongoCollection = database.getCollection("inactiveRent", Rent.class);
    }

    @Override
    public Optional<Rent> findById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(inactiveRentMongoCollection.find(filter).first());
    }

    @Override
    public List<Rent> findAll() {
        return StreamSupport.stream(inactiveRentMongoCollection.find().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rent> getRentsByAccountId(UUID clientId) {
        Bson filter = Filters.eq("account._id", clientId);
        return StreamSupport.stream(inactiveRentMongoCollection.find(filter).spliterator(), false)
                .collect(Collectors.toList());
    }
}
