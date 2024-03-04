package test.Reqres;

import base.baseReqresSetup;
import data.Reqres.AuthenticationPOJO;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Test(dataProvider = "getAthData")
public class TestAuthenticationToken extends baseReqresSetup {

    public void testAuthToken(String email, String password) {
        AuthenticationPOJO requestBody = new AuthenticationPOJO(email, password);

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .and()
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @DataProvider
    public Iterator<Object[]> getAthData() {
        List<Object[]> getData = new ArrayList<>();
        getData.add(new Object[]{"eve.holt@reqres.in", "pistol"});
        return getData.iterator();
    }
}
