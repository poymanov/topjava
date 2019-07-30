package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private String url;
    private ErrorType type;
    private String detail;

    public ErrorInfo() {
    }

    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ru.javawebinar.topjava.util.exception.ErrorType getType() {
        return type;
    }

    public void setType(ru.javawebinar.topjava.util.exception.ErrorType type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}