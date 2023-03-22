package com.softure.task.infrastructure;

import com.softure.shared.infrastructure.SharedCRUDMapperMybatis;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskFilter;

public interface TaskMapper  extends SharedCRUDMapperMybatis<TaskDTO, TaskFilter> {

}