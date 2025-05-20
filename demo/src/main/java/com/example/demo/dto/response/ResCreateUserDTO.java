package com.example.demo.dto.response;

import com.example.demo.util.Enum.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
    private CompanyUser companyUser;

    @Getter
    @Setter
    public static class CompanyUser{
        private long id;
        private String name;
    }
}
