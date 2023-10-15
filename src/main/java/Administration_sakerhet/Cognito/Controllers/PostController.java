package Administration_sakerhet.Cognito.Controllers;

import Administration_sakerhet.Cognito.Models.Cognito;
import Administration_sakerhet.Cognito.Models.Post;
import Administration_sakerhet.Cognito.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @PostMapping
    public Object createPost(@RequestBody Post post, @RequestHeader("Amazon-Cognito-Token") String token) {
        // Kollar om användaren är inloggad, med hjälp av en token.
        // Här hanterar jag också unauthorized access.

        if (Cognito.loggedInUser == null) {
            //skickar meddelande till användaren
            return new Exception("Not an authorised user, try to log in first");
        }

        // sparar inlägget till databasen
        return postRepository.save(post);
    }
}