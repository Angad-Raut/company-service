package com.projectx.company_service.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditCompanyDto {
    private Long id;
    private String companyName;
    private String companyAddress;
    private byte[] companyLogo;
    private String startDate;
    private String endDate;
}
