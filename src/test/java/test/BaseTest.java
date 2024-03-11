package test;

import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import utils.AuthTokenHandler;
import utils.RequestCapability;

import static io.restassured.RestAssured.given;

public class BaseTest implements RequestCapability {

    protected String projectKey;
    protected String credString;
    protected String baseUri;
    protected RequestSpecification request;

    @BeforeSuite
    public void beforeSuite () {
        credString = AuthTokenHandler.getCredString(email, token);
        baseUri = "https://pnghia211.atlassian.net";
        projectKey = "RES";
    }

    @BeforeTest
    public void beforeTest () {
        request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(acceptJSONHeader);
        request.header(getAuth.apply(credString));
    }
}
