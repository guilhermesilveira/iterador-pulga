Writer2LaTeX source version 0.4 beta2 (2004-12-29)
=====================================

Writer2LaTeX is (c) 2002-2004 by Henrik Just.
The source is available under the terms and conditions of the
GNU LESSER GENERAL PUBLIC LICENSE, version 2.1.
Please see the file COPYING.TXT for details.

The file xmergefix.jar contains a modified version of the
file xmerge.jar distributed with OpenOffice.org.
Xmerge is (c) by Sun Microsystems. The original source can
be found at
  http://xml.openoffice.org/
The modified source is included in xmergefix.jar (see the
file OfficeDocument.java).

Currently parts of the source for Writer2LaTeX is somewhat messy and
undocumented. This situation is improving from time to time :-)

Building Writer2LaTeX
---------------------

Writer2LaTeX uses Ant version 1.5 or later (http://ant.apache.org) to build.

Step 1: Create a subdirectory "lib" and move xmergefix.jar here

Step 2: At a command shell, navigate to the writer2latex04 directory and type

ant jar

or if you want to create javadoc as well:

ant all

(this assumes, that ant is in your path; otherwise specifify the full path.)

Manual build
------------

You can build Writer2LaTeX manually by compiling each .java file individually.

Here are some simple instructions:

1. Unzip into some directory.

2. First you will have to set the classpath:

WINDOWS:
set classpath=xmergefix.jar;<directory containing writer2latex source>

UNIX:
export CLASSPATH="xmergefix.jar:<directory containing writer2latex source>"
If "export" doesnt work, try "setenv".

3. Next you will have to compile each .java file, eg.: Go in each
subdirectory and do:

javac *.java

4. Finally you have to generate the writer2latex.jar. Go back to the
directory containing the writer2latex source and do:

WINDOWS:
jar -cmf META-INF\manifest.mf writer2latex.jar META-INF\converter.xml writer2latex\symbols.xml writer2latex\*.class writer2latex\util\*.class  writer2latex\xmerge\*.class writer2latex\office\*.class writer2latex\bibtex\*.class writer2latex\latex\*.class writer2latex\latex\content\*.class writer2latex\latex\style\*.class writer2latex\xhtml\*.class

UNIX:
jar -cmf META-INF/manifest.mf writer2latex.jar META-INF/converter.xml writer2latex/symbols.xml writer2latex/*.class writer2latex/util/*.class  writer2latex/xmerge/*.class writer2latex/office/*.class writer2latex/bibtex/*.class writer2latex/latex/*.class writer2latex/latex/content/*.class writer2latex/latex/style/*.class writer2latex/xhtml/*.class


Henrik Just, December 2004

Thanks to Michael Niedermair for writing the ant build file
Thanks to Jan Suhr for improvements to the manual build instructions.

