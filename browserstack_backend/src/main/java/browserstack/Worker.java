package browserstack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Worker {
    private static final String worker_url = "/worker";
    private static final String workers_url = "/workers";
    
    Response res;
    RequestSpecification request;


    Worker(RequestSpecification req) {
        this.request = req;
    }

    protected String get() {
        res = request.log().method().and().log().uri().when().get(workers_url).then().log().body().extract().response();
        return res.body().asString();
    }

    protected String get(String id) {
        res = request.log().method().and().log().uri().when().get(worker_url+"/"+id).then().log().body().extract().response();
        return res.body().asString();
    }

    protected String post(String payload){
        res = request.log().method().and().log().uri().body(payload).when().post(worker_url).then().log().body().extract().response();
        return res.body().asString();
    }

    protected String delete(String id){
        res = request.log().method().and().log().uri().when().delete(worker_url+"/"+id).then().log().body().extract().response();
        return res.body().asString();
    }

    protected void capture(String id,String format) throws Exception{

        if(format!="png" && format!="xml" && format!="json")
            throw new Exception("Invalid Format. Only json,png,xml supported");

        byte[] image = request.log().method().and().log().uri().when().get(worker_url+"/"+id+"/screenshot."+format).asByteArray();
        File outputImageFile = new File("images/"+new Date().toString()+"."+format);
        OutputStream outStream = new FileOutputStream(outputImageFile);
        outStream.write(image);
        outStream.close();
        System.out.println("File saved as "+outputImageFile.getName());
    }
}
