package io.valentinsoare.bloggingengineapi.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Getter
@Setter
@Builder
public class ApiAuthException extends RuntimeException {
    private String resourceName;
    private String message;
    private Map<String, String> resources;

    public ApiAuthException(String resourceName, String message, Map<String, String> resources) {
        super(String.format("ERROR [ %s ] - %s [%s", StringUtils.capitalize(resourceName), message, StringUtils.join(resources.entrySet(), ", ")));
        this.resourceName = resourceName;
        this.resources = resources;
        this.message = message;
    }
}
