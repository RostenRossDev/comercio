package programar.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product", schema = "comercio_muebles")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String img;
    private Integer stock;

    @Column(name = "real_stock")
    private Integer realStock;
    private Double price;
    private Boolean enabled;
    private String tag;
    private String material;
    private Integer sale;
    private Integer bestSeller;
    @Version
    private Integer version; // Columna de versión
}
