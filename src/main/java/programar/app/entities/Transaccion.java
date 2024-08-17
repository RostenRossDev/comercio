package programar.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaccion", schema = "comercio_muebles")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long collectionId;
    private String collectionStatus;
    private Long paymentId;
    private String status;
    private String externalReference;
    private String paymentType;
    private Long merchantOrderId;
    private String preferenceId;
    private String siteId;
    private String processingMode;
    private String merchantAccountId;

    @OneToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;
}
