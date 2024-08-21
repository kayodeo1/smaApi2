package auth.kayodeo1.com;
import io.jsonwebtoken.Claims;
import smaApi.studentModel;
class test2{
	public static void main(String[] args) {
		jwtUtil jwt = new jwtUtil();

		studentModel kay = new studentModel ();
		kay.setEmail("ojokayode566@gmail.com");
		kay.setUserID(190);
		kay.setPassword("agbawe");
		System.out.println(kay.toString());
		String key =jwt.createJWT(kay);

		Claims value = jwtUtil.parseJWT(key);
		System.out.println(value.get("password"));
//
	}

}