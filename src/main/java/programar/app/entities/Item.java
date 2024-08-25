package programar.app.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "items", schema = "comercio_muebles")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "currency_id")
    private String currencyId;
    private String description;

    @Column(name = "picture_url")
    private String pictureUrl;
    private String title;
    private int quantity;

    @Column(name = "unit_price")
    private Double unitPrice;
    private Boolean entregado;

    public Double calcularImporte(){
        return quantity * unitPrice;
    }
}
