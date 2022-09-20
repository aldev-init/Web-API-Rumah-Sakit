package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "daftar_shift_hari")
@Table(name = "daftar_shift_hari")
@Getter
@Setter
public class DaftarShiftHari extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "daftar_shift_id")
    private DaftarShift daftarShiftId;

    @Column(name = "hari")
    private String Hari;
}
