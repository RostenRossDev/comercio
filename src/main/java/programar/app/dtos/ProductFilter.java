package programar.app.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductFilter {
    private Long id;
    private String name;
    private String tag;
    private String material;
    private String price;
    private Integer stock;
    private Integer sale;
    private Boolean enabled;
}
