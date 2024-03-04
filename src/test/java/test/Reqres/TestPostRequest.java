package test.Reqres;

import base.baseReqresSetup;
import data.Reqres.PostData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestPostRequest extends baseReqresSetup {

    @Test(dataProvider = "getPostData")
    public void testPostRequest(String name, String job) {
        PostData postData = new PostData(name, job);
        given()
                .contentType(ContentType.JSON)
                .body(postData)
                .post("/api/users")
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("name", equalTo(name))
                .body("job", equalTo(job))
                .body("id", notNullValue())
                .body("createdAt", notNullValue())
                .extract()
                .response()
                .asString();
    }

    @DataProvider
    public Iterator<Object[]> getPostData() {
        List<Object[]> dataList = new ArrayList<>();
        dataList.add(new Object[]{"Rahul", "QA"});
        dataList.add(new Object[]{"Jane", "Sr.Dev"});
        dataList.add(new Object[]{"Albert", "Dev"});
        dataList.add(new Object[]{"Johnny", "Project Manager"});
        return dataList.iterator();
    }
}
