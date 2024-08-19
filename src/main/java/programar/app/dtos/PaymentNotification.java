package programar.app.dtos;

import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotification {
    private Long id;
    private Boolean live_mode;
    private String type;
    private Date date_created; //": "2015-03-25T10:04:58.396-04:00",
    private Long user_id; //": 44444,
    private String api_version; //": "v1",
    private String action; //": "payment.created",
    private Object data; //": {
        //"id": "999999999"
    //}
}
