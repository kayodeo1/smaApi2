//package auth.kayodeo1.com;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
//
//import jakarta.ws.rs.GET;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.QueryParam;
//import jakarta.ws.rs.core.Response;
//import java.util.Arrays;
//
//@Path("/auth")
//public class GoogleAuthResource {
//
//    private static final String CLIENT_ID = "your-client-id";
//    private static final String CLIENT_SECRET = "your-client-secret";
//    private static final String REDIRECT_URI = "http://localhost:8080/your-app/auth/callback";
//
//    @GET
//    @Path("/google")
//    public Response redirectToGoogle() {
//        GoogleAuthorizationCodeRequestUrl url = new GoogleAuthorizationCodeRequestUrl(
//                CLIENT_ID, REDIRECT_URI, Arrays.asList("email", "profile"));
//        String authUrl = url.build();
//        return Response.seeOther(URI.create(authUrl)).build();
//    }
//
//    @GET
//    @Path("/callback")
//    public Response handleGoogleCallback(@QueryParam("code") String code) {
//        try {
//            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
//                    new NetHttpTransport(),
//                    JacksonFactory.getDefaultInstance(),
//                    CLIENT_ID,
//                    CLIENT_SECRET,
//                    code,
//                    REDIRECT_URI).execute();
//
//            String accessToken = tokenResponse.getAccessToken();
//
//            // Use the access token to fetch the user's information
//            GoogleIdToken idToken = tokenResponse.parseIdToken();
//            GoogleIdToken.Payload payload = idToken.getPayload();
//            String userId = payload.getSubject();
//            String email = payload.getEmail();
//            String name = (String) payload.get("name");
//
//            // Here, you would typically create a session for the user,
//            // store their information in your database if they're a new user,
//            // and redirect them to your application's main page
//
//            return Response.ok("Authentication successful for user: " + email).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Authentication failed").build();
//        }
//    }
//}