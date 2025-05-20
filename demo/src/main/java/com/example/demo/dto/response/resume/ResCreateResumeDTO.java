package com.example.demo.dto.response.resume;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResCreateResumeDTO {
    private long id;
    private Instant createdAt;
    private String createdBy;
}
