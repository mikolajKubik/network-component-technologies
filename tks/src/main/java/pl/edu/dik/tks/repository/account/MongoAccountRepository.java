package pl.edu.dik.tks.repository.account;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.edu.dik.tks.model.account.Account;

import java.util.List;
import java.util.Optional;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Updates.inc;

@Getter
@Repository
public class MongoAccountRepository implements AccountRepository {
    private final MongoCollection<Account> collection;

    public MongoAccountRepository(MongoDatabase mongoDatabase) {
        this.collection = mongoDatabase.getCollection("account", Account.class);
    }

    @Override
    public Optional<Account> findById(Object id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(collection.find(filter).first());
    }

    @Override
    public Account update(Account updatedAccount) {
        Object id = updatedAccount.getId();
        Bson filter = Filters.eq("_id", id);
        return collection.replaceOne(filter, updatedAccount).wasAcknowledged() ? updatedAccount : null;
    }

    @Override
    public List<Account> findAll() {
        return StreamSupport.stream(collection.find().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Account> findByLogin(String login) {
        Bson filter = eq("login", login);
        return Optional.ofNullable(getCollection().find(filter).first());
    }

    public List<Account> findByMatchingLogin(String loginSubstring) {
        Bson filter = regex("login", loginSubstring, "i"); // Remove unnecessary and()
        return StreamSupport.stream(collection.find(filter).spliterator(), false)
                .collect(Collectors.toList());
    }
}
