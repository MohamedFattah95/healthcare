package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

public class FAQsModel {

    /**
     * id : 1
     * is_active : 1
     * ip : 125.32.326.210
     * access_user_id : 1
     * created_at : 2020-08-09T01:32:38.000000Z
     * updated_at : 2020-08-08T22:32:38.000000Z
     * question : {"en":"how to subscribe","ar":"هل يقوم التطبيق بحفظ البيانات وقائمة الاتصال الخاصة؟"}
     * answer : {"en":"answer","ar":"نعم، يحتفظ التطبيق بالبيانات و قوائم الاتصال لاستخدامها في إدارة  المنصة و الموقع الإلكتروني وأعمالنا على شبكة الإنترنت،  وفي تخصيص  المنصة و الموقع ليكون أكثر ملاءمة بالنسبة للمستخدمين والعملاء و استطلاع رأي العملاء في تحسين جودة الخدمات، و في تمكين المستخدمين والعملاء من استخدام الخدمات المتاحة على المنصة وإرسال البيانات والفواتير والتنبيهات والإشعارات الدفع، وتحصيل المدفوعات، و إرسال رسائل تجارية وتسويقية، و التعامل مع الاستفسارات والشكاوى ولحماية المنصات من الاحتيال والحفاظ على أمنه"}
     */

    @SerializedName("id")
    private int id;
    @SerializedName("is_active")
    private int isActive;
    @SerializedName("ip")
    private String ip;
    @SerializedName("access_user_id")
    private int accessUserId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("question")
    private QuestionBean question;
    @SerializedName("answer")
    private AnswerBean answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getAccessUserId() {
        return accessUserId;
    }

    public void setAccessUserId(int accessUserId) {
        this.accessUserId = accessUserId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public QuestionBean getQuestion() {
        return question;
    }

    public void setQuestion(QuestionBean question) {
        this.question = question;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public static class QuestionBean {
        /**
         * en : how to subscribe
         * ar : هل يقوم التطبيق بحفظ البيانات وقائمة الاتصال الخاصة؟
         */

        @SerializedName("en")
        private String en;
        @SerializedName("ar")
        private String ar;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public String getAr() {
            return ar;
        }

        public void setAr(String ar) {
            this.ar = ar;
        }
    }

    public static class AnswerBean {
        /**
         * en : answer
         * ar : نعم، يحتفظ التطبيق بالبيانات و قوائم الاتصال لاستخدامها في إدارة  المنصة و الموقع الإلكتروني وأعمالنا على شبكة الإنترنت،  وفي تخصيص  المنصة و الموقع ليكون أكثر ملاءمة بالنسبة للمستخدمين والعملاء و استطلاع رأي العملاء في تحسين جودة الخدمات، و في تمكين المستخدمين والعملاء من استخدام الخدمات المتاحة على المنصة وإرسال البيانات والفواتير والتنبيهات والإشعارات الدفع، وتحصيل المدفوعات، و إرسال رسائل تجارية وتسويقية، و التعامل مع الاستفسارات والشكاوى ولحماية المنصات من الاحتيال والحفاظ على أمنه
         */

        @SerializedName("en")
        private String en;
        @SerializedName("ar")
        private String ar;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public String getAr() {
            return ar;
        }

        public void setAr(String ar) {
            this.ar = ar;
        }
    }
}
