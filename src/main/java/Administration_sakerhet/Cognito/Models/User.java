package Administration_sakerhet.Cognito.Models;

public class User {

    //Constructors för att göra en ny användare.
    public User(String username, String refreshToken, String accessToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }

    public String username;
    public String refreshToken;
    public String AccessToken;
}
