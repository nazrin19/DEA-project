package com.example.Lankatools.service;

import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToolService {
    @Autowired
    private ToolRepository toolRepository;

    public Tool saveTool(Tool tool) {
        tool.setStatus(Toolstatus.PENDING);
        return toolRepository.save(tool);
    }

    public List<Tool> getAllTools() {
        return toolRepository.findAll();
    }

    public Page<Tool> getToolsWithPagination(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return toolRepository.findAll(pageable);
    }

    public Tool updateTool(Long id, Tool updatedTool) {
        return toolRepository.findById(id).map(existingTool -> {
            existingTool.setName(updatedTool.getName());
            existingTool.setDescription(updatedTool.getDescription());
            existingTool.setCategory(updatedTool.getCategory());
            existingTool.setDailyRate(updatedTool.getDailyRate());
            if (updatedTool.getImageUrl() != null) {
                existingTool.setImageUrl(updatedTool.getImageUrl());
            }
            return toolRepository.save(existingTool);
        }).orElseThrow(() -> new RuntimeException("Tool not found with id: " + id));
    }


    public Tool updateToolStatus(Long id, Toolstatus status) {
        return toolRepository.findById(id).map(tool -> {
            tool.setStatus(status);
            return toolRepository.save(tool);
        }).orElseThrow(() -> new RuntimeException("Tool not found with id: " + id));
    }

    public List<Tool> getToolsByStatus(Toolstatus status) {
        return toolRepository.findByStatus(status);
    }

    public List<Tool> searchToolsByName(String name) {
        return toolRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<Tool> getToolById(Long id) {
        return toolRepository.findById(id);
    }

    public void deleteTool(Long id) {
        toolRepository.deleteById(id);
    }
}

    
