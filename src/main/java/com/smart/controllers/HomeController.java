package com.smart.controllers;

import java.awt.Stroke;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.data.repository.query.parser.Part.IgnoreCaseType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.ImageUploadCloudService;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.MessageHelper;
import com.smart.mailservice.MailService;

import jakarta.annotation.Generated;
import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private MailService mailService;
	
	private final String from="aftabjamil2022@gift.edu.in";
	public static String generateOTP(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive number.");
        }
        
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            otp.append(digit);
        }

        return otp.toString();
    }
	@ModelAttribute
	public void removeUnwantedMessage(HttpSession session) {
		session.removeAttribute("message");
	}
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home-Smart Contact Manager");
		return "home";
		
	}
	
	
	@RequestMapping("/about")
	public String toAbout(Model model) {
		model.addAttribute("title", "About-Smart Contact Manager");
		System.out.println("fetching...");
		User user= userRepository.getUserByEmail("aftabjamil07861@gmail.com");
		model.addAttribute("user", user);
		return "about";	
	}
	@GetMapping("/signin")
	public String toLogin(Model model) {
		model.addAttribute("title", "Login-Smart Contact Manager");
		return "login";
			
	}
	
	@GetMapping("/signup")
	public String toSign(Model model) {
		model.addAttribute("title", "Sign Up-Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
		
	}
	@PostMapping("/success")
	public String signed(@Valid @ModelAttribute("user") User user,BindingResult bindingResult,Model m,@RequestParam(value = "agreement" ,defaultValue = "false") boolean agreement,HttpSession session) {
		try {
		if(!agreement) {
			System.out.println("you have not agreed terms and conditions");
			throw new Exception("you have not agreed terms and conditions");
		}
		if(bindingResult.hasErrors()) {
			System.out.println(bindingResult.toString());
			return "signup";
		}
		user.setRole("ROLE_NORMAL");
		user.setEnabled(true);
		user.setImageUrl("https://res.cloudinary.com/desbyrloi/image/upload/c_fill,h_300,w_300/user.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));  // it is used to save password in encoded type in database so that no one can know your password even admin or web ownner
		String subject="Smart Contact Manager : otp verification";
		int otp=Integer.parseInt(generateOTP(6));
		String message="your otp for registring on smart contact manager is : "+otp;
		// cheaking either this email already present in database or not
		User isUser =userRepository.getUserByEmail(user.getEmail());
		if(isUser==null) {
			mailService.sendMail(from,user.getEmail(), subject, message);
			m.addAttribute("user", user);
			session.setAttribute("registeringUser", user);// session will store data temporarily
			session.setAttribute("otp", otp);
			return "otpverification";
		}
		session.removeAttribute("message");
		session.setAttribute("message", new MessageHelper("this email is already register","alert-warning"));
		m.addAttribute("user", user);
		return "signup";
//		User result=userRepository.save(user);
//		m.addAttribute("user", new User());
//		session.setAttribute("message",new MessageHelper("successfully Registered ","alert-success"));
//		return "signup";
		}catch(Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message",new MessageHelper("Something went wrong! "+e.getMessage(),"alert-warning"));
			return "signup";
		}
	}
	
	@PostMapping("/otpverification")
	public String otpVerification(@RequestParam("otp") String otp,HttpSession session,Model model) {
		int genOtp=(int) session.getAttribute("otp");
		User user=(User) session.getAttribute("registeringUser");
		System.out.println("i am running");
		if(genOtp==Integer.parseInt(otp)) {
			User result=userRepository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message",new MessageHelper("successfully Registered ","alert-success"));
			return "signup";
		}
		model.addAttribute("user",user);
		session.setAttribute("message",new MessageHelper("Something went wrong! or wrong OTP ","alert-warning"));
		return "signup";
		
	}
	@GetMapping("/forgot_password")
	public String forgotPasswordPage() {
		return "forgotpasswordpage";
	}
	@PostMapping("/otpforgotpassword")
	public String toOtpPage(@RequestParam("email") String email,Model model,HttpSession session) {
		model.addAttribute("email", email);
		User user=userRepository.getUserByEmail(email);
		if(user!=null) {
			session.removeAttribute("otp"); // it will remove key-value pair whose key is otp if its available
			int otp=Integer.parseInt(generateOTP(6));
			session.setAttribute("otp",otp);
			session.removeAttribute("user");
			session.setAttribute("user",user);
			String subject="Smart Contact Manager : forgor password otp verification";
			String message="your otp for forgot password is : "+otp;
			mailService.sendMail(from, email, subject, message);
			return "otpverificationforforgotpassword";
		}
		session.removeAttribute("message");
		session.setAttribute("message", new MessageHelper("this email id is not register please sign up", "alert-warning"));
		return "forgotpasswordpage";
	}
	@PostMapping("/verifyotp")
	public String verifyOtp(@RequestParam("otp") String otp,HttpSession session){
		int genOtp=(int)session.getAttribute("otp");
		User user=(User)session.getAttribute("user");
		if(genOtp==Integer.parseInt(otp)) {
			return "save_password";
		}
		session.removeAttribute("message");
		session.setAttribute("message", new MessageHelper("otp enter is wrong", "alert-warning"));
		return "forgotpasswordpage";
		
	}
	@PostMapping("/savepassword")
	public String savingNewPassword(@RequestParam("new_password") String newPass,@RequestParam("confirm_password") String confirmPass,HttpSession session) {
		if(newPass.equals(confirmPass)) {
			User user=(User)session.getAttribute("user");
			user.setPassword(passwordEncoder.encode(newPass));
			userRepository.save(user);
			session.removeAttribute("message");
			session.setAttribute("message", new MessageHelper("password changed successfully","alert-success"));
			return "login";
		}
		session.removeAttribute("message");
		session.setAttribute("message", new MessageHelper("new password and confirm password not matched","alert-warning"));
		return "save_password";
	}
	
	
	

}
