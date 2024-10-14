package org.stapledon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.stapledon.data.ItemService;
import org.stapledon.entities.Item;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items")
    @Operation(summary = "Get all items", description = "Get all items in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Items"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Items not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllFolders();
        return ResponseEntity.ok(items);
    }

}
