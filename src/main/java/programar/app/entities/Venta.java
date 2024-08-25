package programar.app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@ToString(exclude = "factura")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "venta", schema = "comercio_muebles")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToOne(mappedBy = "venta")
    private Transaccion transaccion;

    private String preferenceId; // Añade este campo

    @ManyToMany
    @JoinTable(
            name = "item_venta",
            joinColumns = @JoinColumn(name = "venta_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;
    private Boolean entregado;
    private Boolean isValido;
    private Boolean pagado;
    private String codigo;
    private LocalDate fechaDeCompra;

    @OneToOne(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Factura factura;

    private Double total;

    @PrePersist
    public void prePersist() {
        fechaDeCompra = LocalDate.now();
    }

    public void setGranTotal() {
        total = 0.0;
        for (Item item : items) {
            Double precioConDescuento = item.getUnitPrice() * (1 - item.getDiscount() / 100.0);
            total += precioConDescuento * item.getQuantity();
        }
    }
}
