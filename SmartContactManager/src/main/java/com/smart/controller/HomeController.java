package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.repository.UserRepository;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;

//	@GetMapping("/test")
//	public String home() {		
//		User user = new User();
//		user.setRole("User");
////		user.setEmail("user14@gmail.com");
//		user.setAbout("My name is user.");
//		user.setEnabled(false);
//		user.setImageUrl("imageurl..");
//		user.setName("USERDADA");
//		user.setPassword("userdada");		
//		this.userRepository.save(user);		
//		return "home";
//	}
	
	@GetMapping("/")
	public String startHome() {
		return "home";
	}
	
	@GetMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping(value = "/do_register",method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("user") User user,BindingResult result ,@RequestParam(value = "agreement",defaultValue = "false") 
	boolean agreement,Model model,HttpSession session) {
		try {
			if(!agreement) {
				System.out.println("You have not agreed the terms & conditions...");
			    throw new Exception("You have not agreed the terms & conditions...");
			}
			
			if(result.hasErrors()) {
				System.out.println("ERROR"+result.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("Role_user");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			this.userRepository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message",new Message("Succesfully registered!!", "alert-success"));
			return "signup";
		} catch (Exception e) {
           e.printStackTrace();
           model.addAttribute("user", user);
           session.setAttribute("message", new Message("Something went wrong!!"+e.getMessage(), "alert-danger"));
   		   return "signup";			
		}
	}

}
