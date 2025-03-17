package pl.edu.dik.ports.service;

import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.exception.business.RentNotFoundException;

import java.util.List;
import java.util.UUID;

public interface InactiveRentService {

    Rent findInactiveRentById(UUID id) throws RentNotFoundException;

    List<Rent> getAllInactiveRents();

    List<Rent> getInactiveRentsByClientId(UUID clientId);
}
