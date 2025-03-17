package pl.edu.dik.ports.infrastructure.rent;

import pl.edu.dik.ports.exception.business.ClientRentCancellationException;
import pl.edu.dik.ports.exception.business.GameRentCancellationException;
import pl.edu.dik.ports.exception.business.RentNotFoundException;

import java.util.UUID;

public interface DeleteRentPort {

    void deleteById(UUID id) throws ClientRentCancellationException, GameRentCancellationException, RentNotFoundException;
}
