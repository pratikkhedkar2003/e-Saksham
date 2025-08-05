package com.sarthi.e_Saksham.repository.client;

import com.sarthi.e_Saksham.model.client.ESakshamRegisteredClient;

import java.util.List;

public interface ESakshamRegisteredClientRepository {

    void save(ESakshamRegisteredClient registeredClient);

    ESakshamRegisteredClient findById(String id);

    ESakshamRegisteredClient findByClientId(String clientId);

    List<String> getAllClientDomainNames();
}
