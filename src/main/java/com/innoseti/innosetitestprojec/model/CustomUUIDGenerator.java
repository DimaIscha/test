package com.innoseti.innosetitestprojec.model;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;

public class CustomUUIDGenerator implements IdentifierGenerator {
    @Override
    public String generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        return UUID.randomUUID().toString();
    }
}
