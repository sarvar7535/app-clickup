package pdp.uz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pdp.uz.entity.template.AbsUUIDEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimeTracked extends AbsUUIDEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Task task;

    @Column
    private Timestamp startedAt;

    @Column
    private Timestamp stoppedAt;
}
