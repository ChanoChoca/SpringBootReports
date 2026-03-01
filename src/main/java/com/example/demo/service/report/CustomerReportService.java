package com.example.demo.service.report;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.dto.CustomerReportDTO;
import com.example.demo.domain.entity.Customer;
import com.example.demo.domain.mapper.CustomerMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.search.SearchCriteria;
import com.example.demo.specification.GenericSpecificationBuilder;

@Service
@Transactional(readOnly = true)
public class CustomerReportService
        extends AbstractReportService<Customer, CustomerReportDTO> {

    private static final Logger log = LoggerFactory.getLogger(CustomerReportService.class);
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerReportService(CustomerRepository repository,
                                 CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected List<Customer> fetchData(List<SearchCriteria> filters) {

        log.debug("Fetching data for Customer report with {} filters",
            filters != null ? filters.size() : 0);

        GenericSpecificationBuilder<Customer> builder =
            new GenericSpecificationBuilder<>();

        filters.forEach(builder::with);

        Specification<Customer> spec = builder.build();

        List<Customer> result = repository.findAll(spec);

        log.debug("Customer report query returned {} records", result.size());

        return result;
    }

    @Override
    protected List<CustomerReportDTO> mapToReportDTO(List<Customer> data) {
        return mapper.toReportDTO(data);
    }
}