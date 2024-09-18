package smaApi;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import auth.kayodeo1.com.KeyGenerator;
import auth.kayodeo1.com.NotificationModel;
import auth.kayodeo1.com.PasswordCrypto;
import auth.kayodeo1.com.auth;
import auth.kayodeo1.com.jwtUtil;
import auth.kayodeo1.com.mailSender;
import auth.kayodeo1.com.validator;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/students")
public class StudentResource {
	public final ArrayList<String> groups = new ArrayList<> (Arrays.asList("programming","embeded","web development","data analysis","networking"));
	public ArrayList<String> getKeys() {
		ArrayList<String> keys  = new ArrayList<>();
		keys.add(System.getenv("KEY1"));
		keys.add(System.getenv("KEY2"));
		keys.add(System.getenv("KEY3"));
		keys.add(System.getenv("KEY4"));

		return keys;

	}

	String key = KeyGenerator.generateKey(getKeys());
	private dbHelper helper = new dbHelper();
	auth authenticate = new auth();
	jwtUtil jwt = new jwtUtil();

	// SEND EMAIL WITH VERIFICATION CODE
	@Path("/authenticate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public  Response authenticate(studentModel student) throws SQLException {

	    String recipientEmail = student.getEmail();
	    String email = System.getenv("APP_EMAIL");
	    String appPassword = System.getenv("APP_PASSWORD");
	    String email1 = System.getenv("APP_EMAIL1");
	    String appPassword1 = System.getenv("APP_PASSWORD1");
	    long currentTime = System.currentTimeMillis();
		long cooldownPeriod = 60000;
	    if ( EmailTracker.isEmailRecentlySent(recipientEmail, currentTime, cooldownPeriod)) {
    	    return sendResponse("Failed", "Email has already been sent. Please wait before requesting another one.");
    	}
	    if (helper.checkStudentExists(recipientEmail)) {
	        return sendResponse("Failed", "Student already exists");
	    }

	    try {
	        mailSender msg = new mailSender(email, appPassword);
	        String subject = "Verification Mail from SMA Ministry of Science and Technology";
	        int code = authenticate.genAuthInstance(recipientEmail).getCode();
	        System.out.println("fine here so far");

	        String codeValue = String.format("%06d", code);
	        String htmlContent = auth.generateHtmlContent(codeValue);

	        msg.sendEmail(recipientEmail, subject, htmlContent);

	        return sendResponse("Successful", "Email Sent to " + recipientEmail);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	        return sendResponse("badRequest", "Couldn't send email to " + recipientEmail + ": " + e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return sendResponse("LionError", "An unexpected error occurred: " + e.getMessage());
	    }
	}
	// READ single student
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudent(@QueryParam("jwt") String token) throws Exception {
	    Claims jwtValues = jwtUtil.parseJWT(token);
	    if (jwtValues==null) {
			return sendResponse("Failed","invalid or expired token");	    }
	    String email = jwtValues.get("email").toString();

	    studentModel student;
	    try {
	        String decryptedPassword = PasswordCrypto.decrypt(PasswordCrypto.decrypt(jwtValues.get("password").toString(), key), key);
	        System.out.println(decryptedPassword);
	        student = helper.getStudent(email, decryptedPassword);
	        return Response.ok(student).build();
	    } catch (SQLException e) {
	        e.printStackTrace();
			return sendResponse("Failed","Database Error");	    } catch (Exception e) {
	        e.printStackTrace();
			return sendResponse("Failed","invalid token or decryption error");	    }
	}
	// UPDATE
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStudent(studentModel student) throws SQLException {
		Claims jwtValues = jwtUtil.parseJWT(student.getJwt());
		if (jwtValues==null) {

			return sendResponse("Failed","can not update ,invalid or expire token");
	}

		System.out.println(jwtValues);
		if (jwtValues.get("email").equals(student.getEmail())) {
			if (helper.updateStudent(student)) {
		        String decryptedPassword;
				try {
					decryptedPassword = PasswordCrypto.decrypt(PasswordCrypto.decrypt(jwtValues.get("password").toString(), key), key);
					studentModel updatedStudent = helper.getStudent(jwtValues.get("email").toString(),
							decryptedPassword);
					updatedStudent.setJwt(student.getJwt());
					return Response.ok(updatedStudent).build();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return sendResponse("failed","check inputs"+e.toString())	;
					}

			} else {

				return sendResponse("Failed","No Field to update , update failed");
		}
		}

			return sendResponse("Failed","unauthorized to update this student");
	}


	// generate token DONE
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(studentModel student) {
		try {
			studentModel Student = helper.getStudent(student.getEmail(), student.getPassword());
			if (Student != null) {
				Student.setJwt(jwtUtil.createJWT(Student));
				return Response.ok(Student).build();
			} else {
				return sendResponse("Failed","Student not found or incorrect password");

			}
		} catch (SQLException e) {
			e.printStackTrace();

			return sendResponse("Failed","Error retriving student");		}
	}

	@Path("/validate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validate(validator student) throws SQLException {
		String recipentEmail = student.getEmail();
		int code = Integer.parseInt(student.getCode());
		if (authenticate.validateAuthInstance(recipentEmail, code)) {
			studentModel Student = new studentModel();
			Student.setEmail(recipentEmail);
			Student.setPassword(student.getPassword());
			Student.setStatus("validated");
			helper.addStudent(Student);
			return login(Student);
		}

		return sendResponse("Failed","incorrect code for "+recipentEmail);
	}

	@PUT
	@Path("/password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(studentModel student) {
		if(student.getOldPassword().equals(student.getNewPassword())) {
			return Response.ok("Old password is equal to new password").build();
		}

		Claims jwtValues = jwtUtil.parseJWT(student.getJwt());

			if (jwtValues==null) {
				return sendResponse("Failed","Faile to change password , token expired");
		}
		if (jwtValues.get("email").equals(student.getEmail())) {
			try {
				if (helper.updatePassword(student.getEmail(), student.getOldPassword(), student.getNewPassword())) {
					jwtUtil.addToBlacklist(student.getJwt());
					student.setPassword(student.getNewPassword());

					return sendResponse("Success","Password Changed but token revoked");
								}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return sendResponse("Failed","Failed to change Password");
	}

	
	@Path("/logout")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(studentModel student) {
		try {
	        String token = student.getJwt();
	        // Add the token to the blacklist
	        jwtUtil.addToBlacklist(token);

	      return sendResponse("Success","Token Invaloidated and Logout Successful");	    } catch (Exception e) {
	        e.printStackTrace();
	       return sendResponse("Failed","Logout Failed ,Invalid token");
	    }
	}
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStudents(studentModel admin) throws Exception {
    	ArrayList<studentModel> students = helper.getAllStudent();
    	Claims jwtValues = jwtUtil.parseJWT(admin.getJwt());
		if (jwtValues==null) {

			return sendResponse("Failed","unauthorized");
	}
        if (jwtValues.get("role").toString().equals("admin")){
            return Response.ok(students).build();

        }return sendResponse("Failed","unauthorized");
    }


	public Response sendResponse(String title , String body) {

		responseOut response = new responseOut();
		response.setTitle(title);
		response.setMessage(body);
		return Response.ok(response).build();




	}

	@POST
	@Path("/notifications")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotifications(studentModel student) throws SQLException, JsonProcessingException {
		Claims jwtValues = jwtUtil.parseJWT(student.getJwt());
		if (jwtValues==null) {

			return sendResponse("badRequest","bad token");
		}
		ArrayList<notification> n = new ArrayList();
		String group = jwtValues.get("group").toString();
		notification notifications = new notification();
		notifications.setData( helper.getNotifications("General"));
		notifications.setType("General");

		n.add(notifications);
		notification notifications2 = new notification();

		notifications2.setType(group);
		notifications2.setData( helper.getNotifications(group));
		n.add(notifications2);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(n);

		return Response.ok(json).build();



	}




	@PUT
	@Path("/notifications")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

public Response addNotifications(NotificationModel notification) throws SQLException {
		Claims jwtValues = jwtUtil.parseJWT(notification.getJwt());
		if (jwtValues==null) {

			return sendResponse("badRequest","unauthorized");
	}else {
		if (jwtValues.get("role").toString().equals("admin")){


			Date currentDate = new Date();
			NotificationModel newNotification = notification;
			newNotification.setTitle(notification.getTitle());
			newNotification.setContent(notification.getContent());
			newNotification.setTimeStamp(currentDate.getTime());
			newNotification.setTime(newNotification.getTime());
			if (notification.getAuthor()==null || notification.getAuthor()=="" ) {
			newNotification.setAuthor("Admin:"+jwtValues.get("email").toString());
			}if (notification.getCategory()==null) {
			newNotification.setCategory(jwtValues.get("group").toString());
			}else {
				newNotification.setCategory(notification.getCategory());

			}
			System.out.println(newNotification.toString());
			helper.addNotification(newNotification);
			return sendResponse("Success ","notification "+newNotification.getTitle()+" added");
			//TODO send notification to all email and others


		}else {
			return sendResponse("Failed"," not an admin , only an admin can add notification neefex");

		}


	}


	}

	@DELETE
	@Path("/notifications")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNotifications(studentModel student) throws SQLException, JsonProcessingException {
		Claims jwtValues = jwtUtil.parseJWT(student.getJwt());
		if (jwtValues==null) {

			return sendResponse("badRequest","bad token");
		}else {
			if (jwtValues.get("role").toString().equals("admin")){
				helper.deleteNotification(student);

				}
			}
		return null;

	}

	@GET
	@Path("/notifications")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response readNotifications(NotificationModel student) throws SQLException, JsonProcessingException {

		return null;

	}


	@PUT
	@Path("/reset")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

public Response resetPassword(studentModel admin) {
		Claims jwtValues = jwtUtil.parseJWT(admin.getJwt());
		if (jwtValues==null) {

			return sendResponse("Failed","unauthorized");
	}else {
        if (jwtValues.get("role").toString().equals("admin")){

        	if(helper.resetPassword(admin.getEmail(),admin.getNewPassword())) {
        	return sendResponse("success","password reset for "+admin.getEmail());
        	}else {
            	return sendResponse("Failed","Data does'nt exist");


        	}



        }else{
        	return sendResponse("unAuthorized","not admin profile");
        }

	}
    }

	@PUT
	@Path("/changeRole")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeRole(studentModel admin) {
		Claims jwtValues = jwtUtil.parseJWT(admin.getJwt());
		if (jwtValues==null) {

			return sendResponse("Failed","unauthorized");
	}else {
        if (jwtValues.get("role").toString().equals("admin")){
        	if (!(admin.getRole().equals("student")||admin.getRole().equals("admin")) ) {


        	return sendResponse("Error","role must be student or admin");
        	}
        	if(helper.changeRole(admin.getEmail(),admin.getRole())) {
        	return sendResponse("success ",admin.getEmail()+" is now a "+admin.getRole());
        	}else {
            	return sendResponse("Failed","Data does'nt exist");


        	}



        }else{
        	return sendResponse("unAuthorized","not admin profile");
        }

	}

	}


	@PUT
	@Path("/changeGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeGroup(studentModel admin) {
		Claims jwtValues = jwtUtil.parseJWT(admin.getJwt());
		if (jwtValues==null) {

			return sendResponse("Failed","unauthorized");
	}else {
        if (jwtValues.get("role").toString().equals("admin")){
        	if (!(groups.contains(admin.getGroup())) ) {

            		return sendResponse("Error","invalid group ,group should only be one of"+groups.toString());
            		}
        	if(helper.changeGroup(admin.getEmail(),admin.getGroup())) {
        	return sendResponse("success ",admin.getEmail()+" is now in "+admin.getGroup() +" group");
        	}else {
            	return sendResponse("Failed","Data does'nt exist");


        	}



        }else{
        	return sendResponse("unAuthorized","not admin profile");
        }

	}

	}


}


class EmailTracker {

    private static Map<String, Long> emailSentRecords = new HashMap<>();

    // Check if an email was sent recently
    public static boolean isEmailRecentlySent(String email, long currentTime, long cooldownPeriod) {
        Long lastSentTime = emailSentRecords.get(email);
        if (lastSentTime != null) {
            return (currentTime - lastSentTime) < cooldownPeriod;
        }
        return false;
    }

    // Record the time an email was sent
    public static void recordEmailSent(String email, long currentTime) {
        emailSentRecords.put(email, currentTime);
    }


}



