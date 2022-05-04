package org.test.springboot.app.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.springboot.app.model.Bank;
import org.test.springboot.app.model.Count;
import org.test.springboot.app.repository.BankRepository;
import org.test.springboot.app.repository.CountRepository;
import org.test.springboot.app.service.CountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class CountServiceImpl implements CountService {

    private CountRepository countRepository;
    private BankRepository bankRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Count> findAll() {
        return countRepository.findAll();
    }

    @Override
    @Transactional
    public Count save(Count count) {
        return countRepository.save(count);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Count> findById(Long id) {
        return countRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public int checkTotalTransfers(Long bankId) {
        return bankRepository.findById(bankId).orElseThrow().getTotalTransference();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal checkAmount(Long id) {
        return countRepository.findById(id).orElseThrow().getAmount();
    }

    @Override
    @Transactional
    public void transfer(Long originCountId, Long destinyCountId, BigDecimal amount, Long bankId) {

        Optional<Count> originCountOptional = countRepository.findById(originCountId);
        Count originCount = originCountOptional.orElseThrow();
        originCount.debit(amount);
        countRepository.save(originCount);

        Optional<Count> destinyCountOptional = countRepository.findById(destinyCountId);
        Count destinyCount = destinyCountOptional.orElseThrow();
        destinyCount.credit(amount);
        countRepository.save(destinyCount);

        Optional<Bank> bankOptional = bankRepository.findById(bankId);
        Bank bank = bankOptional.orElseThrow();
        Integer totalTransfers = bank.getTotalTransference();
        bank.setTotalTransference(++totalTransfers);
        bankRepository.save(bank);

    }
}
