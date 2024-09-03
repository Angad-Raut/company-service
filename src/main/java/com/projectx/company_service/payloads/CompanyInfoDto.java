package com.projectx.company_service.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyInfoDto {
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private Date startDate;
    private Date endDate;
}
