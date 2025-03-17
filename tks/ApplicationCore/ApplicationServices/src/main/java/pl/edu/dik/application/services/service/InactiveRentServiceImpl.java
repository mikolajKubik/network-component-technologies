package pl.edu.dik.application.services.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.exception.business.RentNotFoundException;
import pl.edu.dik.ports.infrastructure.inactiveRent.ReadInactiveRentPort;
import pl.edu.dik.ports._interface.InactiveRentService;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InactiveRentServiceImpl implements InactiveRentService {

    private final ReadInactiveRentPort readInactiveRentPort;

    @Override
    public Rent findInactiveRentById(UUID id) throws RentNotFoundException {
        return readInactiveRentPort.findById(id)
                .orElseThrow(() -> new RentNotFoundException("Rent with ID " + id + " not found"));
    }

    @Override
    public List<Rent> getAllInactiveRents() {
        return readInactiveRentPort.findAll();
    }


    @Override
    public List<Rent> getInactiveRentsByClientId(UUID clientId) {
        return readInactiveRentPort.getRentsByAccountId(clientId);
    }
}
