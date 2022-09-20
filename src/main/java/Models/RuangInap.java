package Models;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "ruang_inap")
@Table(name = "ruang_inap")
@Getter
@Setter
public class RuangInap extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @Column(name = "prefix_ruangan")
    private String prefixRuangan;

    @Column(name = "nomor_ruangan")
    private String nomorRuangan;

    @Column(name = "kategori_ruangan")
    private String kategoriRuangan;

    @Column(name = "is_kosong")
    private boolean isKosong;
}
