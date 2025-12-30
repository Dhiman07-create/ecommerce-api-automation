package utils.test_data;

public class EnvUtils {

    public static String getEnv() {
        var defaultEnv = "dev";
        var envFromProperties = System.getenv("env") == null ? System.getProperty("env") : System.getenv("env");
        return envFromProperties == null ? defaultEnv : envFromProperties;
    }
}