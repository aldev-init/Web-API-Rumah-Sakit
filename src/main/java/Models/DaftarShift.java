package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "daftar_shift")
@Table(name = "daftar_shift")
@Getter
@Setter
public class DaftarShift extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @Column(name = "kategori")
    private String Kategori;

    //sementara
    @ManyToOne
    @JoinColumn(name = "perawat_id")
    private Perawat perawatId;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staffId;

    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

}
