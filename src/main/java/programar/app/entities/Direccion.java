package programar.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "direccion", schema = "comercio_muebles")
public class Direccion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "calle_id")
    private Calle calle;

    @ManyToOne
    @JoinColumn(name = "altura_id")
    private Altura altura;

    @ManyToOne
    @JoinColumn(name = "barrio_id")
    private Barrio barrio;

    // Otros campos si es necesario, como un campo de referencia o descripción adicional

    // Getters y setters
}