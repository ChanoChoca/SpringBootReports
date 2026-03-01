package com.example.demo.service.customer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.dto.CustomerRequestDTO;
import com.example.demo.domain.dto.CustomerResponseDTO;
import com.example.demo.domain.entity.Customer;
import com.example.demo.domain.mapper.CustomerMapper;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.search.SearchCriteria;
import com.example.demo.specification.GenericSpecificationBuilder;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    public CustomerServiceImpl(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> getAll(Pageable pageable) {

        log.debug("Fetching customers page: {}", pageable);

        return repository.findAllByDeletedFalse(pageable)
                .map(mapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> search(
        List<SearchCriteria> filters,
        Pageable pageable) {
    
        log.debug("Searching customers with filters: {}", filters);
    
        GenericSpecificationBuilder<Customer> builder =
            new GenericSpecificationBuilder<>();

        filters.forEach(builder::with);

        Specification<Customer> spec = builder.build();
    
        return repository.findAll(spec, pageable)
                .map(mapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getById(Long id) {

        log.debug("Fetching customer with ID: {}", id);

        Customer customer = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new ResourceNotFoundException("Customer not found");
                });

        return mapper.toResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO create(CustomerRequestDTO dto) {

        log.info("Creating customer with email: {}", dto.email());

        if (repository.existsByEmailAndDeletedFalse(dto.email())) {
            log.warn("Attempt to create customer with existing email: {}", dto.email());
            throw new BusinessException("Email already exists");
        }

        Customer customer = mapper.toEntity(dto);
        Customer saved = repository.save(customer);

        log.debug("Customer created with ID: {}", saved.getId());

        return mapper.toResponseDTO(saved);
    }

    @Override
    public CustomerResponseDTO update(Long id, CustomerRequestDTO dto) {

        log.info("Updating customer with ID: {}", id);

        Customer customer = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new ResourceNotFoundException("Customer not found");
                });

        mapper.updateEntityFromDto(dto, customer);

        return mapper.toResponseDTO(repository.save(customer));
    }

    @Override
    public void delete(Long id) {

        log.info("Soft deleting customer with ID: {}", id);

        Customer customer = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Customer not found for delete, ID: {}", id);
                    return new ResourceNotFoundException("Customer not found");
                });

        customer.setDeleted(true);
        repository.save(customer);
    }
}
