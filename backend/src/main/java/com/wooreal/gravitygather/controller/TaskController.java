package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.task.Task;
import com.wooreal.gravitygather.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Task Controller", description = "일정 컨트롤러")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    // 인증
    @PostMapping("/add")
    @ApiOperation(value = "일정 등록 api")
    public ResponseEntity<?> addTesk(@RequestBody Task task) {
        taskService.addTesk(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
