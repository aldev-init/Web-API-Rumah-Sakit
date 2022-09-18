package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "user_premission")
@Table(name = "user_premission")
@Getter
@Setter
public class UserPremission extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userId;

    @Column(name = "name")
    private String Name;
}
