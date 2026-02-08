package tests;

import base.BaseTest;
import clients.AuthClient;
import clients.CartsClient;
import clients.ProductsClient;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.Getter;
import models.AuthResponse;
import models.Cart;
import models.Product;
import org.testng.collections.Maps;
import utils.TestDataFactory;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.test_data.dummyAPI.dummyApiTestDataYml;
import webservice.ApiRest;

import static io.restassured.RestAssured.baseURI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.JsonUtils.modifyJson;
import static utils.test_data.ExternalizedTestDataProvider.getDummyApiTestDataYml;
import static utils.test_data.ExternalizedTestDataProvider.getWebServiceDataYml;
import static utils.test_data.LocalPropertyReader.getTestDataProperties;


public class E2ETest extends BaseTest {

    @Getter
    private static final dummyApiTestDataYml testData = getDummyApiTestDataYml();
    private static Map<String, String> requestBodyMap = new HashMap<>();
    private static Map<String, String> headerMap = Maps.newHashMap();
    private static Response response;
    private static String ssid;
    private static List<Product> products;
    private static int firstProductId;

    AuthClient authClient = new AuthClient();
    ProductsClient productsClient = new ProductsClient();
    CartsClient cartsClient = new CartsClient();

    @Test
    public void tc01_LoginAndProductSearchByID() throws InterruptedException {
        login();
        search(testData.getSearchKey());
        getProductById(firstProductId);
    }

    @Test
    public void tc02_AddToCart() throws InterruptedException {
        login();
        search(testData.getSearchKey());
        addToCart();
        getCart();
    }

    @Test
    public void tc03_UpdateCart()throws InterruptedException {
        login();
        search(testData.getSearchKey());
        addToCart();
        getCart();
    }

    @Test
    public void tc04_DeleteCart() throws InterruptedException {
        login();
        search(testData.getSearchKey());
        addToCart();
        getCart();
        deleteCart();
    }

    @Step
    public static void login()
    {
        baseURI = getWebServiceDataYml().getLoginUrl();
        requestBodyMap.put("username", testData.getUsername());
        requestBodyMap.put("password", testData.getPassword());
        String updatedJSON = modifyJson(getTestDataProperties("loginJsonPath"), requestBodyMap);
        response = ApiRest.post(baseURI, updatedJSON, 200);
        Allure.step("Login successful!");
        ApiRest.validateResponseContainsKey(response, "accessToken");
        ssid = response.body().jsonPath().getString("accessToken");
        Assert.assertNotNull(ssid, "Token should not be null");
    }

    @Step
    public static void search(String searchKey) throws InterruptedException {
        baseURI = getWebServiceDataYml().getSearchUrl();
        response = ApiRest.get(baseURI+searchKey, 200);
        ApiRest.validateResponseContainsKey(response, "products");
        Allure.step("Product Searched!");
        products=response.body().jsonPath().getList("products");
        Assert.assertFalse(products.isEmpty(), "Search results should not be empty");
        firstProductId = response.jsonPath().getInt("products[0].id");
    }

    @Step
    public static void getProductById(int id) throws InterruptedException {
        baseURI = getWebServiceDataYml().getProductByIdUrl();
        response = ApiRest.get(baseURI+id, 200);
        ApiRest.validateResponseContainsKey(response, "id");
    }

    @Step
    public static void addToCart(){
        baseURI = getWebServiceDataYml().getAddToCartUrl();
        requestBodyMap.clear();
        requestBodyMap.put("userId", "1");
        String updatedJSON = modifyJson(getTestDataProperties("addToCartJsonPath"), requestBodyMap);
        response = ApiRest.post(baseURI, updatedJSON, 201);
        ApiRest.validateResponseContainsKey(response, "id");
        ApiRest.validateResponseContainsKey(response, "products");
    }

    @Step
    public static void getCart(){
        baseURI = getWebServiceDataYml().getGetCartUrl();
        requestBodyMap.clear();
        requestBodyMap.put("userId", "1");
        String updatedJSON = modifyJson(getTestDataProperties("getCartJSonPath"), requestBodyMap);
        response = ApiRest.get(baseURI, updatedJSON, 200);
        ApiRest.validateResponseContainsKey(response, "id");
        ApiRest.validateResponseContainsKey(response, "producta");
    }

    @Step
    public static void deleteCart(){
        baseURI = getWebServiceDataYml().getGetCartUrl();
        requestBodyMap.clear();
        requestBodyMap.put("userId", "1");
        String updatedJSON = modifyJson(getTestDataProperties("getCartJSonPath"), requestBodyMap);
        response = ApiRest.delete(baseURI, updatedJSON, 200);
        ApiRest.validateResponseContainsKey(response, "id");
        ApiRest.validateResponseContainsKey(response, "products");
        ApiRest.validateResponseContainsKey(response, "isDeleted");
        ApiRest.validateResponseContainsKey(response, "deletedOn");
    }
}
