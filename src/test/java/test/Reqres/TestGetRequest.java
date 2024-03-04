package test.Reqres;

import base.baseReqresSetup;
import data.Reqres.PostData;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestGetRequest extends baseReqresSetup {

    @Test(dataProvider = "getUserData")
    public void testGetRequest(int userId) {
        given().get("/api/users/" + userId)
                .then()
                .log().all()
                .statusCode(200)
                .and()
                .assertThat()
                .body("data.id",equalTo(userId))
                .extract()
                .response()
                .asString();
    }

    @DataProvider
    public Iterator<Object[]> getUserData() {
        List<Object[]> dataList = new ArrayList<>();
        dataList.add(new Object[]{2});
        dataList.add(new Object[]{10});
        return dataList.iterator();
    }
}
