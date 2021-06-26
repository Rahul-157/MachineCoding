package browserstack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BrowserStack {
    private String key,u_name;
    static final String hosturl = "https://api.browserstack.com/5";
    RequestSpecification request ;
    RequestSpecBuilder builder;
    private Status status;
    private Browsers browsers;
    private Worker worker;
    BrowserStack(String key,String u_name){
        this.key = key;
        this.u_name = u_name;
       
        this.builder = new RequestSpecBuilder();
        this.builder.setBaseUri(hosturl);
        this.builder.setAuth(basic(u_name,key));
        this.builder.addHeader("Content-Type", "application/json");
        this.request = RestAssured.given().spec(this.builder.build());
        this.status  = new Status(this.request);
        this.browsers = new Browsers(this.request);
        this.worker = new Worker(this.request);
    }
    
    public String getStatus(){
        return this.status.get();
    }

    public String getBrowsers(Map<String,String> params){
       return this.browsers.get(params);
    }

    public String getBrowsers(){
    
       return this.browsers.get(new HashMap<String,String>());
    }

    public String createWorker(Map<String,String> map){
        String payload = new Gson().toJson(map);
        return this.worker.post(payload);
    }

    public String getWorker(){
        return this.worker.get();
    }

    public String getWorker(String id){
        return this.worker.get(id);
    }

    public String deleteWorker(String id){
        return this.worker.delete(id);
    }
    
    public void screenshot(String id,String format) {
        try{
        this.worker.capture(id, format); 
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static AuthenticationScheme basic(String userName, String password) {
        final PreemptiveBasicAuthScheme preemptiveBasicAuthScheme = new PreemptiveBasicAuthScheme();
        preemptiveBasicAuthScheme.setUserName(userName);
        preemptiveBasicAuthScheme.setPassword(password);
        return preemptiveBasicAuthScheme;
      }
}
