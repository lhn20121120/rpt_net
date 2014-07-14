//ǧһ���� www.cftea.com
//ColorDialog����ɫ�Ի��� v2.0
//http://www.cftea.com/products/webComponents/ColorDialog/
//��Ҫ ControlInfo v1.0
//Ӧ��ʾ����
/*
var colorDialog = new ColorDialog("colorButton");
colorDialog.onColorSelecting = function () { document.body.style.backgroundColor = colorDialog.selectingColor; }
colorDialog.onColorSelected = function () { document.body.style.backgroundColor = colorDialog.selectedColor; }
colorDialog.onColorCancelled = function () { document.body.style.backgroundColor = colorDialog.selectedColor; }
colorDialog.create();
*/
//����ʹ���г����ڿհ����򵥻������������Ի�����ʧ���������ע�⣬�ⲻ���ڳ���Ĵ��󣬹��ڸ������˵������μ���http://www.cftea.com/c/2007/11/T8E0QPE0V0KIN6W9.asp��


//���� targetIdOrTarget�������Ի�����ʾ�� HTML �ؼ� Id ���ƻ�ؼ�����
//���� selectingColor������ѡ�����ɫ��
//���� selectedColor���Ѿ�ѡ�����ɫ��
//�¼� onColorSelecting��������ѡ����ɫʱ��Ӧ�ķ�����
//�¼� onColorSelected�����Ѿ�ѡ����ɫ���Ӧ�ķ�����
//�¼� onColorCancelled����û��ѡ����ɫ���رնԻ�����Ӧ�ķ�����
//���� create()�������Ի���
function ColorDialog(targetIdOrTarget)
{
    this.target = (typeof(targetIdOrTarget)=="string") ? document.getElementById(targetIdOrTarget) : targetIdOrTarget; //target �����Ի�����ʾ
    this.selectingColor = ""; //����ѡ�����ɫ
    this.selectedColor = ""; //�Ѿ�ѡ�����ɫ
    this.onColorSelecting = null; //������ѡ����ɫʱ��Ӧ�ķ���
    this.onColorSelected = null; //���Ѿ�ѡ����ɫ���Ӧ�ķ���
    this.onColorCancelled = null; //��û��ѡ����ɫ���رնԻ�����Ӧ�ķ���
    
    this.colorHex = new Array("00", "33", "66", "99", "CC", "FF");
    this.spColorHex = new Array("FF0000", "00FF00", "0000FF", "FFFF00", "00FFFF", "FF00FF");
    
    this.dialog = null;
    this.previewBox = null;
    this.previewValueBox = null;
    
    this.create = ColorDialog_create; //�����Ի��򣨵�����ʾ������
    this.createPreviewPanel = ColorDialog_createPreviewPanel;
    this.createColorPanel = ColorDialog_createColorPanel;
    this.createColorElement = ColorDialog_createColorElement;
    
    this.initializeDialog = ColorDialog_initializeDialog;
    this.showDialog = ColorDialog_showDialog;
    this.getIsDialogShowing = ColorDialog_getIsDialogShowing;
    
    //��ʼ��
    this.commandSource = "body"; //�������ȴ����¼������ĸ�����Ϊ�Կؼ��Ķ����������������� document.body Ҳ����յ���Ϊ��֪�����׵����˭���Ӵ˳�Ա��
    var me = this;
    if (window.attachEvent)
    {
        //IE ϵ�������
        this.target.attachEvent("onclick", function () {
            me.commandSource = "target";
            me.initializeDialog();
            me.showDialog(true);
            });
        document.body.attachEvent("onclick", function () {
            if (me.commandSource == "body")
            {
                //ֱ�ӵ���� document.body
                if (me.getIsDialogShowing())
                {
                    //�Ի�������ʾ״̬����ʱ���ضԻ��򣬲������¼���
                    me.showDialog(false);
                    
                    if (typeof(me.onColorCancelled) == "function")
                    {
                        me.onColorCancelled();
                    }
                }
            }
            me.commandSource = "body"; //�ڱ��ζ����У�commandSource �Ѿ�����ʹ�ã��ָ�Ĭ��ֵ��
            });
    }
    else
    {
        //FF ϵ�������
        this.target.addEventListener("click", function () {
            me.commandSource = "target";
            me.initializeDialog();
            me.showDialog(true);
            }, false);
        document.body.addEventListener("click", function () {
            if (me.commandSource == "body")
            {
                //ֱ�ӵ���� document.body
                if (me.getIsDialogShowing())
                {
                    //�Ի�������ʾ״̬����ʱ���ضԻ��򣬲������¼���
                    me.showDialog(false);
                    
                    if (typeof(me.onColorCancelled) == "function")
                    {
                        me.onColorCancelled();
                    }
                }
            }
            me.commandSource = "body"; //�ڱ��ζ����У�commandSource �Ѿ�����ʹ�ã��ָ�Ĭ��ֵ��
            }, false);
    }
}


//�����Ի��򣨵�����ʾ������
//���� dialog
function ColorDialog_create()
{
    var table = document.createElement("table");
    table.border = "0";
    table.cellspacing = "0";
    table.cellpadding = "0";
    table.style.border = "1px solid #000000";
    table.style.borderCollapse = "separate"; //Ϊ�˱�֤�Ի�������֮���м����ʹ�� separate
    table.style.backgroundColor = "#CCCCCC";
    table.style.display = "none"; //�����ĶԻ���Ĭ���ǲ���ʾ�ġ�
    
    var tbody = document.createElement("tbody");
    table.appendChild(tbody);
    
    //����������Ԥ�����
    var tr = document.createElement("tr");
    tbody.appendChild(tr);
    var td = document.createElement("td");
    tr.appendChild(td);
    var previewPanel = this.createPreviewPanel();
    td.appendChild(previewPanel);
    
    //������������ɫ���
    var tr2 = document.createElement("tr");
    tbody.appendChild(tr2);
    var td2 = document.createElement("td");
    tr2.appendChild(td2);
    var colorPanel = this.createColorPanel();
    td2.appendChild(colorPanel);
    
    //���뵽 document.body
    document.body.appendChild(table);
    
    this.dialog = table;
}


//�����Ի����е�Ԥ����壬����������
//���� previewBox �� previewValueBox
function ColorDialog_createPreviewPanel()
{
    var table = document.createElement("table");
    table.border = "0";
    table.cellspacing = "0";
    table.cellpadding = "0";
    table.style.borderCollapse = "collapse";
    
    var tbody = document.createElement("tbody");
    table.appendChild(tbody);
    
    var tr = document.createElement("tr");
    tbody.appendChild(tr);
    
    var td1 = document.createElement("td");
    tr.appendChild(td1);
    
    var td2 = document.createElement("td");
    tr.appendChild(td2);
    
    //Ԥ����
    this.previewBox = document.createElement("input");
    this.previewBox.type = "text";
    this.previewBox.disabled = true;
    this.previewBox.style.width = "40px";
    this.previewBox.style.border = "1px solid #000000";
    this.previewBox.style.fontFamily = "Arial";
    this.previewBox.style.fontSize = "13px";
    td1.appendChild(this.previewBox);
    
    //Ԥ����ɫֵ��
    this.previewValueBox = document.createElement("input");
    this.previewValueBox.type = "text";
    this.previewValueBox.disabled = true;
    this.previewValueBox.style.width = "70px";
    this.previewValueBox.style.border = "1px inset #CCCCCC";
    this.previewValueBox.style.fontFamily = "Arial";
    this.previewValueBox.style.fontSize = "13px";
    this.previewValueBox.style.backgroundColor = "#FFFFFF";
    this.previewValueBox.style.color = "#000000";
    td2.appendChild(this.previewValueBox);
    
    return table;
}


//�����Ի����е���ɫ��壬����������
function ColorDialog_createColorPanel()
{
    var table = document.createElement("table");
    table.border = "0";
    table.cellspacing = "0";
    table.cellpadding = "0";
    table.style.borderCollapse = "collapse";
    
    var tbody = document.createElement("tbody");
    table.appendChild(tbody);
    
    //����ɨ���������ʾ��ɫ
    for (i = 0; i < 2; i++)
    {
        for (j = 0; j < this.colorHex.length; j++)
        {
            var tr = document.createElement("tr");
            tbody.appendChild(tr);
            
            //��ߵ�������ɫ
            if (i == 0)
            {
                tr.appendChild(this.createColorElement("#" + this.colorHex[j] + this.colorHex[j] + this.colorHex[j], false));
            }
            else
            {
                tr.appendChild(this.createColorElement("#" + this.spColorHex[j], false));
            }
            
            //�����
            tr.appendChild(this.createColorElement("", true));
            
            for (k = 0; k < 3; k++)
            {
                for (l = 0; l < this.colorHex.length; l++)
                {
                    tr.appendChild(this.createColorElement("#" + this.colorHex[k+i*3] + this.colorHex[l] + this.colorHex[j], false));
                }
            }
        }
    }
    
    return table;
}


//������ɫ����е���ɫԪ�أ�������ɫԪ�ض���
//���� color����ɫֵ��
//���� isSeparator���Ƿ�ֻ�����ָ������õ���ɫԪ�أ����Ϊ true����ֻ�ǳ��֣�����������¼��ȡ�
function ColorDialog_createColorElement(color, isSeparator)
{
    var me = this;
    
    var td = document.createElement("td");
    td.style.width = "11px";
    td.style.height = "11px";
    
    if (!isSeparator)
    {
        td.style.border = "1px solid #000000";
        td.style.backgroundColor = color;
        td.style.cursor = "pointer";
        
        if (window.attachEvent)
        {
            //IE ϵ�������
            td.attachEvent("onmouseover", function () {
                td.style.backgroundColor = "#FFFFFF"; //ͻ�Ե�ǰѡ����
                me.previewBox.style.backgroundColor = color; //Ԥ��
                me.previewValueBox.value = color; //Ԥ��ֵ
                me.selectingColor = color; //���õ�ǰѡ����ɫ
                if (typeof(me.onColorSelecting) == "function")
                {
                    me.onColorSelecting(); //�¼�
                }
                });
            td.attachEvent("onmouseout", function () {
                td.style.backgroundColor = color; //�ָ���ǰѡ����
                me.previewBox.style.backgroundColor = ""; //Ԥ��
                me.previewValueBox.value = ""; //Ԥ��ֵ
                me.selectingColor = ""; //���õ�ǰѡ����ɫ
                });
            td.attachEvent("onclick", function () {
                me.selectedColor = color; //�����Ѿ�ѡ�����ɫ
                me.commandSource = "dialog"; //��֪ document.body �� onclick �¼����ɶԻ��򴥷��ġ�
                me.showDialog(false); //���ضԻ���
                if (typeof(me.onColorSelected) == "function")
                {
                    me.onColorSelected(); //�¼�
                }
                });
        }
        else
        {
            //FF ϵ�������
            td.addEventListener("mouseover", function () {
                td.style.backgroundColor = "#FFFFFF"; //ͻ�Ե�ǰѡ����
                me.previewBox.style.backgroundColor = color; //Ԥ��
                me.previewValueBox.value = color; //Ԥ��ֵ
                me.selectingColor = color; //���õ�ǰѡ����ɫ
                if (typeof(me.onColorSelecting) == "function")
                {
                    me.onColorSelecting(); //�¼�
                }
                }, false);
            td.addEventListener("mouseout", function () {
                td.style.backgroundColor = color; //�ָ���ǰѡ����
                me.previewBox.style.backgroundColor = ""; //Ԥ��
                me.previewValueBox.value = ""; //Ԥ��ֵ
                me.selectingColor = ""; //���õ�ǰѡ����ɫ
                }, false);
            td.addEventListener("click", function () {
                me.selectedColor = color; //�����Ѿ�ѡ�����ɫ
                me.commandSource = "dialog"; //��֪ document.body �� onclick �¼����ɶԻ��򴥷��ġ�
                me.showDialog(false); //���ضԻ���
                if (typeof(me.onColorSelected) == "function")
                {
                    me.onColorSelected(); //�¼�
                }
                }, false);
        }
    }
    
    return td;
}


//��ʼ���Ի���
function ColorDialog_initializeDialog()
{
    this.previewBox.style.backgroundColor = this.selectedColor;
    this.previewValueBox.value = this.selectedColor;
}


//��ʾ�����ضԻ���
//���� show��ָ������ʾ�������ضԻ���
function ColorDialog_showDialog(show)
{
    if (show)
    {
        //�� this.target ���·���ʾ���� this.target ����롣
        var controlInfo = new ControlInfo(this.target);
        controlInfo.GetInfo();
        this.dialog.style.position = "absolute";
        this.dialog.style.left = controlInfo.x + "px";
        this.dialog.style.top = controlInfo.y + controlInfo.height + "px";

        this.dialog.style.display = "block";
    }
    else
    {
        this.dialog.style.display = "none";
    }
}


//���ضԻ����Ƿ�����ʾ״̬
function ColorDialog_getIsDialogShowing()
{
    return (this.dialog.style.display != "none");
}