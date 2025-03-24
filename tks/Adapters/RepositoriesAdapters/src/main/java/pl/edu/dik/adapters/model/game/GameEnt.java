package pl.edu.dik.adapters.model.game;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.edu.dik.adapters.model.AbstractEntityEnt;

import java.util.UUID;

@Getter @Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GameEnt extends AbstractEntityEnt {

    @BsonProperty("name")
    private String name;

    @BsonProperty("price_per_day")
    private int pricePerDay;

    @BsonProperty("rental_status_count")
    private int rentalStatusCount;

    @BsonProperty("min_players")
    private int minPlayers;

    @BsonProperty("max_players")
    private int maxPlayers;

    public GameEnt(UUID id, String name, int pricePerDay, int rentalStatusCount, int minPlayers, int maxPlayers) {
        super(id);
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.rentalStatusCount = rentalStatusCount;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }
}
