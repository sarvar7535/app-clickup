package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.CheckList;
import pdp.uz.entity.CheckListItem;
import pdp.uz.entity.ChecklistItemUser;
import pdp.uz.entity.User;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.ChecklistItemDto;
import pdp.uz.model.ItemUserDto;
import pdp.uz.repository.ChecklistItemRepo;
import pdp.uz.repository.ChecklistRepo;
import pdp.uz.repository.UserItemRepo;
import pdp.uz.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChecklistItemService {

    private final ChecklistItemRepo checklistItemRepo;

    private final ChecklistRepo checklistRepo;

    private final UserRepository userRepository;

    private final UserItemRepo userItemRepo;

    public ApiResponse getByChecklist(UUID checklistId) {
        if (!checklistRepo.existsById(checklistId)) {
            return new ApiResponse("Checklist not found", false);
        }
        return new ApiResponse("OK", true, checklistItemRepo.findAllByCheckListId(checklistId));
    }

    public ApiResponse create(ChecklistItemDto dto) {
        Optional<CheckList> optionalCheckList = checklistRepo.findById(dto.getChecklistId());
        if (!optionalCheckList.isPresent()) {
            return new ApiResponse("Checklist not found", false);
        }
        CheckList checkList = optionalCheckList.get();
        CheckListItem item = new CheckListItem(dto.getName(), checkList, false);
        checklistItemRepo.save(item);
        return new ApiResponse("Created", true);
    }

    public ApiResponse edit(UUID id, String name) {
        Optional<CheckListItem> optionalCheckListItem = checklistItemRepo.findById(id);
        if (!optionalCheckListItem.isPresent()) {
            return new ApiResponse("Item not found", false);
        }
        CheckListItem item = optionalCheckListItem.get();
        item.setName(name);
        checklistItemRepo.save(item);
        return new ApiResponse("Updated", true);
    }

    public ApiResponse resolve(UUID id) {
        Optional<CheckListItem> optionalCheckListItem = checklistItemRepo.findById(id);
        if (!optionalCheckListItem.isPresent()) {
            return new ApiResponse("Item not found", false);
        }
        CheckListItem item = optionalCheckListItem.get();
        item.setResolved(true);
        checklistItemRepo.save(item);
        return new ApiResponse("Resolved", true);
    }

    public ApiResponse delete(UUID id) {
        Optional<CheckListItem> optionalCheckListItem = checklistItemRepo.findById(id);
        if (!optionalCheckListItem.isPresent()) {
            return new ApiResponse("Item not found", false);
        }
        checklistItemRepo.deleteById(id);
        return new ApiResponse("Deleted", true);
    }

    public ApiResponse assign(ItemUserDto dto) {
        Optional<CheckListItem> optionalCheckListItem = checklistItemRepo.findById(dto.getItemId());
        if (!optionalCheckListItem.isPresent()) {
            return new ApiResponse("Item not found", false);
        }
        Optional<User> optionalUser = userRepository.findById(dto.getUserId());
        if (!optionalUser.isPresent()) {
            return new ApiResponse("User not found", false);
        }
        CheckListItem item = optionalCheckListItem.get();
        User user = optionalUser.get();
        userItemRepo.save(new ChecklistItemUser(item, user));
        return new ApiResponse("Assigned", true);
    }

    public ApiResponse removeUser(ItemUserDto dto) {
        if (!userItemRepo.existsByUserIdAndCheckListItemId(dto.getUserId(),dto.getItemId())){
            return new ApiResponse("Not found", false);
        }
        userItemRepo.deleteByUserIdAndCheckListItemId(dto.getUserId(),dto.getItemId());
        return new ApiResponse("Removed", true);
    }
}
