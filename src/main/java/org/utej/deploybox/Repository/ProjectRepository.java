package org.utej.deploybox.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.utej.deploybox.Model.Project;
import org.utej.deploybox.Model.User;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByNameAndUserUsername(String name, String username);
    Optional<Project> findByNameAndUser(String projectName, User user);
}
