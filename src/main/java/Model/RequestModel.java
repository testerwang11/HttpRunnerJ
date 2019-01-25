package Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class RequestModel {
    private String host;
    private String url;
    private String method;
    private List<BaseModel> headers;
    private List<BaseModel> body;
    private String notSignsParams;

}
