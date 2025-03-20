package pl.edu.dik.adapters.repository.game;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.edu.dik.adapters.model.game.GameEnt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class MongoGameRepository implements GameRepository {

    private final MongoCollection<GameEnt> collection;

    @Autowired
    public MongoGameRepository(MongoDatabase database) {
        this.collection = database.getCollection("game", GameEnt.class);
    }


    @Override
    public GameEnt save(GameEnt game) {
        game.setId(UUID.randomUUID());
        collection.insertOne(game);
        return game;
    }


    @Override
    public Optional<GameEnt> findById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(collection.find(filter).first());
    }


    @Override
    public List<GameEnt> findAll() {
        return StreamSupport.stream(collection.find().spliterator(), false)
                .collect(Collectors.toList());
    }


    @Override
    public GameEnt update(GameEnt updatedEntity) {
        // Extract the ID directly from the object
        Object id = updatedEntity.getId();
        Bson filter = Filters.eq("_id", id);
        return collection.replaceOne(filter, updatedEntity).wasAcknowledged() ? updatedEntity : null;
    }


    @Override
    public void deleteById(UUID id) {
        Bson filter = eq("_id", id);
        collection.deleteOne(filter);
    }
}
