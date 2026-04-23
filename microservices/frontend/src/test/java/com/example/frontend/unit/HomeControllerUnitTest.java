package com.example.frontend.unit;

import com.example.frontend.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;

class HomeControllerUnitTest {

	@Test
	void setsModelAttributes() {
		HomeController controller = new HomeController("http://localhost:8081");
		Model model = new ExtendedModelMap();

		String view = controller.home(model);

		assertThat(view).isEqualTo("home");
		assertThat(model.getAttribute("apiBaseUrl")).isEqualTo("http://localhost:8081");
		assertThat(model.getAttribute("environment")).isEqualTo("Preview");
		assertThat(model.getAttribute("message")).isEqualTo("Interactive microservices demo is live");
		assertThat(model.getAttribute("timestamp")).isNotNull();
	}
}
