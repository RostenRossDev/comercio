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
@Table(name="parameters", schema = "comercio_muebles")
public class Parameter implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "data_type")
    private Integer dataType;

    @Column(name = "actual_value")
    private String actualValue;
}
