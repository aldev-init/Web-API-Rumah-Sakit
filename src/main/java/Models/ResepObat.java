package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "resep_obat")
@Table(name = "resep_obat")
@Getter
@Setter
public class ResepObat extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "pertemuan_id")
    private DaftarPertemuan pertemuanId;

    @OneToMany
    @Column(name = "obat_id")
    private List<Obat> obatId;

    @Column(name = "dosis")
    private String Dosis;

    @Column(name = "deskripsi")
    private String Deskripsi;
}
