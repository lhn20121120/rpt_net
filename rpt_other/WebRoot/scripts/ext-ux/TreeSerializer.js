Ext.tree.JsonTreeSerializer = function(tree){
    this.tree = tree;
    this.standardAttributes = ["expanded", "allowDrag", "allowDrop", "disabled", "icon", "cls", "iconCls", "href", "hrefTarget", "qtip", "singleClickExpand", "uiProvider", "loader"];
    this.attributeFilter = function(attName, attValue){
        return (typeof attValue != 'function') && (this.standardAttributes.indexOf(attName) == -1);
    };
    this.nodeFilter = function(node){
        return true;
    };
    this.toString = function(){
    return this.nodeToString(this.getRootNode());
  };
  this.nodeToString = function(node){
    if (!this.nodeFilter(node)) {
      return '';
    }
    var c = false, result = "{";
    for (var key in node.attributes) {
      if (this.attributeFilter(key, node.attributes[key])) {
        if (c)
          result += ',';
        result += '"' + (this.attributeMap ? (this.attributeMap[key] || key) : key) + '":"' + node.attributes[key] + '"';
        c = true;
      }
    }
    var children = node.childNodes;
    var clen = children.length;
    if(clen != 0){
      if (c) result += ',';
      result += '"children":['
      for(var i = 0; i < clen; i++){
        if (i > 0) result += ',';
          result += this.nodeToString(children[i]);
      }
      result += ']';
    }
    return result + "}";
  };
};