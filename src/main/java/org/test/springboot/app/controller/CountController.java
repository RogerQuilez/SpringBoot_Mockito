package org.test.springboot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.springboot.app.model.Count;
import org.test.springboot.app.model.TransactionDto;
import org.test.springboot.app.service.CountService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/counts")
public class CountController {

    @Autowired
    private CountService countService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Count> list() {
        return countService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Count detail(@PathVariable Long id) {
        return countService.findById(id).orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Count save(@RequestBody Count count) {
        return countService.save(count);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionDto transactionDto) {

        countService.transfer(transactionDto.getOriginCountId(), transactionDto.getDestinyCountId(),
                transactionDto.getAmount(), transactionDto.getBankId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transfer done");
        response.put("transaction", transactionDto);

        return ResponseEntity.ok(response);
    }

}
