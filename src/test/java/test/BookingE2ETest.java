package test;

import base.baseBookingSetup;
import data.Booking.BookingData;
import data.Booking.PartialBookingData;
import data.Booking.Tokencreds;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static data.Booking.BookingDataBuilder.*;
import static data.Booking.TokenBuilder.getTokencreds;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BookingE2ETest extends baseBookingSetup {
    private BookingData newBooking;
    private BookingData updatedBooking;
    private Tokencreds tokencreds;
    private PartialBookingData partialBookingData;
    private int bookingid;

    @BeforeTest
    public void testSetup() {
        newBooking = getBookingData();
        updatedBooking = getBookingData();
        tokencreds = getTokencreds();
        partialBookingData = getPartialBookingData();
    }

    @Test
    public void createNewBooking() {
        bookingid = given()
                .body(newBooking)
                .when().post("/booking")
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo(newBooking.getFirstname()))
                .body("booking.lastname", equalTo(newBooking.getLastname()))
                .body("booking.totalprice", equalTo(newBooking.getTotalprice()))
                .body("booking.depositpaid", equalTo(newBooking.isDepositpaid()))
                .body("booking.bookingdates.checkin", equalTo(newBooking.getBookingdates().getCheckin()))
                .body("booking.bookingdates.checkout", equalTo(newBooking.getBookingdates().getCheckout()))
                .body("booking.additionalneeds", equalTo(newBooking.getAdditionalneeds()))
                .extract().path("bookingid");
    }

    @Test
    public void getBooking() {
        given().get("/booking/" + bookingid)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("firstname", equalTo(newBooking.getFirstname()))
                .body("lastname", equalTo(newBooking.getLastname()))
                .body("totalprice", equalTo(newBooking.getTotalprice()))
                .body("depositpaid", equalTo(newBooking.isDepositpaid()))
                .body("bookingdates.checkin", equalTo(newBooking.getBookingdates().getCheckin()))
                .body("bookingdates.checkout", equalTo(newBooking.getBookingdates().getCheckout()))
                .body("additionalneeds", equalTo(newBooking.getAdditionalneeds()));
    }

    @Test
    public void updateBooking() {
        given().body(updatedBooking)
                .header("Cookie", "token=" + getToken())
                .put("/booking/" + bookingid)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("firstname", equalTo(updatedBooking.getFirstname()))
                .body("lastname", equalTo(updatedBooking.getLastname()))
                .body("totalprice", equalTo(updatedBooking.getTotalprice()))
                .body("depositpaid", equalTo(updatedBooking.isDepositpaid()))
                .body("bookingdates.checkin", equalTo(updatedBooking.getBookingdates().getCheckin()))
                .body("bookingdates.checkout", equalTo(updatedBooking.getBookingdates().getCheckout()))
                .body("additionalneeds", equalTo(updatedBooking.getAdditionalneeds()));
    }

    @Test
    public void updatePartialBooking() {
        given().body(partialBookingData)
                .header("Cookie", "token=" + getToken())
                .patch("/booking/" + bookingid)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("firstname", equalTo(partialBookingData.getFirstname()))
                .body("bookingdates.checkout", equalTo(partialBookingData.getBookingdates().getCheckout()));
    }

    @Test
    public void deleteBooking() {
        given().header("Cookie", "token=" + getToken())
                .delete("/booking/" + bookingid)
                .then()
                .statusCode(201);
    }

    private String getToken() {
        return given()
                .body(tokencreds)
                .post("/auth")
                .then()
                .statusCode(200)
                .assertThat()
                .body("token", notNullValue())
                .extract()
                .path("token");
    }
}
