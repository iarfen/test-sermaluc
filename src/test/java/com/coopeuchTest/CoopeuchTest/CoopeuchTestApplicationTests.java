package com.coopeuchTest.CoopeuchTest;

import com.coopeuchTest.dao.TasksDAO;
import com.coopeuchTest.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CoopeuchTestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TasksDAO tasksDAO;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .build();
	}

	@Test
	public void givenTaskObject_whenCreateTask_thenReturnSavedTask() throws Exception{
		Task task = Task.builder()
                .id(1L)
                .description("Task of test in order to test the POST method of the endpoint /tasks")
				.current(true)
				.build();
		when(tasksDAO.save(task)).thenReturn(task);

		ResultActions response = mockMvc.perform(post("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(task)));

		response.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void givenListOfTasks_whenGetAllTasks_thenReturnTasksList() throws Exception{
		List<Task> listTasks = new ArrayList<>();
		listTasks.add(Task.builder().id(1L).description("This is task 1").current(true).build());
		listTasks.add(Task.builder().id(2L).description("This is task 2").current(false).build());
		when(tasksDAO.findAll()).thenReturn(listTasks);

		ResultActions response = mockMvc.perform(get("/tasks"));

		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listTasks.size())));
	}

	@Test
	public void givenTaskId_whenGetTaskById_thenReturnTaskObject() throws Exception{
		Long taskId = 1L;
		Task task = Task.builder()
				.id(taskId)
				.description("Description of task 1")
				.current(true)
				.build();
		when(tasksDAO.findById(taskId)).thenReturn(Optional.of(task));

		ResultActions response = mockMvc.perform(get("/tasks/{taskId}", taskId));

		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.id", is(task.getId().intValue())))
				.andExpect(jsonPath("$.description", is(task.getDescription())))
				.andExpect(jsonPath("$.current", is(task.getCurrent())));
	}

	@Test
	public void givenInvalidTaskId_whenGetTaskById_thenReturnEmpty() throws Exception{
		Long taskId = 1L;
		Task task = Task.builder()
				.id(taskId)
				.description("Description of task 1")
				.current(true)
				.build();
		given(tasksDAO.findById(taskId)).willReturn(Optional.empty());

		ResultActions response = mockMvc.perform(get("/tasks/{taskId}", taskId));

		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void givenUpdatedTask_whenUpdateTask_thenReturnUpdateTaskObject() throws Exception{
		Long taskId = 1L;
		Task task = Task.builder()
				.id(taskId)
				.description("Description of task 1")
				.current(true)
				.build();

		Task updatedTask = Task.builder()
				.id(taskId)
				.description("Description of task 1")
				.current(true)
				.build();
		given(tasksDAO.findById(taskId)).willReturn(Optional.of(task));
		given(tasksDAO.save(any(Task.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));

		ResultActions response = mockMvc.perform(put("/tasks/{taskId}", taskId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedTask)));

		response.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void givenUpdatedTask_whenUpdateTask_thenReturn404() throws Exception{
		Long taskId = 1L;
		Task task = Task.builder()
				.id(taskId)
				.description("Description of task 1")
				.current(true)
				.build();
		given(tasksDAO.findById(taskId)).willReturn(Optional.empty());

		ResultActions response = mockMvc.perform(put("/tasks/{taskId}", taskId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(task)));

		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void givenTaskId_whenDeleteTask_thenReturn200() throws Exception{
		Long taskId = 1L;
		Task task = Task.builder()
				.id(taskId)
				.description("Description of task 1")
				.current(true)
				.build();
		when(tasksDAO.findById(taskId)).thenReturn(Optional.of(task));
		willDoNothing().given(tasksDAO).deleteById(taskId);

		ResultActions response = mockMvc.perform(delete("/tasks/{taskId}", taskId));

		response.andExpect(status().isOk())
				.andDo(print());
	}
}