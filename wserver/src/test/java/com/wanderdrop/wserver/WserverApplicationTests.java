package com.wanderdrop.wserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class WserverApplicationTests {

	@MockBean
	private ApplicationContext context;

	@Test
	public void contextLoads() {
		assertThat(context).isNotNull();
	}

}
