package org.test.springboot.app;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.test.springboot.app.exception.NotEnoughMoneyException;
import org.test.springboot.app.model.Bank;
import org.test.springboot.app.model.Count;
import org.test.springboot.app.repository.BankRepository;
import org.test.springboot.app.repository.CountRepository;
import org.test.springboot.app.service.impl.CountServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {

	@Mock
	CountRepository countRepository;
	@Mock
	BankRepository bankRepository;
	@InjectMocks
	CountServiceImpl service;

	@Test
	void contextLoads() {

		when(countRepository.findById(1L)).thenReturn(DataMock.createCount001());
		when(countRepository.findById(2L)).thenReturn(DataMock.createCount002());
		when(bankRepository.findById(1L)).thenReturn(DataMock.createBank());

		BigDecimal originAmount = service.checkAmount(1L);
		BigDecimal destinyAmount = service.checkAmount(2L);

		assertEquals("1000", originAmount.toPlainString());
		assertEquals("2000", destinyAmount.toPlainString());

		service.transfer(1L, 2L, new BigDecimal("100"), 1L);

		originAmount = service.checkAmount(1L);
		destinyAmount = service.checkAmount(2L);

		assertEquals("900", originAmount.toPlainString());
		assertEquals("2100", destinyAmount.toPlainString());

		int total = service.checkTotalTransfers(1L);

		assertEquals(1, total);

		verify(countRepository, times(3)).findById(1L);
		verify(countRepository, times(3)).findById(2L);
		verify(countRepository, times(2)).save(any(Count.class));

		verify(bankRepository, times(2)).findById(1L);
		verify(bankRepository).save(any(Bank.class));

	}

	@Test
	void contextLoad2() {

		when(countRepository.findById(1L)).thenReturn(DataMock.createCount001());
		when(countRepository.findById(2L)).thenReturn(DataMock.createCount002());
		when(bankRepository.findById(1L)).thenReturn(DataMock.createBank());

		BigDecimal originAmount = service.checkAmount(1L);
		BigDecimal destinyAmount = service.checkAmount(2L);

		assertEquals("1000", originAmount.toPlainString());
		assertEquals("2000", destinyAmount.toPlainString());

		assertThrows(NotEnoughMoneyException.class, () -> {
			service.transfer(1L, 2L, new BigDecimal("3000"), 1L);
		});

		originAmount = service.checkAmount(1L);
		destinyAmount = service.checkAmount(2L);

		assertEquals("1000", originAmount.toPlainString());
		assertEquals("2000", destinyAmount.toPlainString());

		int total = service.checkTotalTransfers(1L);

		assertEquals(0, total);

		verify(countRepository, times(3)).findById(1L);
		verify(countRepository, times(2)).findById(2L);
		verify(countRepository, never()).save(any(Count.class));

		verify(bankRepository, times(1)).findById(1L);
		verify(bankRepository, never()).save(any(Bank.class));

	}

	@Test
	void contextLoads3() {
		when(countRepository.findById(anyLong())).thenReturn(DataMock.createCount001());

		Optional<Count> count1 = service.findById(1L);
		Optional<Count> count2 = service.findById(2L);

		assertSame(count1, count2);
		verify(countRepository, times(2)).findById(anyLong());
	}

}
