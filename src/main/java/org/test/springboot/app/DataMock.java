package org.test.springboot.app;

import org.test.springboot.app.model.Bank;
import org.test.springboot.app.model.Count;

import java.math.BigDecimal;
import java.util.Optional;

public class DataMock {

    public static final Count COUNT_001 = new Count(1L, "Roger", new BigDecimal("1000"));
    public static final Count COUNT_002 = new Count(2L, "Jhon", new BigDecimal("2000"));
    public static final Bank BANK = new Bank(1L, "Santander", 0);

    public static Optional<Count> createCount001() {
        return Optional.of(new Count(1L, "Roger", new BigDecimal("1000")));
    }

    public static Optional<Count> createCount002() {
        return Optional.of(new Count(2L, "Jhon", new BigDecimal("2000")));
    }

    public static Optional<Bank> createBank() {
        return Optional.of(new Bank(1L, "Santander", 0));
    }
}
