package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.CheckListItem;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChecklistItemRepo extends JpaRepository<CheckListItem, UUID> {

    List<CheckListItem> findAllByCheckListId(UUID checklistId);
}
