package pdp.uz.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class StatusEditDto {

    @NotBlank
    private String name;

    @NotNull
    private String color;

}
