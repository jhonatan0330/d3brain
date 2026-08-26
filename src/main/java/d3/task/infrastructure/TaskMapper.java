package d3.task.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.task.domain.TaskDTO;
import d3.task.domain.TaskFilterDTO;

@D3SqlConnMapper(value = "TaskTaskMapper")
public interface TaskMapper {

	TaskDTO insert(TaskDTO dto);

	TaskDTO update(TaskDTO dto);

	int count(TaskFilterDTO filter);

	TaskDTO getOne(TaskFilterDTO filter);

	List<TaskDTO> getMany(TaskFilterDTO filter);

}