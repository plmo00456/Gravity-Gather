package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.task.Category;
import com.wooreal.gravitygather.dto.task.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskMapper {

    List<Task> getTasks(Map<String, Object> map);

    List<Category> getCategories(Category category);

    int addTask(Task task);

    int addCategory(Category category);

    int addTaskShare(Task task);

    int updateTask(Task task);

    int updateCategory(Category category);

    int updateCategoryOrder(Category category);
}
