package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.config.LoginRequired;
import com.wooreal.gravitygather.dto.task.Category;
import com.wooreal.gravitygather.dto.task.Task;
import com.wooreal.gravitygather.service.TaskService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition(info = @Info(title = "Task Controller", version = "v1", description = "일정 컴트롤러"))
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    // 인증
    /**
     * @param map   int startDatetime    // 1698764400
     *              int endDatetime      // 1701356400
     * @return new ResponseEntity<>(tasks, HttpStatus.OK)
     */
    @LoginRequired
    @PostMapping("/get")
    @Operation(summary = "일정 가져오는 api")
    public ResponseEntity<?> getTasks(@RequestBody Map<String, Object> map) {
        List<Task> tasks = taskService.getTasks(map);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/category/get")
    @Operation(summary = "일정 카테고리 가져오는 api")
    public ResponseEntity<?> getCategories(@RequestBody Category category) {
        List<Category> categories = taskService.getCategories(category);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/add")
    @Operation(summary = "일정 등록 api")
    public ResponseEntity<?> addTesk(@RequestBody Task task) {
        taskService.addTesk(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/category/add")
    @Operation(summary = "일정 카테고리 등록 api")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        taskService.addCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/update")
    @Operation(summary = "일정 수정 api")
    public ResponseEntity<?> updateTesk(@RequestBody Task task) {
        taskService.updateTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/category/update")
    @Operation(summary = "일정 카테고리 수정 api")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        taskService.updateCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/category/update/order")
    @Operation(summary = "일정 카테고리 수정 api")
    public ResponseEntity<?> updateCategoryOrder(@RequestBody Category category) {
        taskService.updateCategoryOrder(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/category/delete")
    @Operation(summary = "일정 카테고리 삭제 api")
    public ResponseEntity<?> deleteCategory(@RequestBody Category category) {
        taskService.deleteCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/delete")
    @Operation(summary = "일정 삭제 api")
    public ResponseEntity<?> deleteTask(@RequestBody Task task) {
        taskService.deleteTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/important")
    @Operation(summary = "일정 중요 api")
    public ResponseEntity<?> importantTask(@RequestBody Task task) {
        taskService.importantTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
