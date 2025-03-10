package pl.edu.dik.tks.dto.game;

import lombok.Data;

import java.util.UUID;

@Data
public class GameResponse {
    private String id;
    private String name;
    private int pricePerDay;
    private int minPlayers;
    private int maxPlayers;
    private int rentalStatusCount;
}
