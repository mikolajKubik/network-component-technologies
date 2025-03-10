package pl.edu.dik.adapters.repository.rent;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.edu.dik.adapters.exception.ClientNotAvailableForRentRepositoryException;
import pl.edu.dik.adapters.exception.ClientRentCancellationRepositoryException;
import pl.edu.dik.adapters.exception.GameNotAvailableForRentRepositoryException;
import pl.edu.dik.adapters.exception.RentNotFoundRepositoryException;
import pl.edu.dik.adapters.model.account.AccountEnt;
import pl.edu.dik.adapters.model.game.GameEnt;
import pl.edu.dik.adapters.model.rent.RentEnt;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.inc;

@Repository
public class MongoRentRepository implements RentRepository {

    private final MongoCollection<RentEnt> rentMongoCollection;
    private final MongoCollection<RentEnt> inactiveRentMongoCollection;
    private final MongoCollection<AccountEnt> accountMongoCollection;
    private final MongoCollection<GameEnt> gameMongoCollection;
    private final MongoClient mongoClient;

    public MongoRentRepository(MongoDatabase database, MongoClient mongoClient) {
        this.rentMongoCollection = database.getCollection("rent", RentEnt.class);
        this.inactiveRentMongoCollection = database.getCollection("inactiveRent", RentEnt.class);
        this.accountMongoCollection = database.getCollection("account", AccountEnt.class);
        this.gameMongoCollection = database.getCollection("game", GameEnt.class);

        this.mongoClient = mongoClient;
    }

    @Override
    public RentEnt save(LocalDate startDate, LocalDate endDate, String login, UUID gameId) throws
            ClientNotAvailableForRentRepositoryException,
            GameNotAvailableForRentRepositoryException {

        ClientSession session = mongoClient.startSession();
        try {
            session.startTransaction();

            Bson accountFilter = eq("login", login);
            AccountEnt account = accountMongoCollection.find(session, accountFilter).first();


            Bson gameFilter = Filters.eq("_id", gameId);
            GameEnt game = gameMongoCollection.find(session, gameFilter).first();


            RentEnt rent = new RentEnt(startDate, endDate, account, game, 0);
            rent.setId(UUID.randomUUID());

            if (!markAccountAsRented(session, rent.getAccount().getId())) {
                session.abortTransaction();
                throw new ClientNotAvailableForRentRepositoryException("Client is not available for rent or does not exist.");
            }

            if (!markGameAsRented(session, rent.getGame().getId())) {
                session.abortTransaction();
                throw new GameNotAvailableForRentRepositoryException("Game is not available for rent or does not exist.");
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
    public Optional<RentEnt> findById(Object id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(rentMongoCollection.find(filter).first());
    }

    @Override
    public List<RentEnt> findAll() {
        return StreamSupport.stream(rentMongoCollection.find().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public RentEnt update(RentEnt updatedEntity) {
        Object id = updatedEntity.getId();
        Bson filter = Filters.eq("_id", id.toString());
        return rentMongoCollection.replaceOne(filter, updatedEntity).wasAcknowledged() ? updatedEntity : null;
    }

    @Override
    public void deleteById(Object id) throws
            GameNotAvailableForRentRepositoryException,
            ClientRentCancellationRepositoryException,
            RentNotFoundRepositoryException {
        RentEnt rent = findById(id).orElseThrow(() -> new RentNotFoundRepositoryException("Rent not found"));

        ClientSession session = mongoClient.startSession();
        try {
            session.startTransaction();
            if (!unmarkAccountAsRented(session, rent.getAccount().getId())) {
                throw new ClientNotAvailableForRentRepositoryException("Client is not available for rent or does not exist.");
            }

            if (!unmarkGameAsRented(session, rent.getGame().getId())) {
                throw new GameNotAvailableForRentRepositoryException("Game is not available for rent or does not exist.");
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

    public List<RentEnt> getRentsByAccountId(UUID clientId) {
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
