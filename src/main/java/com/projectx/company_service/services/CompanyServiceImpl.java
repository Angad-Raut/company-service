package com.projectx.company_service.services;

import com.projectx.company_service.entity.CompanyDetails;
import com.projectx.company_service.exceptions.AlreadyExistsException;
import com.projectx.company_service.exceptions.InvalidDataException;
import com.projectx.company_service.exceptions.ResourceNotFoundException;
import com.projectx.company_service.payloads.*;
import com.projectx.company_service.repository.CompanyRepository;
import com.projectx.company_service.utils.CompanyUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    @Override
    public Boolean addUpdate(CompanyDto dto) throws ResourceNotFoundException,
            AlreadyExistsException, InvalidDataException, IOException, ParseException {
        CompanyDetails companyDetails = null;
        if (dto.getId()==null) {
              isCompanyExists(dto.getCompanyName());
            companyDetails = CompanyDetails.builder()
                    .companyName(dto.getCompanyName())
                    .companyAddress(dto.getCompanyAddress())
                    .companyLogo(dto.getCompanyLogo().getBytes())
                    .status(true)
                    .startDate(CompanyUtils.getISODate(dto.getStartDate()))
                    .endDate(dto.getEndDate()!=null?CompanyUtils.getISODate(dto.getEndDate()):null)
                    .build();
        } else {
            companyDetails = companyRepository.getById(dto.getId());
            if (!dto.getCompanyName().equals(companyDetails.getCompanyName())) {
                isCompanyExists(dto.getCompanyName());
                companyDetails.setCompanyName(dto.getCompanyName());
            }
            if (!dto.getCompanyAddress().equals(companyDetails.getCompanyAddress())) {
                companyDetails.setCompanyAddress(dto.getCompanyAddress());
            }
            if (dto.getCompanyLogo()!=null) {
                if (!dto.getCompanyLogo().getBytes().equals(companyDetails.getCompanyLogo())) {
                    companyDetails.setCompanyLogo(dto.getCompanyLogo().getBytes());
                }
            }
            if (dto.getStartDate()!=null && companyDetails.getStartDate()!=null) {
                if (!dto.getStartDate().equals(CompanyUtils.toExpenseDate(companyDetails.getStartDate()))) {
                    companyDetails.setStartDate(CompanyUtils.getISODate(dto.getStartDate()));
                }
            }
            if (dto.getEndDate()!=null && companyDetails.getEndDate()!=null) {
                if (!dto.getEndDate().equals(CompanyUtils.toExpenseDate(companyDetails.getEndDate()))) {
                    companyDetails.setEndDate(CompanyUtils.getISODate(dto.getEndDate()));
                }
            } else if (dto.getEndDate()!=null && companyDetails.getEndDate()==null) {
                companyDetails.setEndDate(CompanyUtils.getISODate(dto.getEndDate()));
            } else if (dto.getEndDate()==null && companyDetails.getEndDate()!=null) {
                companyDetails.setEndDate(null);
            }
        }
        try {
            return companyRepository.save(companyDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        } catch (RuntimeException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public EditCompanyDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            CompanyDetails companyDetails = companyRepository.getById(dto.getEntityId());
            if (companyDetails==null) {
                throw new ResourceNotFoundException(CompanyUtils.COMPANY_DETAILS_NOT_EXISTS);
            }
            return EditCompanyDto.builder()
                    .id(companyDetails.getId())
                    .companyName(companyDetails.getCompanyName())
                    .companyAddress(companyDetails.getCompanyAddress())
                    .companyLogo(companyDetails.getCompanyLogo())
                    .startDate(CompanyUtils.toExpenseDate(companyDetails.getStartDate()))
                    .endDate(companyDetails.getEndDate()!=null?CompanyUtils.toExpenseDate(companyDetails.getEndDate()):CompanyUtils.DASH)
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<EntityNameAndValueDto> getCompanyDocumentTypeDropDown() {
        List<EntityNameAndValueDto> responseList = new ArrayList<>();
        responseList.add(new EntityNameAndValueDto(CompanyUtils.SALARY_TYPE,"Salary Slip"));
        responseList.add(new EntityNameAndValueDto(CompanyUtils.FORM_16_TYPE,"Form 16"));
        responseList.add(new EntityNameAndValueDto(CompanyUtils.OFFER_LETTER_TYPE,"Offer Letter"));
        responseList.add(new EntityNameAndValueDto(CompanyUtils.EXPERIENCE_LETTER_TYPE,"Experience Letter"));
        responseList.add(new EntityNameAndValueDto(CompanyUtils.SERVICE_LETTER_TYPE,"Service Letter"));
        responseList.add(new EntityNameAndValueDto(CompanyUtils.APPOINTMENT_LETTER_TYPE,"Appointment Letter"));
        return responseList;
    }

    @Override
    public CompanyPageResponseDto getAllCompanies(PageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("companyName")) {
            sortParameter = "company_name";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("startDate")) {
            sortParameter = "start_date";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("endDate")){
            sortParameter = "end_date";
        } else {
            sortParameter = "id";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<CompanyDetails> companies = companyRepository.getAllCompaniesPages(pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<CompanyDetails> listOfCompanies = companies.getContent();
        List<ViewCompanyDto> companiesList = !listOfCompanies.isEmpty()?listOfCompanies.stream()
                .map(data -> ViewCompanyDto.builder()
                        .srNo(index.incrementAndGet())
                        .companyId(data.getId())
                        .companyName(data.getCompanyName()!=null?data.getCompanyName():CompanyUtils.DASH)
                        .startDate(data.getStartDate()!=null?CompanyUtils.toExpenseDate(data.getStartDate()):CompanyUtils.DASH)
                        .endDate(data.getEndDate()!=null?CompanyUtils.toExpenseDate(data.getEndDate()):CompanyUtils.DASH)
                        .build()).toList()
                :new ArrayList<>();
        return !companiesList.isEmpty()?CompanyPageResponseDto.builder()
                .pageNo(companies.getNumber())
                .pageSize(companies.getSize())
                .totalPages(companies.getTotalPages())
                .totalElements(companies.getTotalElements())
                .content(companiesList)
                .build():new CompanyPageResponseDto();
    }

    @Override
    public Boolean updateStatus(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            CompanyDetails companyDetails = companyRepository.getById(dto.getEntityId());
            if (companyDetails==null) {
                throw new ResourceNotFoundException(CompanyUtils.COMPANY_DETAILS_NOT_EXISTS);
            }
            Boolean status = companyDetails.getStatus()?false:true;
            Integer count = companyRepository.updateStatus(dto.getEntityId(),status);
            return count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<EntityIdAndValueDto> getCompanyDropDown() {
        List<Object[]> fetchList = companyRepository.getCompanyDropDown();
        return fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> EntityIdAndValueDto.builder()
                        .entityId(data[0]!=null?Long.parseLong(data[0].toString()):null)
                        .entityValue(data[1]!=null?data[1].toString():null)
                        .build())
                .toList():new ArrayList<>();
    }

    @Override
    public Integer getCompanyCount() {
        Integer count = companyRepository.getCompanyCount();
        return count!=null?count:0;
    }

    @Override
    public CompanyDetails getCompanyDetailsById(Long companyId) {
        return companyRepository.getById(companyId);
    }

    @Override
    public String getCompanyName(EntityIdDto dto) {
        try{
            return companyRepository.getCompanyName(dto.getEntityId());
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public CompanyInfoDto getCompanyInfoById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            CompanyDetails details = companyRepository.getById(dto.getEntityId());
            if (details==null){
                throw new ResourceNotFoundException(CompanyUtils.COMPANY_DETAILS_NOT_EXISTS);
            }
            return CompanyInfoDto.builder()
                    .companyId(details.getId())
                    .companyName(details.getCompanyName())
                    .companyAddress(details.getCompanyAddress())
                    .startDate(details.getStartDate()!=null?details.getStartDate():null)
                    .endDate(details.getEndDate()!=null?details.getEndDate():null)
                    .build();
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(CompanyUtils.COMPANY_DETAILS_NOT_EXISTS);
        }
    }

    private void isCompanyExists(String companyName) {
        if (companyRepository.existsCompanyDetailsByCompanyName(companyName)) {
            throw new AlreadyExistsException(CompanyUtils.COMPANY_DETAILS_ALREADY_EXISTS);
        }
    }
}
