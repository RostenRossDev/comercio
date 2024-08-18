package programar.app.dtos;

import lombok.*;
import programar.app.entities.Product;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmUpdateDeleteProductDTO {
    private List<Product> products;
    private List<Long> toDelete;
}
