set wsgenpath=C:\PROGRA~1\Java\jdk16~1.0_2\bin
set class=controller.serverCommunication.ElectronicShop
set clpth=./war/WEB-INF/classes
set resourcedir=./war/wsdl
set outsourcedir=./src/
set outdir=./war/WEB-INF/classes
%wsgenpath%\wsgen -cp "%clpth%" -wsdl -keep -r "%resourcedir%" -d "%outdir%" -s "%outsourcedir%"  %class%	