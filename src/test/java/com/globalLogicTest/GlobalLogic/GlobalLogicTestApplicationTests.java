package com.globalLogicTest.GlobalLogic;

import com.globalLogicTest.controller.AuthenticationController;
import com.globalLogicTest.dao.UsersDAO;
import com.globalLogicTest.dto.RegisterUserDTO;
import com.globalLogicTest.model.Phone;
import com.globalLogicTest.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.globalLogicTest.response.RegisterResponse;
import com.globalLogicTest.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
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
public class GlobalLogicTestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UsersDAO usersDAO;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AuthenticationController authenticationController;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .build();
	}

	@Test
	public void givenSignUpRequest_whenSignUp_thenReturnRegisteredUser() throws Exception {
		Phone phone1 = Phone.builder().number(11234322L).citycode(9L).countrycode(56L).build();
		List<Phone> userPhones = new ArrayList<>();
		userPhones.add(phone1);
		User user = User.builder()
				.id(1L)
				.name("user1")
				.email("user1@gmail.com")
				.password("Qweriuq29")
				.phones(userPhones)
				.createdAt(new Date())
				.lastLogin(new Date())
				.isActive(true)
				.build();
		when(usersDAO.save(any())).thenReturn(user);

		List<RegisterUserDTO.RegisterPhoneDTO> phones = new ArrayList<>();
		phones.add(new RegisterUserDTO.RegisterPhoneDTO(11234322L,9L,56L));

		RegisterUserDTO registerUserDTO = new RegisterUserDTO("user1","user1@gmail.com","Qweriuq29",phones);

		ResultActions response = mockMvc.perform(post("/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerUserDTO)));

		response.andExpect(status().isOk());
	}

	@Test
	public void givenLoginRequest_whenLogin_thenReturnLoginResponse() throws Exception {
		Phone phone1 = Phone.builder().number(11234322L).citycode(9L).countrycode(56L).build();
		List<Phone> userPhones = new ArrayList<>();
		userPhones.add(phone1);
		User user = User.builder()
				.id(1L)
				.name("user1")
				.email("user1@gmail.com")
				.password("Qweriuq29")
				.phones(userPhones)
				.createdAt(new Date())
				.lastLogin(new Date())
				.isActive(true)
				.build();
		when(usersDAO.save(any())).thenReturn(user);
		when(usersDAO.findByEmail(any())).thenReturn(Optional.of(user));

		List<RegisterUserDTO.RegisterPhoneDTO> phones = new ArrayList<>();
		phones.add(new RegisterUserDTO.RegisterPhoneDTO(11234322L,9L,56L));

		RegisterUserDTO registerUserDTO = new RegisterUserDTO("user1","user1@gmail.com","Qweriuq29",phones);
		ResponseEntity<RegisterResponse> response = authenticationController.register(registerUserDTO);

		ResultActions response2 = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + response.getBody().getToken()));

		response2.andExpect(status().isOk());
	}
}