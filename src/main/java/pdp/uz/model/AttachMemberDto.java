package pdp.uz.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class AttachMemberDto {
   private List<UUID> members;
}
