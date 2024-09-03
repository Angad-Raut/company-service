package com.projectx.company_service.repository;

import com.projectx.company_service.entity.CompanyDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyDetails,Long> {

    @Query(value = "select * from company_details where id=:companyId",nativeQuery = true)
    public CompanyDetails getById(@Param("companyId")Long companyId);
    Boolean existsCompanyDetailsByCompanyName(String companyName);

    @Transactional
    @Modifying
    @Query(value = "update company_details set status=:status where id=:companyId",nativeQuery = true)
    Integer updateStatus(@Param("companyId")Long companyId,@Param("status")Boolean status);

    @Query(value = "select * from company_details",nativeQuery = true)
    Page<CompanyDetails> getAllCompaniesPages(Pageable pageable);

    @Query(value = "select company_name from company_details where id=:companyId",nativeQuery = true)
    String getCompanyName(@Param("companyId")Long companyId);

    @Query(value = "select c.id,c.company_name from company_details c",nativeQuery = true)
    List<Object[]> getCompanyDropDown();
    @Query(value = "select count(*) from company_details",nativeQuery = true)
    Integer getCompanyCount();
}
