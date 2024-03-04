package data.Reqres;

import lombok.Data;

@Data
public class PostData {
    String name;
    String job;

    public PostData(String name, String job) {
        this.name = name;
        this.job = job;
    }
}
