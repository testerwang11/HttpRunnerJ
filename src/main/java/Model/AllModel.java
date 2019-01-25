package Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class AllModel {
    private ConfigModel config;

    private List<TestModel> testSuit;

}
