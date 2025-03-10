package pl.edu.dik.ports.service;

import pl.edu.dik.domain.model.rent.Rent;

import java.util.List;
import java.util.UUID;

public interface InactiveRentService {

    public Rent findInactiveRentById(UUID id);

    public List<Rent> getAllInactiveRents();

    public List<Rent> getInactiveRentsByClientId(UUID clientId);
}
