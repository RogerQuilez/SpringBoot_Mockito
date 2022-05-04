package org.test.springboot.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.test.springboot.app.DataMock;
import org.test.springboot.app.model.Count;
import org.test.springboot.app.model.TransactionDto;
import org.test.springboot.app.service.CountService;

import javax.print.attribute.standard.Media;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(CountController.class)
class CountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CountService service;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void testDetail() throws Exception {
        when(service.findById(1L)).thenReturn(DataMock.createCount001());
        mvc.perform(get("/api/counts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Roger"))
                .andExpect(jsonPath("$.amount").value("1000"));
    }

    @Test
    void testTransfer() throws Exception {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setOriginCountId(1L);
        transactionDto.setDestinyCountId(2L);
        transactionDto.setAmount(new BigDecimal("100"));
        transactionDto.setBankId(1L);

        mvc.perform(post("/api/counts/transfer").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transactionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Transfer done"))
                .andExpect(jsonPath("$.transaction.originCountId").value(transactionDto.getOriginCountId()));
    }

    @Test
    void testList() throws Exception {
        List<Count> counts = Arrays.asList(DataMock.createCount001().orElseThrow(),
                DataMock.createCount002().orElseThrow());

        when(service.findAll()).thenReturn(counts);

        mvc.perform(get("/api/counts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].person").value("Roger"))
                .andExpect(jsonPath("$[1].person").value("Jhon"))
                .andExpect(jsonPath("$[0].amount").value("1000"))
                .andExpect(jsonPath("$[1].amount").value("2000"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testSave() throws Exception {

        Count count = new Count(null, "Pepe", new BigDecimal("3000"));

        when(service.save(any())).then(invocation -> {
            Count c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        mvc.perform(post("/api/counts").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(count)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.person", is("Pepe")))
                .andExpect(jsonPath("$.amount", is(3000)));
    }
}