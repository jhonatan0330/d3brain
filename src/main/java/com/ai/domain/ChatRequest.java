package com.ai.domain;

import java.util.Map;

public record ChatRequest(
        String userId,
        Map<String, Object> context,
        String prompt
) {}
