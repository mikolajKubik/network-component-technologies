package pl.edu.dik.tks.auth;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.edu.dik.tks.exception.auth.DuplicatedKeyException;
import pl.edu.dik.tks.model.account.Account;

import static com.mongodb.client.model.Filters.*;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthRepository {

    private final MongoCollection<Account> collection;

    @Autowired
    public AuthRepository(MongoDatabase database) {
        this.collection = database.getCollection("account", Account.class);
        ensureUniqueIndex();
    }

    public Account save(Account object) throws DuplicatedKeyException {
        try {
            object.setId(UUID.randomUUID());
            collection.insertOne(object);
            return object;
        } catch (MongoWriteException e) {
            if (e.getError().getCode() == 11000) {
                throw new DuplicatedKeyException("Account with this login already exists");
            } else {
                throw e;
            }
        }
    }

    public Optional<Account> findByLogin(String login) {
        Bson filter = eq("login", login);
        return Optional.ofNullable(collection.find(filter).first());
    }

    public boolean update(Account account) {
        Object id = account.getId();
        Bson filter = Filters.eq("_id", id.toString());
        return collection.replaceOne(filter, account).wasAcknowledged();
    }

    private void ensureUniqueIndex() {
        IndexOptions options = new IndexOptions().unique(true);
        collection.createIndex(Indexes.ascending("login"), options);
    }
}
