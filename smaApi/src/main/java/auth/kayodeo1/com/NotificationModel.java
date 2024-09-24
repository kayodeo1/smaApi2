package auth.kayodeo1.com;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import smaApi.studentModel;

public class NotificationModel {
    private String title;
    private long timeStamp;
    private String author = "Admin";
    private String content;
    private String category;
    private String jwt;
    private String time;
    private String status="unread";

    @Override
	public String toString() {
		return "NotificationModel [title=" + title + ", timeStamp=" + timeStamp + ", author=" + author + ", content="
				+ content + ", category=" + category + ", jwt=" + jwt + ", time=" + time + "]";
	}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getTime() {
        return convertTimeStamp(timeStamp);
    }

    public void setTime(String time) {
		this.time = time;
	}

	private String convertTimeStamp(long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toHtml() {
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
	            + "        }\n"
	            + "    </style>\n"
	            + "</head>\n"
	            + "<body>\n"
	            + "    <div class=\"container\">\n"
	            + "        <div class=\"header\">\n"
	            + "            <h1>" + this.title + "</h1>\n"
	            + "        </div>\n"
	            + "        <div class=\"content\">\n"
	            + "            <p>Hello,</p>\n"
	            + "            <p>" + this.content + "</p>\n"
	            + "            <p>Category: " + this.category + "</p>\n"
	            + "            <p>Time: " + this.getTime() + "</p>\n"
	            + "        </div>\n"
	            + "        <div class=\"footer\">\n"
	            + "            <p>Best regards,<br>" + this.author + "<br>SMA, Ministry of Innovation Science and Tech</p>\n"
	            + "        </div>\n"
	            + "    </div>\n"
	            + "</body>\n"
	            + "</html>";

	    return html;
	}
	
	
	public void sendAsEmail(ArrayList<studentModel> allStudent) {
		
		mailSender mail = new mailSender(System.getenv("APP_EMAIL"),System.getenv("APP_PASSWORD"));
		String message = this.toHtml();
		
		for (studentModel s : allStudent) {
			try {
				mail.sendEmail(s.getEmail(), "Notification from MIST SMA", message);
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		
	}

}