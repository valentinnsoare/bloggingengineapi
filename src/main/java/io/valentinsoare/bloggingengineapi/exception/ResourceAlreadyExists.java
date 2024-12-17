package io.valentinsoare.bloggingengineapi.exception;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Getter
@Setter
public class ResourceAlreadyExists extends RuntimeException  {
    private final String resourceName;
    private Map<String, String> resources;

    public ResourceAlreadyExists(String resourceName, Map<String, String> resources) {
        super(String.format("%s already exists %s", StringUtils.capitalize(resourceName), StringUtils.join(resources.entrySet(), ", ")));
        this.resourceName = resourceName;
    }
}
