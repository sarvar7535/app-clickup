package pdp.uz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pdp.uz.entity.template.AbsUUIDEntity;
import pdp.uz.enums.AccessType;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "space_id"}))
public class Project extends AbsUUIDEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne
    private Space space;

    @Column
    private boolean archived = false;

    @Enumerated(EnumType.STRING)
    private AccessType accessType;

    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<Category> categories;
}
