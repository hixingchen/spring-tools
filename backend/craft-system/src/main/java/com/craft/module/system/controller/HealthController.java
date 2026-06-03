package com.craft.module.system.controller;

import com.craft.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统管理")
@RestController
public class HealthController {

    @Operation(summary = "健康检查")
    @GetMapping("/api/health")
    public Result<String> health() {
        return Result.success("ok");
    }
}
