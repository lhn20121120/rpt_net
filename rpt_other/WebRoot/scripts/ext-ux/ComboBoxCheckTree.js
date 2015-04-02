Ext.ux.ComboBoxCheckTree = function() {
  this.treeId = Ext.id()+'-tree';
  this.maxHeight = arguments[0].maxHeight || arguments[0].height || this.maxHeight;
  this.tpl = new Ext.Template('<tpl for="."><div style="height:'+this.maxHeight+'px"><div id="'+this.treeId+'"></div></div></tpl>');
  this.store = new Ext.data.SimpleStore({fields:[],data:[[]]});
  this.selectedClass = '';
  this.mode = 'local';
  this.triggerAction = 'all';
  this.onSelect = Ext.emptyFn;
  this.editable = false;

  this.selectValueModel = 'leaf';

  Ext.ux.ComboBoxCheckTree.superclass.constructor.apply(this, arguments);
}

Ext.extend(Ext.ux.ComboBoxCheckTree, Ext.form.ComboBox, {

  checkField : 'checked',
  separator : ',',
  initEvents : function() {
    Ext.ux.ComboBoxCheckTree.superclass.initEvents.apply(this, arguments);
    this.keyNav.tab = false;

  },

  initComponent : function() {
    this.on({
      scope : this
    });

  },
  expand : function() {
    Ext.ux.ComboBoxCheckTree.superclass.expand.call(this);
    if (!this.tree.rendered) {
      //this.tree.height = this.maxHeight;
      this.tree.height = this.height;
      this.tree.border = false;
      this.tree.autoScroll = true;
      if (this.tree.xtype) {
        this.tree = Ext.ComponentMgr.create(this.tree, this.tree.xtype);
      }
      this.tree.render(this.treeId);
      this.tree.expandAll();
      var combox = this;
      this.tree.on('check', function(node, checked) {
        var values = [];
        var texts = [];
        var checkedNodes = combox.tree.getChecked();
        for (var i = 0; i < checkedNodes.length; i++) {
          var node = checkedNodes[i];
          if (combox.selectValueModel == 'all'
              || (combox.selectValueModel == 'leaf' && node.isLeaf())
              || (combox.selectValueModel == 'folder' && !node.isLeaf())) {
            values.push(node.id);
            texts.push(node.text);
          }
        }
        combox.setValue({values:values.join(combox.separator),texts:texts.join(combox.separator)});
      });
      var root = this.tree.getRootNode();
      if(!root.isLoaded()){
        root.reload();
      }
      if(this.needClear){
        this.needClear = false;
      }
      if(this.needReload){
        this.needReload = false;
      }
    }else{
      if(this.needClear){
        this.clearValue();
        var checkedNodes = this.tree.getChecked();
        for (var i = 0; i < checkedNodes.length; i++) {
          var node = checkedNodes[i];
          node.getUI().checkbox.checked = false;
          node.attributes.checked = false;
          this.tree.fireEvent('check', node, false);
        }
        this.needClear = false;
      }
      if(this.needReload){
        this.tree.root.reload();
        this.tree.expandAll();
        this.needReload = false;
      }
    }
  },

  setValue : function(obj) {
    if (this.hiddenField) {
      this.hiddenField.value = obj.values;
    }
    Ext.form.ComboBox.superclass.setValue.call(this, obj.texts);
    this.value = obj.values;
    this.setRawValue(obj.texts);
  },

  getValue : function() {
    return this.value || '';
  },

  clearValue : function() {
    this.value = '';
    this.setRawValue('');
    if (this.hiddenField) {
      this.hiddenField.value = '';
    }

    //this.tree.getSelectionModel().clearSelections();
  }
});

Ext.reg('combochecktree', Ext.ux.ComboBoxCheckTree);