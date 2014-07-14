package com.fitech.net.hibernate;
import java.io.Serializable;

public class TargetNormal implements Serializable
{
	private Integer normalId;
	
	private String normalName;
	
	private String normalNote;
	
	private int hashValue = 0;
   
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof TargetNormal))
            return false;
        TargetNormal that = (TargetNormal) rhs;
        if (this.getNormalId() == null || that.getNormalId() == null)
            return false;
        return (this.getNormalId().equals(that.getNormalId()));
    }

    /**
     * Implementation of the hashCode method conforming to the Bloch pattern with
     * the exception of array properties (these are very unlikely primary key types).
     * @return int
     */
    public int hashCode()
    {
        if (this.hashValue == 0)
        {
            int result = 17;
            int regionIdValue = this.getNormalId() == null ? 0 : this.getNormalId().hashCode();
            result = result * 37 + regionIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }

	

	/**
	 * @return Returns the normalName.
	 */
	public String getNormalName() {
		return normalName;
	}

	/**
	 * @param normalName The normalName to set.
	 */
	public void setNormalName(String normalName) {
		this.normalName = normalName;
	}

	/**
	 * @return Returns the normalNote.
	 */
	public String getNormalNote() {
		return normalNote;
	}

	/**
	 * @param normalNote The normalNote to set.
	 */
	public void setNormalNote(String normalNote) {
		this.normalNote = normalNote;
	}

	/**
	 * @return Returns the normalId.
	 */
	public Integer getNormalId() {
		return normalId;
	}

	/**
	 * @param normalId The normalId to set.
	 */
	public void setNormalId(Integer normalId) {
		this.normalId = normalId;
	}
}
