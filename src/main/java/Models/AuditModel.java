package Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/*
    anotasi ini digunakan agar property / colom sql dapat diturunkan ke anak class
 */
@MappedSuperclass
@Getter
@Setter
public class AuditModel extends PanacheEntityBase {
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}
