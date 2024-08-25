package programar.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "venta")
@Entity
@Table(name = "factura", schema = "comercio_muebles")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision = LocalDate.now();

    @OneToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;
}