package utils.test_data;

import entities.externalized_data.test_data.CommonTestYml;
import entities.externalized_data.ws.WebServiceDataYml;
import utils.test_data.dummyAPI.dummyApiTestDataYml;

import static utils.test_data.LocalPropertyReader.getTestDataProperties;
import static utils.test_data.TestDataReader.readTestDataFromYaml;

public class ExternalizedTestDataProvider {
    public static final CommonTestYml COMMON_TEST_DATA_YML = readTestDataFromYaml(
            getTestDataProperties("commonTestData"), CommonTestYml.class);
    private static CommonTestYml commonTestDataYml;
    private static WebServiceDataYml commonTestData;

    public static CommonTestYml getCommonTestDataYml() {
        if (commonTestDataYml == null) {
            commonTestDataYml = readTestDataFromYaml(getTestDataProperties("commonTestData"), CommonTestYml.class);
        }
        return commonTestDataYml;
    }

    public static WebServiceDataYml getWebServiceDataYml() {
        if (commonTestData == null) {
            commonTestData = readTestDataFromYaml(getTestDataProperties("ws"), WebServiceDataYml.class);
        }
        return commonTestData;
    }

    public static dummyApiTestDataYml getDummyApiTestDataYml(){
        return readTestDataFromYaml(getTestDataProperties("dummyApiTestData"), dummyApiTestDataYml.class);
    }
}