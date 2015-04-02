/**
 * the entrance of the main function
 * @author wgm
 * @since 2009-1-26
 * @version 1.1
 * @param store
 */

function onLoad(params) {
	var pagesize = 10;
	var conditions = new Array(); // use to save the query condtion
	if (params["pagesize"])
		pagesize = params["pagesize"];
	var readOnlyIndex = params["readOnlyColumn"];// 只读下标
	var title = params["title"];
	var div = params["div"];
	var listOperater = params["listOperater"];
	var gridParams = params["gridParams"];
	var queryCondition = params["queryCondition"];
	var gridconfig = new GridConfig(gridParams);
	var sexSelect = params["sexSelect"];
	var statusSelect = params["statusSelect"];
	var hiddenButton = params["hiddenButton"] == undefined
			? []
			: params["hiddenButton"];
	var expURL = params["expURL"] == undefined ? '' : params["expURL"];
	var win;
	var querywin;// 查询窗体
	var relationTables_form;// 关联信息
	var relationTables = params["relationTablesInfo"];// 关联表
	var relationTablesDIV = params["relationTablesDIV"];
	var parentDIV = params["parentDIV"];
	var relationTablesInfo_td_tree_id = params["relationTablesInfo_td_tree_id"];
	var relationTables_td = params["relationTables_td"];

	var curRowIndex = -1;// 当前选中行的索引
	Ext.onReady(function() {
				// 加上这两行，显示错误信息--begin
				Ext.QuickTips.init();
				Ext.form.Field.prototype.msgTarget = 'side';
				// --------------------------end

				var MyRecord = Ext.data.Record.create(gridconfig.getRecord());

				var store = new Ext.data.Store({
							proxy : new Ext.data.HttpProxy({
										url : listOperater.list
									}),
							reader : new Ext.data.JsonReader({
										root : 'roots',
										totalProperty : 'totalCount',
										id : 'id'
									}, gridconfig.getRecord())
						});

				var colM = gridconfig.getColumn(store, readOnlyIndex);

				var pagingBar = new Ext.PagingToolbar({
							pageSize : pagesize,
							paramNames : {
								start : "start",
								limit : "limit",
								conditions : "conditions"
							},
							store : store,
							displayInfo : true,
							beforePageText : "第",
							afterPageText : "页 共 {0} 页",
							displayMsg : '第 {0} 条到 {1} 条记录--共 {2} 条',
							emptyMsg : "没有记录",
							doLoad : function(C) {
								var B = {}, A = this.paramNames;
								B[A.start] = C;
								B[A.limit] = this.pageSize;
								B[A.conditions] = conditions;
								if (this.fireEvent("beforechange", this, B) !== false) {
									this.store.load({
												params : B
											})
								}
							},
							items : ['-', {
										pressed : true,
										enableToggle : true,
										text : 'Show Preview',
										cls : 'x-btn-text-icon details',
										toggleHandler : function(btn, pressed) {
											var view = grid.getView();
											view.showPreview = pressed;
											view.refresh();
										}
									}]
						});
				var grid = new Ext.grid.EditorGridPanel({
							renderTo : div,
							height : 290,
							// width: 850,
							bodyStyle : 'width:100%;',
							autoWidth : true,
							viewConfig : {
								width : 300,
								autoScroll : true
							},
							stripeRows : true,// 隔行显示条纹
							cm : colM,
							// title:'员工签到信息管理系统',//窗体名称
							// frame:true,//是否显示在frame中
							store : store,
							autoExpandColumn : 2,// 指定地第二列为自动拉伸列
							bbar : pagingBar,
							loadMask : true,// 显示加载遮罩层
							// selModel: new
							// Ext.grid.RowSelectionModel({singleSelect:false}),//设置单行选中模式,
							// 否则将无法删除数据
							// 全选Checkbox

							selModel : new Ext.grid.CheckboxSelectionModel({
										singleSelect : false
									}),
							clicksToEdit : 1,// 单击编辑
							tbar : [new Ext.Toolbar.TextItem(title), {
										xtype : "tbfill"
									}, {
										pressed : false,
										text : '关联信息',
										tooltip : '显示关闭关联信息',
										id : "showORclose_",
										iconCls : 'count',
										hidden : hiddenButton['search_'],
										handler : function() {
											showORclose(grid, parentDIV);
										}
									}, {
										xtype : "tbseparator"
									}, {
										pressed : false,
										text : '查询',
										tooltip : '查询',
										id : "query",
										iconCls : 'icon-srch',
										hidden : hiddenButton['search_'],
										handler : function() {
											queryRecord(store);
										}
									}, {
										xtype : "tbseparator"
									}, {
										pressed : false,
										text : "全部",
										tooltip : '全部',
										id : "all",
										iconCls : 'allInfo',
										hidden : hiddenButton['all_'],
										handler : function() {
											conditions = [];
											store.load({
														params : {
															start : 0,
															limit : pagesize,
															conditions : conditions
														}
													});
										}
									}, {
										xtype : "tbseparator"
									}, {
										pressed : false,
										text : "添加",
										tooltip : '添加',
										id : "addin",
										iconCls : 'add',
										hidden : hiddenButton['add_'],
										handler : function() {
											addNewRow(
													store,
													grid,
													Ext.data.Record
															.create(gridconfig
																	.getRecord()));
										}
									}, {
										xtype : "tbseparator"
									}, {
										pressed : false,
										text : '新面板中添加',
										tooltip : '新面板中添加',
										id : "add",
										iconCls : 'add',
										hidden : hiddenButton['panelAdd_'],
										handler : function() {
											addRecord(store);
										}
									}, {
										xtype : "tbseparator"
									}, {
										pressed : false,
										text : "删除",
										tooltip : '删除',
										id : "delete",
										iconCls : 'delete',
										hidden : hiddenButton['delete_'],
										handler : function() {
											deleteRecord(store, grid);
										}
									}, {
										xtype : "tbseparator"
									}, {
										pressed : false,
										text : "保存",
										tooltip : '保存',
										id : "save",
										iconCls : 'save',
										hidden : hiddenButton['save_'],
										handler : function() {
											updateRecord(store, grid);
										}
									}, {
										xtype : "tbseparator"
									}, {
										pressed : false,
										text : "导出Excel",
										id : "export",
										iconCls : 'export',
										hidden : hiddenButton['expToExcel_'],
										handler : function() {
											exp2Excel(conditions, expURL);
										}
									}

							/*
							 * new Ext.Toolbar.MenuButton({ text:'导出',
							 * tooltip:'导出', iconCls:'export',
							 * hidden:hiddenButton['expToExcel_'], menu:{items:[
							 * {text:'导出该页',tooltip:'只将显示的该页数据导出到Excel',iconCls:'export',handler:gridconfig.exportExcelHandler(grid)},
							 * {text:'导出全部',tooltip:'将全部数据导出到Excel',iconCls:'export',handler:gridconfig.exportExcelHandler(grid)} ]} })
							 */
							]
						});
				grid.on("cellclick", function(grid, rowIndex, columnIndex, e) {
							var record = grid.getStore().getAt(rowIndex); // 获取点击行数据集
							var fieldName = grid.getColumnModel()
									.getDataIndex(columnIndex); // 获取列名
							var data = record.get(fieldName);// 获取值
							curRowIndex = rowIndex;
							// var cell = grid.selModel.getSelectedCell(); //
							//alert(grid.getSelectionModel().isSelected(0));// 获得选中行
							if (grid.getSelectionModel().getCount() == 0) {
								curRowIndex = -1;
							}
							//Ext.MessageBox.alert('show', record.get('id')
											//+ "--" + record.get('loginName')
											//+ '当前选中的数据是:' + data);
						});
				store.load({
							params : {
								start : 0,
								limit : pagesize,
								conditions : conditions
							}
						});

				// /关联表信息--静态树
				var relationTree = new Ext.tree.TreePanel({
							el : relationTablesInfo_td_tree_id,// 应用到的html元素id
							animate : true,// 以动画形式伸展,收缩子节点
							// title:"关联信息",
							collapsible : true,
							enableDD : true,// 不仅可以拖动,还可以通过Drag改变节点的层次结构(drap和drop)
							enableDrag : true,// 树的节点可以拖动Drag(效果上是),注意不是Draggable
							rootVisible : true,// 是否显示根节点
							autoScroll : true,
							autoHeight : false,// //自动高度,默认为false
							containerScroll : true,// 是否支持滚动条
							border : false,// 不显示边框
							width : 200,
							height : 178,
							lines : true,
							listeners : {
								"click" : function(node) {
									if (node != relationTree.root) {
										// alert(node.id+"--"+node.attributes.index);
										// var cell =
										// grid.getSelectionMode().getSelectedCell();
										// alert(grid.store.getAt(curRowIndex).get('id'));
										subTableInfo(relationTables,
												node.attributes.index);// 生成表单
										if (curRowIndex > -1)
											fillForm(node.id, grid);// 填充表单
										/*
										 * if(grid.getSelectionModel().getCount()>0){
										 * for(var i=0,j=grid.store.getCount();i<j;i++){
										 * if(grid.getSelectionModel().isSelected(i)){
										 * alert(grid.store.getAt(i).get('id'));
										 * break; } } }
										 */
									}
								}
							}
						});
				// 根节点
				var root = new Ext.tree.TreeNode({
							id : "root",
							text : "关联信息",
							expanded : true
						});

				for (var i = 0; i < relationTables.length; i++) {
					root.appendChild(new Ext.tree.TreeNode({
								id : relationTables[i].tableName,
								text : relationTables[i].showTableName,
								index : i
							}));

				}
				relationTree.setRootNode(root);// 设置根节点
				relationTree.render();// 不要忘记render()下,不然不显示哦

			});

	// 显示关闭关联信息\
	function showORclose(grid, parentDiv) {
		var parentDIV = document.getElementById(parentDiv);
		if (parentDIV && parentDIV != null) {
			parentDIV.style.display = parentDIV.style.display == 'none'
					? ''
					: 'none';
			// grid.setHeight(parentDIV.style.display == 'none' ? 472 : 290);
		}
	}

	// 移除创建DIV
	function createDiv() {
		var tt = document.getElementById(relationTablesDIV);
		var dd = document.getElementById(relationTables_td);
		tt.removeNode(true);
		dd.innerHTML = "<div id='" + relationTablesDIV + "' >";
	}

	function subTableInfo(relationTables, index) {
		// ///// 关联信息
		// if(!relationTables_form){
		createDiv();

		relationTables_form = new Ext.FormPanel({
			frame : false,
			// width : 600,
			// monitorValid:true,//绑定验证
			layout : "form",
			bodyStyle : 'padding:5px 5px,0',
			labelWidth : 70,
			title : relationTables[index].title,
			labelAlign : "left",
			autoScroll : false,
			autoHeight : true,
			defaults : {
				msgTarget : 'side'
			},
			// height : 60,
			renderTo : relationTablesDIV,
			defaultType : 'textfield',
			submit : function() {
				this.getEl().dom.action = '', this.getEl().dom.method = 'POST', this
						.getEl().dom.submit();
			},
			items : new GridConfig(relationTables[index].cols).getFormItems(),
			buttons : [{
						text : "确定",
						handler : function() {
							alert('确定')
						}
					}, {
						text : "重置",
						handler : function() {
							relationTables_form.form.reset();
						}
					}]
		});
		// }
		relationTables_form.show();

	}

	function fillForm(tableName, grid) {
		//alert(tableName);
		/*var params = {
			user_id : grid.store.getAt(curRowIndex).get('id'),
			table_name : tableName
		};*/
        var params = 'user_id=' + grid.store.getAt(curRowIndex).get('id') + "&table_name=" + tableName;
        //alert(doActionFromAJAX('/Ext_struts2_spring_hibernate/system/resource!getTree.action',{node:0},false,false));
		var json = doActionFromAJAX(listOperater.relationURL, params, true,
				true);
		alert(json+"--");
	}

	// 添加按钮--新面板
	function addRecord(store) {
		if (!win) {
			var innerForm = new Ext.FormPanel({
						labelWidth : 95,
						frame : true,
						bodyStyle : 'padding:5px 5px,0',
						width : 450,
						autoHeight : true,
						defaults : {
							width : 230
						},
						layout : 'form',
						defaultType : 'textfield',
						items : gridconfig.getFormItems()
					});
			win = new Ext.Window({
						layout : 'fit',
						resize : true,
						defaults : {
							width : 450,
							height : 800
						},
						width : 450,
						title : "- 添加 -",
						autoHeight : true,
						closeAction : 'hide',
						plain : true,
						buttonAlign : 'center',
						buttons : [{
							text : ' 保存 ',
							type : 'submit',
							disabled : false,
							handler : function() {
								var record = gridconfig.getAddRecord();
								/*
								 * var key=gridconfig.key; var isAdd =
								 * gridconfig.isAdd; var
								 * header=gridconfig.header; for (var i = 0; i <
								 * key.length; i++){ if( header[i]!="" &&
								 * isAdd[i]){ //alert(gridconfig.key[i]); } }
								 */
								// alert(record['loginName'])
								// 得到表单值
								// alert(innerForm.getForm().findField('passWord').getValue());
								// alert(innerForm.getForm().getValues([false]).toString());
								// 验证表单
								if (!innerForm.getForm().isValid())
									return;
								var result = doActionFromAJAX(
										listOperater.addRecord, record, false,
										false);
								// alert(result);
								this.disabled = false;
								store.reload();
								innerForm.getForm().reset();
								win.hide();
								/*
								 * eval(listOperater["addRecord"])(record,
								 * function(msg){ //save data
								 * 
								 * Ext.MessageBox.alert("信息：", msg); //display
								 * the back infomation this.disabled = false;
								 * store.reload(); innerForm.getForm().reset();
								 * win.hide(); });
								 */
							}
						}, {
							text : ' 关闭 ',
							handler : function() {
								innerForm.getForm().reset();
								win.hide();
							}
						}, {
							text : ' 重置 ',
							handler : function() {
								innerForm.getForm().reset();
							}
						}]
					});
			win.add(innerForm);
		}
		win.show();
	}
	// 删除按钮
	function deleteRecord(store, grid) {
		var selectionModel = grid.getSelectionModel();
		var selectedcount = selectionModel.getCount();
		if (selectedcount == 0) {
			Ext.Msg.alert("删除数据", "您没有选中任何数据!");
			return;
		} else {
			Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
						if (btn == 'yes') {
							// var records = selectionModel.getSelected();
							var records = selectionModel.getSelections();
							var objRecords = new Array();
							for (var i = 0; i < records.length; i++) {
								objRecords[i] = records[i].data;
							}

							doActionFromAJAX(listOperater.deleteRecord,
									objRecords, false, true);
							this.disabled = false;
							store.reload();
						}
					});
		}
	}

	// 保存按钮
	function updateRecord(store, grid) {
		var selectionModel = grid.getSelectionModel();
		var selectedcount = selectionModel.getCount();
		if (selectedcount == 0) {
			Ext.Msg.alert("更新数据", "您没有选中任何数据!");
			return;
		} else {
			Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
						if (btn == 'yes') {

							var records = selectionModel.getSelections();
							// var _msg = "";
							var objRecords = new Array();

							for (var i = 0; i < records.length; i++) {

								objRecords[i] = records[i].data;
							}
							/*
							 * eval(listOperater["updateRecord"])(objRecords,
							 * {callback:function(msg) //update data { _msg =
							 * msg; //display the back infomation }, async:false
							 * });
							 */

							doActionFromAJAX(listOperater.updateRecord,
									objRecords, false, true);
							this.disabled = false;
							// Ext.MessageBox.alert("信息", "数据更新成功");
							store.reload();
						}
					});
		}
	}

	// 查询窗体
	function queryRecord(store) {
		var queryConfig;
		var querygrid;

		if (!querywin) {
			queryConfig = new QueryConfig(queryCondition);
			querygrid = queryConfig.getQueryGrid();
			querywin = new Ext.Window({
						layout : 'fit',
						resize : true,
						defaults : {
							width : 700
						},
						width : 700,
						title : '--查询--',
						autoHeight : true,
						closeAction : 'hide',
						plain : true,
						buttonAlign : 'center',
						buttons : [{
							text : '查询',
							type : 'submit',
							disabled : false,
							handler : function() {
								var dataLength = querygrid.store.data.length;
								var queryGridRds = querygrid.getStore()
										.getRange(0, dataLength - 1);
								for (var i = 0; i < queryGridRds.length; i++) {
									if (queryGridRds[i].get("operator")) {
										alert(queryGridRds[i].get("operator"));
										conditions.push(queryGridRds[i].data);
									}
								}

								querywin.hide();
								store.load({
											params : {
												start : 0,
												limit : pagesize,
												conditions : conditions
											}
										});
							}
						}, {
							text : '关闭',
							handler : function() {
								querywin.hide();
							}
						}, {
							text : '重置',
							handler : function() {
								conditions = new Array();
								queryConfig = new QueryConfig(queryCondition);
								querygrid = queryConfig.getQueryGrid();
								querywin.remove("querygrid");
								querywin.hide()
								querywin.add(querygrid);
								querywin.show();
							}
						}]
					});
			querywin.add(querygrid);
		}
		querywin.show();
	}
}

/**
 * init class gridConfig
 * 
 * @param gridParameters
 */
function GridConfig(gridParameters) {
	this.key = new Array();
	this.type = new Array();
	this.header = new Array();
	this.required = new Array();
	this.minLength = new Array();
	this.maxLength = new Array();
	this.minValue = new Array();
	this.maxValue = new Array();
	this.isAdd = new Array();
	this.isGridShow = new Array();
	if (typeof gridParameters == "object") {
		for (var i = 0; i < gridParameters.length; i++) {
			this.key[i] = gridParameters[i].tableColName;
			this.type[i] = gridParameters[i].type;
			this.header[i] = gridParameters[i].header;
			this.required[i] = gridParameters[i].required == undefined
					? true
					: gridParameters[i].required == true ? false : true;
			this.minLength[i] = gridParameters[i].minLength == undefined
					? ''
					: gridParameters[i].minLength;
			this.maxLength[i] = gridParameters[i].maxLength == undefined
					? ''
					: gridParameters[i].maxLength;
			this.minValue[i] = gridParameters[i].minValue == undefined
					? ''
					: gridParameters[i].minValue;
			this.maxValue[i] = gridParameters[i].maxValue == undefined
					? ''
					: gridParameters[i].maxValue;
			this.isAdd[i] = gridParameters[i].isAdd == undefined
					? true
					: gridParameters[i].isAdd;
			this.isGridShow[i] = gridParameters[i].isGridShow == undefined
					? true
					: gridParameters[i].isGridShow;

		}
	} else {
		Ext.alert("Error,the parameters is error");
	}
	// alert(this.required);
}
/**
 * define the Record that be used to receive the data for the grid
 * 
 * @param Key
 */
GridConfig.prototype.getRecord = function() {
	var key = this.key;
	var record = new Array();
	for (var i = 0; i < key.length; i++) {
		record[i] = {
			name : key[i]
		};
	}
	return record;
}
/**
 * define the column of the grid
 * 
 * @param Key
 */
GridConfig.prototype.getColumn = function(store, readOnlyIndex) {
	var key = this.key;
	var type = this.type;
	var header = this.header;
	var required = this.required;
	var minLength = this.minLength;
	var maxLength = this.maxLength;
	var minValue = this.minValue;
	var maxValue = this.maxValue;
	var isGridShow = this.isGridShow;
	var j = 2;
	var column = new Array();
	column[0] = new Ext.grid.RowNumberer();
	column[1] = new Ext.grid.CheckboxSelectionModel();

	for (var i = 0; i < key.length; i++) {
		if (header[i] == "") {
		} else {
			if (isGridShow[i]) {
				if (type[i] == "String") {
					if (minLength[i] != '' && maxLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextField({
										minLength : minLength[i],
										maxLength : minLength[i]
									})
						};
					} else if (minLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextField({
										minLength : minLength[i]
									})
						};
					} else if (maxLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextField({
										maxLength : maxLength[i]
									})
						};
					} else {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextField({})
						};
					}
				}
				if (type[i] == "PassWord") {
					if (maxLength[i] != '' && minLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextField({
										minLength : minLength[i],
										maxLength : maxLength[i]
									})
						};
					} else if (maxLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextField({
										maxLength : maxLength[i]
									})
						};
					} else if (minLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextField({
										minLength : minLength[i]
									})
						};
					} else {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextField({})
						};
					}
				}
				if (type[i] == "Datetime") {// 时间

					column[j] = {
						header : header[i],
						dataIndex : key[i],
						sortable : true,
						width : 160,
						align : 'center',
						// renderer:new Ext.util.Format.dateRenderer('Y年m月d日'),
						// renderer:new Ext.util.Format.dateRenderer('Y-m-d
						// H:i:s'),
						renderer : function(value, p, record) {
							var jsondate = record.data.createTime;
							var d_ = '';
							if (jsondate != undefined)
								d_ = formatDate(jsondate)
							return d_;
						},
						/*
						 * renderer:function(value, p, record){ var
						 * format="yyyy-MM-dd hh:mm:ss"; var jsondate =
						 * record.data.createTime; //alert(value+"--"+jsondate);
						 * return format_date(format,jsondate); },
						 */
						editor : new Ext.form.DateField({
									// altFormats: 'd/m/Y H:i:s',
									format : 'Y-m-d H:i:s',
									menu : new DatetimeMenu(),
									minValue : minValue[i],
									maxValue : maxValue[i],
									disabledDays : [0, 6],
									disabledDaysText : '请不要选择周末'
								})
					};
				}/*
					 * if(type[i] == "Number") { column[j] =
					 * {header:header[i],dataIndex:key[i],sortable:true,width:160,align:'center',editor:new
					 * Ext.form.NumberField({})}; }
					 */
				if (type[i] == "LongString")// TextArea
				{
					if (maxLength[i] != '' && maxLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextArea({
										minLength : minLength[i],
										maxLength : maxLength[i]
									})
						};
					} else if (maxLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextArea({
										maxLength : maxLength[i]
									})
						};
					} else if (minLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextArea({
										minLength : minLength[i]
									})
						};
					} else {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 160,
							align : 'center',
							editor : new Ext.form.TextArea({})
						};
					}
				}
				if (type[i] == "Number")//
				{
					if (maxValue[i] != '' && minValue[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 60,
							align : 'center',
							renderer : function(v) {
								spanText(v)
							},
							editor : new Ext.form.NumberField({
										allowBlank : false,
										allowNegative : false,
										allowDecimals : false,
										maxValue : maxValue[i],
										minValue : minValue[i]
									})
						};
					} else if (minLength[i] != '' && maxLength[i] != '') {
						column[j] = {
							header : header[i],
							dataIndex : key[i],
							sortable : true,
							width : 60,
							align : 'center',
							renderer : function(v) {
								spanText(v)
							},
							editor : new Ext.form.NumberField({
										allowBlank : false,
										allowNegative : false,
										allowDecimals : false,
										minLength : minLength[i],
										maxLength : maxLength[i]
									})
						};

					} else {
						if (maxValue[i] != '' || minValue[i] != '') {
							if (maxValue[i] != '') {
								column[j] = {
									header : header[i],
									dataIndex : key[i],
									sortable : true,
									width : 60,
									align : 'center',
									renderer : function(v) {
										spanText(v)
									},
									editor : new Ext.form.NumberField({
												allowBlank : false,
												allowNegative : false,
												allowDecimals : false,
												maxValue : maxValue[i]
											})
								};
							} else if (minValue[i] != '') {
								column[j] = {
									header : header[i],
									dataIndex : key[i],
									sortable : true,
									width : 60,
									align : 'center',
									renderer : function(v) {
										spanText(v)
									},
									editor : new Ext.form.NumberField({
												allowBlank : false,
												allowNegative : false,
												allowDecimals : false,
												minValue : minValue[i]
											})
								};
							}
						} else if (maxLength[i] != '' || minLength[i] != '') {
							if (maxLength[i] != '') {
								column[j] = {
									header : header[i],
									dataIndex : key[i],
									sortable : true,
									width : 60,
									align : 'center',
									renderer : function(v) {
										spanText(v)
									},
									editor : new Ext.form.NumberField({
												allowBlank : false,
												allowNegative : false,
												allowDecimals : false,
												maxLength : maxLength[i]
											})
								};
							} else if (minLength[i] != '') {
								column[j] = {
									header : header[i],
									dataIndex : key[i],
									sortable : true,
									width : 60,
									align : 'center',
									renderer : function(v) {
										spanText(v)
									},
									editor : new Ext.form.NumberField({
												allowBlank : false,
												allowNegative : false,
												allowDecimals : false,
												minLength : minLength[i]
											})
								};
							}
						} else {
							column[j] = {
								header : header[i],
								dataIndex : key[i],
								sortable : true,
								width : 60,
								align : 'center',
								renderer : function(v) {
									spanText(v)
								},
								editor : new Ext.form.NumberField({
											allowBlank : false,
											allowNegative : false,
											allowDecimals : false
										})
							};
						}

					}

				}
				if (type[i] == "Status") {
					column[j] = {
						header : header[i],
						dataIndex : key[i],
						sortable : true,
						width : 60,
						align : 'center',
						// setEditable:true,
						renderer : function(v) {
							var test_v = v.toString().replace(/^\s+|\s+$/g, "");// 去除左右空格
							var code = '<span style="color:';
							if (test_v == '1') {
								code += '#008000';
							} else {
								code += '#804000';
							}
							return code + ';">'
									+ (test_v == '1' ? ' 正常' : ' 失效')
									+ '</span>';
							// return test_v=='1'?' 正常':' 失效';
						},
						editor : new Ext.form.ComboBox({
									typeAhead : true,
									triggerAction : 'all',
									transform : statusSelect,
									lazyRender : true,
									listClass : 'x-combo-list-small'
								})
					};
				}
				if (type[i] == "Sex")// 性别
				{
					column[j] = {
						header : header[i],
						dataIndex : key[i],
						sortable : true,
						width : 60,
						align : 'center',
						renderer : function(v) {
							var test_v = v.replace(/^\s+|\s+$/g, "");// 去除左右空格
							return '<img src="../images/' + test_v + '.png"/>'
									+ (test_v == 'm' ? ' 男' : ' 女');
						},
						editor : new Ext.form.ComboBox({
									typeAhead : true,
									triggerAction : 'all',
									transform : sexSelect,
									lazyRender : true,
									listClass : 'x-combo-list-small'
								})
					};
				}
				j++;
			}
		}
	}
	// return new Ext.grid.ColumnModel(column);

	var colModel_ = new Ext.grid.ColumnModel({
				columns : column,
				isCellEditable : function(col, row) {
					var record = store.getAt(row);

					for (var i = 0; i < readOnlyIndex.length; i++) {
						if (col == readOnlyIndex[i]) { // replace with your
							// condition
							return false;
						}
					}
					return Ext.grid.ColumnModel.prototype.isCellEditable.call(
							this, col, row);
				}
			});
	return colModel_;

}
/**
 * define the items for the form which used to add a new record
 * 
 * @param Key
 *            添加窗体
 */
GridConfig.prototype.getFormItems = function() {
	var key = this.key;
	var type = this.type;
	var header = this.header;
	var items = new Array();
	var required = this.required;
	var minLength = this.minLength;
	var maxLength = this.maxLength;
	var minValue = this.minValue;
	var maxValue = this.maxValue;
	var isAdd = this.isAdd;

	var j = 0;
	for (var i = 0; i < key.length; i++) {
		if (header[i] == "") {
		} else {
			if (isAdd[i]) {
				if (type[i] == "String") {
					if (maxLength[i] != '' && minLength[i] != '') {
						items[j] = {
							fieldLabel : header[i],
							name : key[i],
							anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
							allowBlank : required[i],
							maxLength : maxLength[i],
							minLength : minLength[i]
						};
					} else {
						if (maxLength[i] != '') {
							items[j] = {
								fieldLabel : header[i],
								name : key[i],
								anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
								allowBlank : required[i],
								maxLength : maxLength[i]
							};
						} else if (minLength[i] != '') {
							items[j] = {
								fieldLabel : header[i],
								name : key[i],
								anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
								allowBlank : required[i],
								minLength : minLength[i]
							};
						} else {
							items[j] = {
								fieldLabel : header[i],
								name : key[i],
								anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
								allowBlank : required[i]
							};
						}
					}
				}
				if (type[i] == "PassWord") {
					// items[j] =
					// {fieldLabel:header[i],name:key[i],allowBlank:required[i],inputType:'password',readOnly:
					// true,emptyText:'借阅者编号'};
					items[j] = {
						fieldLabel : header[i],
						name : key[i],
						allowBlank : required[i],
						readOnly : true,
						anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
						emptyText : '123'
					};
				}
				if (type[i] == "Datetime") {
					items[j] = {
						fieldLabel : header[i],
						name : key[i],
						allowBlank : required[i],
						xtype : 'datefield',
						renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s'),
						format : 'Y-m-d H:i:s',
						maxValue : maxValue[i],
						minValue : minValue[i],
						anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
						menu : new DatetimeMenu()
					};
				}
				if (type[i] == "Number") {
					if (maxValue[i] != '' && minValue[i] != '') {
						items[j] = {
							fieldLabel : header[i],
							name : key[i],
							allowBlank : required[i],
							maxValue : maxValue[i],
							minValue : minValue[i],
							anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
							xtype : 'numberfield'
						};
					} else if (minLength[i] != '' && maxLength[i] != '') {
						items[j] = {
							fieldLabel : header[i],
							name : key[i],
							allowBlank : required[i],
							minLength : minLength[i],
							maxLength : maxLength[i],
							anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
							xtype : 'numberfield'
						};

					} else {
						if (maxValue[i] != '' || minValue[i] != '') {
							if (maxValue[i] != '') {
								items[j] = {
									fieldLabel : header[i],
									name : key[i],
									anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
									allowBlank : required[i],
									maxValue : maxValue[i],
									xtype : 'numberfield'
								};
							} else if (minValue[i] != '') {
								items[j] = {
									fieldLabel : header[i],
									name : key[i],
									anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
									allowBlank : required[i],
									minValue : minValue[i],
									xtype : 'numberfield'
								};
							}
						} else if (maxLength[i] != '' || minLength[i] != '') {
							if (maxLength[i] != '') {
								items[j] = {
									fieldLabel : header[i],
									name : key[i],
									anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
									allowBlank : required[i],
									maxLength : maxLength[i],
									xtype : 'numberfield'
								};
							} else if (minLength[i] != '') {
								items[j] = {
									fieldLabel : header[i],
									name : key[i],
									anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
									allowBlank : required[i],
									minLength : minLength[i],
									xtype : 'numberfield'
								};
							}
						} else {
							items[j] = {
								fieldLabel : header[i],
								name : key[i],
								allowBlank : required[i],
								anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
								xtype : 'numberfield'
							};
						}

					}
				}
				if (type[i] == "LongString") {
					items[j] = {
						fieldLabel : header[i],
						name : key[i],
						xtype : 'textarea',
						anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
						minLength : minLength[i],
						maxLength : maxLength[i]
					};
				}
				if (type[i] == "Status") {
					items[j] = new Ext.form.ComboBox({
								fieldLabel : header[i],
								name : key[i],
								allowBlank : required[i],
								mode : 'local',
								hiddenName : key[i],// 提交到后台的字段名称
								triggerAction : 'all',
								store : statuSelectStore,
								valueField : 'value',
								displayField : 'text',
								anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
								readOnly : true
							});
				}
				if (type[i] == "Sex") {
					items[j] = new Ext.form.ComboBox({
								fieldLabel : header[i],
								name : key[i],
								allowBlank : required[i],
								mode : 'local',
								hiddenName : key[i],// 提交到后台的字段名称
								triggerAction : 'all',
								store : sexSelectStore,
								valueField : 'value',
								displayField : 'text',
								anchor : '90%',// 输入框的长度为列宽减去标题的宽度后的90%（anchor:'90%'），余下的10%的是给显示错误信息图标用的。
								readOnly : true
							});
				}

				j++;
			}
		}
	}

	return items;
}
/**
 * get the data for the form
 * 
 * @param Key
 */
GridConfig.prototype.getAddRecord = function() {
	var key = this.key;
	var type = this.type;
	var header = this.header;
	var addRecord = new Object();
	var isAdd = this.isAdd;
	for (var i = 0; i < key.length; i++) {
		if (header[i] != "" && isAdd[i]) {
			if (type[i] == "Datetime") {
				addRecord[key[i]] = new Date(Ext.get(key[i]).dom.value);
			} else {
				addRecord[key[i]] = Ext.get(key[i]).dom.value;
			}
		}
	}
	return addRecord;
}

// 导出Excel处理器
GridConfig.prototype.exportExcelHandler = function(grid) {
	var grid = grid || {};
	this.pageHandler = function() {
		var jsonDatas = GetDisplayColumns();
		var filterDatas = arrayToJson(grid.filters.getFilterData());
		var pageIndex = grid.bottomToolbar.getPageData().activePage;
		var remoteSort = grid.store.remoteSort;
		var dir = grid.store.sortInfo.direction;
		var sort = grid.store.sortInfo.field;
		var pageCount = 25;
		window.open(grid.svcUrl + '/PageExportExcel?cols=' + jsonDatas
				+ "&filters=" + filterDatas + "&pageIndex=" + pageIndex
				+ "&pageCount=" + pageCount + "&remoteSort=" + remoteSort
				+ "&sort=" + sort + "&dir=" + dir);
	}

	this.allHandler = function() {
		var jsonDatas = GetDisplayColumns();
		var filterDatas = arrayToJson(grid.filters.getFilterData());
		window.open(grid.svcUrl + '/ExportExcel?cols=' + jsonDatas
				+ "&filters=" + filterDatas);
	}
	function GetDisplayColumns() {
		var i = 0;
		var canExportColumnModels = new Array();
		for (i = 0; i < grid.colModel.config.length; i++) {
			var cm = grid.colModel.config[i];
			if (cm.dataIndex != '' && !cm.hidden) {
				var c = new ColModel(cm.dataIndex, cm.header);
				canExportColumnModels.push(c);
			}
		}
		var jsonDatas = arrayToJson(canExportColumnModels);
		return jsonDatas;
	}
};

/**
 * class query
 */
function QueryConfig(data) {
	this.data = data;
	this.stringOp = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [["", "选择关系运算符"], ['=', '='], ['<>', '<>'],
						["like '|%'", '以...开头'], ["like '%|'", '以...结尾'],
						["like '%|%'", '包含字符'], ["not like '%|%'", '不包含字符'],
						['is null', '空白'], ['is not null', '非空白']]
			});
	this.datetimeOp = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [["", "选择关系运算符"], ['=', '='], ['<>', '<>'], ['<', '<'],
						['<=', '<='], ['<', '>'], ['>=', '>='],
						['is null', '空白'], ['is not null', '非空白'],
						["between", "between"]]
			});
	this.numberOp = this.datetimeOp;

}

QueryConfig.prototype.getOpStore = function(type) {
	var _store;// alert(type);
	if (type == "String") {
		_store = this.stringOp;
	} else if (type == "Number") {
		_store = this.numberOp;
	} else if (type = "Datetime") {
		_store = this.datetimeOp;
	} else if (type = "PassWord") {
		_store = this.stringOp;
	}
	return _store;
}
/**
 * get the Editor for each column
 * 
 * @param type
 */
QueryConfig.prototype.getEditor = function(type) {
	var editor;// alert(type);
	if (type == "String") {
		editor = new Ext.form.TextField({});
	} else if (type == "PassWord") {
		editor = new Ext.form.TextField({});
	} else if (type == "Number") {
		editor = new Ext.form.NumberField({});
	} else if (type = "Datetime") {
		editor = new Ext.form.DateField({
					fomat : 'Y-m-d H:i:s',
					menu : new DatetimeMenu()
				});
	} else if (type = "Sex") {/*
								 * editor = new Ext.form.ComboBox({
								 * fieldLabel:'性别', name:'sex',
								 * allowBlank:false, mode: 'local',
								 * readOnly:true, triggerAction:'all',
								 * anchor:'90%', store:sexSelectStore,
								 * valueField: 'value', displayField: 'text' })
								 */
	}
	return editor;
}
/**
 * init the query grid
 */

QueryConfig.prototype.getQueryGrid = function() {
	var qeuryConfig = this;
	var stringeditor = new Ext.form.TextField({});
	var store = new Ext.data.SimpleStore({
				data : qeuryConfig.data,
				fields : ["entity", "type", "columnName", "operator",
						"firstValue", "secondValue", "andOr", "bracket", "name"]
			});
	// alert(qeuryConfig.data);
	var colM = new Ext.grid.ColumnModel([{
				header : "字段名称",
				dataIndex : "name",
				sortable : true
			}, {
				header : "运算符",
				dataIndex : "operator",
				editor : stringeditor
			}, {
				header : "数值1",
				dataIndex : "firstValue",
				width : 150,
				editor : stringeditor,
				renderer : formatDate
			}, {
				header : "数值2",
				dataIndex : "secondValue",
				width : 150,
				editor : stringeditor,
				renderer : formatDate
			}, {
				header : "关系",
				dataIndex : "andOr",
				editor : new Ext.form.ComboBox({
							store : [["", "请选择括号.."], ["and", "and"],
									["or", "or"]],
							mode : 'local',
							triggerAction : 'all',
							valueField : 'value',
							displayField : 'text',
							editable : false
						})
			}, {
				header : "括号",
				dataIndex : "bracket",
				hidden : true,
				editor : new Ext.form.ComboBox({
							store : [["", "请选择括号.."], ["(", "("], [")", ")"]],
							mode : 'local',
							triggerAction : 'all',
							valueField : 'value',
							displayField : 'text',
							editable : false
						})
			}]);

	var grid = new Ext.grid.EditorGridPanel({
				id : "querygrid",
				// title : "查询",
				autoHeight : true,
				width : 700,
				cm : colM,
				clicksToEdit : 1,
				store : store,
				selModel : new Ext.grid.RowSelectionModel({
							singleSelect : false
						}),
				autoExpandColumn : 2
			});
	grid.on("beforeedit", function(e) {
				var record = grid.getSelectionModel().getSelected();
				// alert(record.get("type"));
				if (e.column == 1) {
					var _store = qeuryConfig.getOpStore(record.get("type"));
					var Opcombox = new Ext.form.ComboBox({
								store : _store,
								mode : 'local',
								triggerAction : 'all',
								valueField : 'value',
								displayField : 'text',
								editable : false
							});
					grid.colModel.setEditor(e.column,
							new Ext.grid.GridEditor(Opcombox));
				}
				if (e.column == 2) {
					var gridEditor2 = qeuryConfig.getEditor(record.get("type"));
					if (getParamsNum(record.get("operator")) == 0) {
						grid.colModel.setEditor(e.column,
								new Ext.grid.GridEditor(null));
					}
					if (getParamsNum(record.get("operator")) == 1
							|| getParamsNum(record.get("operator")) == 2) {
						grid.colModel.setEditor(e.column,
								new Ext.grid.GridEditor(gridEditor2));
					}
				}
				if (e.column == 3) {
					var gridEditor3 = qeuryConfig.getEditor(record.get("type"));
					if (getParamsNum(record.get("operator")) == 0
							|| getParamsNum(record.get("operator")) == 1) {
						grid.colModel.setEditor(e.column,
								new Ext.grid.GridEditor(null));
					}
					if (getParamsNum(record.get("operator")) == 2) {
						grid.colModel.setEditor(e.column,
								new Ext.grid.GridEditor(gridEditor3));
					}
				}
			});

	grid.on("afteredit", function(e) {
				var record = grid.getSelectionModel().getSelected();
				if (e.column == 1) {
					if (getParamsNum(record.get("operator")) == 0) {
						record.set("firstValue", "");
						record.set("secondValue", "");
					}
					if (getParamsNum(record.get("operator")) == 1) {
						record.set("secondValue", "");
					}
				}
			});
	return grid;
};
/**
 * according the operator's type,we can get the amount of the params that used
 * to query
 * 
 * @param operator
 */
function getParamsNum(operator) {
	var ParamsNum;
	if (operator == "is null" || operator == "is not null" || operator == "") {
		ParamsNum = 0;
	} else if (operator == "between") {
		ParamsNum = 2;
	} else {
		ParamsNum = 1;
	}
	return ParamsNum;
}
/**
 * format the date.formatDate
 * 
 * @param value
 */
function formatDate(value) {
	var formatValue = value;
	if (formatValue.toString().indexOf("UTC") >= 0) {
		var dt = new Date(formatValue);
		formatValue = dt.format('Y-m-d H:i:s');
	}
	return formatValue;
}

// 性别下拉框
var sexSelectStore = new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['m', '男'], ['w', '女']]
		});

// 状态下拉框
var statuSelectStore = new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['1', '正常'], ['0', '失效']]
		});

function arrayToJson(items) {
	if (items.length == 0) {
		return '';
	}
	var jsonData = "[";
	for (i = 0; i < items.length; i++) {
		record = items[i];
		jsonData += Ext.util.JSON.encode(record) + ",";
	}
	jsonData = jsonData.substring(0, jsonData.length - 1) + "]";
	return jsonData;
}

function exp2Excel(conditions, expURL) {
	if (undefined == expURL || expURL == '')
		return;
	var params_ = '';

	Ext.Ajax.request({
		url : expURL,
		params : '',
		success : function(response) {
			// var json = eval('('+response.responseText.trim()+')');
			var json = response.responseText.trim().evalJSON(true);
			var records = json.records;

			var html = ['<table border=1><tr><th>姓名</th><th>日期</th><th>时间</th><th>星期</th></tr>'];
			for (var i = 0; i < records.length; i += 1) {
				var rc = records[i];
				html.push('<tr><td>' + rc.cname + '</td><td>' + rc.signDate
						+ '</td><td>' + rc.signTime + '</td><td>'
						+ getWeek(rc.signDate) + '</td></tr>');
			}
			html.push('</table>');
			html = html.join(''); // 最后生成的HTML表格
			tableToExcel(html);
			// document.getElementById('debuger').innerHTML=html;
		},
		failure : function(response) {
			Ext.Msg.alert('信息', response.responseText.trim());
		}
	});
}

// ///////添加新行无法动态添加
// 添加新行
function addNewRow(store, grid, record) {
	// 默认值

	var newRow = new record({
				loginName : '新用户',
				createTime : '',
				phone : '',
				u_type : '1',
				userName : '新用户',
				status : 1
			});

	grid.stopEditing();
	store.insert(0, newRow);
	grid.startEditing(0, 2);
	store.getAt(0).dirty = true; // 设置该行记录为脏数据(默认为非脏数据),否则在保存时将无法判断该行是否已修改
}

/*
 * url:请求的地址 params :请求参数 。json 请求则格式,eg:
 * [{LoginName:value1,LoginPassword:value2...},{...} ] 非json方式则格式
 * eg：LoginName:value1,LoginPassword:value2... isAsynchronism :是否同步,false 异步
 * true 同步 默认是false isJSON : TRUE 通过JSON方式，普通方式
 */
function doActionFromAJAX(url, params, isSynchronization, isJSON) {
	var result;
	if (isJSON) {
		if (isSynchronization)
			result = doActionWithSynAJAXJSON(url, params);
		else
			result = doActionWithAsynAJAXJSON(url, params);
	} else {
		if (isSynchronization)
			result = doActionWithSynAJAX(url, params);
		else
			result = doActionWithAsynAJAX(url, params);
	}
	return result;
}
// JSON异步
function doActionWithAsynAJAXJSON(url, params) {
	Ext.Ajax.request({
		method : 'POST',// (1)发送方式.'POST'或'GET',一般是'POST'
		url : url,// (2)发送到页面
		success : function(request) {// (3)发送成功的回调函数
			var result = request.responseText;// (4)取得从JSP文件out.print(...)传来的文本
			var json = '';
			if (result != '')
				json = Ext.util.JSON.decode(result);// 对json进行反编码
			
            return json;
			// Ext.MessageBox.alert('信息', message);// (5)弹出对话框
		},
		failure : function() {// (7)发送失败的回调函数
			Ext.MessageBox.alert("错误", "与后台联系时出现问题，请稍候重试");
		},
		// params:{email:params} ,//(8)发送名为email参数
		// params:params ,
		jsonData : params,
		timeout : 2000
			// (9)超时
		});
}
// JSON同步
function doActionWithSynAJAXJSON(url, params) {
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url + "?" + params, false);
	conn.send(null);
	// 成功状态码为200
	if (conn.status == "200") {
		return Ext.util.JSON.decode(conn.responseText);
	} else {
		return '';
	}
}

// 非JSON异步
function doActionWithAsynAJAX(url, params) {
	Ext.Ajax.request({
		method : 'POST',// (1)发送方式.'POST'或'GET',一般是'POST'
		url : url,// (2)发送到页面
		success : function(request) {// (3)发送成功的回调函数
			var result = request.responseText;// (4)取得从JSP文件out.print(...)传来的文本
			var json = '';
            
			if (result != '')
				json = Ext.util.JSON.decode(result);// 对json进行反编码

			return json;
			// Ext.MessageBox.alert('信息', message);// (5)弹出对话框
		},
		failure : function() {// (7)发送失败的回调函数
			Ext.MessageBox.alert("错误", "与后台联系时出现问题，请稍候重试");
		},
		// params:{email:params} ,//(8)发送名为email参数
		params : params,
		// jsonData:params,
		timeout : 2000
			// (9)超时
		});
}
// 非JSON同步
function doActionWithSynAJAX(url, params) {
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url + "?email=" + params, false);
	conn.send(null);
	// 成功状态码为200
	if (conn.status == "200") {
		return conn.responseText;
	} else {
		return '';
	}
}

function spanText(v) {
	var code = '<span style="color:';
	if (v <= 25)
		code += '#008000';
	else if (v > 25 && v <= 30)
		code += '#CC6600';
	else if (v > 30)
		code += '#804000';
	return code + ';">' + v + '</span>';
}