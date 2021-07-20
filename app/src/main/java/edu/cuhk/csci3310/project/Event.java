package edu.cuhk.csci3310.project;

public class Event {
    private String mDescription;
    private String mPeriod;
    private String mCal_burn;
    private String mLocation;
    private String mRemark;
    private String mStartTime;
    private String mEndTime;
    private String mWebLink;

    public Event(){}


    public Event(String description,String period,String cal_burn,String location,
            String Remark,String start_time,String end_time,String webLink){

        mDescription = description;
        mPeriod = period;
        mCal_burn = cal_burn;
        mRemark = Remark;
        mLocation = location;
        mStartTime = start_time;
        mEndTime = end_time;
        mWebLink = webLink;

    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getPeriod() {
        return mPeriod;
    }

    public void setPeriod(String mPeriod) {
        this.mPeriod = mPeriod;
    }

    public String getCal_burn() {
        return mCal_burn;
    }

    public void setCal_burn(String mCal_burn) {
        this.mCal_burn = mCal_burn;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getRemark() {
        return mRemark;
    }

    public void setRemark(String mRemark) {
        this.mRemark = mRemark;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }


    public void setWebLink(String webLink) {
        this.mWebLink = webLink;
    }

    public String getWebLink() {
        return this.mWebLink;
    }

}
