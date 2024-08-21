package smaApi;

import java.sql.SQLException;

import auth.kayodeo1.com.auth;
import auth.kayodeo1.com.jwtUtil;
import auth.kayodeo1.com.mailSender;
import io.jsonwebtoken.Claims;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import auth.kayodeo1.com.validator;
@Path("/students")
public class StudentResource {
    private dbHelper helper = new dbHelper();
    auth authenticate = new auth();
	jwtUtil jwt = new jwtUtil();

 // SEND EMAIL WITH VERIFICATION CODE
	@Path("/authenticate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response authenticate(studentModel student) throws SQLException {
	    // Retrieve recipient email and other necessary details
	    String recipientEmail = student.getEmail();
	    String appPassword = "Olecram2.";
	    String email = "ojokayode566@outlook.com";
	    if (helper.checkStudentExists(recipientEmail)) {
	    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to send verification email: " + "Student already exist with email:"+recipientEmail)
                    .build();
	    }
	    // Initialize mailSender object
	    mailSender msg = new mailSender(email, appPassword);
	    String subject = "Verification Mail from SMA Ministry of Science and Technology";

	    try {
	        // Generate authentication code and HTML content
	        int code = authenticate.genAuthInstance(recipientEmail).getCode();
	        System.out.println("fine here so far");
	        String htmlContent = auth.generateHtmlContent(String.valueOf(code));

	        // Send the email
	        msg.sendEmail(recipientEmail, subject, htmlContent);

	        // Return success response
	        return Response.ok("Verification email sent successfully.").build();
	    } catch (Exception e) {
	        // Log the exception
	        e.printStackTrace();

	        // Return error response
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity("Failed to send verification email: " + e.getMessage())
	                       .build();
	    }
	}



    // READ (single student)
    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("email") String userID, @QueryParam("jwt") String token) throws SQLException {
    	System.out.println("okY");
    	Claims jwtValues = jwtUtil.parseJWT(token);
    	if (jwtValues.get("email").equals(userID)) {
    		studentModel student = helper.getStudent(userID, jwtValues.get("password").toString());
    		return Response.ok(student).build();
    	}


		return null;


    }

    // UPDATE
    @PUT
    @Path("/{userID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(studentModel student) throws SQLException {
    	

    	Claims jwtValues = jwtUtil.parseJWT(student.getJwt());
    	if (jwtValues.get("email").equals(student.getUserID())) {
    		studentModel Student = student;
    		helper.updateStudent(Student);
    		Student=helper.getStudent(jwtValues.get("email").toString(), jwtValues.get("password").toString());
    		
    		return Response.ok(student).build();
    	}



		return Response.ok("Failed to update student").build();

    }


    //generate token DONE
    @GET
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login (studentModel student) {
    	 try {
             studentModel Student = helper.getStudent(student.getEmail(), student.getPassword());
             if (Student != null) {
            	 Student.setJwt(jwtUtil.createJWT(Student))  ;
            	 return Response.ok(Student).build();
             } else {
                 return Response.status(Response.Status.NOT_FOUND).entity("Student not found or incorrect password").build();
             }
         } catch (SQLException e) {
             e.printStackTrace();
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving student").build();
         }
     }

    @Path("/validate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response validate(validator student) throws SQLException {
	    String recipentEmail = student.getEmail();
	    int code = Integer.parseInt(student.getCode());
	    if(authenticate.validateAuthInstance(recipentEmail, code)) {
	    	studentModel Student = new studentModel();
	    	Student.setEmail(recipentEmail);
	    	Student.setPassword(student.getPassword());
	    	helper.addStudent(Student);
	    			
	    	return  login(Student);
	    }
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Failed to verify: " + "Try entering code again")
                .build();
    	
    	
    }
    
    
    
    
    }

