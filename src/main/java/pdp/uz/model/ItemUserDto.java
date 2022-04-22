package pdp.uz.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ItemUserDto {

    private UUID itemId;

    private UUID userId;
}
