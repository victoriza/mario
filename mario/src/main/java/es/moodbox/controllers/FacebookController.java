package es.moodbox.controllers;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/fb")
public class FacebookController {

	private static final Logger log = Logger.getLogger(FacebookController.class.getName());

    private Facebook facebook;

    @Inject
    public FacebookController(Facebook facebook) {
        this.facebook = facebook;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String helloFacebook(Model model) {
    	log.info("model: " + model == null ? " no model " : model.toString()+" @ facebook -- : " + facebook);

    	if (!facebook.isAuthorized()) {
            return "redirect:/connect/facebook";
        }
    	FacebookProfile profile = facebook.userOperations().getUserProfile();
    	log.info("user data: " + profile.getName());
    	log.info("user data: " + profile.getFirstName());
    	log.info("user data: " + profile.getLastName());
    	log.info("user data: " + profile.getEmail());
    	log.info("user data: " + profile.getLink());
    	log.info("user data: " + profile.getLocale());
    	log.info("user data: " + profile.getAgeRange());

    	model.addAttribute(profile);
        //PagedList<Post> homeFeed = facebook.feedOperations().getHomeFeed();
        //model.addAttribute("feed", homeFeed);

        return "hello";
    }

}
