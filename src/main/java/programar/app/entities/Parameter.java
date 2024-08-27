package programar.app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="parameters", schema = "comercio_muebles")
public class Parameter {

    private Long id;
    private String name;
    private String defaultValue;
    private Integer dataType;
    private String actualValue;
}
