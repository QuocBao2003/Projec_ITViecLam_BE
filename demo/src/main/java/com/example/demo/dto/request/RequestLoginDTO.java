package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestLoginDTO {
    @NotBlank(message = "username không được rỗng")
    private String username;

    @NotBlank(message = "password không được rỗng")
    private String password;
}
