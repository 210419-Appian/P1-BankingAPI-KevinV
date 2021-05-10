package com.revature.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controllers.AccountController;
import com.revature.controllers.LoginController;
import com.revature.controllers.UserController;
import com.revature.models.JSONmessage;


public class FrontControllerServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	HttpSession ses;
	PrintWriter out;
	String currentUser;
	int currentRole;
	JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
	ObjectMapper om = new ObjectMapper();
	


	private LoginController loginControl = new LoginController();
	private UserController uControl = new UserController();
	private AccountController accControl = new AccountController();


	//we obtain our ServletConfig Parameters from web.xml
	private String BaseURL;	
	@Override
	public void init(ServletConfig config) throws ServletException {
		BaseURL = config.getInitParameter("BaseURL");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("application/json");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);
		//default is 200, but that means some 200s will be lies(stuff we're not set up to handle). 
		//we make 404 the default
		res.setStatus(404);

		//use ServletConfig resources

		//parse the incoming request
		System.out.println(BaseURL);
		//remove the filler http://localhost:8080/HelloFrontController/ from the link
		final String URL = req.getRequestURI().replace(BaseURL,""); //url only has /avengers or /whateverRequest
		System.out.println(URL);

		/*a Path Varible is a way to pass info about the req in the URL itself. 
		 * Generally, you have a final / in the URL that takes a variable input; 
		 * i.e. user/1 will get the avenger with ID 1, 
		 * while user/2 will you a different one
		 */
		//we split URL by slashes to isolate Path Variables
		String[] sections = URL.split("/");
		for(String s : sections) System.out.println(s);

		//router
		//route according to the request via switch{}
		//routes to each controller from Controllers package; this file is the only one in Web package
		switch(sections[0]) {
		case "login":
			if(req.getMethod().equals("POST")) {
				loginControl.login(req, res);
			}
			else {
				out = res.getWriter();
				out.print("<h1> you're doing login with the wrong HTTP Verb</h1>");
			}
			break;
		case "loginSuccess":
			res.setContentType("text/html");
			out = res.getWriter();

			//gets the session we created in LoginController onSuccess
			ses = req.getSession(false);
			if(ses == null) {
				out.print("<h1> you are NOT logged in </h1>");
			} else {
				String name = (String) ses.getAttribute("username");
				out.print("<h2> you logged in: " + name + ". Welcomae! </h2>");
				out.print("<a href='logout'>Click Here to Log Out</a>");
			}
			break;
		case "logout":
			//logouts should end their session
			ses = req.getSession(); //there's technically no problem w/ not doing (false)

			if(ses != null) {
				ses.invalidate();
			}
			res.setContentType("text/html");
			out = res.getWriter();
			out.print("<h1> you logged out</h1>");
			break;
		case "users":
			if(req.getMethod().equals("GET")) {
				//check path variables: requesting a specific user
				if(sections.length == 2) {
					//only employee,admin, or currentUserID={id} can search this
					uControl.getUserById(req,res, Integer.parseInt(sections[1]));
					break;
				}
				
				//check that only admins/employees can check-all-users
				ses = req.getSession(false);
				if(ses == null) {
					out = res.getWriter();
					System.out.println("No Active Sesssion in GET /users");
					out.print(unauthorized);
					res.setStatus(401);
					res.setContentType("application/json");
					break;
				} 
				
				currentRole = Integer.parseInt(ses.getAttribute("role").toString());
				System.out.println("current role = " + currentRole);
				if(currentRole != 1 && currentRole != 2) {
					out = res.getWriter();
					out.print(unauthorized);
					res.setStatus(401);
					res.setContentType("application/json");
					break;
				}
				System.out.println("uControl.getAllUsers()");
				uControl.getAllUsers(req, res);
				//pass to uControl.getAllUsers(), which passes to UserService which passes to DAO
			} else if (req.getMethod().equals("PUT")) {
				System.out.println("PUT in /users");
				//check that only admin/matching User can update the User in req.body
				uControl.updateUser(req,res);
				//read req.body in uControl.updateUser(req,res)
			} else {
				out = res.getWriter();
				out.print("<h1> you're doing users with the an unsupported HTTP Verb</h1>");
			}
			break;
			
		case "accounts":
			if(req.getMethod().equals("GET")) {
				if(sections.length == 3) {
					if(sections[1].equals("owner")) {
						//find accounts by UserID
						//employee,admin, or currentUserId={id}
						accControl.getAccountsByUserId(req,res,Integer.parseInt(sections[2]));
						break;
					} else if (sections[1].equals("status")) {
						//find accounts by status
						//employee/admin only
						accControl.getAccountsByStatus(req, res,Integer.parseInt(sections[2]));
						break;
					}
				} else if (sections.length == 2) {
					//find account by ID
					//employee,admin,account belongs to currentUser
					accControl.getAccountById(req,res, Integer.parseInt(sections[1]));
					break;
				} else if (sections.length == 1){
					//check that only admins/employees can check-all-accounts
					ses = req.getSession(false);
					if(ses == null) {
						out = res.getWriter();
						System.out.println("No Active Sesssion in GET /accounts");
						out.print(unauthorized);
						res.setStatus(401);
						res.setContentType("application/json");
						break;
					} 
					
					currentRole = Integer.parseInt(ses.getAttribute("role").toString());
					if(currentRole != 1 && currentRole != 2) {
						out = res.getWriter();
						out.print(unauthorized);
						res.setStatus(401);
						res.setContentType("application/json");
						break;
					}
					System.out.println("accControl.getAllAccounts()");
					accControl.getAllAccounts(req, res);
					break;
				}
			} else if (req.getMethod().equals("POST")) {
				//register new account
			} else if (req.getMethod().equals("PUT")) {
				//update account
				//admin only
				accControl.updateAccount(req,res);
				break;
			}
		default:
			out = res.getWriter();
			out.print("<h1> your router went to Default</h1>");
			break;
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//instead of making a new switch/case, we send it to the switchcase in our Get
		doGet(req,res);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
