package es.moodbox.controllers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import app.Greeting;
import es.moodbox.dao.IUserDAO;

@Controller // Defines that this class is a spring bean
@RestController
public class EmailController {
    
	private static final Logger log = Logger.getLogger(EmailController.class.getName());

	// Tells the application context to inject an instance of UserService here
    @Autowired
    private IUserDAO userDAO;
    
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping(value = "/me", method = RequestMethod.POST)
    public String postUser(@RequestBody String requestBody) {

        final BasicDBObject user = (BasicDBObject) JSON.parse(requestBody);

        final String email = user.containsField("email") ? user.get("email").toString() : null;
        final String password = user.containsField("password") ? user.get("password").toString() : null;

        log.info("email: " + email + " password: " + password);
        final Document existingUser = userDAO.find("email", email);

        if (existingUser == null) {
            //All ok, we can create the user
            userDAO.createUser(user);
            return "All ok";
        } else {
            log.warning("email is already in use");
            return "User already exists";
        }
    }
    
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public String me(
            @RequestHeader("user-agent") String userAgent,
            @RequestHeader("accept-language") String lang,
            @RequestHeader(value="Authorization", defaultValue="0", required=true) String token) {

        log.info("user-agent: " + userAgent);
        log.info("token: " + token);
        log.info("lang: " + lang);

        log.info("GET user that has token: " + token);

        //Invalid credentials
        if (token == null || token.isEmpty()) {
            return "Invalid auth";
        } else {
        	final Document user = userDAO.findUserByToken(token);
        	if (user == null) {
        		return "User with token: "+token+ " was not found";
        	} else {
        		return user.toJson();
        	}
        }
    }
    
    @RequestMapping(value = "/nobody", method = RequestMethod.GET)
    public String getUser() {
    	
    	if (userDAO == null) {
    		return "AutoWired is failing";
    	}
    	
    	log.info("System is ok");

    	final String userDocument = userDAO.findUserByToken("AIzaSyDI-sgcdYEm3K_WPeSXUkiDyBMFQJoxzRA").toJson();
        return userDocument;
    }
}
