package com.softure.task.infrastructure;

import com.softure.shared.infrastructure.SharedCRUDMapperMybatis;
import com.softure.task.domain.TaskTaskDTO;
import com.softure.task.domain.TaskTaskFilter;

public interface TaskTaskMapper  extends SharedCRUDMapperMybatis<TaskTaskDTO, TaskTaskFilter> {

}