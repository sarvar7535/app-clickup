package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.*;

import pdp.uz.enums.AccessType;
import pdp.uz.enums.StatusType;
import pdp.uz.enums.WorkspacePermissionName;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.AttachMemberPermissionDto;
import pdp.uz.model.ProjectDto;
import pdp.uz.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepo projectRepo;

    private final SpaceUserRepo spaceUserRepo;

    private final SpaceRepo spaceRepo;

    private final ProjectUserRepo projectUserRepo;

    private final UserRepository userRepository;

    private final StatusRepo statusRepo;

    public ApiResponse create(ProjectDto dto) {
        if (projectRepo.existsByNameAndSpaceId(dto.getName(), dto.getSpaceId())) {
            return new ApiResponse("Project has already existed", false);
        }
        Optional<Space> optionalSpace = spaceRepo.findById(dto.getSpaceId());
        if (!optionalSpace.isPresent()) {
            return new ApiResponse("Space not found", false);
        }
        Space space = optionalSpace.get();
        Project project = new Project();
        List<Category> categories = new ArrayList<>();
        for (String list : dto.getLists()) {
            Category category = new Category(list, project);
            categories.add(category);
        }

        project.setColor(dto.getColor());
        project.setCategories(categories);
        project.setAccessType(dto.getAccessType());
        project.setSpace(space);
        project.setName(dto.getName());
        Project savedProject = projectRepo.save(project);
        for (Category category : savedProject.getCategories()) {
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
        }
        if (dto.getAccessType().equals(AccessType.PUBLIC)) {
            for (SpaceUser spaceUser : spaceUserRepo.findAllBySpaceId(dto.getSpaceId())) {
                for (WorkspacePermissionName value : WorkspacePermissionName.values()) {
                    projectUserRepo.save(new ProjectUser(spaceUser.getMember(), savedProject, value));
                }
            }
        } else {
            for (WorkspacePermissionName value : WorkspacePermissionName.values()) {
                projectUserRepo.save(new ProjectUser(User.getCurrentUser(), savedProject, value));
            }
        }
        return new ApiResponse("Created", true);
    }

    public ApiResponse attachMember(UUID id, AttachMemberPermissionDto dto) {
        Optional<Project> optionalProject = projectRepo.findById(id);
        if (!optionalProject.isPresent()) {
            return new ApiResponse("Project not found", false);
        }
        Project project = optionalProject.get();
        List<ProjectUser> projectUsers = new ArrayList<>();
        dto.getMemberPermission().forEach((uuid, permissionNames) -> {
                    if (spaceUserRepo.existsByMemberIdAndSpaceId(uuid, project.getSpace().getId())) {
                        for (WorkspacePermissionName permissionName : permissionNames) {
                            projectUsers.add(new ProjectUser(userRepository.getById(uuid), project, permissionName));
                        }
                    }
                }
        );
        projectUserRepo.saveAll(projectUsers);
        return new ApiResponse("OK", true);
    }

    public ApiResponse deleteMember(UUID id, UUID memberId) {
        if (projectUserRepo.existsByProjectIdAndMemberId(id, memberId)) {
            projectUserRepo.deleteAllByProjectIdAndMemberId(id, memberId);
        }
        return new ApiResponse("Deleted", true);
    }
}
