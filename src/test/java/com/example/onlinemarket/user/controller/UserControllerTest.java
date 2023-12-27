package com.example.onlinemarket.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinemarket.user.UserController;
import com.example.onlinemarket.user.dto.UserDTO;
import com.example.onlinemarket.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	UserService userService;

	@Test
	@DisplayName("회원가입에 성공한다.")
	void signUp_Success() throws Exception {

		// given
		final UserDTO userDTO = new UserDTO("a@a.a", "aa11111!", "건희", "010-1111-1111");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
			.post("/users/sign-up")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userDTO)));

		// then
		actions.andExpect(status().isCreated()).andDo(print());

		Mockito.verify(userService).register(userDTO);
	}
}
