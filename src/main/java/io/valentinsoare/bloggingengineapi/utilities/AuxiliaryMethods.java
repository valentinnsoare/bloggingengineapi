package io.valentinsoare.bloggingengineapi.utilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
public class AuxiliaryMethods {
    private final ObjectMapper jsonStyle;
    private static AuxiliaryMethods instance;

    private AuxiliaryMethods() {
        this.jsonStyle = new ObjectMapper();
        this.jsonStyle.registerModule(new JavaTimeModule());
    }

    public static AuxiliaryMethods getInstance() {
        if (instance == null) {
            instance = new AuxiliaryMethods();
        }

        return instance;
    }

    public Pageable sortingWithDirections(String sortDir, String sortBy, int pageNo, int pageSize) {
        Pageable pageable;

        if (Sort.Direction.ASC.name().equalsIgnoreCase(sortDir)) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        return pageable;
    }

    public <T> T updateIfPresent(T newValue, T currentValue) {
        if (newValue instanceof String) {
            return !((String) newValue).isBlank() ? newValue : currentValue;
        }

        return newValue != null ? newValue : currentValue;
    }
}
