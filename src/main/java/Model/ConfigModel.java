package Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class ConfigModel {

    public String config;
    private String name;
    public List<BaseModel> variables;

    public RequestModel request;

}
