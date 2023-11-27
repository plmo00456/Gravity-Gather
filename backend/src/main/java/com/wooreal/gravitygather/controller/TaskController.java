package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.task.Category;
import com.wooreal.gravitygather.dto.task.Task;
import com.wooreal.gravitygather.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    /**
     * @param map   int user_seq                        required
     *              int startDatetime    // 1698764400
     *              int endDatetime      // 1701356400
     * @return new ResponseEntity<>(tasks, HttpStatus.OK)
     */
    @PostMapping("/get")
    @ApiOperation(value = "일정 가져오는 api")
    public ResponseEntity<?> getTasks(@RequestBody Map<String, Object> map) {
        List<Task> tasks = taskService.getTasks(map);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // 인증
    @PostMapping("/category/get")
    @ApiOperation(value = "일정 카테고리 가져오는 api")
    public ResponseEntity<?> getCategories(@RequestBody Category category) {
        List<Category> categories = taskService.getCategories(category);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // 인증
    @PostMapping("/add")
    @ApiOperation(value = "일정 등록 api")
    public ResponseEntity<?> addTesk(@RequestBody Task task) {
        taskService.addTesk(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 인증
    @PostMapping("/category/add")
    @ApiOperation(value = "일정 카테고리 등록 api")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        taskService.addCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 인증
    @PostMapping("/update")
    @ApiOperation(value = "일정 수정 api")
    public ResponseEntity<?> updateTesk(@RequestBody Task task) {
        taskService.updateTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 인증
    @PostMapping("/category/update")
    @ApiOperation(value = "일정 카테고리 수정 api")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        taskService.updateCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/category/update/order")
    @ApiOperation(value = "일정 카테고리 수정 api")
    public ResponseEntity<?> updateCategoryOrder(@RequestBody Category category) {
        taskService.updateCategoryOrder(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/category/delete")
    @ApiOperation(value = "일정 카테고리 삭제 api")
    public ResponseEntity<?> deleteCategory(@RequestBody Category category) {
        taskService.deleteCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "일정 삭제 api")
    public ResponseEntity<?> deleteTask(@RequestBody Task task) {
        taskService.deleteTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
