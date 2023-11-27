package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.task.Category;
import com.wooreal.gravitygather.dto.task.Task;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.CommonMapper;
import com.wooreal.gravitygather.mapper.TaskMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final TaskMapper taskMapper;
    private final CommonService commonService;

    public TaskService(TaskMapper taskMapper, CommonService commonService) {
        this.taskMapper = taskMapper;
        this.commonService = commonService;
    }

    public List<Task> getTasks(Map<String, Object> map){
        return taskMapper.getTasks(map);
    }

    public List<Category> getCategories(Category category){
        return taskMapper.getCategories(category);
    }

    public void addTesk(Task task){
        if(!task.getIs_all_day() && (
                task.getStart_date_time() == null ||
                task.getEnd_date_time() == null)){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "일정 기간을 입력 해 주세요.");
        }

        int result = taskMapper.addTask(task);

        if(result == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "일정 등록 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }

        if(!task.getSeq().equals(0)
                && task.getShared_user_seq() != null && task.getShared_user_seq().length > 0){

            result = taskMapper.addTaskShare(task);

            for(int seq : task.getShared_user_seq()){
                String title = task.getTitle();
                if (title.length() > 15) {
                    title = title.substring(0, 12) + "...";
                }
                commonService.sendAlarm(seq, task.getUser_seq(), "\"" + title + "\" 일정이 공유되었습니다.");
            }

            if(result == 0){
                throw new BusinessLogicException(HttpStatus.valueOf(500), "일정 공유 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
            }
        }
    }

    public void addCategory(Category category){
        if(taskMapper.addCategory(category) == 0)
            throw new BusinessLogicException(HttpStatus.valueOf(500), "카테고리 등록 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
    }

    public void updateTask(Task task){
        if(taskMapper.updateTask(task) == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "일정 변경 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }
    }

    public void updateCategory(Category category){
        if(taskMapper.updateCategory(category) == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "카테고리 변경 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }
    }

    public void updateCategoryOrder(Category category){
        if(taskMapper.updateCategoryOrder(category) == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "카테고리 변경 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }
    }

    public void deleteTask(Task task){
        if(taskMapper.deleteTask(task) == 0)
            throw new BusinessLogicException(HttpStatus.valueOf(500), "일정 삭제 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
    }

    public void deleteCategory(Category category){
        Task task = new Task();
        task.setCategory_seq(category.getSeq());
        task.setIs_delete(true);
        if(taskMapper.deleteCategory(category) == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "카테고리 삭제 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }
        taskMapper.deleteTask(task);
    }


}
