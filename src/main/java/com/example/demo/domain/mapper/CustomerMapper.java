package com.example.demo.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.domain.dto.CustomerReportDTO;
import com.example.demo.domain.dto.CustomerRequestDTO;
import com.example.demo.domain.dto.CustomerResponseDTO;
import com.example.demo.domain.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Customer toEntity(CustomerRequestDTO dto);

    CustomerResponseDTO toResponseDTO(Customer entity);

    @Mapping(target = "fullName",
             expression = "java(entity.getFirstName() + \" \" + entity.getLastName())")
    @Mapping(target = "createdAt", 
         expression = "java(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : \"Undated\")")
    CustomerReportDTO toReportDTO(Customer entity);

    List<CustomerReportDTO> toReportDTO(List<Customer> customers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(CustomerRequestDTO dto, @MappingTarget Customer entity);
}