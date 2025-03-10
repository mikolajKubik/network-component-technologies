//package pl.edu.dik.application.services.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import pl.edu.dik.tks.exception.business.GameNotFoundException;
//import pl.edu.dik.tks.exception.business.GameRentedException;
//import pl.edu.dik.tks.exception.business.IncorrectPlayerNumberException;
//import pl.edu.dik.tks.model.game.Game;
//import pl.edu.dik.tks.repository.game.MongoGameRepository;
//import pl.edu.dik.tks.repository.rent.RentRepository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class GameService {
//    private final MongoGameRepository gameRepository;
//    private final RentRepository rentRepository;
//
//
//    public Game createGame(Game game) throws IncorrectPlayerNumberException {
//        if (game.getMaxPlayers() < game.getMinPlayers()) {
//            throw new IncorrectPlayerNumberException("Max players cannot be less than min players");
//        }
//        return gameRepository.save(game);
//    }
//
//    public Game findGameById(UUID id) throws GameNotFoundException {
//        return gameRepository.findById(id)
//                .orElseThrow(() -> new GameNotFoundException("Game with ID " + id + " not found"));
//    }
//
//    public List<Game> getAllGames() {
//        return gameRepository.findAll();
//    }
//
//    public Game updateGame(Game game) throws GameNotFoundException {
//        Game foundGame = gameRepository.findById(game.getId())
//                .orElseThrow(() -> new GameNotFoundException("Game with ID " + game.getId() + " not found"));
//
//        if (!game.getName().equals(foundGame.getName())) {
//            foundGame.setName(game.getName());
//        }
//        if (game.getPricePerDay() != foundGame.getPricePerDay()) {
//            foundGame.setPricePerDay(game.getPricePerDay());
//        }
//
//        return gameRepository.update(foundGame);
//    }
//
//    public void deleteGameById(UUID id) throws GameRentedException, GameNotFoundException {
//        Game game = gameRepository.findById(id)
//                .orElseThrow(() -> new GameNotFoundException("Game with ID " + id + " not found"));
//
//        if (game.getRentalStatusCount() != 0) {
//            throw new GameRentedException("Game with ID " + id + " is currently rented.");
//        }
//        gameRepository.deleteById(game.getId());
//    }
//}
