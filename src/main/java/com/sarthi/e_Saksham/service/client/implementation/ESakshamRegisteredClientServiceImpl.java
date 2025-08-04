package com.sarthi.e_Saksham.service.client.implementation;

import com.sarthi.e_Saksham.repository.client.ESakshamRegisteredClientRepository;
import com.sarthi.e_Saksham.service.client.ESakshamRegisteredClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ESakshamRegisteredClientServiceImpl implements ESakshamRegisteredClientService {
    private static final Logger log = LoggerFactory.getLogger(ESakshamRegisteredClientServiceImpl.class);

    private final ESakshamRegisteredClientRepository eSakshamRegisteredClientRepository;

    public ESakshamRegisteredClientServiceImpl(ESakshamRegisteredClientRepository eSakshamRegisteredClientRepository) {
        this.eSakshamRegisteredClientRepository = eSakshamRegisteredClientRepository;
    }

    @Override
    public List<String> getAllClientDomainNames() {
        log.info("Inside getAllClientDomainNames Method, trying to Fetch all Client Domain names");
        return this.eSakshamRegisteredClientRepository.getAllClientDomainNames();
    }
}
