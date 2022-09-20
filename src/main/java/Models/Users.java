package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "user")
@Table(name = "user")
@Getter
@Setter
public class Users extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @Column(name = "name")
    private String Name;

    @Column(name = "username")
    private String Username;

    @Column(name = "password")
    private String Password;

    @Column(name = "email")
    private String Email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "user_type")
    private String userType;


}
