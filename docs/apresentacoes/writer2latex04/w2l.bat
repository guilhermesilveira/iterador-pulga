@echo off
rem *Very* simple batch file to run Writer2LaTeX
rem Created by Henrik Just, october 2003
rem Modfied december 2003 as suggested by Juan Julian Merelo Guervos
rem Please edit the following line to contain the full path to Writer2LaTeX:

set W2LPATH="c:\writer2latex04"

rem If the Java executable is not in your path, please edit the following
rem line to contain the full path and file name

set JAVAEXE="java"

if "%1"=="-clean" goto clean
if "%1"=="-ultraclean" goto ultraclean
if "%1"=="-pdfscreen" goto pdfscreen
if "%1"=="-pdfprint" goto pdfprint
if "%1"=="-article" goto article
if "%1"=="-cleanxhtml" goto cleanxhtml

%JAVAEXE% -jar %W2LPATH%\writer2latex.jar %1 %2 %3 %4 %5
goto finish

:clean
%JAVAEXE% -jar %W2LPATH%\writer2latex.jar -latex -config %W2LPATH%\clean.xml %2 %3 
goto finish

:ultraclean
%JAVAEXE% -jar %W2LPATH%\writer2latex.jar -latex -config %W2LPATH%\ultraclean.xml %2 %3  
goto finish

:pdfscreen
%JAVAEXE% -jar %W2LPATH%\writer2latex.jar -latex -config %W2LPATH%\pdfscreen.xml %2 %3
goto finish

:pdfprint
%JAVAEXE% -jar %W2LPATH%\writer2latex.jar -latex -config %W2LPATH%\pdfprint.xml %2 %3
goto finish

:article
%JAVAEXE% -jar %W2LPATH%\writer2latex.jar -latex -config %W2LPATH%\article.xml %2 %3
goto finish

:cleanxhtml
%JAVAEXE% -jar %W2LPATH%\writer2latex.jar -xhtml -config %W2LPATH%\cleanxhtml.xml %2 %3  
goto finish

:finish
set W2LPATH=
set JAVAEXE=

