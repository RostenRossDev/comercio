package programar.app.dtos;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EntregaRequest {
    private Long itemId;
    private Long ventaId;
}
