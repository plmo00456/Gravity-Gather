package com.wooreal.gravitygather.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wooreal.gravitygather.config.WebSocketHandler;
import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.team.Todo;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.CommonMapper;
import com.wooreal.gravitygather.mapper.RoomMapper;
import com.wooreal.gravitygather.mapper.TodoMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoMapper todoMapper;
    private final RoomService roomService;
    private final WebSocketHandler webSocketHandler;
    private final CommonService commonService;
    private final UserService userService;

    public TodoService(TodoMapper todoMapper, RoomService roomService, WebSocketHandler webSocketHandler, CommonService commonService, UserService userService) {
        this.todoMapper = todoMapper;
        this.roomService = roomService;
        this.webSocketHandler = webSocketHandler;
        this.commonService = commonService;
        this.userService = userService;
    }

    public List<Todo> getTodos(int room_seq){
        UserResponse user = roomService.isInTheRoom();
        Integer userSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null && user.getSeq() == userSeq)
            return todoMapper.getTodos(room_seq);
        else
            throw new BusinessLogicException(HttpStatus.valueOf(500), "권한이 없습니다.");
    }

    // seq에 해당하는 방 안에 todo
    public List<Todo> getTodosBySeqInRoom(int seq){
        return todoMapper.getTodosBySeqInRoom(seq);
    }

    public void addTodo(Todo todo){
        Integer userSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        todo.setUser_seq(userSeq);
        todoMapper.addTodo(todo);
        if(todo.getReceive_seq() != null){
            User userInfo = userService.getUserBySeq();
            commonService.sendAlarm(todo.getReceive_seq(), userSeq, userInfo.getNickname() + "님께서 할 일을 부여 했습니다.", "06");
        }
        try{
            JsonObject jo = new JsonObject();
            jo.addProperty("type1", "room");
            jo.addProperty("type2", "todoUpdMsg");
            jo.addProperty("todo", getTodos(todo.getRoom_seq()).toString());
            webSocketHandler.sendMessageToRoom(todo.getRoom_seq(), jo);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateTodo(Todo todo){
        todoMapper.updateTodo(todo);
        updateTodoMsg(todo.getSeq());
    }

    public void updateCompleteTodo(Todo todo){
        todoMapper.updateCompleteTodo(todo);
        updateTodoMsg(todo.getSeq());
    }

    public void deleteTodo(Todo todo){
        todoMapper.deleteTodo(todo);
        updateTodoMsg(todo.getSeq());
    }

    public void updateTodoMsg(int seq){
        try{
            List<Todo> list = getTodosBySeqInRoom(seq);
            int roomSeq = 0;
            if(list.size() > 0){
                roomSeq = list.get(0).getRoom_seq();
            }
            if(roomSeq != 0){
                JsonObject jo = new JsonObject();
                jo.addProperty("type1", "room");
                jo.addProperty("type2", "todoUpdMsg");
                jo.addProperty("todo", list.toString());
                webSocketHandler.sendMessageToRoom(roomSeq, jo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
