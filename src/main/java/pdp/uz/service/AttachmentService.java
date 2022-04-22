package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import pdp.uz.entity.Attachment;
import pdp.uz.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final Path root = Paths.get("C:\\file");


    public void download(String fileName, HttpServletResponse response) {
        Attachment attachment = attachmentRepository.findByName(fileName);

        response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getOriginalName() + "\"");
        response.setContentType(attachment.getContentType());

        Path filePath = root.resolve(fileName).normalize();

        try {
            FileCopyUtils.copy(new FileInputStream(filePath.toString()), response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
