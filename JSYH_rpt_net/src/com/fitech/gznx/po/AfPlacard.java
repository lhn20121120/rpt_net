package com.fitech.gznx.po;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.sql.Blob;
/**
 * AfPlacard entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfPlacard implements java.io.Serializable {

	// Fields

	private Long placardId;
	private String title;
	private Blob contents;
	private String publicUserId;
	private Date publicDate;
	private Long fileId;
	 private Set afPlacardUserViews = new HashSet(0);

	// Constructors

	/** default constructor */
	public AfPlacard() {
	}

	/** minimal constructor */
	public AfPlacard(Long placardId) {
		this.placardId = placardId;
	}

	/** full constructor */
	public AfPlacard(Long placardId, String title, Blob contents,
			String publicUserId, Date publicDate, Long fileId, Set afPlacardUserViews) {
		this.placardId = placardId;
		this.title = title;
		this.contents = contents;
		this.publicUserId = publicUserId;
		this.publicDate = publicDate;
		this.fileId = fileId;
		this.afPlacardUserViews = afPlacardUserViews;
	}

	// Property accessors

	public Long getPlacardId() {
		return this.placardId;
	}

	public void setPlacardId(Long placardId) {
		this.placardId = placardId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Blob getContents() {
		return this.contents;
	}

	public void setContents(Blob contents) {
		this.contents = contents;
	}

	public String getPublicUserId() {
		return this.publicUserId;
	}

	public void setPublicUserId(String publicUserId) {
		this.publicUserId = publicUserId;
	}

	public Date getPublicDate() {
		return this.publicDate;
	}

	public void setPublicDate(Date publicDate) {
		this.publicDate = publicDate;
	}

	public Long getFileId() {
		return this.fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Set getAfPlacardUserViews() {
		return afPlacardUserViews;
	}

	public void setAfPlacardUserViews(Set afPlacardUserViews) {
		this.afPlacardUserViews = afPlacardUserViews;
	}

}