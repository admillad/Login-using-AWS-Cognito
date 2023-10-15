package Administration_sakerhet.Cognito.Models;

//Constructors för att göra en ny Post.
public class Post {
    public Post(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    private String id;
    private String title;
    private String content;
}
