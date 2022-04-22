package pdp.uz.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Getter
public class ChecklistItemDto {

    @NotBlank
    private String name;

    @NotNull
    private UUID checklistId;
}
