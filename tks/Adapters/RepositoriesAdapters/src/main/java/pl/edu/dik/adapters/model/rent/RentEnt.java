package pl.edu.dik.adapters.model.rent;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.edu.dik.adapters.model.AbstractEntityEnt;
import pl.edu.dik.adapters.model.account.AccountEnt;
import pl.edu.dik.adapters.model.game.GameEnt;

import java.time.LocalDate;
import java.util.UUID;

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

    public RentEnt(UUID id, LocalDate startDate, LocalDate endDate, AccountEnt account, GameEnt game, int rentalPrice) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.account = account;
        this.game = game;
        this.rentalPrice = rentalPrice;
    }
}
