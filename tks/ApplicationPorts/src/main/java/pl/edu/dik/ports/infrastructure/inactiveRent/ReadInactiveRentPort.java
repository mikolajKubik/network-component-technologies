package pl.edu.dik.ports.infrastructure.inactiveRent;

import pl.edu.dik.domain.model.rent.Rent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadInactiveRentPort {

    Optional<Rent> findById(UUID id);

    List<Rent> findAll();

    List<Rent> getRentsByAccountId(UUID clientId);
}
