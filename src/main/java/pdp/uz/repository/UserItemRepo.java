package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.ChecklistItemUser;

import java.util.UUID;

@Repository
public interface UserItemRepo extends JpaRepository<ChecklistItemUser, UUID> {

    boolean existsByUserIdAndCheckListItemId(UUID userId, UUID itemId);

    void deleteByUserIdAndCheckListItemId(UUID userId, UUID itemId);
}
