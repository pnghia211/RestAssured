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

public class TestPutRequest extends baseReqresSetup {

    @Test(dataProvider = "getPutData")
    public void testPutRequest(String name, String job, String id) {
        PostData postData = new PostData(name, job);
        given()
                .contentType(ContentType.JSON)
                .body(postData)
                .put("/api/users/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .and()
                .assertThat()
                .body("name", equalTo(name))
                .body("job", equalTo(job))
                .body("updatedAt", notNullValue())
                .extract()
                .response()
                .asString();
    }

    @DataProvider
    public Iterator<Object[]> getPutData() {
        List<Object[]> dataList = new ArrayList<>();
        dataList.add(new Object[]{"Rahul", "QA", "2"});
        dataList.add(new Object[]{"Jane", "Sr.Dev", "10"});
        return dataList.iterator();
    }
}
