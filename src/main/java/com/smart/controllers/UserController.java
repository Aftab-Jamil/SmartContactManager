package com.smart.controllers;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.aspectj.weaver.patterns.IfPointcut.IfFalsePointcut;
import org.hibernate.boot.jaxb.hbm.transform.SourceColumnAdapterJaxbHbmColumnType;
import org.hibernate.property.access.spi.EnhancedSetterImpl;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToUrl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.razorpay.*;
import com.smart.config.HelperToFindUserName;
import com.smart.ImageUploadCloudService;
import com.smart.config.GeneralBeans;
import com.smart.config.OAuthAuthenticationSuccessHandler;
import com.smart.dao.ContactRepository;
import com.smart.dao.OrderRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.ChangePassword;
import com.smart.entities.Contact;
import com.smart.entities.Orders;
import com.smart.entities.User;
import com.smart.helper.MessageHelper;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
//@PreAuthorize("hasRole('ROLE_NORMAL')")
@RequestMapping("/user")
public class UserController {
//	@Autowired
//	private UserRepository userRepository;
//	@Autowired
//	private ContactRepository contactRepository;
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
//	@Autowired
//	private OrderRepository orderRepository;
//	
//	private String path=new ClassPathResource("static/images/").getFile().getAbsolutePath();
//	
//	@ModelAttribute
//	public void passingCommonThingsToEachHandler(Model model,Principal principal,HttpSession session) {
//		String username=principal.getName();
//		User user=userRepository.getUserByEmail(username);
//		model.addAttribute("user", user);
//		System.out.println("user: "+user);
//		session.removeAttribute("message");
//		
//	}
//	
//	public UserController() throws Exception{
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	
//	@RequestMapping("/index")
//	public String dashboard(Model model) {
//		model.addAttribute("title", "SCM-Home");
//		return "common/user_dashboard";
//	}
//	@GetMapping("/add_contact")
//	public String add_Contact(Model model) {
//		model.addAttribute("title", "Add Contact");
//		model.addAttribute("contact", new Contact());
//		return "common/add_contact";
//	}
//	@PostMapping("/added")
//	public String contactAdded(@Valid @ModelAttribute("contact") Contact contact,
//			BindingResult bindingResult,
//			Model model,
//			@RequestParam("imagefile") MultipartFile multipartFile,
//			Principal principal,
//			HttpSession session) {
//	try {
//		if(bindingResult.hasErrors()) {
//			throw new Exception();
//		}
//		
//		if(!multipartFile.isEmpty()) {
//			contact.setImage(multipartFile.getOriginalFilename());
//			System.out.println("not enmpty");
//			 try {
//				Files.copy(multipartFile.getInputStream(),Paths.get(path+File.separator+multipartFile.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}else {
//			contact.setImage("user.png");
//		}
//		String email= principal.getName();
//		User user=userRepository.getUserByEmail(email);
//		contact.setUser(user);
//		user.getContacts().add(contact);
//		userRepository.save(user);
//		model.addAttribute("contact", new Contact());
//		session.setAttribute("message", new MessageHelper("Added Successfully","success"));
//		}
//	catch (Exception e) {
//			e.printStackTrace();
//			session.setAttribute("message", new MessageHelper("Something Went Wrong", "danger"));
//		}
//		return "common/add_contact";
//	}
//	@GetMapping("/contacts/{page}")
//	public String showContacts(@PathVariable("page")int page,Model model,Principal principal) {
//		String email=principal.getName();
//		int userId=userRepository.getUserByEmail(email).getId();
//		Pageable pageable= PageRequest.of(page, 5);
//		Page<Contact> contacts= contactRepository.findAllBYUserId(userId,pageable);
//		model.addAttribute("contacts", contacts);
//		model.addAttribute("Currentpage", page);
//		model.addAttribute("totalpage",contacts.getTotalPages());
//		model.addAttribute("title", "Your contacts");
//		
//		return "common/showcontacts";
//	}
//	@GetMapping("/{id}/contact")
//	public String contactDetail(@PathVariable("id") int id,Model model,Principal principal) {
//		String email= principal.getName();
//		int loginedUserId=userRepository.getUserByEmail(email).getId();
//		Optional<Contact> optContact= contactRepository.findById(id);
//		if(optContact.isEmpty()) {
//			return "common/contactdetail";
//		}
//		Contact contact=optContact.get();
//		if(loginedUserId==contact.getUser().getId())
//			model.addAttribute("contact", contact);
//		return "common/contactdetail";
//	}
//	@GetMapping("/delete/{id}/{currentpage}")
//	public String deleteContact(@PathVariable("id") int id,@PathVariable("currentpage") int currentpage,Principal principal,HttpSession session) {
//		session.removeAttribute("message");
//		String email=principal.getName();
//		int loginedUserId=userRepository.getUserByEmail(email).getId();
//		
//		Contact contact=contactRepository.findById(id).get();
//		if(loginedUserId==contact.getUser().getId()) {
//			contact.setUser(null);
//			contactRepository.delete(contact);
//			session.setAttribute("message",new MessageHelper("Successfully Deleted", "success"));
//		}else {
//			session.setAttribute("message",new MessageHelper("Action Denied: Wrong Access", "danger"));
//		}
//		return "redirect:/user/contacts/{currentpage}";
//	}
//	@PostMapping("/update/{cId}")
//	public String updateContact(@PathVariable("cId") int cId,Model model) {
//		model.addAttribute("title", "SMC-Update Contact");
//		Contact contact= contactRepository.findById(cId).get();
//		model.addAttribute("contact",contact);
//		return "common/update";
//	}
//	@PostMapping("/updateprocess")
//	public String updateChanges(@ModelAttribute Contact contact,Model model,Principal principal,@RequestParam("imagefile") MultipartFile multipartFile) {
//		try {
//			// fetching database contact having this id
//			Contact dbContact=contactRepository.findById(contact.getcId()).get();
//			if(multipartFile.isEmpty()) {
//				contact.setImage(dbContact.getImage());
//			}
//			else {
//				//deleting older photo
//				if(multipartFile.getOriginalFilename()!="user.png")
//					Files.deleteIfExists(Paths.get(path+File.separator+dbContact.getImage()));
//				
//				//setting new photo
//				contact.setImage(multipartFile.getOriginalFilename());
//				Files.copy(multipartFile.getInputStream(), Paths.get(path+File.separator+multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//			}
//			User user=userRepository.getUserByEmail(principal.getName());
//			contact.setUser(user); // this contact is come from update form with cid is same as in database bcs chages not reflect till now
//			contactRepository.save(contact);
//			model.addAttribute("contact", contact);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "common/contactdetail";
//	}
//	@GetMapping("/userprofile")
//	public String toUserProfile() {
//		return "common/userprofile";
//	}
//	@PostMapping("/updateuser")
//	public String updateUser() {
//		return "common/updateuser";
//	}
//	@PostMapping("/updatechanges")
//	public String updateChangesInUser(@ModelAttribute User user,@RequestParam("imagefile") MultipartFile multipartFile) {
//		try {
//			System.out.println(user.getAbout());
//			System.out.println(user.getName());
//		User dbUser=userRepository.findById(user.getId()).get();
//		dbUser.setName(user.getName());
//		dbUser.setAbout(user.getAbout());
//		if(multipartFile.isEmpty()) {
//			dbUser.setImageUrl("user.png");
//		}
//		else {
//			//deleting old image
//			Files.deleteIfExists(Paths.get(path+File.separator+user.getImageUrl()));
//			
//			//uploading new image
//			Files.copy(multipartFile.getInputStream(), Paths.get(path+File.separator+user.getImageUrl()), StandardCopyOption.REPLACE_EXISTING);
//			dbUser.setImageUrl(multipartFile.getOriginalFilename());
//		}
//		userRepository.save(dbUser);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "common/userprofile";
//	}
//	@GetMapping("/passwordsetting")
//	public String passwordSetting(Model model) {
//		model.addAttribute("title", "SCM-Settings");
//		model.addAttribute("changePassword", new ChangePassword());
//		return "common/settings";
//	}
//	@PostMapping("/changepassword")
//	public String toChangePassword(@Valid @ModelAttribute ChangePassword changePassword,BindingResult bindingResult,Principal principal,HttpSession session) throws Exception {
//		try {
//			
//			if(bindingResult.hasErrors()) {
//				return "common/settings";
//			}
//			User user=userRepository.getUserByEmail(principal.getName());
//			if(!changePassword.getNew_password().equals(changePassword.getConfirm_password())) {
//				session.setAttribute("message", new MessageHelper("new password and confirm password does not matches","alert-warning"));
//			}
//			else if(bCryptPasswordEncoder.matches(changePassword.getOld_password(), user.getPassword())){
//				user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNew_password()));
//				session.setAttribute("message", new MessageHelper("password changes successfully","alert-success"));
//				userRepository.save(user);
//			}
//			else {
//				session.setAttribute("message", new MessageHelper("Enter old password is wrong","alert-warning"));
//			}
//			return "common/settings";
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "common/user_dashboard";  // never executed
//	
//	}
//	
//	
//	// payment integration code
//	@PostMapping("/create_order")
//	@ResponseBody
//	public String creatingOrder(@RequestBody Map<String, Object> data,Principal principal) throws Exception {
//		// information getting from form of payment
//		int amount=Integer.parseInt(data.get("amount").toString());
//		String info=data.get("info").toString();
//		System.out.println(amount);
//		System.out.println(info);
//		
//		//
//		RazorpayClient rozarClientMe= new RazorpayClient("rzp_test_bz6KxeRkMV1MiJ","8nuJneWViH0SMaVE2SuYytjG");
//		
//		JSONObject orderRequest = new JSONObject();
//		orderRequest.put("amount",amount*100);
//		orderRequest.put("currency","INR");
//		Order order =rozarClientMe.orders.create(orderRequest);
//		System.out.println(order);
//		// saving created order in our database
//		Orders orders=new Orders();
//		int amt=order.get("amount");
//		orders.setAmount(amt/100);
//		orders.setOrderId(order.get("id"));
//		orders.setStatus("created");
//		orders.setUser(userRepository.getUserByEmail(principal.getName()));
//		orderRepository.save(orders);
//			
//		return order.toString();
//		
//	}
//	@PostMapping("/updatestatus")
//	public void updateOrderStatus(@RequestBody Map<String,Object> data) {
//		String orderId=(String) data.get("orderId");
//		Orders orders=orderRepository.getByorderid(orderId);
//		orders.setPaymentId(data.get("paymentId").toString());
//		orders.setStatus(data.get("status").toString());
//		orderRepository.save(orders);
//		
//	}
//	
	
	/*
	  Difference between above and below code is of only using Authentication in place of Principal : Authentication is an interface which extends of Principal interface
	  * Above code can be used when we  login using email and password which is saved
	  * Below code can be used when we login normally or google login or github login or any other provider because when we login with these provider then they gives id
	   in place of email (as a username) so to get email we use Authenctication in place of Principal
	  
	 */
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ImageUploadCloudService imageUploadCloudService;
	private String path=new ClassPathResource("static/images/").getFile().getAbsolutePath();
	
	@ModelAttribute
	public void passingCommonThingsToEachHandler(Model model,Authentication authentication,HttpSession session) {
		String username=HelperToFindUserName.getEmailOfLoggedinUser(authentication);
		User user=userRepository.getUserByEmail(username);
		model.addAttribute("user", user);
		System.out.println("user: "+user);
		session.removeAttribute("message");
		
	}
	
	public UserController() throws Exception{
		super();
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("/index")
	public String dashboard(Model model) {
		model.addAttribute("title", "SCM-Home");
		return "common/user_dashboard";
	}
	@GetMapping("/add_contact")
	public String add_Contact(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "common/add_contact";
	}
	@PostMapping("/added")
	public String contactAdded(@Valid @ModelAttribute("contact") Contact contact,
			BindingResult bindingResult,
			Model model,
			@RequestParam("imagefile") MultipartFile multipartFile,
			Authentication authentication,
			HttpSession session) {
	try {
		if(bindingResult.hasErrors()) {
			throw new Exception();
		}
		
		if(!multipartFile.isEmpty()) {
		
			System.out.println("not enmpty");
			 // 1. below will save image on folder of computer
//			contact.setImage(multipartFile.getOriginalFilename());
//			 try {
//				Files.copy(multipartFile.getInputStream(),Paths.get(path+File.separator+multipartFile.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
//			 } catch (IOException e) {
//				e.printStackTrace();
//			}
			 // 2. below will save image on cloudanary cloud
			String imageurl= imageUploadCloudService.uploadImage(multipartFile);
			contact.setImage(imageurl);
			
		}else {
			contact.setImage("https://res.cloudinary.com/desbyrloi/image/upload/c_fill,h_300,w_300/user.png");
		}
		String email= HelperToFindUserName.getEmailOfLoggedinUser(authentication);
		User user=userRepository.getUserByEmail(email);
		contact.setUser(user);
		user.getContacts().add(contact);
		userRepository.save(user);
		model.addAttribute("contact", new Contact());
		session.setAttribute("message", new MessageHelper("Added Successfully","success"));
		}
	catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new MessageHelper("Something Went Wrong", "danger"));
		}
		return "common/add_contact";
	}
	@GetMapping("/contacts/{page}")
	public String showContacts(@PathVariable("page")int page,Model model,Authentication authentication) {
		String email=HelperToFindUserName.getEmailOfLoggedinUser(authentication);
		int userId=userRepository.getUserByEmail(email).getId();
		Pageable pageable= PageRequest.of(page, 5);
		Page<Contact> contacts= contactRepository.findAllBYUserId(userId,pageable);
		List<Contact> allContacts=contactRepository.findByUser(userId);
		model.addAttribute("allContacts", allContacts);
		
		model.addAttribute("contacts", contacts);
		model.addAttribute("Currentpage", page);
		model.addAttribute("totalpage",contacts.getTotalPages());
		model.addAttribute("title", "Your contacts");
		
		return "common/showcontacts";
	}
	@GetMapping("/{id}/contact")
	public String contactDetail(@PathVariable("id") int id,Model model,Authentication authentication) {
		String email=HelperToFindUserName.getEmailOfLoggedinUser(authentication);
		int loginedUserId=userRepository.getUserByEmail(email).getId();
		Optional<Contact> optContact= contactRepository.findById(id);
		if(optContact.isEmpty()) {
			return "common/contactdetail";
		}
		Contact contact=optContact.get();
		if(loginedUserId==contact.getUser().getId())
			model.addAttribute("contact", contact);
		return "common/contactdetail";
	}
	@GetMapping("/delete/{id}/{currentpage}")
	public String deleteContact(@PathVariable("id") int id,@PathVariable("currentpage") int currentpage,Authentication authentication,HttpSession session) {
		session.removeAttribute("message");
		String email=HelperToFindUserName.getEmailOfLoggedinUser(authentication);
		int loginedUserId=userRepository.getUserByEmail(email).getId();
		
		Contact contact=contactRepository.findById(id).get();
		if(loginedUserId==contact.getUser().getId()) {
			contact.setUser(null);
			try {
				imageUploadCloudService.deleteImage(contact.getImage().substring(69));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			contactRepository.delete(contact);
			session.setAttribute("message",new MessageHelper("Successfully Deleted", "success"));
		}else {
			session.setAttribute("message",new MessageHelper("Action Denied: Wrong Access", "danger"));
		}
		return "redirect:/user/contacts/{currentpage}";
	}
	@PostMapping("/update/{cId}")
	public String updateContact(@PathVariable("cId") int cId,Model model) {
		model.addAttribute("title", "SMC-Update Contact");
		Contact contact= contactRepository.findById(cId).get();
		model.addAttribute("contact",contact);
		return "common/update";
	}
	@PostMapping("/updateprocess")
	public String updateChanges(@ModelAttribute Contact contact,Model model,Authentication authentication,@RequestParam("imagefile") MultipartFile multipartFile) {
		try {
			// fetching database contact having this id
			Contact dbContact=contactRepository.findById(contact.getcId()).get();
			if(multipartFile.isEmpty()) {
				contact.setImage(dbContact.getImage());
			}
			else {
				// 1. updating photo in folder
//				//deleting older photo
//				if(multipartFile.getOriginalFilename()!="user.png")
//					Files.deleteIfExists(Paths.get(path+File.separator+dbContact.getImage()));
//				
//				//setting new photo
//				contact.setImage(multipartFile.getOriginalFilename());
//				Files.copy(multipartFile.getInputStream(), Paths.get(path+File.separator+multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
				//2. updating photo in cloud
//				System.out.println(dbContact.getImage());
//				System.out.println(dbContact.getImage().substring(69)); 
				String imageUrl=imageUploadCloudService.updateImage(multipartFile, dbContact.getImage().substring(69));
				contact.setImage(imageUrl);
				
			}
			User user=userRepository.getUserByEmail(HelperToFindUserName.getEmailOfLoggedinUser(authentication));
			contact.setUser(user); // this contact is come from update form with cid is same as in database bcs chages not reflect till now
			contactRepository.save(contact);
			model.addAttribute("contact", contact);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return "common/contactdetail";
	}
	@GetMapping("/userprofile")
	public String toUserProfile() {
		return "common/userprofile";
	}
	@PostMapping("/updateuser")
	public String updateUser() {
		return "common/updateuser";
	}
	@PostMapping("/updatechanges")
	public String updateChangesInUser(@ModelAttribute User user,@RequestParam("imagefile") MultipartFile multipartFile,Authentication authentication) {
		try {
			System.out.println(user.getAbout());
			System.out.println(user.getName());
		User dbUser=userRepository.findById(user.getId()).get();
		dbUser.setName(user.getName());
		dbUser.setAbout(user.getAbout());
		if(multipartFile.isEmpty()) {
			//northing to do
		}
		else {
			// 1. changing user image for saved in project folder 
//			//deleting old image
//			Files.deleteIfExists(Paths.get(path+File.separator+user.getImageUrl()));
//			
//			//uploading new image
//			Files.copy(multipartFile.getInputStream(), Paths.get(path+File.separator+user.getImageUrl()), StandardCopyOption.REPLACE_EXISTING);
//			dbUser.setImageUrl(multipartFile.getOriginalFilename());
			// 2. changing user image for cloud saved
//			String imageUrl="";
//			if(HelperToFindUserName.getProvider(authentication)==null)
//				 imageUrl=imageUploadCloudService.updateImage(multipartFile,dbUser.getImageUrl().substring(69));
//			else {
//				System.out.println(HelperToFindUserName.getProvider(authentication));
//				imageUrl=imageUploadCloudService.uploadImage(multipartFile);
//			}
			String imageUrl="";
			if(dbUser.getImageUrl().length()>69) {
			System.out.println("greater than 69");
				imageUrl=imageUploadCloudService.updateImage(multipartFile,dbUser.getImageUrl().substring(69));
			}
			else {
				System.out.println("less than 69");
				imageUrl=imageUploadCloudService.uploadImage(multipartFile);
			}
			dbUser.setImageUrl(imageUrl);
		}
		userRepository.save(dbUser);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return "common/userprofile";
	}
	@GetMapping("/passwordsetting")
	public String passwordSetting(Model model) {
		model.addAttribute("title", "SCM-Settings");
		model.addAttribute("changePassword", new ChangePassword());
		return "common/settings";
	}
	@PostMapping("/changepassword")
	public String toChangePassword(@Valid @ModelAttribute ChangePassword changePassword,BindingResult bindingResult,Authentication authentication,HttpSession session) throws Exception {
		try {
			
			if(bindingResult.hasErrors()) {
				return "common/settings";
			}
			User user=userRepository.getUserByEmail(HelperToFindUserName.getEmailOfLoggedinUser(authentication));
			if(!changePassword.getNew_password().equals(changePassword.getConfirm_password())) {
				session.setAttribute("message", new MessageHelper("new password and confirm password does not matches","alert-warning"));
			}
			else if(bCryptPasswordEncoder.matches(changePassword.getOld_password(), user.getPassword())){
				user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNew_password()));
				session.setAttribute("message", new MessageHelper("password changes successfully","alert-success"));
				userRepository.save(user);
			}
			else {
				session.setAttribute("message", new MessageHelper("Enter old password is wrong","alert-warning"));
			}
			return "common/settings";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "common/user_dashboard";  // never executed
	
	}
	
	
	// payment integration code
	@PostMapping("/create_order")
	@ResponseBody
	public String creatingOrder(@RequestBody Map<String, Object> data,Authentication authentication) throws Exception {
		// information getting from form of payment
		int amount=Integer.parseInt(data.get("amount").toString());
		String info=data.get("info").toString();
		System.out.println(amount);
		System.out.println(info);
		
		//
		RazorpayClient rozarClientMe= new RazorpayClient("rzp_test_bz6KxeRkMV1MiJ","8nuJneWViH0SMaVE2SuYytjG");
		
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount",amount*100);
		orderRequest.put("currency","INR");
		Order order =rozarClientMe.orders.create(orderRequest);
		System.out.println(order);
		// saving created order in our database
		Orders orders=new Orders();
		int amt=order.get("amount");
		orders.setAmount(amt/100);
		orders.setOrderId(order.get("id"));
		orders.setStatus("created");
		orders.setUser(userRepository.getUserByEmail(HelperToFindUserName.getEmailOfLoggedinUser(authentication)));
		orderRepository.save(orders);
			
		return order.toString();
		
	}
	@PostMapping("/updatestatus")
	public void updateOrderStatus(@RequestBody Map<String,Object> data) {
		String orderId=(String) data.get("orderId");
		Orders orders=orderRepository.getByorderid(orderId);
		orders.setPaymentId(data.get("paymentId").toString());
		orders.setStatus(data.get("status").toString());
		orderRepository.save(orders);
		
	}
	
}
