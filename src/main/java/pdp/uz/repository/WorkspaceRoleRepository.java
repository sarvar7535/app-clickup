package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pdp.uz.entity.WorkspaceRole;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRoleRepository extends JpaRepository<WorkspaceRole, UUID> {
    boolean existsByNameAndWorkspaceId(String name, Long workspace_id);

    Optional<WorkspaceRole> findByIdAndWorkspaceId(UUID id, Long workspace_id);
}
