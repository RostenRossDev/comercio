package programar.app.entities;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Data
@Entity
@Table(name="users", schema = "comercio_muebles")
public class CustomUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length=30,unique=true)
    private String username;

    @Column(length=60)
    private String password;

    //	@Column(unique=true)
    private Boolean  enabled;

    private String email;

    private String image;

    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="USER_ID")
    private List<Role> roles;
}