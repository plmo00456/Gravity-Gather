package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.task.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskMapper {

    List<Task> getTasks(Map<String, Object> map);

    int addTask(Task task);

    int addTaskShare(Task task);
}
