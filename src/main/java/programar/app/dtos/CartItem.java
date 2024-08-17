package programar.app.dtos;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    private String name;
    private Long id;
    private String price;
    private Integer discount;
    private int quantity;
    private int stock;
}
