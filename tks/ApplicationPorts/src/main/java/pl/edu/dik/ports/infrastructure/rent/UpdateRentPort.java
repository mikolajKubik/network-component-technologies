package pl.edu.dik.ports.infrastructure.rent;

import pl.edu.dik.domain.model.rent.Rent;

public interface UpdateRentPort {

    Rent update(Rent rent);

}
