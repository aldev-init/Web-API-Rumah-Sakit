package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "riwayat_penyakit")
@Table(name = "riwayat_penyakit")
@Getter
@Setter
public class RiwayatPenyakit extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = true,name = "id")
    private long id;

    @OneToMany
    @Column(name = "pasien_id")
    private List<Pasien> pasienId;

    @Column(name = "nama")
    private String Nama;

    @Column(name = "deskripsi")
    private String Deskripsi;

    @Column(name = "awal_date")
    private LocalDateTime awalDate;

    @Column(name = "sembuh_date")
    private LocalDateTime sembuhDate;
}
