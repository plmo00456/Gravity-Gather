package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.task.Task;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.CommonMapper;
import com.wooreal.gravitygather.mapper.TaskMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public void addTesk(Task task){
        int result = taskMapper.addTask(task);

        if(result == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "일정 등록 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }

        if(!task.getSeq().equals(0)
                && task.getShared_user_seq() != null && task.getShared_user_seq().length > 0){

            result = taskMapper.addTaskShare(task);
            if(result == 0){
                throw new BusinessLogicException(HttpStatus.valueOf(500), "일정 공유 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
            }
        }
    }

}
