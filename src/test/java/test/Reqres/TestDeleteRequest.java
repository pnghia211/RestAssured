package test.Reqres;

import base.baseReqresSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestDeleteRequest extends baseReqresSetup {

    @Test(dataProvider = "getUserData")
    public void testDeleteRequest(int userId) {
        given().delete("/api/users/" + userId)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @DataProvider
    public Iterator<Object[]> getUserData() {
        List<Object[]> dataList = new ArrayList<>();
        dataList.add(new Object[]{2});
        dataList.add(new Object[]{10});
        return dataList.iterator();
    }
}
