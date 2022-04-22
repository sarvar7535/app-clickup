package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Category;
import pdp.uz.entity.Project;
import pdp.uz.entity.Space;
import pdp.uz.entity.Status;
import pdp.uz.enums.StatusType;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.StatusDto;
import pdp.uz.model.StatusEditDto;
import pdp.uz.repository.CategoryRepo;
import pdp.uz.repository.ProjectRepo;
import pdp.uz.repository.SpaceRepo;
import pdp.uz.repository.StatusRepo;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepo statusRepo;

    private final SpaceRepo spaceRepo;

    private final ProjectRepo projectRepo;

    private final CategoryRepo categoryRepo;

    public ApiResponse get(UUID id) {
        Optional<Status> optionalStatus = statusRepo.findById(id);
        return optionalStatus.map(status -> new ApiResponse("OK", true, status)).orElseGet(() -> new ApiResponse("Status not found", false));
    }

    public ApiResponse getByCategory(UUID categoryId) {
        if (!categoryRepo.existsById(categoryId)){
            return new ApiResponse("Category not found", false);
        }
        return new ApiResponse("OK", true, statusRepo.findAllByCategoryId(categoryId));
    }

    public ApiResponse create(StatusDto dto) {
        if (statusRepo.existsByNameAndSpaceId(dto.getName(), dto.getSpaceId())) {
            return new ApiResponse("Status already created!", false);
        }
        Optional<Space> optionalSpace = spaceRepo.findById(dto.getSpaceId());
        if (!optionalSpace.isPresent()) {
            return new ApiResponse("Space not found", false);
        }
        Optional<Project> optionalProject = projectRepo.findById(dto.getProjectId());
        if (!optionalProject.isPresent()) {
            return new ApiResponse("Project not found", false);
        }
        Optional<Category> optionalCategory = categoryRepo.findById(dto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ApiResponse("Category not found", false);
        }
        Category category = optionalCategory.get();
        Project project = optionalProject.get();
        Space space = optionalSpace.get();
        Status status = new Status(dto.getName(), dto.getColor(), space, project, category, StatusType.CUSTOM);
        statusRepo.save(status);
        return new ApiResponse("Created", true);
    }

    public ApiResponse edit(UUID id, StatusEditDto dto) {
        Optional<Status> optionalStatus = statusRepo.findById(id);
        if (!optionalStatus.isPresent()) {
            return new ApiResponse("Status not found", false);
        }
        Status status = optionalStatus.get();
        if (statusRepo.existsByNameAndSpaceIdAndIdNot(dto.getName(), status.getSpace().getId(), id)) {
            return new ApiResponse("Status has already existed", false);
        }
        status.setName(dto.getName());
        status.setColor(dto.getColor());
        statusRepo.save(status);
        return new ApiResponse("Updated", true);
    }

    public ApiResponse delete(UUID id) {
        if (!statusRepo.existsById(id)){
            return new ApiResponse("Status not found", false);
        }
        statusRepo.deleteById(id);
        return new ApiResponse("Delete", true);
    }


}
