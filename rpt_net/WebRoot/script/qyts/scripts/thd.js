


var thd = {}


thd.getO = function(id){//通过id或者对象本身获取对象

	if(typeof(id) == "string"){

		if(document.getElementById(id)){

			return document.getElementById(id);

		}else{

			return null;

		}

	}else{

		return id;

	}

}

thd.create = function(htmlTagName){//创建HTML对象

	return document.createElement(htmlTagName);

}


var TabPage = function(idString){

	this.idStr = idString;
	
	this.tabPDiv = thd.create("div");//tabPage最外层div
	this.tabPDiv.id = idString;

	this.tabPDivCss = "";//tabPDiv的样式

	this.tabMenu = thd.create("div");//选项卡菜单div
	this.tabMenu.id = "thd_tabPage_menuUl";

	this.tabMenu.appendChild(thd.create("span"));

	this.tabConDiv = thd.create("div");//选项卡内容pDiv
	this.tabConDiv.style.background = "#FFFFFF";//内容框背景
	this.tabConDiv.id = "thd_tabPage_sdPDiv";//内容框样式ID

	this.tabObj = [];//数组中的元素{title,id}
	this.setTabObj = function(divArray){
		this.tabObj = divArray;
	}

	this.setTabPDivCss = function(css){//设置tabPDiv的样式
		
		this.tabPDivCss = css;

	}

	this.addedTo = function(id){//添加到某个HTML元素的内部 id为html元素的id或者对象
		
		var _this = this;

		this.tabPDiv.style.cssText = this.tabPDivCss;		

		this.tabPDiv.appendChild(_this.tabMenu);//加入菜单

		var cls = thd.create("div");

		cls.className = "thd_tabPage_cls";

		this.tabPDiv.appendChild(cls);//加入菜单和选项卡内容之间的清除浮动层


		var menuArray = [];
		var conArray = [];

		for(var i = 0 ; i < this.tabObj.length ; i++){
		
			var tabObj = this.tabObj[i];

			var tit = thd.create("div");//选项卡菜单
			tit.id = _this.idStr + "_tit" + i;
			tit.innerHTML = tabObj.title;
			tit.className = (i == 0) ? "hover" : "";
			menuArray.push(tit.id);


			var con = thd.create("div");//选项卡内容层
			con.appendChild(thd.getO(tabObj.id));
			con.id = _this.idStr + "_con" + i;
			con.style.display = (i == 0) ? "block" : "none";
			conArray.push(con.id);		

			this.tabMenu.appendChild(tit);
			this.tabConDiv.appendChild(con);
		
		}

		this.tabPDiv.appendChild(_this.tabConDiv);//加入选项卡内容pDiv

		thd.getO(id).appendChild(_this.tabPDiv);

		var SDmodel = new scrollDoor();

		SDmodel.sd(menuArray,conArray,"hover","");
	
	}
	
}







function scrollDoor(){
	this.value = 0;
}


scrollDoor.prototype = {

	onlyMenu : function(menus,openClass,closeClass){ // only menu no have content
		var _this = this;
		for(var i = 0 ; i < menus.length ; i++)
		{	
			_this.$(menus[i]).flag = ++this.value;
			_this.$(menus[i]).value = i;
			_this.$(menus[i]).onclick = function(){										
				for(var j = 0 ; j < menus.length ; j++)
				{						
					_this.$(menus[j]).className = closeClass;
					//_this.$(divs[j]).style.display = "none";					
				}				
				_this.$(menus[this.value]).className = openClass;	
				//_this.$(divs[this.value]).style.display = "block";				
			}
		}
		
		
	},
	
	sd : function(menus,divs,openClass,closeClass){// two class
		var _this = this;
		if(menus.length != divs.length)
		{
			alert("菜单层数量和内容层数量不一样!");
			return false;
		}				
		for(var i = 0 ; i < menus.length ; i++)
		{	
			_this.$(menus[i]).flag = ++this.value;
			_this.$(menus[i]).value = i;
			_this.$(menus[i]).onclick = function(){										
				for(var j = 0 ; j < menus.length ; j++)
				{						
					_this.$(menus[j]).className = closeClass;
					_this.$(divs[j]).style.display = "none";					
				}				
				_this.$(menus[this.value]).className = openClass;	
				_this.$(divs[this.value]).style.display = "block";				
			}
		}
		},
	sd3class : function(menus,divs,openClass,closeClass,middleClass){ //three class
		var _this = this;
		for(var x = 0 ; x < menus.length ; x++)
		{
			_this.$(menus[x]).state = _this.$(menus[x]).className == openClass ?  "open" : "close";
		}

		if(menus.length != divs.length)
		{
			alert("菜单层数量和内容层数量不一样!");
			return false;
		}				
		for(var i = 0 ; i < menus.length ; i++)
		{	
			_this.$(menus[i]).flag = ++this.value;
			_this.$(menus[i]).value = i;

			_this.$(menus[i]).onclick = function(){		
				
				for(var j = 0 ; j < menus.length ; j++)
				{
					_this.$(menus[j]).className = closeClass;
					_this.$(divs[j]).style.display = "none";
					_this.$(menus[j]).state = "close";
				}
				this.state = "open";
				_this.$(menus[this.value]).className = openClass;
				_this.$(divs[this.value]).style.display = "block";				
			}

			_this.$(menus[i]).onclick = function(){
				//alert(this.state);
				for(var j = 0 ; j < menus.length ; j++)
				{					
					
					if(_this.$(menus[j]).state != "open")
					{
						_this.$(menus[j]).className = closeClass;						
						_this.$(menus[j]).state = "close";
					}					
				}
				if(this.state == "open")
				{
				}
				else
				{
					this.className = middleClass;
				}				
			}

			_this.$(menus[i]).onclick = function(){
				if(this.state != "open")
				{
					this.className = closeClass;
				}
			}
		}
		},
	$ : function(oid){
		if(typeof(oid) == "string")
		return document.getElementById(oid);
		return oid;
	}
}





































