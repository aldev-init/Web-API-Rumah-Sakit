package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "staff")
@Table(name = "staff")
@Getter
@Setter
public class Staff extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @Column(name = "nama_lengkap")
    private String namaLengkap;

    @Column(name = "gender")
    private String Gender;

    @Column(name = "posisi")
    private String Posisi;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "gaji")
    private long Gaji;

    @Column(name = "email")
    private String Email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private String Status;
}
