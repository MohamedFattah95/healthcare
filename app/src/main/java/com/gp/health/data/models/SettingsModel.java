package com.gp.health.data.models;


import com.google.gson.annotations.SerializedName;

public class SettingsModel {

    /**
     * app_title : موقع أعمال - للمهنيين
     * tax : null
     * delivery_note :
     * google_analytics : null
     * phone_1 : 0555630533    -   0509747177
     * back_ground : settings/back_ground_rtboard_–_1.png__60296e268f2c6_.png
     * mail : info@qrc.com.sa
     * address : السعودية - المدينة المنورة - القصواء - ممشى الهجرة
     * logo : settings/logo_roup_1769.svg__60296ac510cf3_.svg
     * about_us_image : settings/about_us_image_meh_224_user_inst3624.jpg__6029704f964d4_.jpg
     * statment_01_image : settings/about_us_image_meh_224_user_inst3624.jpg__6029704f964d4_.jpg
     * statment_02_image : settings/about_us_image_meh_224_user_inst3624.jpg__6029704f964d4_.jpg
     * lat : 98.325666
     * lng : 96.3215444
     * app_android_lnk : nadroid link
     * app_ios_link : ios link
     * app_share_note : st
     * facebook : https://facebook.com
     * slogan :
     * tweeter : tweeter
     * instagram : tweeter
     * youtube : youtube.com
     * commission_desc :
     * statment_01 : احدث عروض العقارات
     * statment_02 : احدث عروض العقارات 2
     * add_commerce : خدمة اضافة نشاط تجارى2
     * website : www.heraj.com
     * about_us : <p>&nbsp;</p>
     *
     * <p>عن الموقع&nbsp;لوريم إيبسوم طريقة لكتابة النصوص في النشر والتصميم الجرافيكي تستخدم بشكل شائع لتوضيح الشكل المرئي للمستند أو الخط دون الاعتماد على محتوى ذي معنى. يمكن استخدام لوريم إيبسوم قبل نشر النسخة النهائية.&nbsp;عن الموقع&nbsp;لوريم إيبسوم طريقة لكتابة النصوص في النشر والتصميم الجرافيكي تستخدم بشكل شائع لتوضيح الشكل المرئي للمستند أو الخط دون الاعتماد على محتوى ذي معنى. يمكن استخدام لوريم إيبسوم قبل نشر النسخة النهائية.&nbsp;</p>
     * terms : <p>&nbsp;</p>
     *
     * <p>&nbsp;سياسة الاستخدام&nbsp;&nbsp;&nbsp;لوريم إيبسوم طريقة لكتابة النصوص في النشر والتصميم الجرافيكي تستخدم بشكل شائع لتوضيح الشكل المرئي للمستند أو الخط دون الاعتماد على محتوى ذي معنى. يمكن استخدام لوريم إيبسوم قبل نشر النسخة النهائية.&nbsp;</p>
     * privacy : <p>&nbsp;</p>
     *
     * <p>&nbsp;سياسة الخصوصية&nbsp;عن الموقع&nbsp;لوريم إيبسوم طريقة لكتابة النصوص في النشر والتصميم الجرافيكي تستخدم بشكل شائع لتوضيح الشكل المرئي للمستند أو الخط دون الاعتماد على محتوى ذي معنى. يمكن استخدام لوريم إيبسوم قبل نشر النسخة النهائية.&nbsp;</p>
     * register_st_1 :
     * customer_service : null
     * register_st_2 :
     * register_st_3 :
     * add_adv_condition : شروط اضافة الاعلان
     * adv_commission : رسوم الاعلان **** رسوم اضافة اعلان ****
     */

    @SerializedName("app_title")
    private String appTitle;
    @SerializedName("tax")
    private Object tax;
    @SerializedName("delivery_note")
    private String deliveryNote;
    @SerializedName("google_analytics")
    private Object googleAnalytics;
    @SerializedName("phone_1")
    private String phone1;
    @SerializedName("back_ground")
    private String backGround;
    @SerializedName("mail")
    private String mail;
    @SerializedName("address")
    private String address;
    @SerializedName("logo")
    private String logo;
    @SerializedName("about_us_image")
    private String aboutUsImage;
    @SerializedName("statment_01_image")
    private String statment01Image;
    @SerializedName("statment_02_image")
    private String statment02Image;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("app_android_lnk")
    private String appAndroidLnk;
    @SerializedName("app_ios_link")
    private String appIosLink;
    @SerializedName("app_share_note")
    private String appShareNote;
    @SerializedName("facebook")
    private String facebook;
    @SerializedName("slogan")
    private String slogan;
    @SerializedName("tweeter")
    private String tweeter;
    @SerializedName("instagram")
    private String instagram;
    @SerializedName("youtube")
    private String youtube;
    @SerializedName("commission_desc")
    private String commissionDesc;
    @SerializedName("statment_01")
    private String statment01;
    @SerializedName("statment_02")
    private String statment02;
    @SerializedName("add_commerce")
    private String addCommerce;
    @SerializedName("website")
    private String website;
    @SerializedName("about_us")
    private String aboutUs;
    @SerializedName("terms")
    private String terms;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("register_st_1")
    private String registerSt1;
    @SerializedName("customer_service")
    private Object customerService;
    @SerializedName("register_st_2")
    private String registerSt2;
    @SerializedName("register_st_3")
    private String registerSt3;
    @SerializedName("add_adv_condition")
    private String addAdvCondition;
    @SerializedName("adv_commission")
    private String advCommission;
    /**
     * snapchat : snaplink
     */

    @SerializedName("snapchat")
    private String snapchat;

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public Object getTax() {
        return tax;
    }

    public void setTax(Object tax) {
        this.tax = tax;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Object getGoogleAnalytics() {
        return googleAnalytics;
    }

    public void setGoogleAnalytics(Object googleAnalytics) {
        this.googleAnalytics = googleAnalytics;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getBackGround() {
        return backGround;
    }

    public void setBackGround(String backGround) {
        this.backGround = backGround;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAboutUsImage() {
        return aboutUsImage;
    }

    public void setAboutUsImage(String aboutUsImage) {
        this.aboutUsImage = aboutUsImage;
    }

    public String getStatment01Image() {
        return statment01Image;
    }

    public void setStatment01Image(String statment01Image) {
        this.statment01Image = statment01Image;
    }

    public String getStatment02Image() {
        return statment02Image;
    }

    public void setStatment02Image(String statment02Image) {
        this.statment02Image = statment02Image;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAppAndroidLnk() {
        return appAndroidLnk;
    }

    public void setAppAndroidLnk(String appAndroidLnk) {
        this.appAndroidLnk = appAndroidLnk;
    }

    public String getAppIosLink() {
        return appIosLink;
    }

    public void setAppIosLink(String appIosLink) {
        this.appIosLink = appIosLink;
    }

    public String getAppShareNote() {
        return appShareNote;
    }

    public void setAppShareNote(String appShareNote) {
        this.appShareNote = appShareNote;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getTweeter() {
        return tweeter;
    }

    public void setTweeter(String tweeter) {
        this.tweeter = tweeter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getCommissionDesc() {
        return commissionDesc;
    }

    public void setCommissionDesc(String commissionDesc) {
        this.commissionDesc = commissionDesc;
    }

    public String getStatment01() {
        return statment01;
    }

    public void setStatment01(String statment01) {
        this.statment01 = statment01;
    }

    public String getStatment02() {
        return statment02;
    }

    public void setStatment02(String statment02) {
        this.statment02 = statment02;
    }

    public String getAddCommerce() {
        return addCommerce;
    }

    public void setAddCommerce(String addCommerce) {
        this.addCommerce = addCommerce;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getRegisterSt1() {
        return registerSt1;
    }

    public void setRegisterSt1(String registerSt1) {
        this.registerSt1 = registerSt1;
    }

    public Object getCustomerService() {
        return customerService;
    }

    public void setCustomerService(Object customerService) {
        this.customerService = customerService;
    }

    public String getRegisterSt2() {
        return registerSt2;
    }

    public void setRegisterSt2(String registerSt2) {
        this.registerSt2 = registerSt2;
    }

    public String getRegisterSt3() {
        return registerSt3;
    }

    public void setRegisterSt3(String registerSt3) {
        this.registerSt3 = registerSt3;
    }

    public String getAddAdvCondition() {
        return addAdvCondition;
    }

    public void setAddAdvCondition(String addAdvCondition) {
        this.addAdvCondition = addAdvCondition;
    }

    public String getAdvCommission() {
        return advCommission;
    }

    public void setAdvCommission(String advCommission) {
        this.advCommission = advCommission;
    }


    public String getSnapchat() {
        return snapchat;
    }

    public void setSnapchat(String snapchat) {
        this.snapchat = snapchat;
    }
}
