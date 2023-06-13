package org.stapledon.security.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.stapledon.security.model.AccountAto;
import org.stapledon.security.service.AccountService;
import org.springframework.web.server.ResponseStatusException;
import org.stapledon.security.service.DuplicateAccountException;


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
    //@PreAuthorize("hasRole('ADMIN')")
    public List<AccountAto> fetchAllAccounts() {
        return accountService.fetchAll();
    }

    @Tag(name = "Create Account", description = "Create a new account")
    @PutMapping("/user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = AccountAto.class))),
        @ApiResponse(responseCode = "409", description = "error creating account", content = @Content)})
    //@PreAuthorize("hasRole('ADMIN') or ")
    public AccountAto saveAccount(@RequestBody AccountAto accountAto) {
        try {
            accountService.save(accountAto);
        } catch (DuplicateAccountException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
        return accountAto;
    }

    @Tag(name = "Fetch Account", description = "Fetch an account by id")
    @GetMapping("/user/{id}")
    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = AccountAto.class)))
    public AccountAto fetchAccount(@PathVariable("id") Long id) {
        log.warn("Fetch requested for {}", id);
        var result = accountService.fetch(id);
        if (result == null) {
            log.error("No account located");
        }
        return result;
    }

    @Tag(name = "Delete Account", description = "Delete an account by id")
    @DeleteMapping("/user/{id}")
    public void deleteAccount(@PathVariable("id") Long id) {
        accountService.delete(id);
    }

    @Tag(name = "Fetch Account by Username", description = "Fetch an account by username")
    @GetMapping("/user/username/{username}")
    public AccountAto fetchAccountByUsername(@PathVariable("username") String username) {
        return accountService.fetchByUsername(username);
    }

    @Tag(name = "Fetch Account by Email", description = "Fetch an account by email")
    @GetMapping("/user/email/{email}")
    public AccountAto fetchAccountByEmail(@PathVariable("email") String email) {
        return accountService.fetchByEmail(email);
    }
}
