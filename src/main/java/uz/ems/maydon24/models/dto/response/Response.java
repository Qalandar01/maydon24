package uz.ems.maydon24.models.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T, V> implements Serializable {
    private static final String success = "OK";
    private String message;
    private T data;
    private V error;
    private Integer code;


    public static <T, V> Response<T, V> success(T data) {
        return Response.<T, V>builder()
                .message(success)
                .data(data)
                .code(200)
                .build();
    }

    public static <T, V> Response<T, V> success() {
        return Response.<T, V>builder()
                .message(success)
                .code(200)
                .data(null)
                .build();
    }

    public static <T, V> Response<T, V> success(T data, String message) {
        return Response.<T, V>builder()
                .message(message)
                .code(200)
                .data(data)
                .build();
    }

    public static <T, V> Response<T, V> error(V error) {
        return Response.<T, V>builder()
                .error(error)
                .data(null)
                .build();
    }

    public static <T, V> Response<T, V> error(String message) {
        return Response.<T, V>builder()
                .message(message)
                .data(null)
                .build();
    }

    public static <T, V> Response<T, V> error(Integer code, String message) {
        return Response.<T, V>builder()
                .message(message)
                .code(code)
                .data(null)
                .build();
    }
}
