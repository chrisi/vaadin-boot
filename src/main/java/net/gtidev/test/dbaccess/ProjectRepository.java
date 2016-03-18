package net.gtidev.test.dbaccess;

import net.gtidev.test.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
