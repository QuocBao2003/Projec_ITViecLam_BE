package com.example.demo.dto.response;

import com.example.demo.util.Enum.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updateAt;
    private CompanyUser companyUser;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyUser{
        private long id;
        private String name;
    }
}
