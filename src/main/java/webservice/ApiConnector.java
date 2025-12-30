package webservice;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public class ApiConnector {
    RequestSpecification requestSpecification = given().filter(new AllureRestAssured())
            .filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter());

    @Step("Send post to backend")
    protected Response post(String url, String payload) {
        AtomicReference<Response> response = new AtomicReference<>();
        await("Wait until post has been sent without authentication issue").atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).header("Content-Type", "text/xml; charset=utf-8").body(payload).relaxedHTTPSValidation().post(url).then().extract().response());
                    return response.get().asString().contains("Authentication Fault: OAM ERR 2008 Unknown Error");
                });
        return response.get();
    }

    @Step("Send post with soap action to backend")
    protected Response postWithSoapAction(String url, String action, String payload) {
        AtomicReference<Response> response = new AtomicReference<>();
        await("Wait until post has been sent without authentication issue").atMost(30, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).header("Content-Type", "text/xml; charset=utf-8")
                            .header("SOAPAction", action).body(payload).relaxedHTTPSValidation().post(url).then().extract().response());
                    return response.get().asString().contains("Authentication Fault: OAM ERR 200 Unknown Error");
                });
        return response.get();
    }
}