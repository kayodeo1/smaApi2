package smaApi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;

import auth.kayodeo1.com.jwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/uploadImage")
@MultipartConfig
public class imageServlet extends HttpServlet {
	private static final String UPLOAD_DIRECTORY = "profile_pictures";
	dbHelper db = new dbHelper();

	jwtUtil JWT = new jwtUtil();

	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String userId = req.getParameter("email");
		String jwt = req.getParameter("jwt");
		if (jwt == null) {
			output Out = new output("Failed", " Jwt is nul , please add jwt", userId, "no image to show you mf");
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(Out);
			res.setContentType("APPLICATION/JSON");
			res.getWriter().write(json);
			return;
		}
		Part filePart = req.getPart("profilePicture");
		Claims values = jwtUtil.parseJWT(jwt);
		if (values == null) {
			output Out = new output("Failed", " bad or expired token", userId, "null");
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(Out);
			res.getWriter().write(json);
			res.setContentType("APPLICATION/JSON");
			return;
		}
		if (!values.get("email").toString().equals(userId)) {

			output Out = new output("Failed", " Email does'nt match jwt", userId, "null");
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(Out);
			res.setContentType("APPLICATION/JSON");
			res.getWriter().write(json);
			return;

		}
		userId = values.get("email").toString();
		String webAppPath = getServletContext().getRealPath("/");
        File webAppsDir = new File(webAppPath).getParentFile();
		String uploadPath = webAppsDir+ File.separator + UPLOAD_DIRECTORY;
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}

		// get extension
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		String fileExtension = fileName.substring(fileName.lastIndexOf("."));
		// new file name
		String newFileName = userId + "_" + System.currentTimeMillis() + fileExtension;
		String filePath = uploadPath + File.separator + newFileName;

		String contentType = filePart.getContentType();
		if (!contentType.startsWith("image/")) {

			output Out = new output("Failed", " Only Image files allowed ", userId, "upload a valid image");
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(Out);
			res.setContentType("APPLICATION/JSON");
			res.getWriter().write(json);
			return;

		}

		try (InputStream input = filePart.getInputStream()) {
			Files.copy(input, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
		}

		// Save file URI to database
		String fileUri = "192.168.100.30/" + UPLOAD_DIRECTORY + "/" + newFileName;
		try {
			db.updateProfilePicture(userId, fileUri);
			output Out = new output("Success", "image uploaed successfully", userId, fileUri);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(Out);
			res.setContentType("APPLICATION/JSON");
			res.getWriter().write(json);
			return;
		} catch (SQLException e) {
			output Out = new output("Failed", " failed to upload image", userId, "null");
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(Out);
			res.setContentType("APPLICATION/JSON");
			res.getWriter().write(json);
			return;

		}

	}

}

class output {
	private String imgUri;
	private String message;
	private String email;
	private String title;

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	public output(String title, String message, String email, String imgUri) {
		super();
		this.imgUri = imgUri;
		this.message = message;
		this.email = email;
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
