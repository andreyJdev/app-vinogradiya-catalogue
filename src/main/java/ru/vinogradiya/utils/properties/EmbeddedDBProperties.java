package ru.vinogradiya.utils.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "db.properties")
public class EmbeddedDBProperties {

    private Integer port;
    private String name;
    private String currentSchema;
    private String initialDataPath;
}