package com.gazecode.controller;

import java.io.ByteArrayOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
 
 
@Controller
public class SpringSecuritySearchController {
 
	@RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "This page is publicly accessible. Viewing as default page.");
        
        return "public";
    }
	
    @RequestMapping("/public")
    public String accessPublicPage(Model model) {
        model.addAttribute("message", "This page is publicly accessible. No authentication is required to view.");
 
        return "public";
    }
     
    @RequestMapping("/secured/mypage")
    public String accessSecuredPage(Model model) {
        model.addAttribute("message", "Only you are authenticated and authorized to view this page.");
 
        return "/secured/mypage";
    }
    
    @RequestMapping(value = { "/welcome**" }, method = RequestMethod.GET)
	public ModelAndView defaultPage() {
 
	  ModelAndView model = new ModelAndView();
	  model.addObject("title", "Spring Security Login Form - Database Authentication");
	  model.addObject("message", "This is default page!");
	  model.setViewName("hello");
	  return model;
 
	}
 
	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {
  
	  ModelAndView model = new ModelAndView();
	  model.addObject("title", "Spring Security Login Form - Database Authentication");
	  model.addObject("message", "This page is for ROLE_ADMIN only!");
	  model.setViewName("admin");
	  return model;
 
	}
 
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "logout", required = false) String logout) {
 
	  ModelAndView model = new ModelAndView();
	  if (error != null) {
		model.addObject("error", "Invalid username and password!");
	  }
 
	  if (logout != null) {
		model.addObject("msg", "You've been logged out successfully.");
	  }
	  model.setViewName("login");
 
	  return model;
 
	}
 
	//for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {
 
	  ModelAndView model = new ModelAndView();
 
	  //check if user is login
	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	  if (!(auth instanceof AnonymousAuthenticationToken)) {
		UserDetails userDetail = (UserDetails) auth.getPrincipal();	
		model.addObject("username", userDetail.getUsername());
	  }
 
	  model.setViewName("403");
	  return model;
 
	}

	@RequestMapping(value="/getpdf", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getPdf(@RequestBody String json){
		String text = json;
		
        if (text == null || text.trim().length() == 0) {
             text = "You didn't enter any text.";
        }
        // step 1
        Document document = new Document();
        // step 2
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			PdfWriter.getInstance(document, baos);
			 // step 3
	        document.open();
	        // step 4
	        document.add(new Paragraph(String.format(
	            "You have submitted the following text using the %s method:",
	            "getpdf")));
	        document.add(new Paragraph(text));
	        // step 5
	        document.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       
        
	    // retrieve contents of "C:/tmp/report.pdf"
	    byte[] contents = baos.toByteArray();

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename = "output.pdf";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    return response;
	}

}