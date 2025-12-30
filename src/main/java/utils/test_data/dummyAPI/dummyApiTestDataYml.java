package utils.test_data.dummyAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class dummyApiTestDataYml {

    private String username;
    private String password;
    private String searchKey;
}
