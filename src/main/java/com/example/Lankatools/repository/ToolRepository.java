package com.example.Lankatools.repository;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.Toolstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Lankatools.entity.Tool;
import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    List<Tool> findByStatus(Toolstatus status);

    List<Tool> findByNameContainingIgnoreCase(String name);

    List<Tool> findByOwner(User owner);

    List<Tool> findByCategoryContainingIgnoreCase(String category);
}
