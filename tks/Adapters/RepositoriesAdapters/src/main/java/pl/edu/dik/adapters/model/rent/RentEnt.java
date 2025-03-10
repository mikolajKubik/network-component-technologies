package pl.edu.dik.adapters.model.rent;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.edu.dik.adapters.model.AbstractEntityEnt;
import pl.edu.dik.adapters.model.account.AccountEnt;
import pl.edu.dik.adapters.model.game.GameEnt;

import java.time.LocalDate;

@Getter @Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RentEnt extends AbstractEntityEnt {

    @BsonProperty("start_date")
    private LocalDate startDate;

    @BsonProperty("end_date")
    private LocalDate endDate;

    @BsonProperty("account")
    private AccountEnt account;

    @BsonProperty("game")
    private GameEnt game;

    @BsonProperty("rental_price")
    private int rentalPrice;
}
