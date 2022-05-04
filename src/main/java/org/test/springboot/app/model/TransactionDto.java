package org.test.springboot.app.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {

    private Long originCountId;
    private Long destinyCountId;
    private BigDecimal amount;
    private Long bankId;

}
