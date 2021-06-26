package browserstack;

import java.util.*;


import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Browsers {
    private static final String browser_url = "/browsers";
    Response res;
    RequestSpecification request;


    Browsers(RequestSpecification req){
         this.request = req;
    }

    protected String get(Map<String,String> params){
         res = request.log().method().and().log().uri().params(params).when().get(browser_url).then().log().body().extract().response();
         return res.body().asString();
    }
}
