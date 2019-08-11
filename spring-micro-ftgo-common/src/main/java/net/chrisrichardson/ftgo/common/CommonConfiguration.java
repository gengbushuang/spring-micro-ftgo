package net.chrisrichardson.ftgo.common;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    public CommonJsonMapperInitializer commonJsonMapperInitializer() {
        return new CommonJsonMapperInitializer();
    }
}
