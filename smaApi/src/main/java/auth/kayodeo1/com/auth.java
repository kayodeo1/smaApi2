package auth.kayodeo1.com;

import java.sql.SQLException;
import java.time.Instant;
public class auth {
	dbHelper db = new dbHelper();

	public  Instance genAuthInstance(String id ) {
		int code = generateCode();
		long genTime = Instant.now().getEpochSecond();
		long time = genTime+180;
		Instance instance = new Instance(id,code,time) ;
		try {
			db.writeDb(instance);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instance;

	}

	public boolean validateAuthInstance(String email ,int code ) throws SQLException {
		Instance instance = db.validate(email);
		if (code == instance.getCode()) {
			System.out.println("validated");
			return true;
		}
		System.out.println("auth failed");
		return false;


	}

	public static int generateCode () {
		return (int)(Math.random()*1000000) ;


}
	 public static String generateHtmlContent(String verificationCode) {
	        // Escape the double quotes and format the HTML content
	        String html = "<!DOCTYPE html>\n"
	                + "<html lang=\"en\">\n"
	                + "<head>\n"
	                + "    <meta charset=\"UTF-8\">\n"
	                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
	                + "    <style>\n"
	                + "        body {\n"
	                + "            font-family: Arial, sans-serif;\n"
	                + "            margin: 0;\n"
	                + "            padding: 0;\n"
	                + "            background-color: #f4f4f4;\n"
	                + "        }\n"
	                + "        .container {\n"
	                + "            width: 100%;\n"
	                + "            max-width: 600px;\n"
	                + "            margin: 20px auto;\n"
	                + "            background-color: #ffffff;\n"
	                + "            border-radius: 8px;\n"
	                + "            overflow: hidden;\n"
	                + "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n"
	                + "        }\n"
	                + "        .header {\n"
	                + "            background-color: #007bff;\n"
	                + "            color: #ffffff;\n"
	                + "            padding: 20px;\n"
	                + "            text-align: center;\n"
	                + "        }\n"
	                + "        .content {\n"
	                + "            padding: 20px;\n"
	                + "        }\n"
	                + "        .code {\n"
	                + "            font-size: 24px;\n"
	                + "            font-weight: bold;\n"
	                + "            letter-spacing: 2px;\n"
	                + "            margin: 20px 0;\n"
	                + "            text-align: center;\n"
	                + "        }\n"
	                + "        .code span {\n"
	                + "            display: inline-block;\n"
	                + "            padding: 5px;\n"
	                + "            color: #ffffff;\n"
	                + "            font-size: 24px;\n"
	                + "            font-weight: bold;\n"
	                + "            margin-right: 4px; /* Space between digits */\n"
	                + "        }\n"
	                + "        .code span:nth-child(1) { color: #ff6f61; }\n"
	                + "        .code span:nth-child(2) { color: #ffca28; }\n"
	                + "        .code span:nth-child(3) { color: #4caf50; }\n"
	                + "        .code span:nth-child(4) { color: #2196f3; }\n"
	                + "        .code span:nth-child(5) { color: #9c27b0; }\n"
	                + "        .code span:nth-child(6) { color: #00bcd4; }\n"
	                + "        .code span:nth-child(7) { color: #e91e63; }\n"
	                + "        .footer {\n"
	                + "            background-color: #f1f1f1;\n"
	                + "            color: #777777;\n"
	                + "            text-align: center;\n"
	                + "            padding: 20px;\n"
	                + "            font-size: 14px;\n"
	                + "        }\n"
	                + "        @media only screen and (max-width: 600px) {\n"
	                + "            .container {\n"
	                + "                width: 100%;\n"
	                + "                margin: 10px auto;\n"
	                + "            }\n"
	                + "            .code {\n"
	                + "                font-size: 20px;\n"
	                + "            }\n"
	                + "            .code span {\n"
	                + "                font-size: 20px;\n"
	                + "                margin-right: 2px; /* Reduced space between digits */\n"
	                + "            }\n"
	                + "        }\n"
	                + "    </style>\n"
	                + "</head>\n"
	                + "<body>\n"
	                + "    <div class=\"container\">\n"
	                + "        <div class=\"header\">\n"
	                + "            <h1>Verification Code</h1>\n"
	                + "        </div>\n"
	                + "        <div class=\"content\">\n"
	                + "            <p>Hello [User],</p>\n"
	                + "            <p>Thank you for registering with us. To complete the verification process, please use the following code:</p>\n"
	                + "            <div class=\"code\">\n";

	        for (char c : verificationCode.toCharArray()) {
	            html += "                <span>" + c + "</span>\n";
	        }

	        html += "            </div>\n"
	                + "            <p>This code is valid for the next 2 minutes. Please enter it promptly to verify your email address.</p>\n"
	                + "            <p>If you did not request this verification, please ignore this email.</p>\n"
	                + "        </div>\n"
	                + "        <div class=\"footer\">\n"
	                + "            <p>Best regards,<br>SMA, Ministry of Innovation Science and Tech</p>\n"
	                + "        </div>\n"
	                + "    </div>\n"
	                + "</body>\n"
	                + "</html>";

	        return html;
	    }






}