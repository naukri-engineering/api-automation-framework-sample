import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;
import utils.ApiUtils;


public class BasicApiTest extends BaseTest {

    public ApiUtils session;

    @Test
    public void T001_demo_test(){
        res=session.createSession("api.weather","weather")  // api end point and application name pass
                .update_url("city","Hyderabad")
                .hit();
        System.out.println(res.asString());
        session.hasValidStatusCode();
        session.hasValidResponseSchema();

        // Map<String, Object> account1 = testdataconfig.getMap("accounts.account1");  --- to take body data
        // setBody method to set body data in api call
        // appendHeader method to append header other than default header
        // getJsonPath method to parse json
        // you can add authorisation method in api utils to authorize api calls

        // to interact with different base uri at test level
        res=new ApiUtils().createSession("api.petstore","petstore")
                .hit();

        // res stores the request response
    }


    @BeforeMethod
    public void setupmethod() {
        session = new ApiUtils();
    }

    @AfterMethod
    public void teardownmethod() {
        session = null;
    }


}
