package org.utej.deploybox.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.utej.deploybox.Model.Project;
import org.utej.deploybox.Model.User;
import org.utej.deploybox.Repository.ProjectRepository;
import org.utej.deploybox.Repository.UserRepository;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;
import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final String STORAGE_BASE = "/storage";
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }
    public Project createProject(String username, String projectName) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Username not found"));

    File userDir = new File(STORAGE_BASE, user.getUsername());
    if (!userDir.exists() && !userDir.mkdirs()) {
        throw new RuntimeException("Failed to create user folder");
    }

    File projectDir = new File(userDir, projectName);
    if (!projectDir.exists() && !projectDir.mkdirs()) {
        throw new RuntimeException("Failed to create project folder");
    }

    Project project = new Project();
    project.setName(projectName);
    project.setFolderpath(projectDir.getAbsolutePath()); // <-- set folderpath
    project.setUser(user);
    project.setCreated(LocalDateTime.now());

    return projectRepository.save(project);
}

    public void uploadFiles(String username, String projectName, MultipartFile file) throws IOException {
        // Find user and project
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Project> projectOpt = projectRepository.findByNameAndUser(projectName, user);
        if (projectOpt.isEmpty()) throw new RuntimeException("Project not found");

        String projectFolderPath = STORAGE_BASE + "/" + projectName;
        File projectFolder = new File(projectFolderPath);

        if (!projectFolder.exists()) {
            throw new RuntimeException("Project folder does not exist");
        }

        // If user uploads a ZIP → extract it
        if (file.getOriginalFilename() != null && file.getOriginalFilename().endsWith(".zip")) {
            Path zipPath = Paths.get(projectFolderPath, file.getOriginalFilename());
            Files.copy(file.getInputStream(), zipPath, StandardCopyOption.REPLACE_EXISTING);

            try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipPath))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    Path newFile = Paths.get(projectFolderPath).resolve(entry.getName());
                    if (entry.isDirectory()) {
                        Files.createDirectories(newFile);
                    } else {
                        Files.createDirectories(newFile.getParent());
                        Files.copy(zis, newFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            Files.deleteIfExists(zipPath); // optional cleanup
        } else {
            // Otherwise, save single file directly
            Path targetPath = Paths.get(projectFolderPath, file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}

