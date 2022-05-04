package org.test.springboot.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.test.springboot.app.model.Count;
import org.test.springboot.app.repository.CountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
public class IntegrateJpaTest {

    @Autowired
    private CountRepository repository;

    @Test
    void testFindById_whenIdExists() {
        Optional<Count> countOptional = repository.findById(1L);

        assertTrue(countOptional.isPresent());
        assertEquals("Roger", countOptional.orElseThrow().getPerson());
    }

    @Test
    void testFindByPerson_whenPersonExists() {
        Optional<Count> countOptional = repository.findByPerson("Roger");

        assertTrue(countOptional.isPresent());
        assertEquals("Roger", countOptional.orElseThrow().getPerson());
    }

    @Test
    void testFindByPerson_whenPersonNoExists() {
        Optional<Count> countOptional = repository.findByPerson("Watemala");

        assertThrows(NoSuchElementException.class, countOptional::orElseThrow);
        assertFalse(countOptional.isPresent());
    }

    @Test
    void testFindAll() {
        List<Count> counts = repository.findAll();

        assertFalse(counts.isEmpty());
        assertEquals(2, counts.size());
    }

    @Test
    void testSave() {
        Count count = new Count(null, "Pepe", new BigDecimal("3000"));
        Count save = repository.save(count);

        assertEquals("Pepe", save.getPerson());
        assertEquals("3000", save.getAmount().toPlainString());
    }

    @Test
    void testEdit() {
        Count count = new Count(null, "Pepe", new BigDecimal("3000"));
        Count saveCount = repository.save(count);

        assertEquals("Pepe", saveCount.getPerson());
        assertEquals("3000", saveCount.getAmount().toPlainString());

        saveCount.setAmount(new BigDecimal(2000));
        Count editCount = repository.save(saveCount);

        assertEquals("2000", editCount.getAmount().toPlainString());
    }

    @Test
    void testRemove() {
        Count count = repository.findById(1L).orElseThrow();
        assertEquals("Roger", count.getPerson());

        repository.delete(count);

        assertThrows(NoSuchElementException.class, () -> {
            repository.findById(1L).orElseThrow();
        });
    }

}
