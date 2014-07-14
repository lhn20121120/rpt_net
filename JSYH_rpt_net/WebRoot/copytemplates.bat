@echo off

echo ++模板抓取程序++
echo -----------------
echo 应用目录路径参考：[weblogic目录]\user_projects\domains\base_domain\servers\AdminServer\tmp\_WL_user\rpt_net\f2i25y\war
echo ---------------------------------------

set dspath=C:\bea\user_projects\domains\base_domain\servers\AdminServer\tmp\_WL_user\rpt_net\f2i25y\war\
set dtpath=E:\

:stpath
set /p spath=请输入应用根目录：（不输入则为默认值，默认值为：%dspath%）
set /p tpath=请输入输出路径：（不输入则为默认值，默认值为：%dtpath%）

if not defined spath (set spath=%dspath%)
if not defined tpath (set tpath=%dtpath%)

echo ---------------------------------------
echo 应用目录：%spath%
echo 输出目录：%tpath%
echo ---------------------------------------

:choose
set /p choice=如目录路径输入不正确，请输入“N”返回，继续请输入“Y”：

if not defined choice (goto choose)

if /I "%choice%"=="Y" (goto copyed) 
if /I "%choice%"=="N" (goto stpath) 

:copyed

cd /d %spath%

if not exist "navimenu.jsp" (echo 应用路径输入不正确&goto stpath)

set tpath=%tpath%\模板文件\
echo ---------------------------------------
xcopy /e /q /k %spath%\report_mgr\excel %tpath%\report_mgr\excel\
xcopy /e /q /k %spath%\Reports\templates %tpath%\Reports\templates\
xcopy /e /q /k %spath%\templateFiles %tpath%\templateFiles\
echo ---------------------------------------

echo 抓取结束
pause
exit