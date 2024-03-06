package builder;

import com.google.gson.Gson;

public class JSONBuilder {
    public static <T> String getJSON (T dataType) {
        return new Gson().toJson(dataType);
    }
}
