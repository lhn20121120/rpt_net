@echo off

echo ++ģ��ץȡ����++
echo -----------------
echo Ӧ��Ŀ¼·���ο���[weblogicĿ¼]\user_projects\domains\base_domain\servers\AdminServer\tmp\_WL_user\rpt_net\f2i25y\war
echo ---------------------------------------

set dspath=C:\bea\user_projects\domains\base_domain\servers\AdminServer\tmp\_WL_user\rpt_net\f2i25y\war\
set dtpath=E:\

:stpath
set /p spath=������Ӧ�ø�Ŀ¼������������ΪĬ��ֵ��Ĭ��ֵΪ��%dspath%��
set /p tpath=���������·��������������ΪĬ��ֵ��Ĭ��ֵΪ��%dtpath%��

if not defined spath (set spath=%dspath%)
if not defined tpath (set tpath=%dtpath%)

echo ---------------------------------------
echo Ӧ��Ŀ¼��%spath%
echo ���Ŀ¼��%tpath%
echo ---------------------------------------

:choose
set /p choice=��Ŀ¼·�����벻��ȷ�������롰N�����أ����������롰Y����

if not defined choice (goto choose)

if /I "%choice%"=="Y" (goto copyed) 
if /I "%choice%"=="N" (goto stpath) 

:copyed

cd /d %spath%

if not exist "navimenu.jsp" (echo Ӧ��·�����벻��ȷ&goto stpath)

set tpath=%tpath%\ģ���ļ�\
echo ---------------------------------------
xcopy /e /q /k %spath%\report_mgr\excel %tpath%\report_mgr\excel\
xcopy /e /q /k %spath%\Reports\templates %tpath%\Reports\templates\
xcopy /e /q /k %spath%\templateFiles %tpath%\templateFiles\
echo ---------------------------------------

echo ץȡ����
pause
exit