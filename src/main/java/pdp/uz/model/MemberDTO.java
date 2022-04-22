package pdp.uz.model;

import lombok.Data;
import pdp.uz.enums.AddType;

import java.util.UUID;

@Data
public class MemberDTO {
    private UUID id;

    private UUID roleId;

    private AddType addType;//ADD, EDIT, REMOVE
}
