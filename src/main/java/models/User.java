package models;

public class User {
    private int user_id;
    private String user_name;
    private String user_password;
    private String role;

    public User(int user_id, String user_name, String user_password, String role) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_password = user_password;
        this.role = role;
    }

    public int getId() { return user_id; }
    public String getName() { return user_name; }
    public String getPassword() { return user_password; }
    public String getRole() { return role; }
}
