package com.sarthi.e_Saksham.repository.client;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.List;

public interface ESakshamRegisteredClientRepository extends RegisteredClientRepository {
    List<String> getAllClientDomainNames();
}
