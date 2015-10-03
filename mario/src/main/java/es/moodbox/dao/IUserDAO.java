package es.moodbox.dao;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.stereotype.Service;

/**
 * Created by victoriza on 29/08/15.
 */

public interface IUserDAO {

    public void createUser(BasicDBObject user);

    public Document findUserByToken(String token);

    public Document find(String key, String value);
}
