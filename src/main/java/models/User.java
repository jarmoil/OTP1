package models;

public class User {
    private int userId;
    private String userName;
    private String userPassword;
    private String role;

    public User(int userId, String userName, String userPassword, String role) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.role = role;
    }

    public int getId() { return userId; }
    public String getName() { return userName; }
    public String getPassword() { return userPassword; }
    public String getRole() { return role; }
}
