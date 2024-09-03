package com.projectx.company_service.services;

import com.projectx.company_service.entity.CompanyDetails;
import com.projectx.company_service.exceptions.AlreadyExistsException;
import com.projectx.company_service.exceptions.InvalidDataException;
import com.projectx.company_service.exceptions.ResourceNotFoundException;
import com.projectx.company_service.payloads.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CompanyService {
    Boolean addUpdate(CompanyDto dto) throws ResourceNotFoundException,
            AlreadyExistsException, InvalidDataException, IOException, ParseException;
    EditCompanyDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityNameAndValueDto> getCompanyDocumentTypeDropDown();
    CompanyPageResponseDto getAllCompanies(PageRequestDto dto);
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityIdAndValueDto> getCompanyDropDown();
    Integer getCompanyCount();
    CompanyDetails getCompanyDetailsById(Long companyId);
    String getCompanyName(EntityIdDto dto)throws ResourceNotFoundException;
    CompanyInfoDto getCompanyInfoById(EntityIdDto dto)throws ResourceNotFoundException;
}
