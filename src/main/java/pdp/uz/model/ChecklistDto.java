package pdp.uz.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Getter
public class ChecklistDto {

    @NotNull
    private String name;

    @NotNull
    private UUID taskId;
}
