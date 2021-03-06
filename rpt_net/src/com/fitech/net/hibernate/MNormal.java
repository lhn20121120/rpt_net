/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Sat Jul 22 14:57:30 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.fitech.net.hibernate;

import java.io.Serializable;

/**
 * A class that represents a row in the M_NORMAL table. 
 * You can customize the behavior of this class by editing the class, {@link MNormal()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public  class MNormal 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.Integer normalId;

    /** The value of the simple nmNormalsortname property. */
    private java.lang.String normalName;

    /** The value of the simple nmNote property. */
    private java.lang.String normalNote;

    private java.util.Set  targetDefine;
    /**
     * Simple constructor of AbstractMNormal instances.
     */
    public MNormal()
    {
    }

    /**
     * Constructor of AbstractMNormal instances given a simple primary key.
     * @param nmNormalsortid
     */
    public MNormal(java.lang.Integer nmNormalsortid)
    {
        this.setNormalId(nmNormalsortid);
    }

   
   
    /**
     * Implementation of the hashCode method conforming to the Bloch pattern with
     * the exception of array properties (these are very unlikely primary key types).
     * @return int
     */
   

	public java.util.Set getTargetDefine() {
		return targetDefine;
	}

	public void setTargetDefine(java.util.Set targetDefine) {
		this.targetDefine = targetDefine;
	}

	public java.lang.Integer getNormalId() {
		return normalId;
	}

	public void setNormalId(java.lang.Integer normalId) {
		this.normalId = normalId;
	}

	public java.lang.String getNormalName() {
		return normalName;
	}

	public void setNormalName(java.lang.String normalName) {
		this.normalName = normalName;
	}

	public java.lang.String getNormalNote() {
		return normalNote;
	}

	public void setNormalNote(java.lang.String normalNote) {
		this.normalNote = normalNote;
	}
	
	public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof MNormal))
            return false;
        MNormal that = (MNormal) rhs;
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

}
