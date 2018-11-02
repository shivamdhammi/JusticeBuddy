package com.example.shivamdhammi.justicebuddy;

public class posts {
    public String imagurl;
    public String status;
    public String mobile;
    public String email;
    public String postId;

    public posts(String imagurl, String status, String mobile, String email, String postId) {
        this.imagurl = imagurl;
        this.status = status;
        this.mobile = mobile;
        this.email = email;
        this.postId = postId;
    }

    public String getImagurl() {
        return imagurl;
    }

    public void setImagurl(String imagurl) {
        this.imagurl = imagurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}