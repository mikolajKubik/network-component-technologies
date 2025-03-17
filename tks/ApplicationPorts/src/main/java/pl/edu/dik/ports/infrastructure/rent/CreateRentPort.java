package pl.edu.dik.ports.infrastructure.rent;

import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.exception.business.ClientNotAvailableForRentException;
import pl.edu.dik.ports.exception.business.GameNotAvailableForRentException;

import java.time.LocalDate;
import java.util.UUID;

public interface CreateRentPort {

    Rent save(LocalDate startDate, LocalDate endDate, String login, UUID gameId) throws ClientNotAvailableForRentException, GameNotAvailableForRentException;

}
