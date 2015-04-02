package com.gather.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Notice implements Serializable {

    /** identifier field */
    private Integer noticeId;

    /** nullable persistent field */
    private String noticeTitle;

    /** nullable persistent field */
    private String content;

    /** nullable persistent field */
    private Date publishTime;

    /** nullable persistent field */
    private Integer state;

    /** full constructor */
    public Notice(Integer noticeId, String noticeTitle, String content, Date publishTime, Integer state) {
        this.noticeId = noticeId;
        this.noticeTitle = noticeTitle;
        this.content = content;
        this.publishTime = publishTime;
        this.state = state;
    }

    /** default constructor */
    public Notice() {
    }

    /** minimal constructor */
    public Notice(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public Integer getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return this.noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return this.publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("noticeId", getNoticeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Notice) ) return false;
        Notice castOther = (Notice) other;
        return new EqualsBuilder()
            .append(this.getNoticeId(), castOther.getNoticeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getNoticeId())
            .toHashCode();
    }

}
