package pl.edu.dik.application.services.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.exception.business.*;
import pl.edu.dik.ports.infrastructure.rent.CreateRentPort;
import pl.edu.dik.ports.infrastructure.rent.DeleteRentPort;
import pl.edu.dik.ports.infrastructure.rent.ReadRentPort;
import pl.edu.dik.ports.infrastructure.rent.UpdateRentPort;
import pl.edu.dik.ports.service.RentService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentServiceImpl implements RentService {

    private final CreateRentPort createRentPort;
    private final ReadRentPort readRentPort;
    private final UpdateRentPort updateRentPort;
    private final DeleteRentPort deleteRentPort;

    public Rent createRent(LocalDate startDate, LocalDate endDate, String login, UUID gameId) throws
            StartDateBeforeEndDateException,
            GameNotAvailableForRentException,
            ClientNotAvailableForRentException {

        if (!startDate.isBefore(endDate)) {
            throw new StartDateBeforeEndDateException("Start date must be before end date.");
        }
        return createRentPort.save(startDate, endDate, login, gameId);
    }

    public Rent findRentById(UUID id) throws RentNotFoundException {
        return readRentPort.findById(id)
                .orElseThrow(() -> new RentNotFoundException("Rent with ID " + id + " not found"));
    }

    public List<Rent> getAllRents() {
        return readRentPort.findAll();
    }

    public List<Rent> getActiveRentsByClientId(UUID clientId) {
        return readRentPort.getRentsByAccountId(clientId);
    }

    public void endRent(UUID id) throws
            RentNotFoundException,
            GameRentCancellationException,
            ClientRentCancellationException {
        deleteRentPort.deleteById(id);
    }

    public Rent updateRent(Rent rent) {
        return updateRentPort.update(rent);
    }
}
