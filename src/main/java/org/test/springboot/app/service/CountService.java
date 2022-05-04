package org.test.springboot.app.service;

import org.test.springboot.app.model.Count;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CountService {

    List<Count> findAll();
    Optional<Count> findById(final Long id);
    Count save(final Count count);
    int checkTotalTransfers(final Long bankId);
    BigDecimal checkAmount(final Long id);
    void transfer(final Long originCountId, final Long destinyCountId, final BigDecimal amount, final Long bankId);

}
