package pdp.uz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pdp.uz.enums.WorkspacePermissionName;
import pdp.uz.entity.template.AbsUUIDEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProjectUser extends AbsUUIDEntity {

    @ManyToOne
    private User member;

    @ManyToOne
    private Project project;

    @Enumerated(EnumType.STRING)
    private WorkspacePermissionName permission;
}
