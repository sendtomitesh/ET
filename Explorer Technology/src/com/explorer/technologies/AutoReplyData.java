package com.explorer.technologies;

public class AutoReplyData {
	String id;
	String keyword;
	String message;
	int count = 0;

	public AutoReplyData() {

	}

	public AutoReplyData(String id, String keyword, String message) {
		this.id = id;
		this.keyword = keyword;
		this.message = message;
	}
	
	public AutoReplyData(String id, String keyword, String message,int count) {
		this.id = id;
		this.keyword = keyword;
		this.message = message;
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getMessage() {
		return message;
	}
	public int getCount() {
		return count;
	}

}
