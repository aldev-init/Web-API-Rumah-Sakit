package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "daftar_rawat_inap")
@Table(name = "daftar_rawat_inap")
@Getter
@Setter
public class DaftarRawatInap extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "pasien_id")
    private Pasien pasienId;

    @ManyToOne
    @JoinColumn(name = "ruang_inap_id")
    private RuangInap ruangInapId;

    @ManyToOne
    @JoinColumn(name = "dokter_id")
    private Dokter dokterId;

    @ManyToOne
    @JoinColumn(name = "perawat_satu_id")
    private Perawat perawatSatuId;

    @ManyToOne
    @JoinColumn(name = "perawat_dua_id")
    private Perawat perawatDuaId;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "is_checkout")
    private boolean isCheckout;
}
