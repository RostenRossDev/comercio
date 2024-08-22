package programar.app.entities;

import lombok.*;
import programar.app.dtos.CartItem;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserBuyer {
    private List<CartItem> items;
    private String nombre;
    private String  apellido;
    private String  email;
    private String  calle;
    private Integer altura;
    private String  barrio;
    private String  casa;
    private String  departamento;
    private String  piso;
    private String  entreCalles;
    private String  telefono;
    private String  descripcion;
    private String  rangoEntrega;
}
