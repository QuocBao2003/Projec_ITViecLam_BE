package com.example.demo.dto.response.file;


import com.example.demo.util.annotation.ApiMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResUploadFileDTO {
    private String fileName;
    private Instant uploadedAt;
}
