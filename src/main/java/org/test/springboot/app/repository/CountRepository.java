package org.test.springboot.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.springboot.app.model.Bank;
import org.test.springboot.app.model.Count;

import java.util.Optional;

public interface CountRepository extends JpaRepository<Count, Long> {

    Optional<Count> findByPerson(String person);

}
