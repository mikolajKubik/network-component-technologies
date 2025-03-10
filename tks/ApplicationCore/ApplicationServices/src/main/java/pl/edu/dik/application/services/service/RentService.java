//package pl.edu.dik.application.services.service;
//
//import com.mongodb.client.ClientSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import pl.edu.dik.tks.exception.business.*;
//import pl.edu.dik.tks.model.rent.Rent;
//import pl.edu.dik.tks.repository.rent.MongoRentRepository;
//import pl.edu.dik.tks.repository.rent.RentRepository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class RentService {
//
//    private final MongoRentRepository mongoRentRepository;
//
//    public Rent createRent(LocalDate startDate, LocalDate endDate, String login, UUID gameId) throws StartDateBeforeEndDateException, AccountNotActiveException, GameNotAvailableForRentException, ClientNotAvailableForRentException {
//
//        if (!startDate.isBefore(endDate)) {
//            throw new StartDateBeforeEndDateException("Start date must be before end date.");
//        }
//
//        return mongoRentRepository.save(startDate, endDate, login, gameId);
//    }
//
//    public Rent findRentById(UUID id) throws RentNotFoundException {
//        return mongoRentRepository.findById(id)
//                .orElseThrow(() -> new RentNotFoundException("Rent with ID " + id + " not found"));
//    }
//
//    public List<Rent> getAllRents() {
//        return mongoRentRepository.findAll();
//    }
//
//    public List<Rent> getActiveRentsByClientId(UUID clientId) {
//        return mongoRentRepository.getRentsByAccountId(clientId);
//    }
//
//    public void endRent(UUID id) throws GameNotAvailableForRentException, ClientNotAvailableForRentException, RentNotFoundException {
//        mongoRentRepository.deleteById(id);
//    }
//}
