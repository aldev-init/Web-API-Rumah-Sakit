package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "obat")
@Table(name = "obat")
@Getter
@Setter
public class Obat extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @Column(name = "nama_obat")
    private String namaObat;

    @Column(name = "produksi")
    private String Produksi;

    @Column(name = "obat_kategori")
    private String obatKategori;

    @Column(name = "deskripsi")
    private String Deskripsi;
}
