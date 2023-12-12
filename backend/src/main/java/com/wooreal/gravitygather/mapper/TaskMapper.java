package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.task.Category;
import com.wooreal.gravitygather.dto.task.Task;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskMapper {

    List<Task> getTasks(Map<String, Object> map);

    List<Category> getCategories(Category category);

    int addTask(Task task);

    int addCategory(Category category);

    int addTaskShare(Task task);

    int updateTask(Task task);

    int deleteTask(Task task);

    int updateCategory(Category category);

    int updateCategoryOrder(Category category);

    int deleteCategory(Category category);

    int importantTask(Task task);
}
