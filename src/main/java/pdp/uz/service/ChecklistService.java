package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.CheckList;
import pdp.uz.entity.Task;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.ChecklistDto;
import pdp.uz.repository.ChecklistRepo;
import pdp.uz.repository.TaskRepo;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepo checklistRepo;

    private final TaskRepo taskRepo;


    public ApiResponse getChecklistByTask(UUID taskId) {
        if (!taskRepo.existsById(taskId)) {
            return new ApiResponse("Task not found", false);
        }
        return new ApiResponse("OK", true, checklistRepo.findAllByTaskId(taskId));
    }

    public ApiResponse create(ChecklistDto dto) {
        Optional<Task> optionalTask = taskRepo.findById(dto.getTaskId());
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Task not found", false);
        }
        CheckList checkList = new CheckList(dto.getName(), optionalTask.get());
        checklistRepo.save(checkList);
        return new ApiResponse("Created", true);
    }

    public ApiResponse edit(UUID id, String name) {
        Optional<CheckList> optionalCheckList = checklistRepo.findById(id);
        if (!optionalCheckList.isPresent()) {
            return new ApiResponse("Checklist not found", false);
        }
        CheckList checkList = optionalCheckList.get();
        checkList.setName(name);
        checklistRepo.save(checkList);
        return new ApiResponse("Updated", true);
    }

    public ApiResponse delete(UUID id) {
        if (!checklistRepo.existsById(id)){
            return new ApiResponse("Checklist not found", false);
        }
        checklistRepo.deleteById(id);
        return new ApiResponse("Deleted", true);
    }
}
