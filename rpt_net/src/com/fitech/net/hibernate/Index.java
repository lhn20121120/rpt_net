/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Tue Oct 17 14:59:58 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.fitech.net.hibernate;

import java.io.Serializable;

/**
 * A class that represents a row in the INDEX table. 
 * You can customize the behavior of this class by editing the class, {@link Index()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public class Index 
    implements Serializable
{
	
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.Integer id;

    /** The value of the simple iName property. */
    private java.lang.String name;

    /** The value of the simple iDefaultvalue property. */
    private java.lang.String defaultvalue;

    /** The value of the simple iPrecision property. */
    private java.lang.String precision;

    /** The value of the simple iType property. */
    private java.lang.String type;

    /** The value of the simple iIscollect property. */
    private java.lang.String iscollect;

    /**
     * Simple constructor of AbstractIndex instances.
     */
    public Index()
    {
    }

    /**
	 * @return Returns the defaultvalue.
	 */
	public java.lang.String getDefaultvalue() {
		return defaultvalue;
	}
	
	/**
	 * @param defaultvalue The defaultvalue to set.
	 */
	public void setDefaultvalue(java.lang.String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	/**
	 * @return Returns the hashValue.
	 */
	public int getHashValue() {
		return hashValue;
	}

	/**
	 * @param hashValue The hashValue to set.
	 */
	public void setHashValue(int hashValue) {
		this.hashValue = hashValue;
	}

	/**
	 * @return Returns the id.
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the iscollect.
	 */
	public java.lang.String getIscollect() {
		return iscollect;
	}

	/**
	 * @param iscollect The iscollect to set.
	 */
	public void setIscollect(java.lang.String iscollect) {
		this.iscollect = iscollect;
	}

	/**
	 * @return Returns the name.
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * @return Returns the precision.
	 */
	public java.lang.String getPrecision() {
		return precision;
	}

	/**
	 * @param precision The precision to set.
	 */
	public void setPrecision(java.lang.String precision) {
		this.precision = precision;
	}
	
	/**
	 * @return Returns the type.
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	/**
     * Implementation of the equals comparison on the basis of equality of the primary key values.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof Index))
            return false;
        Index that = (Index) rhs;
        if (this.getId() == null || that.getId() == null)
            return false;
        return (this.getId().equals(that.getId()));
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
            int iIdValue = this.getId() == null ? 0 : this.getId().hashCode();
            result = result * 37 + iIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
