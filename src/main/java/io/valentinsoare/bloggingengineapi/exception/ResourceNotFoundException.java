package io.valentinsoare.bloggingengineapi.exception;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceName;
    private Map<String, String> resources;

    public ResourceNotFoundException(String resourceName, Map<String, String> resources) {
        super(String.format("%s not found with %s", StringUtils.capitalize(resourceName), StringUtils.join(resources.entrySet(), ", ")));
        this.resourceName = resourceName;
    }
}
