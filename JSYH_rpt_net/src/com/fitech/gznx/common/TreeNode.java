package com.fitech.gznx.common;

import java.util.List;

public class TreeNode {
    private String key;
    private String caption;    
    private List subNodes;

    public TreeNode() {
    }

    public TreeNode(String caption) {
        this.caption = caption;
    }

    public TreeNode(String key, String caption) {
        this.key = key;
        this.caption = caption;
    }

    public TreeNode(String key, String caption, List subNodes) {
        this.key = key;
        this.caption = caption;
        this.subNodes = subNodes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List getSubNodes() {
        return subNodes;
    }

    public void setSubNodes(List subNodes) {
        this.subNodes = subNodes;
    }

    public boolean hasChild(){
        return (subNodes!=null && subNodes.size()>0);
    }
}
