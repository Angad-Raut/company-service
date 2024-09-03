package com.projectx.company_service.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDto {
    private Long id;
    @NotNull(message = "Please enter company name!!")
    private String companyName;
    @NotNull(message = "Please enter company address!!")
    private String companyAddress;
    @NotNull(message = "Please enter company logo!!")
    private MultipartFile companyLogo;
    @NotNull(message = "Please enter company start date!!")
    private String startDate;
    private String endDate;
}
