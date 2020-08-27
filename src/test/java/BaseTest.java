import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.*;

import java.io.File;

public class BaseTest {
    public Response res = null; //Response
    public JsonPath jp = null; //JsonPath
    public String env=ReadWritePropertyFile.getProperty("env", ApiUtils.configFile).toString();
    public String testdata=System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator+"testData" +File.separator + "testdata.yaml";
    public YamlReader testdataconfig = new YamlReader(testdata);

    @BeforeClass
    public void setup (){
        //Test Setup
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.baseURI=ApiUtils.applicationconfig.get("application.petstore."+env);
    }

    @AfterClass
    public void afterTest (){
        RestAssured.baseURI=null;
        RestAssured.basePath=null;
    }
}
