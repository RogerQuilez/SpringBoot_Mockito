package org.test.springboot.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.test.springboot.app.exception.NotEnoughMoneyException;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="counts")
public class Count {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String person;
    @Column
    private BigDecimal amount;

    public void debit(BigDecimal amount) {
        BigDecimal newAmount = this.amount.subtract(amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) throw new NotEnoughMoneyException("Not enough money");
        this.amount = newAmount;
    }

    public void credit(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

}
