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
public class TaskAttachment extends AbsUUIDEntity {

    @ManyToOne
    private Task task;

    @ManyToOne
    private Attachment attachment;

    @Column
    private boolean pinCoverImage = false;

    public TaskAttachment(Task task, Attachment attachment) {
        this.task = task;
        this.attachment = attachment;
    }
}
