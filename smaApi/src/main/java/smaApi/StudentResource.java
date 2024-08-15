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
@Path("/students")
public class StudentResource {
    private dbHelper helper = new dbHelper();
    auth authenticate = new auth();
	jwtUtil jwt = new jwtUtil();

 // CREATE
    @Path("/authenticate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void authenticate(studentModel student) {
    	System.out.println("works");
        String recipientEmail = student.getEmail();
        String appPassword = "Olecram2.";
        String email = "ojokayode566@outlook.com";
        mailSender msg = new mailSender(email,appPassword);
        String subject= "Verification Mail from SMA Ministry of science and technology";
        int code = authenticate.genAuthInstance(recipientEmail).getCode();
        System.out.println("fine here so far");
        String htmlContent = auth.generateHtmlContent(String.valueOf(code));
        msg.sendEmail(recipientEmail, subject, htmlContent);


    }


    // READ (single student)
    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("email") String userID, @QueryParam("jwt") String token) {
    	Claims jwtValues = jwtUtil.parseJWT(token);
    	if (jwtValues.get("email").equals(userID)) {
    		studentModel student = new studentModel();
    		return Response.ok(student).build();
    	}


		return null;


    }

    // UPDATE
    @PUT
    @Path("/{userID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(@PathParam("userID") String userID, @QueryParam("jwt") String token) {


		return null;



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




}