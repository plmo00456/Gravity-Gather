package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.config.LoginRequired;
import com.wooreal.gravitygather.dto.community.ArticleMaster;
import com.wooreal.gravitygather.dto.team.Todo;
import com.wooreal.gravitygather.service.CommunityService;
import com.wooreal.gravitygather.service.TodoService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition(info = @Info(title = "Todo Controller", version = "v1", description = "할일 컴트롤러"))
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @LoginRequired
    @PostMapping("/{room_seq}/get")
    @Operation(summary = "할일들 가져오는 api")
    public ResponseEntity<?> getTodos(@PathVariable int room_seq) {
        List<Todo> todos = todoService.getTodos(room_seq);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/add")
    @Operation(summary = "할일 추가 api")
    public ResponseEntity<?> addTodo(@RequestBody Todo todo) {
        todoService.addTodo(todo);
        return new ResponseEntity<> (HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/update")
    @Operation(summary = "할일 수정 api")
    public ResponseEntity<?> updateTodo(@RequestBody Todo todo) {
        todoService.updateTodo(todo);
        return new ResponseEntity<> (HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/complete")
    @Operation(summary = "할일 수정 api")
    public ResponseEntity<?> updateCompleteTodo(@RequestBody Todo todo) {
        todoService.updateCompleteTodo(todo);
        return new ResponseEntity<> (HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/delete")
    @Operation(summary = "할일 수정 api")
    public ResponseEntity<?> deleteTodo(@RequestBody Todo todo) {
        todoService.deleteTodo(todo);
        return new ResponseEntity<> (HttpStatus.OK);
    }

}
