package me.gavin.net;

import com.google.gson.annotations.SerializedName;

/**
 * 标准网络请求结果
 *
 * @author gavin.xiong 2018/6/30.
 */
public final class Result<T> {

    public static final int STATUS_OK = 1000;
    public static final int STATUS_ERR_NO_ATUH = 1001; // 需要授权
    public static final int STATUS_ERR_ACCOUNT_NO_PHONE = 1002030; // 忘记密码-账户未绑定手机号和邮箱
    public static final int STATUS_ERR_PROJECT_NO_CONTENT = 1002050; // 项目详情-尽调无内容

    private int status;
    private String msg;
    private String subtopic;
    @SerializedName("appContentResponse")
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(String subtopic) {
        this.subtopic = subtopic;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return status == STATUS_OK
                || status == STATUS_ERR_NO_ATUH
                || status == STATUS_ERR_ACCOUNT_NO_PHONE
                || status == STATUS_ERR_PROJECT_NO_CONTENT;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", subtopic='" + subtopic + '\'' +
                ", data=" + data +
                '}';
    }
}
