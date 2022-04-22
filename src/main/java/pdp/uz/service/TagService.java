package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Tag;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.TagEditDto;
import pdp.uz.repository.TagRepo;
import pdp.uz.repository.WorkspaceRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepo tagRepo;

    private final WorkspaceRepository workspaceRepository;


    public ApiResponse get(Long workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            return new ApiResponse("Workspace not found", false);
        }
        return new ApiResponse("OK", true, tagRepo.findAllByWorkspaceId(workspaceId));
    }

    public ApiResponse editTag(UUID tagId, TagEditDto dto) {
        Optional<Tag> optionalTag = tagRepo.findById(tagId);
        if (!optionalTag.isPresent()) {
            return new ApiResponse("Tag not found", false);
        }
        Tag tag = optionalTag.get();
        tag.setName(dto.getName());
        tag.setColor(dto.getColor());
        tagRepo.save(tag);
        return new ApiResponse("Updated", true);
    }

    public ApiResponse deleteTag(UUID tagId) {
        if (!tagRepo.existsById(tagId)) {
            return new ApiResponse("Tag not found", false);
        }
        tagRepo.deleteById(tagId);
        return new ApiResponse("Deleted", true);
    }
}
