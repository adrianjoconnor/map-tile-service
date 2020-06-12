package com.adrianoc.maptileservice.testconfig;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.web.WebAppConfiguration;

@ComponentScan(basePackages = {"com.adrianoc.maptileservice"})
@WebAppConfiguration
@SpringBootConfiguration
public class ControllerTestConfiguration {
}
