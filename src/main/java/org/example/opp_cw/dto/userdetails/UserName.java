package org.example.opp_cw.dto.userdetails;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserName {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9@_*.]+$", message = "invalid username")
    @Size(min = 8)
    private String username;
}
