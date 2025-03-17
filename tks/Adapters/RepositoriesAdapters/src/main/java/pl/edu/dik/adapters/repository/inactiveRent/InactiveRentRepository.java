package pl.edu.dik.adapters.repository.inactiveRent;

import pl.edu.dik.adapters.model.rent.RentEnt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InactiveRentRepository {

    Optional<RentEnt> findById(UUID id);

    List<RentEnt> findAll();

    List<RentEnt> getRentsByAccountId(UUID clientId);

}
