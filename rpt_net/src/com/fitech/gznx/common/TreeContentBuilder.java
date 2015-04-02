package com.fitech.gznx.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fitech.gznx.common.TreeNode;

public class TreeContentBuilder {
    private String treeName;
    private List content;
    private Map checkItem;
    private boolean multiSel;


    public TreeContentBuilder(String treeName,List content, Map checkItem,boolean multiSel) {
        this.treeName = treeName;
        this.content = content;
        this.checkItem = checkItem;
        this.multiSel = multiSel;
    }

    private boolean checkStaute(String key){
        if(checkItem!=null && checkItem.size()>0 && checkItem.containsKey(key)){
            return true;
        }else{
            return false;
        }
    }

    public String buildTreeContent(){
        StringBuffer buffer = new StringBuffer();
        if(content!=null && content.size()>0){
            buildStart(buffer);
            buildNodes(content,buffer,1);
            buildEnd(buffer);
        }
        return buffer.toString();
    }

    private void buildStart(StringBuffer buffer){
        buffer.append("var ").append(treeName).append(" = [\n");
    }

    private void buildEnd(StringBuffer buffer){
        buffer.append("\n];");
    }

    private void buildNodes(List treeList , StringBuffer buffer,int level){
        if(treeList!=null && treeList.size()>0){
            Iterator iterator = treeList.iterator();
            TreeNode treeNode;
            while(iterator.hasNext()){
                treeNode = (TreeNode)iterator.next();
                buffer.append("\n");
                for(int i=0;i<level;i++){
                    buffer.append("\t");
                }
                buffer.append("['");
                buffer.append(treeNode.getCaption());
                buffer.append("',null,null,'");
                buffer.append(treeNode.getKey().replaceAll("'","\'")).append("',");
                if(multiSel && checkStaute(treeNode.getKey())){
                    buffer.append("'checked'");
                }else{
                    buffer.append("''");
                }
                if(treeNode.hasChild()){
                    buffer.append(",");
                    buildNodes(treeNode.getSubNodes(),buffer,level+1);
                    buffer.append("\n");
                    for(int i=0;i<level;i++){
                        buffer.append("\t");
                    }
                }

                buffer.append("],");
            }
            buffer.setLength(buffer.length()-1);
        }
    }

    public List getContent() {
        return content;
    }

    public void setContent(List content) {
        this.content = content;
    }

    public boolean isMultiSel() {
        return multiSel;
    }

    public void setMultiSel(boolean multiSel) {
        this.multiSel = multiSel;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public Map getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(Map checkItem) {
        this.checkItem = checkItem;
    }

}
