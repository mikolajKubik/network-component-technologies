package pl.edu.dik.tks.repository.rent;

import pl.edu.dik.tks.exception.business.ClientNotAvailableForRentException;
import pl.edu.dik.tks.exception.business.GameNotAvailableForRentException;
import pl.edu.dik.tks.exception.business.RentNotFoundException;
import pl.edu.dik.tks.model.rent.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentRepository {

    Rent save(LocalDate startDate, LocalDate endDate, String login, UUID gameId) throws
            ClientNotAvailableForRentException,
            GameNotAvailableForRentException;

    Optional<Rent> findById(Object id);

    List<Rent> findAll();

    Rent update(Rent rent);

    void deleteById(Object id) throws GameNotAvailableForRentException, ClientNotAvailableForRentException, RentNotFoundException;

}
