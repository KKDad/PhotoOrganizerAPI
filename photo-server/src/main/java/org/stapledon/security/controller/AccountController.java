package org.stapledon.security.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.stapledon.security.model.AccountAto;
import org.stapledon.security.service.AccountService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Tag(name = "List Accounts", description = "List all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountAto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid status value", content = @Content)})
    @GetMapping("/users")
    public List<AccountAto> fetchAllAccounts() {
        return accountService.fetchAll();
    }

    @PutMapping("/user")
    public AccountAto saveAccount(@RequestBody AccountAto accountAto) {
        accountService.save(accountAto);
        return accountAto;
    }

    @GetMapping("/user/{id}")
    public AccountAto fetchAccount(@PathVariable("id") Long id) {
        log.warn("Fetch requested for {}", id);
        var result = accountService.fetch(id);
        if (result == null) {
            log.error("No account located");
        }
        return result;
    }

    @DeleteMapping("/user/{id}")
    public void deleteAccount(@PathVariable("id") Long id) {
        accountService.delete(id);
    }

    @GetMapping("/user/username/{username}")
    public AccountAto fetchAccountByUsername(@PathVariable("username") String username) {
        return accountService.fetchByUsername(username);
    }
}
