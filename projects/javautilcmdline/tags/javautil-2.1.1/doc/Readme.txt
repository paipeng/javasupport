
================================================================================
ABOUT
================================================================================
javautil is a small Java Library that provide reusable Java Classes
and many common static methods for those mostly repeated programming tasks. This
package also contains many small Java programs that provide text and file
management. The goal of this project is keep simple and do not use any third 
party librarie dependency, other than just the default JDK lib. 

Most of the utility programs have both Unix and Windows wrapper scripts
that start the actual Java program. Type "-h" to any program to get
more help on each usage.

The current release is tested with Sun's JDK1.4.

SourceForge project: javautilcmdline

Project's Official Site:
http://javautilcmdline.sourceforge.net

================================================================================
AUTHOR and LICENSE
================================================================================
This project is created and maintained by Zemian Deng, and it's intended
as an Open Source project using Apache License. See License.txt and 
http://www.apache.org/foundation/licence-FAQ.html.

Any bugs or problem may send direct email to: thebugslayer@gmail.com       


================================================================================
INSTALL
================================================================================
Unzip the package into anywhere in your local file system. I suggest 
/opt/javautil, or C:\opt\javautil

All utlities in this package have both Unix and Windows wrapper script ready to
be run. Simply append package's bin directory to your PATH env. 
Try -h or --help on any utility command to see usage.

Example:
$ # Add bin directory to system path:
$ export PATH=$PATH:$JAVAUTIL_HOME/bin
  
$ #Then you may run any programs under the bin dir. For example:
$ millistime -h

================================================================================
DEVELOPOMENT
================================================================================
Source:
svn checkout https://javautilcmdline.svn.sourceforge.net/svnroot/javautilcmdline/trunk/javautil-2.x

Shell Server:
ssh <sourceforgeUsername>@javautilcmdline.sourceforge.net
See $HOME/README.txt after login

Ftp Releases
ftp anonymous@upload.sourceforge.net
#change local dir like, lcd /Users/zemian/Desktop/projects/javautil-2.x/dist
put put javautil-2.1.1-*.zip
#the manage release in https://sourceforge.net/projects/javautilcmdline/ under Admin > File Release.
