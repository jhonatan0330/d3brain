package com.softure.task.infrastructure;

import com.shared.infrastructure.SharedCRUDMapperMybatis;
import com.softure.SoftureSqlConnMapper;
import com.softure.task.domain.TaskTaskDTO;
import com.softure.task.domain.TaskTaskFilter;

@SoftureSqlConnMapper("TaskTaskMapper")
public interface TaskTaskMapper  extends SharedCRUDMapperMybatis<TaskTaskDTO, TaskTaskFilter> {

}