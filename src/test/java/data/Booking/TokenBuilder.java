package data.Booking;

public class TokenBuilder {
    public static Tokencreds getTokencreds () {
        return Tokencreds.builder()
                .username("admin")
                .password("password123")
                .build();
    }
}
