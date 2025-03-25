package pl.edu.dik.ports._interface;

import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.exception.business.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentService {

    Rent createRent(LocalDate startDate, LocalDate endDate, String login, UUID gameId) throws
            StartDateBeforeEndDateException,
            GameNotAvailableForRentException,
            ClientNotAvailableForRentException;

    Rent findRentById(UUID id) throws RentNotFoundException;

    List<Rent> getAllRents();

    List<Rent> getActiveRentsByClientId(UUID clientId);

    void endRent(UUID id) throws
            RentNotFoundException,
            GameRentCancellationException,
            ClientRentCancellationException;

    Rent updateRent(Rent rent);
}
