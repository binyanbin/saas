package com.bzw.api.module.basic.param;

public class WechatTemplatetMessageParam {
    private String touser;
    private String template_id;
    private String page;
    private String form_id;
    private WechatTemplateData data;

    public WechatTemplateData getData() {
        return data;
    }

    public void setData(WechatTemplateData data) {
        this.data = data;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

}
