package org.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.backend.enums.CustomerTypes;

/**
 * Define the values that need to be saved in the files and send through http requests.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDto extends Dto {
    private CustomerTypes type;
}
