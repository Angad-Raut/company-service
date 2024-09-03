package com.projectx.company_service.controller;

import com.projectx.company_service.exceptions.AlreadyExistsException;
import com.projectx.company_service.exceptions.InvalidDataException;
import com.projectx.company_service.exceptions.ResourceNotFoundException;
import com.projectx.company_service.payloads.*;
import com.projectx.company_service.services.CompanyService;
import com.projectx.company_service.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/companyDetails")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping(value = "/addUpdate",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseDto<Boolean>> addUpdate(
            @ModelAttribute @Valid CompanyDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = companyService.addUpdate(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistsException
                 | InvalidDataException | ParseException | IOException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<EditCompanyDto>> getById(
            @Valid @RequestBody EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            EditCompanyDto data = companyService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/updateStatus")
    public ResponseEntity<ResponseDto<Boolean>> updateStatus(
            @Valid @RequestBody EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = companyService.updateStatus(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getCompanyDocumentTypeDropDown")
    public ResponseEntity<ResponseDto<List<EntityNameAndValueDto>>> getCompanyDocumentTypeDropDown() {
        try {
            List<EntityNameAndValueDto> data = companyService.getCompanyDocumentTypeDropDown();
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (Exception e){
            return errorHandler.handleError(e);
        }
    }

    @GetMapping(value = "/getCompanyDropDown")
    public ResponseEntity<ResponseDto<List<EntityIdAndValueDto>>> getCompanyDropDown() {
        try {
            List<EntityIdAndValueDto> data = companyService.getCompanyDropDown();
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/getAllCompanies")
    public ResponseEntity<ResponseDto<CompanyPageResponseDto>> getAllCompanies(
            @Valid @RequestBody PageRequestDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            CompanyPageResponseDto data = companyService.getAllCompanies(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/getCompanyInfoById")
    public ResponseEntity<ResponseDto<CompanyInfoDto>> getCompanyInfoById(
            @Valid @RequestBody EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            CompanyInfoDto data = companyService.getCompanyInfoById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getCompanyName")
    public ResponseEntity<ResponseDto<String>> getCompanyName(
            @Valid @RequestBody EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            String data = companyService.getCompanyName(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,null,
                    null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
