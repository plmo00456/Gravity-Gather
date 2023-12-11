package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.dto.team.Todo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper {

    List<Todo> getTodos(int room_seq);

    List<Todo> getTodosBySeqInRoom(int seq);

    int addTodo(Todo todo);

    int updateTodo(Todo todo);

    int updateCompleteTodo(Todo todo);

    int deleteTodo(Todo todo);
}
