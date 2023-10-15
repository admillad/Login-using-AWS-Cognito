package Administration_sakerhet.Cognito.Repositories;

import Administration_sakerhet.Cognito.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    public User findByUsername(String username);

    //Sparar en användare till Repo.
    @Override
    public User save(User user);

}