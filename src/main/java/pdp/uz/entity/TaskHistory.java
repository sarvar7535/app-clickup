package pdp.uz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pdp.uz.entity.template.AbsUUIDEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TaskHistory extends AbsUUIDEntity {

    @ManyToOne
    private Task task;

    @Column
    private String changeFieldName;

    @Column
    private String before;

    @Column
    private String after;

    @Column
    private String data;
}
