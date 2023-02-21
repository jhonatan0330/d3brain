package com.softure.task.domain;

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("TaskFilterDTO")
@Getter
@Setter
@NoArgsConstructor
public class TaskFilterDTO extends BasicFilterDTO
{
	private String user;
    
}