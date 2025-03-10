package pl.edu.dik.ports.infrastructure.rent;

import pl.edu.dik.domain.model.rent.Rent;

import java.time.LocalDate;
import java.util.UUID;

public interface CreateRentPort {

    Rent save(LocalDate startDate, LocalDate endDate, String login, UUID gameId);

}
