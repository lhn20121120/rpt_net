package com.fitech.instiution.hbm;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MCellFormuStandard implements Serializable
{

    /** identifier field */
    private Integer cellFormuId;

    /** nullable persistent field */
    private String cellFormu;

    /** nullable persistent field */
    private Integer formuType;

    /** persistent field */
    private Set colToFormus;

    /** persistent field */
    private Set MCellToFormus;

    /** persistent field */
    private Set dataValidateInfos;

    /**
     * 单元格公式的显示公式
     */
    private String cellFormuView = null;

    private Long level;

    /** full constructor */
    public MCellFormuStandard(Integer cellFormuId, String cellFormu, Integer formuType, Set colToFormus,
            Set MCellToFormus, Set dataValidateInfos, String cellFormuView, Long level)
    {
        this.cellFormuId = cellFormuId;
        this.cellFormu = cellFormu;
        this.formuType = formuType;
        this.colToFormus = colToFormus;
        this.MCellToFormus = MCellToFormus;
        this.dataValidateInfos = dataValidateInfos;
        this.cellFormuView = cellFormuView;
        this.level = level;
    }

    /** default constructor */
    public MCellFormuStandard()
    {
    }

    /** minimal constructor */
    public MCellFormuStandard(Integer cellFormuId, Set colToFormus, Set MCellToFormus, Set dataValidateInfos)
    {
        this.cellFormuId = cellFormuId;
        this.colToFormus = colToFormus;
        this.MCellToFormus = MCellToFormus;
        this.dataValidateInfos = dataValidateInfos;
    }

    public Integer getCellFormuId()
    {
        return this.cellFormuId;
    }

    public void setCellFormuId(Integer cellFormuId)
    {
        this.cellFormuId = cellFormuId;
    }

    public String getCellFormu()
    {
        return this.cellFormu;
    }

    public void setCellFormu(String cellFormu)
    {
        this.cellFormu = cellFormu;
    }

    public Integer getFormuType()
    {
        return this.formuType;
    }

    public void setFormuType(Integer formuType)
    {
        this.formuType = formuType;
    }

    public Set getColToFormus()
    {
        return this.colToFormus;
    }

    public void setColToFormus(Set colToFormus)
    {
        this.colToFormus = colToFormus;
    }

    public Set getMCellToFormus()
    {
        return this.MCellToFormus;
    }

    public void setMCellToFormus(Set MCellToFormus)
    {
        this.MCellToFormus = MCellToFormus;
    }

    public Set getDataValidateInfos()
    {
        return this.dataValidateInfos;
    }

    public void setDataValidateInfos(Set dataValidateInfos)
    {
        this.dataValidateInfos = dataValidateInfos;
    }

    public String toString()
    {
        return new ToStringBuilder(this).append("cellFormuId", getCellFormuId()).toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof MCellFormuStandard))
            return false;
        MCellFormuStandard castOther = (MCellFormuStandard) other;
        return new EqualsBuilder().append(this.getCellFormuId(), castOther.getCellFormuId()).isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder().append(getCellFormuId()).toHashCode();
    }

    public String getCellFormuView()
    {
        return cellFormuView;
    }

    public void setCellFormuView(String cellFormuView)
    {
        this.cellFormuView = cellFormuView;
    }

    public Long getLevel()
    {
        return this.level;
    }

    public void setLevel(Long level)
    {
        this.level = level;
    }
}
