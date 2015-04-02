


var thd = {}


thd.getO = function(id){//ͨ��id���߶������ȡ����

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

thd.create = function(htmlTagName){//����HTML����

	return document.createElement(htmlTagName);

}


var TabPage = function(idString){

	this.idStr = idString;
	
	this.tabPDiv = thd.create("div");//tabPage�����div
	this.tabPDiv.id = idString;

	this.tabPDivCss = "";//tabPDiv����ʽ

	this.tabMenu = thd.create("div");//ѡ��˵�div
	this.tabMenu.id = "thd_tabPage_menuUl";

	this.tabMenu.appendChild(thd.create("span"));

	this.tabConDiv = thd.create("div");//ѡ�����pDiv
	this.tabConDiv.style.backgroundColor = "#FFFFFF";//���ݿ򱳾�
	this.tabConDiv.id = "thd_tabPage_sdPDiv";//���ݿ���ʽID

	this.tabObj = [];//�����е�Ԫ��{title,id}
	this.setTabObj = function(divArray){
		this.tabObj = divArray;
	}

	this.setTabPDivCss = function(css){//����tabPDiv����ʽ
		
		this.tabPDivCss = css;

	}

	this.addedTo = function(id){//��ӵ�ĳ��HTMLԪ�ص��ڲ� idΪhtmlԪ�ص�id���߶���
		
		var _this = this;

		this.tabPDiv.style.cssText = this.tabPDivCss;		

		this.tabPDiv.appendChild(_this.tabMenu);//����˵�

		var cls = thd.create("div");

		cls.className = "thd_tabPage_cls";

		this.tabPDiv.appendChild(cls);//����˵���ѡ�����֮�������


		var menuArray = [];
		var conArray = [];

		for(var i = 0 ; i < this.tabObj.length ; i++){
		
			var tabObj = this.tabObj[i];

			var tit = thd.create("div");//ѡ��˵�
			tit.id = _this.idStr + "_tit" + i;
			tit.innerHTML = tabObj.title;
			tit.className = (i == tabObj.blockNum) ? "hover" : "";
			menuArray.push(tit.id);


			var con = thd.create("div");//ѡ����ݲ�
			con.appendChild(thd.getO(tabObj.id));
			con.id = _this.idStr + "_con" + i;
			con.style.display = (i == tabObj.blockNum) ? "block" : "none";
			conArray.push(con.id);		

			this.tabMenu.appendChild(tit);
			this.tabConDiv.appendChild(con);
		
		}

		this.tabPDiv.appendChild(_this.tabConDiv);//����ѡ�����pDiv

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
			alert("�˵������������ݲ�������һ��!");
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
			alert("�˵������������ݲ�������һ��!");
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





































