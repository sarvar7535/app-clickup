package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Category;
import pdp.uz.entity.Project;
import pdp.uz.entity.Space;
import pdp.uz.entity.Status;
import pdp.uz.enums.StatusType;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.CategoryDto;
import pdp.uz.repository.CategoryRepo;
import pdp.uz.repository.ProjectRepo;
import pdp.uz.repository.SpaceRepo;
import pdp.uz.repository.StatusRepo;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;

    private final ProjectRepo projectRepo;

    private final StatusRepo statusRepo;

    private final SpaceRepo spaceRepo;

    public ApiResponse get(UUID id) {
        Optional<Category> optionalCategory = categoryRepo.findById(id);
        return optionalCategory.map(category -> new ApiResponse("OK", true, category)).orElseGet(() -> new ApiResponse("Category not found", false));
    }

    public ApiResponse getByProject(UUID projectId) {
        if (!projectRepo.existsById(projectId)) {
            return new ApiResponse("Project not found", false);
        }
        return new ApiResponse("OK", true, categoryRepo.findAllByProjectId(projectId));
    }


    public ApiResponse create(CategoryDto dto) {
        if (categoryRepo.existsByNameAndProjectId(dto.getName(), dto.getProjectId())) {
            return new ApiResponse("Category has already existed", false);
        }
        Optional<Project> optionalProject = projectRepo.findById(dto.getProjectId());
        if (!optionalProject.isPresent()) {
            return new ApiResponse("Project not found", false);
        }
        Optional<Space> optionalSpace = spaceRepo.findById(dto.getSpaceId());
        if (!optionalSpace.isPresent()) {
            return new ApiResponse("Space not found", false);
        }
        Space space = optionalSpace.get();
        Project project = optionalProject.get();
        Category category = new Category(dto.getName(), project);
        statusRepo.save(new Status(
                "TO DO",
                "gray",
                space,
                project,
                category,
                StatusType.OPEN
        ));
        statusRepo.save(new Status(
                "Completed",
                "green",
                space,
                project,
                category,
                StatusType.CLOSED
        ));
        return new ApiResponse("Created", true);
    }

    public ApiResponse edit(UUID id, CategoryDto dto) {
        if (categoryRepo.existsByNameAndProjectIdAndIdNot(dto.getName(), dto.getProjectId(), id)) {
            return new ApiResponse("Category has already existed", false);
        }
        Optional<Category> optionalCategory = categoryRepo.findById(id);
        if (!optionalCategory.isPresent()) {
            return new ApiResponse("Category not found", false);
        }
        Optional<Project> optionalProject = projectRepo.findById(dto.getProjectId());
        if (!optionalProject.isPresent()) {
            return new ApiResponse("Project not found", false);
        }
        Project project = optionalProject.get();
        Category category = optionalCategory.get();
        category.setName(dto.getName());
        category.setProject(project);
        categoryRepo.save(category);
        return new ApiResponse("Updated", true);
    }

    public ApiResponse delete(UUID id) {
        if (!categoryRepo.existsById(id)) {
            return new ApiResponse("Category not found", false);
        }
        categoryRepo.deleteById(id);
        return new ApiResponse("Deleted", true);
    }
}
