package com.example.frontend.unit;

import java.util.List;

import com.example.frontend.client.CountryApiClient;
import com.example.frontend.controller.HomeController;
import com.example.frontend.model.CountryCurrency;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeControllerUnitTest {

    @Test
    void setsModelAttributesOnSuccess() {
        CountryApiClient client = mock(CountryApiClient.class);
        List<CountryCurrency> countries = List.of(
                new CountryCurrency("Austria", "Euro", "EUR")
        );
        when(client.fetchCountries()).thenReturn(countries);

        HomeController controller = new HomeController(client);
        Model model = new ExtendedModelMap();

        String view = controller.home(model);

        assertThat(view).isEqualTo("home");
        assertThat(model.getAttribute("apiStatus")).isEqualTo("ok");
        assertThat(model.getAttribute("countries")).isEqualTo(countries);
        assertThat(model.getAttribute("apiError")).isNull();
    }

    @Test
    void setsErrorWhenApiFails() {
        CountryApiClient client = mock(CountryApiClient.class);
        when(client.fetchCountries()).thenThrow(new RestClientException("boom"));

        HomeController controller = new HomeController(client);
        Model model = new ExtendedModelMap();

        controller.home(model);

        assertThat(model.getAttribute("apiStatus")).isEqualTo("unavailable");
        assertThat(String.valueOf(model.getAttribute("apiError"))).contains("boom");
    }
}
