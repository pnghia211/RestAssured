package data.Booking;

import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class BookingDataBuilder {
    private static final Faker FAKER = Faker.instance();
    private  static final SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");

    public static BookingData getBookingData() {
        return BookingData.builder()
                .firstname(FAKER.name().firstName())
                .lastname(FAKER.name().lastName())
                .totalprice(FAKER.number().numberBetween(1, 50000))
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(formatter.format(FAKER.date().past(10, TimeUnit.DAYS)))
                        .checkout(formatter.format(FAKER.date().future(5, TimeUnit.DAYS)))
                        .build())
                .additionalneeds("Breakfast")
                .build();
    }

    public static PartialBookingData getPartialBookingData () {
        return PartialBookingData.builder()
                .firstname(FAKER.name().firstName())
                .bookingdates(BookingDates.builder()
                        .checkout(formatter.format(FAKER.date().future(10,TimeUnit.DAYS)))
                        .build())
                .build();
    }
}
