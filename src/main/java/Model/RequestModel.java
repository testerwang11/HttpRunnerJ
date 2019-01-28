package Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
public class RequestModel {
    private String uri;
    private String method;
    private HashMap<String, Object> headers;
    private HashMap<String, Object> body;
    private String notSignsParams;

}
