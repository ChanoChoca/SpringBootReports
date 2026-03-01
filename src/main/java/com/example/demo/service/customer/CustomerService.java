package com.example.demo.service.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.domain.dto.CustomerRequestDTO;
import com.example.demo.domain.dto.CustomerResponseDTO;
import com.example.demo.search.SearchCriteria;

public interface CustomerService {
    
    Page<CustomerResponseDTO> getAll(Pageable pageable);

    Page<CustomerResponseDTO> search(List<SearchCriteria> filters, Pageable pageable);

    CustomerResponseDTO getById(Long id);

    CustomerResponseDTO create(CustomerRequestDTO dto);

    CustomerResponseDTO update(Long id, CustomerRequestDTO dto);

    void delete(Long id);
} 
