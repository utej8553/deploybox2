package org.utej.deploybox.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.utej.deploybox.Service.ProjectService;

@RestController
@RequestMapping("/api/deploy")
@CrossOrigin(origins = "*")
public class DeployController {

    private final ProjectService projectService;

    @Autowired
    public DeployController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProjectFiles(
            @RequestParam String username,
            @RequestParam String projectName,
            @RequestParam("file") MultipartFile file) {
        try {
            projectService.uploadFiles(username, projectName, file);
            return ResponseEntity.ok("Uploaded successfully to project: " + projectName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }
}
