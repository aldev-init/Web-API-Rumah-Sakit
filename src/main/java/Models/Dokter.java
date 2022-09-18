package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "dokter")
@Table(name = "dokter")
@Getter
@Setter
public class Dokter extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @Column(name = "nama_lengkap")
    private String namaLengkap;

    @Column(name = "is_spesialis")
    private boolean isSpesialis;

    @Column(name = "spesialis_nama")
    private String spesialisNama;

    @Column(name = "email")
    private String Email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private String Status;

    @Column(name = "gaji")
    private long Gaji;
}
