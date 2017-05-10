package org.gobiiproject.gobiiweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


// This class is needed so that when we use an angular2 route, and the
// user hits refresh, we'll be able to route back to the single-page we
// need. This is because when angualr2 router puts gobii-dev/login in the
// URL of the browser, the next thing the browser will do upon refresh
// is do a simple GET, and the GOBIIControllerv1 does not have (and shold not have)
// a resource for that -- it is a pure REST API. We will add redirects (and _only_
// redirects to this controller as our use of the Angular2 router expands.
@Controller
@RequestMapping("/")
public class RedirectController {

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public RedirectView redirectWithUsingRedirectView(RedirectAttributes attributes) {
        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        attributes.addAttribute("attribute", "redirectWithRedirectView");
        return new RedirectView("index.html");
    }
}