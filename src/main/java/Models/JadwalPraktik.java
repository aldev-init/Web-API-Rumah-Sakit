package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "jadwal_praktik")
@Table(name = "jadwal_praktik")
@Getter
@Setter
public class JadwalPraktik extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false,name = "id")
    private long id;

    @Column(name = "hari")
    private String Hari;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "deskripsi")
    private String Deskripsi;

    @ManyToOne
    @JoinColumn(name = "dokter_id")
    private Dokter dokterId;
}
