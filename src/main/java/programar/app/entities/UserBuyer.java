package programar.app.entities;

import lombok.*;
import programar.app.dtos.CartItem;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserBuyer {
    private List<CartItem> items;
}
