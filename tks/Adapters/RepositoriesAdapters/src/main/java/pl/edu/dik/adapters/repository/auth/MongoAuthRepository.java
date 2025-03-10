package pl.edu.dik.adapters.repository.auth;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.edu.dik.adapters.exception.DuplicatedKeyRepositoryException;
import pl.edu.dik.adapters.model.account.AccountEnt;

import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class MongoAuthRepository implements AuthRepository {

    private final MongoCollection<AccountEnt> collection;

    @Autowired
    public MongoAuthRepository(MongoDatabase database) {
        this.collection = database.getCollection("account", AccountEnt.class);
        ensureUniqueIndex();
    }


    @Override
    public AccountEnt save(AccountEnt object) throws DuplicatedKeyRepositoryException {
        try {
            object.setId(UUID.randomUUID());
            collection.insertOne(object);
            return object;
        } catch (MongoWriteException e) {
            if (e.getError().getCode() == 11000) {
                throw new DuplicatedKeyRepositoryException("Account with this login already exists");
            } else {
                throw e;
            }
        }
    }


    @Override
    public Optional<AccountEnt> findByLogin(String login) {
        Bson filter = eq("login", login);
        return Optional.ofNullable(collection.find(filter).first());
    }


    @Override
    public boolean update(AccountEnt account) {
        Object id = account.getId();
        Bson filter = Filters.eq("_id", id.toString());
        return collection.replaceOne(filter, account).wasAcknowledged();
    }


    private void ensureUniqueIndex() {
        IndexOptions options = new IndexOptions().unique(true);
        collection.createIndex(Indexes.ascending("login"), options);
    }
}
