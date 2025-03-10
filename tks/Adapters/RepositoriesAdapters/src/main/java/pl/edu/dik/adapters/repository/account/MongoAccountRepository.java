package pl.edu.dik.adapters.repository.account;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.edu.dik.adapters.model.account.AccountEnt;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

@Getter
@Repository
public class MongoAccountRepository implements AccountRepository {

    private final MongoCollection<AccountEnt> collection;

    public MongoAccountRepository(MongoDatabase mongoDatabase) {
        this.collection = mongoDatabase.getCollection("account", AccountEnt.class);
    }

    @Override
    public Optional<AccountEnt> findById(Object id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(collection.find(filter).first());
    }


    @Override
    public AccountEnt update(AccountEnt updatedAccount) {
        Object id = updatedAccount.getId();
        Bson filter = Filters.eq("_id", id);
        return collection.replaceOne(filter, updatedAccount).wasAcknowledged() ? updatedAccount : null;
    }


    @Override
    public List<AccountEnt> findAll() {
        return StreamSupport.stream(collection.find().spliterator(), false)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<AccountEnt> findByLogin(String login) {
        Bson filter = eq("login", login);
        return Optional.ofNullable(getCollection().find(filter).first());
    }


    @Override
    public List<AccountEnt> findByMatchingLogin(String loginSubstring) {
        Bson filter = regex("login", loginSubstring, "i"); // Remove unnecessary and()
        return StreamSupport.stream(collection.find(filter).spliterator(), false)
                .collect(Collectors.toList());
    }
}
