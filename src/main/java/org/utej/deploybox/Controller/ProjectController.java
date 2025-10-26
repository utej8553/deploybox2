package org.utej.deploybox.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.utej.deploybox.Model.Project;
import org.utej.deploybox.Service.ProjectService;
@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    @PostMapping("/api/projects/create")
public ResponseEntity<?> createProject(@RequestParam String username,
                                       @RequestParam String projectName) {
    try {
        Project project = projectService.createProject(username, projectName);
        return ResponseEntity.ok(project);
    } catch (Exception e) {
        e.printStackTrace(); // this will show the exact error in the backend logs
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}


}
