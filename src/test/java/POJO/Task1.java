package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task1 {
    private int userId;
    private int id;
    private String baslik;
    private Boolean completed;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }
    @JsonProperty("title")
    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Task1{" +
                "userId='" + userId + '\'' +
                ", id=" + id +
                ", baslik/title='" + baslik + '\'' +
                ", completed='" + completed + '\'' +
                '}';
    }
}
