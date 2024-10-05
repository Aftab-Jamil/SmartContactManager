package com.smart.helper;

public class MessageHelper {
	public String content;
    public String type;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public MessageHelper(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "MessageHelper [message=" + content + ", type=" + type + "]";
	}
	
}
