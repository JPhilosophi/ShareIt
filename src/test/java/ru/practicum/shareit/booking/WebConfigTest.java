package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.format.FormatterRegistry;
import ru.practicum.shareit.booking.converter.StringToEnumConverter;
import ru.practicum.shareit.booking.converter.WebConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class WebConfigTest {
    @Test
    void webConfig() {
        StringToEnumConverter stringToEnumConverter = new StringToEnumConverter();
        FormatterRegistry registry = new ApplicationConversionService();
        WebConfig webConfig = new WebConfig();
        webConfig.addFormatters(registry);
        assertNotNull(webConfig);
    }
}
