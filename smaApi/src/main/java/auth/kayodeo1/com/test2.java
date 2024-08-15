package auth.kayodeo1.com;
import io.jsonwebtoken.Claims;
import smaApi.studentModel;
class test2{
	public static void main(String[] args) {
		jwtUtil jwt = new jwtUtil();

		studentModel kay = new studentModel ();
//		kay.setEmail("ojokayode566@gmail.com");
//		kay.setUserID(190);
//		System.out.println(kay.toString());
		String key ="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvam9rYXlvZGU1NjZAZ21haWwuY29tIiwiaWF0IjoxNzIzNjM5NzQ4LCJleHAiOjE3MjM3MjYxNDgsImVtYWlsIjoib2pva2F5b2RlNTY2QGdtYWlsLmNvbSIsInJvbGUiOiJTdHVkZW50In0.2I9VM6Ry6t_CreZL-yMOEDwrdyBJASqyGspG1EugilM";

		Claims value = jwtUtil.parseJWT(key);
		System.out.println(value.get("role"));
//
	}

}