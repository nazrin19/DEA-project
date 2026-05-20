package com.example.Lankatools.service;

import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {
    @Autowired
    private ToolRepository toolRepository;

    public Tool saveTool(Tool tool){
        tool.setStatus(Toolstatus.PENDING);
        return toolRepository.save(tool);
    }

    public List<Tool> getAllTools(){
        return toolRepository.findAll();
    }

    public List<Tool> getToolsByStatus(Toolstatus status){
        return toolRepository.findByStatus(status);
    }

    public List<Tool> searchToolsByName(String name){
        return toolRepository.findByNameContainingIgnoreCase(name);
    }
}
