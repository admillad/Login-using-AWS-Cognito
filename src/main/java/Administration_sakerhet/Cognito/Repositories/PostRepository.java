package Administration_sakerhet.Cognito.Repositories;

import Administration_sakerhet.Cognito.Models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    //Sparar ett inlägg till repo.
    @Override
    public Post save(Post post);
}

