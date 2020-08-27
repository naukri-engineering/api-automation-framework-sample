package utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.File;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ApiUtils {
    //Global Setup Variables


    public  RequestSpecification Request = RestAssured.given();
    public  String path;
    public  String method;
    public  String responseSchema;
    public  String body;
    public  int successStatusCode;
    public Response res;
    public YamlReader apiconfig;
    public YamlReader testdataconfig;
    public static String jsonPathTerm;
    public String env= ReadWritePropertyFile.getProperty("env", ApiUtils.configFile).toString();
    public String filepath=System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "CriticalApi.yaml";
    public String testdata=System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator+"testData" +File.separator + "testdata.yaml";
    public static String configFile = System.getProperty("user.dir") + File.separator + "configuration.properties";
    public static String applicationdata=System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "Application.yaml";
    public static YamlReader applicationconfig = new YamlReader(applicationdata);

    public ApiUtils createSession(String apiId, String application){

        apiconfig = new YamlReader(filepath);
        this.method=apiconfig.get(apiId+".method");
        this.responseSchema=apiconfig.get(apiId+".responseSchema");
        this.path=apiconfig.get(apiId+".path");
        this.body=apiconfig.get(apiId+".body");
        try {
            this.successStatusCode = Integer.parseInt(apiconfig.get(apiId + ".successStatusCode"));
        }
        catch (Exception e){
            //success reponse code not found
            System.out.println("---");
        }
        System.out.println(this.path);
        System.out.println(this.method);
        this.getDefaultHttpRequestHeader(application);
        return this ;

    }

    public   Response hit(){
        switch(method.toLowerCase()){
            case "get":
                this.res=this.Request.get(this.path);
                // this.res = getDefaultHttpRequestHeader(application).get(this.path);
                break;
            case "post":
                this.res=this.Request.post(this.path);
                //this.res = getDefaultHttpRequestHeader(application).post(this.path);
                break;
            case "put":
                this.res=this.Request.put(this.path);
                break;
            case "delete":
                this.res=this.Request.delete(this.path);
                break;

        }

        return this.res;
    }

    public  Map<String, Object> testdataprovider(String nodepath){
        testdataconfig = new YamlReader(testdata);
        return testdataconfig.getMap(nodepath);
    }
    //Sets query parameter
    public ApiUtils setQueryParam(String key, String value){
        this.Request.queryParam(key,value);
        //Request.queryParam(key,value);
        return this;
    }
    public ApiUtils setBody(Map<String,Object> body){
        this.Request.body(new JSONObject(body).toString());
        //Request.body(new JSONObject(body).toString());
        return this;
    }

    public ApiUtils hasValidResponseSchema(){
        System.out.println(this.responseSchema);
        this.res.then().assertThat().body(matchesJsonSchemaInClasspath("response_schema/"+this.responseSchema+".json"));
        return this;
    }



    public ApiUtils hasValidStatusCode(){
        Assert.assertEquals(this.res.getStatusCode(),this.successStatusCode);
        return this;

    }
    public ApiUtils update_url(String key, String value) {
        this.path = this.path.replace("{" + key + "}", value);
        return this;
    }


    private  RequestSpecification getDefaultHttpRequestHeader(String application) {
        if(application.equalsIgnoreCase("petstore")){
            RequestSpecification spec = new RequestSpecBuilder().setBaseUri(applicationconfig.get("application.petstore."+env)).build();
            return this.Request=RestAssured.given(spec).contentType("application/json\r\n").header("Accept", "application/json")
                    .header("User-Agent", "PostmanRuntime/7.20.1")
                    .header("Cache-Control", "no-cache")
                     .log().all();

            // put default request header here for the petstore application
        }
        else if (application.equalsIgnoreCase("weather")){
            RequestSpecification spec = new RequestSpecBuilder().setBaseUri(applicationconfig.get("application.weather."+env)).build();
            return this.Request=RestAssured.given(spec).contentType("application/json\r\n")
                    .header("Accept", "application/json")
                    .log().all();
            // put default request header here for the weather application

        }
        else{
          // put deafult request header for the default application
            return this.Request;
        }

    }



    public ApiUtils appendHeader(String key, String value) {
        this.Request.header(key,value);
        //Request.header(key,value);
        return this;
    }



    //Sets ContentType
    public  void setContentType (ContentType Type){
        this.Request.contentType(Type);
        //Request.contentType(Type);
    }


    //Returns response
    public static Response getResponse() {
        return RestAssured.get();
    }

    //Returns JsonPath object
    public static JsonPath getJsonPath (Response res) {
        String json = res.asString();
        //System.out.print("returned json: " + json +"\n");
        return new JsonPath(json);
    }
}
