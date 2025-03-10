package pl.edu.dik.adapters.model.game;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.edu.dik.adapters.model.AbstractEntityEnt;

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
}
