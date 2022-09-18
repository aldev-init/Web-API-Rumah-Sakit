package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "pasien")
@Table(name = "pasien")
@Getter
@Setter
public class Pasien extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @Column(name = "nama_lengkap")
    private String namaLengkap;

    @Column(name = "gender")
    private String Gender;

    @Column(name = "status")
    private String Status;

    @Column(name = "address")
    private String Address;

    @Column(name = "email")
    private String Email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_cover_bpjs")
    private boolean isCoverBpjs;
}
