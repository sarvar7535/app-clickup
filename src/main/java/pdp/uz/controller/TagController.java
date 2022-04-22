package pdp.uz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.TagEditDto;
import pdp.uz.service.TagService;

import java.util.UUID;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/workspace/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") Long workspaceId) {
        ApiResponse apiResponse = tagService.get(workspaceId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editTag(@PathVariable(value = "id") UUID tagId, @RequestBody TagEditDto dto) {
        ApiResponse apiResponse = tagService.editTag(tagId, dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable(value = "id") UUID tagId) {
        ApiResponse apiResponse = tagService.deleteTag(tagId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
