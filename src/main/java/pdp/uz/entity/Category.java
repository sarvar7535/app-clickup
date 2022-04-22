package pdp.uz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pdp.uz.entity.template.AbsUUIDEntity;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "project_id"}))
public class Category extends AbsUUIDEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Project project;

    @Column
    private boolean archived = false;

    @Transient
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private List<Status> statuses;

    public Category(String name, Project project) {
        this.name = name;
        this.project = project;
    }
}
