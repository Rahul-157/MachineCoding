package browserstack;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Status {
     private static final String status_url = "/status";
     Response res;
     RequestSpecification request;


     Status(RequestSpecification req){
          this.request = req;
     }

     protected String get(){
          res = request.log().method().and().log().uri().when().get(status_url).then().log().body().extract().response();
          return res.body().asString();
     }
}
