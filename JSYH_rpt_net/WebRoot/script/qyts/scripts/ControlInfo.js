//ǧһ���� www.cftea.com
//ControlInfo v1.0
//Ӧ��ʾ����
/*
var controlInfo = new ControlInfo("btn");
controlInfo.GetInfo();
alert("�ÿؼ���������ԣ�\r\nx=" + controlInfo.x + "\r\ny=" + controlInfo.y + "\r\nwidth=" + controlInfo.width + "\r\nheight=" + controlInfo.height);
*/

//���� targetIdOrTarget���ؼ� Id ���ƻ�ؼ�����
//���� x���ؼ�����ҳ�������� X ���ϵľ���λ�á�
//���� y���ؼ�����ҳ�������� Y ���ϵľ���λ�á�
//���� width���ؼ��ľ��Կ�ȣ�Ҳ���ǿؼ�ʵ��ռ�ݵĿ�ȡ�
//���� height���ؼ��ľ��Ը߶ȣ�Ҳ���ǿؼ�ʵ��ռ�ݵĸ߶ȡ�
//���� GetInfo��ȡ�ÿؼ���λ�úͿ�ߡ�
function ControlInfo(targetIdOrTarget)
{
    this.target = (typeof(targetIdOrTarget)=="string") ? document.getElementById(targetIdOrTarget) : targetIdOrTarget;
    this.x;
    this.y;
    this.width;
    this.height;
    
    this.GetInfo = ControlInfo_GetInfo;
}


function ControlInfo_GetInfo()
{
    var x = this.target.offsetLeft;
    var y = this.target.offsetTop;
    var pe = this.target.offsetParent;
    while (pe)
    {
        x += pe.offsetLeft;
        y += pe.offsetTop;
        pe = pe.offsetParent;
    }
    
    this.x = x;
    this.y = y;
    this.width = this.target.offsetWidth;
    this.height = this.target.offsetHeight;
}