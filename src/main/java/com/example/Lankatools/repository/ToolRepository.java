package com.example.Lankatools.repository;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.Toolstatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Lankatools.entity.Tool;
import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {

    List<Tool> findByStatus(Toolstatus status);

    List<Tool> findByOwner(User owner);

    // Overloaded with Pageable to support clean pagination across category selections
    Page<Tool> findByCategoryContainingIgnoreCase(String category, Pageable pageable);

    // Overloaded with Pageable to support clean pagination across search queries
    Page<Tool> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Kept standard list variants if you still need them elsewhere in your project
    List<Tool> findByNameContainingIgnoreCase(String name);
    List<Tool> findByCategoryContainingIgnoreCase(String category);
}