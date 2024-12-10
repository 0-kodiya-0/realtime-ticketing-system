package org.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.backend.enums.CustomerTypes;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDto extends Dto {
    private CustomerTypes type;
}
