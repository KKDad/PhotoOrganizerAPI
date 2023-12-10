package org.stapledon.security.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stapledon.security.dto.AccountInfoDto;
import org.stapledon.security.service.AccountInfoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AccountController {

    private final AccountInfoService accountInfoFacade;

    @GetMapping("/accounts")
    @Operation(summary = "Get all accounts", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the accounts"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Accounts not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AccountInfoDto>> getAllaccounts() {
        List<AccountInfoDto> accounts = accountInfoFacade.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get a account by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountInfoDto> getaccount(@PathVariable Long id) {
        AccountInfoDto account = accountInfoFacade.getAccount(id);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the account"),
            @ApiResponse(responseCode = "400", description = "Invalid account supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts")
    public ResponseEntity<AccountInfoDto> createaccount(@RequestBody AccountInfoDto accountInfo) {
        AccountInfoDto createdaccount = accountInfoFacade.addAccount(accountInfo);
        return new ResponseEntity<>(createdaccount, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the account"),
            @ApiResponse(responseCode = "400", description = "Invalid account supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/accounts/{id}")
    public ResponseEntity<AccountInfoDto> updateaccount(@PathVariable Long id, @RequestBody AccountInfoDto accountInfo) {
        AccountInfoDto updatedaccount = accountInfoFacade.updateAccount(id, accountInfo);
        return ResponseEntity.ok(updatedaccount);
    }

    @Operation(summary = "Delete a account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the account"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteaccount(@PathVariable Long id) {
        accountInfoFacade.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}