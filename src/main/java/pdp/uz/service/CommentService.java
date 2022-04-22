package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Comment;
import pdp.uz.entity.Task;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.CommentDto;
import pdp.uz.repository.CommentRepo;
import pdp.uz.repository.TaskRepo;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;

    private final TaskRepo taskRepo;

    public ApiResponse get(UUID id) {
        Optional<Comment> optionalComment = commentRepo.findById(id);
        return optionalComment.map(comment -> new ApiResponse("Ok", true, comment)).orElseGet(() -> new ApiResponse("Comment not found", false));
    }

    public ApiResponse getByTask(UUID taskId) {
        if (!taskRepo.existsById(taskId)){
            return new ApiResponse("Task not found", false);
        }
        return new ApiResponse("OK", true, commentRepo.findAllByTaskId(taskId));
    }

    public ApiResponse addComment(CommentDto dto) {
        Optional<Task> optionalTask = taskRepo.findById(dto.getTaskId());
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Task not found", false);
        }
        Task task = optionalTask.get();
        Comment comment = new Comment(dto.getText(), task);
        commentRepo.save(comment);
        return new ApiResponse("Created", true);
    }

    public ApiResponse editComment(UUID id, String text) {
        Optional<Comment> optionalComment = commentRepo.findById(id);
        if (!optionalComment.isPresent()) {
            return new ApiResponse("Comment not found", false);
        }
        Comment comment = optionalComment.get();
        comment.setText(text);
        commentRepo.save(comment);
        return new ApiResponse("Updated", true);
    }


    public ApiResponse delete(UUID id) {
        if (!commentRepo.existsById(id)) {
            return new ApiResponse("Comment not found", false);
        }
        commentRepo.deleteById(id);
        return new ApiResponse("Deleted", true);
    }
}
