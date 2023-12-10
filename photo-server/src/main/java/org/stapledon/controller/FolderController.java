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
import org.stapledon.data.FolderService;
import org.stapledon.entities.Folder;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FolderController {

    private final FolderService folderService;

    @GetMapping("/folders")
    @Operation(summary = "Get all folders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the folders"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Folders not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Folder>> getAllFolders() {
        List<Folder> folders = folderService.getAllFolders();
        return ResponseEntity.ok(folders);
    }

}
