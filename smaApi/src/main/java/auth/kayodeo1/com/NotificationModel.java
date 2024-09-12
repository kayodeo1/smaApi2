package auth.kayodeo1.com;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationModel {
    private String title;
    private long timeStamp;
    private String author = "Admin";
    private String content;
    private String category = "General";
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

}