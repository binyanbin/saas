package com.bzw.common.exception;

/**
 * @author :yanbin
 */
public class ApplicationException extends RuntimeException {

    private String code;
    private String reasoning;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String code, String reasoning) {
        super();
        this.code = code;
        this.reasoning = reasoning;
    }
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return the reasoning
     */
    public String getReasoning() {
        return reasoning;
    }
    /**
     * @param reasoning the reasoning to set
     */
    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}
