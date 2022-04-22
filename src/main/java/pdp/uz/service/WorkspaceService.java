package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pdp.uz.entity.*;
import pdp.uz.enums.AddType;
import pdp.uz.enums.WorkspacePermissionName;
import pdp.uz.enums.WorkspaceRoleName;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.MemberDTO;
import pdp.uz.model.WorkspaceDTO;
import pdp.uz.repository.*;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    private final AttachmentRepository attachmentRepository;

    private final WorkspaceUserRepository workspaceUserRepository;

    private final WorkspaceRoleRepository workspaceRoleRepository;

    private final WorkspacePermissionRepository workspacePermissionRepository;

    private final UserRepository userRepository;

    public ApiResponse getWorkspaceMembers(Long id) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
        if (!optionalWorkspace.isPresent()) {
            return new ApiResponse("Workspace not found", false);
        }
        List<User> members = new ArrayList<>();
        for (WorkspaceRoleName value : WorkspaceRoleName.values()) {
            if (value != WorkspaceRoleName.ROLE_GUEST) {
                List<WorkspaceUser> workspaceUsers = workspaceUserRepository.findAllByWorkspaceIdAndWorkspaceRole_Name(id, value.name());
                workspaceUsers.forEach(workspaceUser -> members.add(workspaceUser.getUser()));
            }
        }
        return new ApiResponse("OK", true, members);
    }

    public ApiResponse getWorkspaceGuests(Long id) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
        if (!optionalWorkspace.isPresent()) {
            return new ApiResponse("Workspace not found", false);
        }
        List<User> guests = new ArrayList<>();
        List<WorkspaceUser> workspaceUsers =
                workspaceUserRepository.findAllByWorkspaceIdAndWorkspaceRole_Name(id, WorkspaceRoleName.ROLE_GUEST.name());
        workspaceUsers.forEach(workspaceUser -> guests.add(workspaceUser.getUser()));
        return new ApiResponse("OK", true, guests);
    }


    public ApiResponse getUserWorkspaces(UUID userId) {
        return new ApiResponse("OK", true, workspaceRepository.findAllByOwnerId(userId));
    }


    public ApiResponse addWorkspace(WorkspaceDTO dto, User user) {

        if (workspaceRepository.existsByOwnerIdAndName(user.getId(), dto.getName())) {
            return new ApiResponse("The office name already exists", false);
        }
        Workspace workspace = new Workspace(
                dto.getName(),
                dto.getColor(),
                user,
                dto.getAvatarId() == null ? null : attachmentRepository.findById(dto.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("attachment"))
        );
        workspaceRepository.save(workspace);

        WorkspaceRole ownerRole = workspaceRoleRepository.save(new WorkspaceRole(
                workspace,
                WorkspaceRoleName.ROLE_OWNER.name(),
                null
        ));
        WorkspaceRole adminRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_ADMIN.name(), null));
        WorkspaceRole memberRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_MEMBER.name(), null));
        WorkspaceRole guestRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_GUEST.name(), null));

        WorkspacePermissionName[] workspacePermissionNames = WorkspacePermissionName.values();
        List<WorkspacePermission> workspacePermissions = new ArrayList<>();

        for (WorkspacePermissionName workspacePermissionName : workspacePermissionNames) {
            WorkspacePermission workspacePermission = new WorkspacePermission(
                    ownerRole,
                    workspacePermissionName);
            workspacePermissions.add(workspacePermission);
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)) {
                workspacePermissions.add(new WorkspacePermission(
                        adminRole,
                        workspacePermissionName));
            }
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_MEMBER)) {
                workspacePermissions.add(new WorkspacePermission(
                        memberRole,
                        workspacePermissionName));
            }
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_GUEST)) {
                workspacePermissions.add(new WorkspacePermission(
                        guestRole,
                        workspacePermissionName));
            }
        }

        workspacePermissionRepository.saveAll(workspacePermissions);

        workspaceUserRepository.save(new WorkspaceUser(
                workspace,
                user,
                ownerRole,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())

        ));

        return new ApiResponse("The office was saved", true);
    }

    public ApiResponse editWorkspace(Long id, WorkspaceDTO dto) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
        if (!optionalWorkspace.isPresent()) {
            return new ApiResponse("Workspace not found", false);
        }
        Workspace workspace = optionalWorkspace.get();
        if (workspaceRepository.existsByOwnerIdAndNameAndIdNot(workspace.getOwner().getId(), dto.getName(), id)){
            return new ApiResponse("The office name already exists", false);
        }

        workspace.setAvatar(dto.getAvatarId() == null ? null : attachmentRepository.findById(dto.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("attachment")));
        workspace.setColor(dto.getColor());
        workspace.setName(dto.getName());
        workspaceRepository.save(workspace);
        return new ApiResponse("Updated", true);
    }

    public ApiResponse changeOwnerWorkspace(Long id, UUID ownerId) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
        if (!optionalWorkspace.isPresent()) {
            return new ApiResponse("Workspace not found", false);
        }
        Workspace workspace = optionalWorkspace.get();
        Optional<User> optionalUser = userRepository.findById(ownerId);
        if (!optionalUser.isPresent())
            return new ApiResponse("User not found", false);
        workspace.setOwner(optionalUser.get());
        workspaceRepository.save(workspace);
        return new ApiResponse("Owner edited", false);
    }

    public ApiResponse deleteWorkspace(Long id) {
        try {
            workspaceRepository.deleteById(id);
            return new ApiResponse("Deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    public ApiResponse addOrEditOrRemoveWorkspace(Long id, MemberDTO dto) {
        if (dto.getAddType().equals(AddType.ADD)) {
            WorkspaceUser workspaceUser = new WorkspaceUser(
                    workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id")),
                    userRepository.findById(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                    workspaceRoleRepository.findById(dto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                    new Timestamp(System.currentTimeMillis()),
                    null
            );
            workspaceUserRepository.save(workspaceUser);

        } else if (dto.getAddType().equals(AddType.EDIT)) {
            WorkspaceUser workspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, dto.getId()).orElseGet(WorkspaceUser::new);
            workspaceUser.setWorkspaceRole(workspaceRoleRepository.findById(dto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")));
            workspaceUserRepository.save(workspaceUser);
        } else if (dto.getAddType().equals(AddType.REMOVE)) {
            workspaceUserRepository.deleteByWorkspaceIdAndUserId(id, dto.getId());
        }
        return new ApiResponse("Success", true);
    }

    public ApiResponse joinToWorkspace(Long id, User user) {
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, user.getId());
        if (optionalWorkspaceUser.isPresent()) {
            WorkspaceUser workspaceUser = optionalWorkspaceUser.get();
            workspaceUser.setDateJoined(new Timestamp(System.currentTimeMillis()));
            workspaceUserRepository.save(workspaceUser);
            return new ApiResponse("Success", true);
        }
        return new ApiResponse("Error", false);
    }
}
