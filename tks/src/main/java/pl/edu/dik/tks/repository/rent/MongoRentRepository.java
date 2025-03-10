package pl.edu.dik.tks.repository.rent;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.edu.dik.tks.exception.business.ClientNotAvailableForRentException;
import pl.edu.dik.tks.exception.business.GameNotAvailableForRentException;
import pl.edu.dik.tks.exception.business.RentNotFoundException;
import pl.edu.dik.tks.model.account.Account;
import pl.edu.dik.tks.model.game.Game;
import pl.edu.dik.tks.model.rent.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Updates.inc;

@Repository
public class MongoRentRepository implements RentRepository {
    private final MongoCollection<Rent> rentMongoCollection;
    private final MongoCollection<Rent> inactiveRentMongoCollection;
    private final MongoCollection<Account> accountMongoCollection;
    private final MongoCollection<Game> gameMongoCollection;
    private final MongoClient mongoClient;

    public MongoRentRepository(MongoDatabase database, MongoClient mongoClient) {
        this.rentMongoCollection = database.getCollection("rent", Rent.class);
        this.inactiveRentMongoCollection = database.getCollection("inactiveRent", Rent.class);
        this.accountMongoCollection = database.getCollection("account", Account.class);
        this.gameMongoCollection = database.getCollection("game", Game.class);

        this.mongoClient = mongoClient;
    }

    @Override
    public Rent save(LocalDate startDate, LocalDate endDate, String login, UUID gameId) throws
            ClientNotAvailableForRentException,
            GameNotAvailableForRentException {

        ClientSession session = mongoClient.startSession();
        try {
            session.startTransaction();

            Bson accountFilter = eq("login", login);
            Account account = accountMongoCollection.find(session, accountFilter).first();


            Bson gameFilter = Filters.eq("_id", gameId);
            Game game = gameMongoCollection.find(session, gameFilter).first();


            Rent rent = new Rent(startDate, endDate, account, game, 0);
            rent.setId(UUID.randomUUID());

            if (!markAccountAsRented(session, rent.getAccount().getId())) {
                session.abortTransaction();
                throw new ClientNotAvailableForRentException("Client is not available for rent or does not exist.");
            }

            if (!markGameAsRented(session, rent.getGame().getId())) {
                session.abortTransaction();
                throw new GameNotAvailableForRentException("Game is not available for rent or does not exist.");
            }

            rent.setRentalPrice(calculateRentPrice(rent.getStartDate(), rent.getEndDate(), rent.getGame().getPricePerDay()));

            rent.setId(UUID.randomUUID());
            rentMongoCollection.insertOne(session, rent);
            session.commitTransaction();

            return rent;
        } catch (Exception e) {
            session.abortTransaction();
            session.close();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Rent> findById(Object id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(rentMongoCollection.find(filter).first());
    }

    @Override
    public List<Rent> findAll() {
        return StreamSupport.stream(rentMongoCollection.find().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Rent update(Rent updatedEntity) {
        Object id = updatedEntity.getId();
        Bson filter = Filters.eq("_id", id.toString());
        return rentMongoCollection.replaceOne(filter, updatedEntity).wasAcknowledged() ? updatedEntity : null;
    }

    @Override
    public void deleteById(Object id) throws GameNotAvailableForRentException, ClientNotAvailableForRentException, RentNotFoundException {
        Rent rent = findById(id).orElseThrow(() -> new RentNotFoundException("Rent with ID " + id + " not found"));

        ClientSession session = mongoClient.startSession();
        try {
            session.startTransaction();
            if (!unmarkAccountAsRented(session, rent.getAccount().getId())) {
                throw new ClientNotAvailableForRentException("Client is not available for rent or does not exist.");
            }

            if (!unmarkGameAsRented(session, rent.getGame().getId())) {
                throw new GameNotAvailableForRentException("Game is not available for rent or does not exist.");
            }

            rent.setEndDate(LocalDate.now());
            rent.setRentalPrice(calculateRentPrice(rent.getStartDate(), rent.getEndDate(), rent.getGame().getPricePerDay()));

            Bson filter = Filters.eq("_id", id);
            rentMongoCollection.deleteOne(filter);

            inactiveRentMongoCollection.insertOne(session, rent);

            session.commitTransaction();
        } catch (Exception e) {
            session.abortTransaction();
            session.close();
            throw e;
        } finally {
            session.close();
        }

    }

//    @Override
//    public void deleteById(Object id) {
//        Bson filter = eq("_id", id.toString());
//        collection.deleteOne(filter);
//    }

    public List<Rent> getRentsByAccountId(UUID clientId) {
        Bson filter = Filters.eq("account._id", clientId);
        return StreamSupport.stream(rentMongoCollection.find(filter).spliterator(), false)
                .collect(Collectors.toList());
    }

    // Atomic increment to mark as rented
    // https://medium.com/@codersauthority/handling-race-conditions-and-concurrent-resource-updates-in-node-and-mongodb-by-performing-atomic-9f1a902bd5fa
    public boolean markGameAsRented(ClientSession session, UUID gameId) {
        Bson filter = and(eq("_id", gameId), eq("rental_status_count", 0)); // Ensure game is not rented
        Bson update = inc("rental_status_count", 1); // Increment rental status count by 1

        Document updatedGame = gameMongoCollection.withDocumentClass(Document.class)
                .findOneAndUpdate(session, filter, update);

        return updatedGame != null; // Returns true if update was successful, false if not
    }

    // Atomic decrement to unmark as rented
    // https://medium.com/@codersauthority/handling-race-conditions-and-concurrent-resource-updates-in-node-and-mongodb-by-performing-atomic-9f1a902bd5fa
    public boolean unmarkGameAsRented(ClientSession session, UUID gameId) {
        Bson filter = and(eq("_id", gameId), eq("rental_status_count", 1)); // Ensure game is rented by one renter
        Bson update = inc("rental_status_count", -1); // Decrement rental status count by 1

        Document updatedGame = gameMongoCollection.withDocumentClass(Document.class)
                .findOneAndUpdate(session, filter, update);
        return updatedGame != null; // Returns true if update was successful, false if not
    }

    // Atomic increment to mark a client as renting another item
    // https://medium.com/@codersauthority/handling-race-conditions-and-concurrent-resource-updates-in-node-and-mongodb-by-performing-atomic-9f1a902bd5fa
    public boolean markAccountAsRented(ClientSession session, UUID clientId) {
        Bson filter = and(eq("_id", clientId), lt("rental_count", 5)); // Ensure rentalCount < 5
        Bson update = inc("rental_count", 1); // Increment rental count by 1

        Document updatedClient = accountMongoCollection.withDocumentClass(Document.class)
                .findOneAndUpdate(session, filter, update);
        return updatedClient != null; // Returns true if update was successful, false if not
    }

    // Atomic decrement to unmark a client as renting an item
    // https://medium.com/@codersauthority/handling-race-conditions-and-concurrent-resource-updates-in-node-and-mongodb-by-performing-atomic-9f1a902bd5fa
    public boolean unmarkAccountAsRented(ClientSession session, UUID clientId) {
        Bson filter = and(eq("_id", clientId), gt("rental_count", 0)); // Ensure rentalCount > 0
        Bson update = inc("rental_count", -1); // Decrement rental count by 1

        Document updatedClient = accountMongoCollection.withDocumentClass(Document.class)
                .findOneAndUpdate(session, filter, update);
        return updatedClient != null; // Returns true if update was successful, false if not
    }

    private int calculateRentPrice(LocalDate startDate, LocalDate endDate, int pricePerDay) {
        return pricePerDay * startDate.until(endDate).getDays() + pricePerDay;
    }
}
