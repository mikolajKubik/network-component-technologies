package pl.edu.dik.ports.infrastructure.rent;

import pl.edu.dik.domain.model.rent.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadRentPort {

    Optional<Rent> findById(Object id);

    List<Rent> findAll();

    List<Rent> getRentsByAccountId(UUID clientId);

}
