package org.internship.jpaonlinebanking.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.internship.jpaonlinebanking.annotations.ValidPassword;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateRequest {
    @ValidPassword
    private String password;
}
