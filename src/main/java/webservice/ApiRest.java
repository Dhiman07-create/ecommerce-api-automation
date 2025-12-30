package webservice;

import com.google.common.truth.Truth;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.codec.binary.Base64;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static io.restassured.RestAssured.given;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static webservice.ApiClientFactory.getRestApiFilters;

public class ApiRest {

    public static RequestSpecification getRestApiClient() {
        return RestAssured.given().contentType(ContentType.JSON).filters(getRestApiFilters());
    }

    @Step("Validating Response for key:{0} with value:{1}")
    public static void validateResponse(Response response, String key, String value) {
        try {
            // Try JSON path extraction
            Object jsonValue = response.jsonPath().get(key);
            if (jsonValue != null && jsonValue.toString().equals(value)) {
                Allure.step("Response has '" + key + "' with value: " + value, Status.PASSED);
            } else {
                Allure.step("Response does not have '" + key + "' with value: " + value, Status.FAILED);
                assertThat(jsonValue != null && jsonValue.toString().equals(value)).isTrue();
            }
        } catch (Exception e) {
            // Fallback: treat as raw string
            String raw = response.asString();
            if (raw.equals(value)) {
                Allure.step("Raw response contains value: " + value, Status.PASSED);
            } else {
                Allure.step("Raw response does not contain value: " + value, Status.FAILED);
                assertThat(raw.equals(value)).isTrue();
            }
        }
    }

    @Step("Validating Response for key:{0} with value:{1}")
    public static void validateResponseContains(Response response, String key, String value) {
        if (response.jsonPath().get(key).toString().contains(value))
            Allure.step("Response has '" + key + "' with value: " + value, Status.PASSED);
        else {
            Allure.step("Response does not have '" + key + "' with value: " + value, Status.FAILED);
            assertThat(response.body().jsonPath().get(key).toString().equals(value)).isTrue();
        }
    }

    @Step("Validating Response for key:{0}")
    public static void validateResponseContainsKey(Response response, String key) {
        if (response.prettyPrint().contains(key))
            Allure.step("Response has key : '" + key, Status.PASSED);
        else {
            Allure.step("Response does not have key : '" + key, Status.FAILED);
            assertThat(response.body().jsonPath().toString().equals(key)).isTrue();
        }
    }

    @Step("Validating Response status code {1}")
    public static boolean validateResposeStatusCode(int responseStatusCode, int expectedStatusCode) {
        boolean flag = false;
        if (responseStatusCode == expectedStatusCode) {
            Allure.step("Expected status code " + responseStatusCode + " is obtained", Status.PASSED);
            flag = true;
        } else
            Allure.step("Expected status code is " + expectedStatusCode + ", but actual is " + responseStatusCode);
        Allure.step("Expected status code is " + expectedStatusCode + ", but actual is " + responseStatusCode,
                Status.FAILED);
        return flag;
    }

    @Step("Send post to backend")
    public static Response post(String url, String payload, int expectedStatusCode) {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        await("Wait until post has been sent without authentication issue").atMost(30, TimeUnit.SECONDS)
                .pollInterval(4, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).header("Content-Type", "application/json")
                            .body(payload).relaxedHTTPSValidation().post(url).then().extract().response());
                    return validateResposeStatusCode(response.get().statusCode(), expectedStatusCode);
                });
        System.out.println(response.get().asString());
        return response.get();
    }

    @Step("Send post to backend")
    public static Response post(String url, String userName, String pwd, String payload, int expectedStatusCode) {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        await("Wait until post has been sent without authentication issue").atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification)
                            .headers("Content-Type", "application/json", "Authorization",
                                    "Basic " + getAuth(userName + ":" + pwd))
                            .body(payload).relaxedHTTPSValidation().post(url).then().extract().response());
                    return validateResposeStatusCode(response.get().statusCode(), expectedStatusCode);
                });
        System.out.println(response.get().asString());
        return response.get();
    }

    public static Response post(String url, Map headers, String payload, int expectedStatusCode) {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        await("Wait until post has been sent without authentication issue").atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).headers(headers).body(payload)
                            .relaxedHTTPSValidation().post(url).then().extract().response());
                    return validateResposeStatusCode(response.get().statusCode(), expectedStatusCode);
                });
        System.out.println(response.get().asString());
        return response.get();
    }

    @Step("Send post to backend")
    public static Response put(String url, String payload, int expectedStatusCode) {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();

        await("Wait until post has been sent without authentication issue").atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).header("Content-Type", "application/json")
                            .body(payload).relaxedHTTPSValidation().put(url).then().extract().response());

                    return validateResposeStatusCode(response.get().statusCode(), expectedStatusCode);
                });

        System.out.println(response.get().asString());
        return response.get();
    }

    @Step("Send get to backend")
    public static Response get(String url, Map queryParam, int expectedStatusCode) {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        await("Wait until get has been sent without authentication issue").atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).header("Content-Type", "application/json")
                            .queryParams(queryParam).relaxedHTTPSValidation().get(url).then().extract().response());
                    return validateResposeStatusCode(response.get().statusCode(), expectedStatusCode);
                });

        System.out.println(response.get().asString());
        return response.get();
    }

    @Step("Send post to backend")
    public static Response get(String url, String payload, int expectedStatusCode) {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        await("Wait until post has been sent without authentication issue").atMost(30, TimeUnit.SECONDS)
                .pollInterval(4, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).header("Content-Type", "application/json")
                            .body(payload).relaxedHTTPSValidation().get(url).then().extract().response());
                    return validateResposeStatusCode(response.get().statusCode(), expectedStatusCode);
                });
        System.out.println(response.get().asString());
        return response.get();
    }

    public static Response get(String url, int expectedStatusCode) throws InterruptedException {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        int maxRetries = 50;
        int attempt = 0;
        do {
            attempt++;
            response.set(given().spec(requestSpecification).header("Content-Type", "application/json")
                    .relaxedHTTPSValidation().get(url).then().extract().response());
            sleep(1000);
        } while (!validateResposeStatusCode(response.get().statusCode(), expectedStatusCode) && attempt <= maxRetries);
        System.out.println(attempt);
        Truth.assertThat(response.get().statusCode() == expectedStatusCode).isTrue();
        System.out.println(response.get().asString());

        return response.get();
    }

    public static String getAuth(String auth) {
        byte[] encodedAuth = auth.getBytes();
        byte[] encodedOutput = Base64.encodeBase64(encodedAuth);
        auth = new String(encodedOutput);
        return auth;
    }

    @Step("Send post to backend")
    public static Response post(String url, String payload, int expectedStatusCode, String key, String value) throws InterruptedException {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        int maxRetries = 10;
        int attempt = 0;
        String result;
        do {
            attempt++;

            response.set(given().spec(requestSpecification).header("Content-Type", "application/json").body(payload)
                    .relaxedHTTPSValidation().post(url).then().extract().response());
            result = response.get().jsonPath().getString(key);
            sleep(5000);
        } while (((!((validateResposeStatusCode(response.get().statusCode(), expectedStatusCode))
                && Boolean.valueOf(result))) && (attempt <= maxRetries)));
        return response.get();
    }


    @Step("Send post to backend")
    public static Response getMethod(String url, Map headerMap, int expectedStatusCode) {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        await("Wait until get has been sent without authentication issue").atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).headers(headerMap)
                            .relaxedHTTPSValidation().get(url).then().extract().response());
                    return validateResposeStatusCode(response.get().statusCode(), expectedStatusCode);
                });

        System.out.println(response.get().asString());
        return response.get();
    }

    public static Response post(String url, Map headers, int expectedStatusCode) {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        await("Wait until post has been sent without authentication issue").atMost(100, TimeUnit.SECONDS)
                .pollInterval(4, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).headers(headers)
                            .relaxedHTTPSValidation().post(url).then().extract().response());
                    return validateResposeStatusCode(response.get().statusCode(), expectedStatusCode);
                });
        System.out.println(response.get().asString());
        return response.get();
    }

    @Step("Send post to backend")
    public static Response delete(String url, String payload, int expectedStatusCode) {
        AtomicReference<Response> response = new AtomicReference<>();
        var requestSpecification = ApiClientFactory.getRestApiClient();
        await("Wait until post has been sent without authentication issue").atMost(30, TimeUnit.SECONDS)
                .pollInterval(4, TimeUnit.SECONDS).until(() -> {
                    response.set(given().spec(requestSpecification).header("Content-Type", "application/json")
                            .body(payload).relaxedHTTPSValidation().delete(url).then().extract().response());
                    return validateResposeStatusCode(response.get().statusCode(), expectedStatusCode);
                });
        System.out.println(response.get().asString());
        return response.get();
    }

}