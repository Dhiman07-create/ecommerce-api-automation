package entities.externalized_data.ws;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class WebServiceDataYml {
    private String loginUrl;
    private String searchUrl;
    private String productByIdUrl;
    private String addToCartUrl;
    private String getCartUrl;
}
