package org.example.opp_cw.dto.userdetails;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@NoArgsConstructor
public abstract class Password {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9@_*.]+$", message = "invalid password")
    @Size(min = 8)
    private String password;

    public Password(String password) {
        setPassword(password);
    }

    public void setPassword(@NotNull @Pattern(regexp = "^[a-zA-Z0-9@_*.]+$", message = "invalid password") @Size(min = 8) String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        this.password = encoder.encode(password);
    }
}
