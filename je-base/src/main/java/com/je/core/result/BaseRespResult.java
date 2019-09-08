package com.je.core.result;

import com.je.core.facade.extjs.JsonBuilder;

import java.io.Serializable;

public class BaseRespResult<T> implements Serializable {
    private static final long serialVersionUID = -4507574037830481719L;

    private static final String OK_CODE = "1000";
    private static final String OK_MESSAGE = "操作成功";

    boolean success;
    private String code;
    private String errorCode;
    private String message;
    private String exceptionId;
    private T obj;

    public BaseRespResult() {}


    public BaseRespResult(boolean success, T obj, String code, String message,String errorCode,String exceptionId){
        this.success = success;
        this.obj = obj;
        this.code = code;
        this.message = message;
        this.errorCode=errorCode;
        this.exceptionId=exceptionId;
    }
    public static BaseRespResult errorResult(String code, String obj) {
        return new BaseRespResult(false, obj, code, obj,"","");
    }
    public static BaseRespResult errorResult(String code,String errorCode, String message) {
        return new BaseRespResult(false, null, code, message,errorCode,"");
    }
    public static BaseRespResult errorResult(String code,String errorCode,String obj, String message) {
        return new BaseRespResult(false, obj, code, message,errorCode,"");
    }
    public static BaseRespResult errorResult(String code,String errorCode,String obj, String message,String exceptionId) {
        return new BaseRespResult(false, obj, code, message,errorCode,exceptionId);
    }
    public static BaseRespResult successResult(Object obj) {
        return new BaseRespResult(true, obj, OK_CODE, OK_MESSAGE,"","");
    }
    public static BaseRespResult successResult(Object obj,String message) {
        return new BaseRespResult(true, obj, OK_CODE, message,"","");
    }
    @Override
    public String toString() {
        return JsonBuilder.getInstance().toJson(this);
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObj() {
        return obj;
    }

    public void seObj(T obj) {
        this.obj = obj;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    //    public static class BaseRespResult<T> implements Serializable {
//
//        private static final String OK_CODE = "1000";
//        private static final String OK_MESSAGE = "操作成功";
//
//        boolean success;
//        private String code;
//        private String message;
//        private T obj;
//
//        public BaseRespResult() {
//        }
//
//
//        public BaseRespResult(boolean success, T obj, String code, String message) {
//            this.success = success;
//            this.obj = obj;
//            this.code = code;
//            this.message = message;
//        }
//
//        public static BaseRespResult errorResult(String code, String message) {
//            return new BaseRespResult(false, null, code, message);
//        }
//
//        public static BaseRespResult successResult(Object obj) {
//            return new BaseRespResult(true, obj, OK_CODE, OK_MESSAGE);
//        }
//
//        @Override
//        public String toString() {
//            final StringBuffer sb = new StringBuffer("{");
//            sb.append("\"success\":")
//                    .append(success);
//            sb.append(",\"code\":\"")
//                    .append(code).append('\"');
//            sb.append(",\"message\":\"")
//                    .append(message).append('\"');
//
//            if (obj != null) {
//                sb.append(",\"obj\":")
//                        .append(obj);
//            }
//
//            sb.append('}');
//            return new String(sb);
//        }
//
//        public boolean getSuccess() {
//            return success;
//        }
//
//        public void setSuccess(boolean success) {
//            this.success = success;
//        }
//
//        public String getCode() {
//            return code;
//        }
//
//        public void setCode(String code) {
//            this.code = code;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//
//        public T getObj() {
//            return obj;
//        }
//
//        public void seObj(T obj) {
//            this.obj = obj;
//        }
//    }
}
