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
    @PostMapping("/create")
    public Project createProject(@RequestParam String username, @RequestParam String projectName){
        return projectService.createProject(username,projectName);
    }
}