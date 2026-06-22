package com.ynova.medisalud.appointments.infrastructure.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ynova.medisalud.appointments.infrastructure.adapter.in.mcp.AppointmentMcpTools;

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider appointmentToolCallbackProvider(AppointmentMcpTools tools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(tools)
                .build();
    }
}
