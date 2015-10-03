package es.moodbox.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import es.moodbox.dao.IUserDAO;
import es.moodbox.utils.TokenHelper;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Created by victoriza on 27/08/15.
 */
@Component
public class MongoUserDAO implements IUserDAO {

    private static final Logger log = Logger.getLogger(MongoUserDAO.class.getName());

    private final static String TOKEN_KEY = "token";

    private final static String MARIO_DB = "mario";
    private final static String USERS_COLLECTION = "users";

    public void createUser(BasicDBObject user) {
        MongoCollection<Document> collection = getUsersCollection();
        log.info(user.toString());

        Document doc = new Document(user);
        doc.put(TOKEN_KEY, TokenHelper.generateToken());

        collection.insertOne(doc);
    }

    public Document findUserByToken(String token) {
        return find(TOKEN_KEY, token);
    }

    public Document find(String key, String value) {
        MongoCollection<Document> collection = getUsersCollection();

        Document doc = new Document();
        doc.put(key, value);

        FindIterable<Document> iterable = collection.find(doc).limit(1);
        if (iterable.iterator().hasNext()) {
            return iterable.iterator().next();
        } else {
            return null;
        }
    }

    private MongoCollection<Document> getUsersCollection(MongoDatabase db) {
        return db.getCollection(USERS_COLLECTION);
    }
    private MongoCollection<Document> getUsersCollection() {
        MongoClient mongoClient = new MongoClient();
        return getUsersCollection(getDatabase(mongoClient));
    }

    private MongoDatabase getDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(MARIO_DB);
    }
}
