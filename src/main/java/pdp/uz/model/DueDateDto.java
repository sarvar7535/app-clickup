package pdp.uz.model;

import lombok.Getter;
import lombok.Setter;
import pdp.uz.enums.AddType;

import java.sql.Timestamp;

@Setter
@Getter
public class DueDateDto {

    private Timestamp startedDate;

    private boolean startTimeHas = false;

    private boolean dueTimeHas = false;

    private Timestamp dueDate;

    private AddType addType;
}
