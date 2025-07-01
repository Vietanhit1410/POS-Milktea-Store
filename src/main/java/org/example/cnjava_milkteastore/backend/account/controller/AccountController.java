package org.example.cnjava_milkteastore.backend.account.controller;

import org.example.cnjava_milkteastore.backend.account.service.AccountService;
import org.example.cnjava_milkteastore.backend.account.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAll());
    }

    @GetMapping(params = "id")
    public ResponseEntity<AccountDTO> getById(@RequestParam int id) {
        return accountService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountDTO> saveAccount(@RequestBody AccountDTO dto) {
        return ResponseEntity.ok(accountService.createOrUpdate(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@RequestBody AccountDTO dto) {
        accountService.delete(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-id")
    public ResponseEntity<Void> deleteById(@RequestParam int id) {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        accountService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}