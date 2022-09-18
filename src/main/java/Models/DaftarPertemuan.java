package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "daftar_pertemuan")
@Table(name = "daftar_pertemuan")
@Getter
@Setter
public class DaftarPertemuan extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @OneToMany
    @Column(name = "pasien_id")
    private List<Pasien> pasienId;

    @OneToMany
    @Column(name = "dokter_id")
    private List<Dokter> dokterId;

    @Column(name = "kategori")
    private String Kategori;

    @Column(name = "deskripsi")
    private String Deskripsi;

    @Column(name = "tanggal")
    private LocalDateTime Tanggal;
}
