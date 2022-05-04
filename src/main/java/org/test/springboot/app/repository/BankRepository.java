package org.test.springboot.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.springboot.app.model.Bank;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {

}
