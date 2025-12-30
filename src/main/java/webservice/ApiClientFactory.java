package webservice;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.List;

public class ApiClientFactory {

//        public static RequestSpecification getSoapApiClient() {
//            return RestAssured.given().header("Content-Type","text/xml",new Object[0]).relaxedHTTPSValidation().filters(getSoapApiFilters());
//        }

        public static RequestSpecification getRestApiClient() {
            return RestAssured.given().contentType(ContentType.JSON).filters(getRestApiFilters());
        }

//      private static List<Filter> getSoapApiFilters() {
//             List<Filter> filters = new ArrayList(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new WorkfilePayloadDecodingFilter()))
//      if (TestUtils.isTest()) {
//          filters.add(new AllureRestAssured());
//          }
//      return filters;
//      }

    public static List<Filter> getRestApiFilters() {
        List<Filter> filters = new ArrayList(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        if (TestUtils.isTest()) {
            filters.add(new AllureRestAssured());
        }
        return filters;
    }
}