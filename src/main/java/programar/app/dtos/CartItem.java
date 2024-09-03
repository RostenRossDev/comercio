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
    private String img;
    private Long id;
    private String price;
    private Integer discount;
    private int quantity;
    private int stock;
}
