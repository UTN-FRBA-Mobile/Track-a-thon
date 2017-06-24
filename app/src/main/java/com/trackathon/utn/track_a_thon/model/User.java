package com.trackathon.utn.track_a_thon.model;

import java.io.Serializable;

public class User implements Serializable {

    static final long serialVersionUID = 0L;

    private static User CurrentUser = null;
    private static User USAIN_BOLT = new User() {{
        setName("Usain Bolt");
        setEmail("usain.bolt@gmail.com");
        setImageUrl("http://rs1226.pbsrc.com/albums/ee410/punjabiportal/Usain-Bolt.jpg~c200");
    }};

    public static User getCurrentUser() {
        return CurrentUser != null ? CurrentUser : USAIN_BOLT;
    }
    public static void setCurrentUser(User user) {
        CurrentUser = user;
    }

    private String name;
    private String email;
    private String imageUrl;

    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
