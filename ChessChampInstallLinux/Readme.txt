How to install the chess program in debian based OS (Ubuntu, Linux Mint, Debian):
 In the terminal:
-cd Path/chesschamp
-sudo dpkg -i chesschamp_1.0-ubuntu_all.deb
Or open the chesschamp_1.0-ubuntu_all.deb in Synaptic os Softwercenter, and install.

The program has been made with Java 8, so the running condition is the JRE 8 or JDK 8 installation!

 If you don't have JDK 8 on your computer, you can install in terminal:
-sudo apt-get install python-software-properties (optional, maybe not needed)
-sudo add-apt-repository ppa:webupd8team/java
-sudo apt-get update
-sudo apt-get install oracle-java8-installer
-sudo apt-get install oracle-java8-set-default

Up to 200 ( 200-200 white and black ) moves may be done in a game,
but this is only theoretical limit, because a game with 60 moves is already very long!

Ununstalling the program:
 In the terminal:
-sudo dpkg -r chesschamp
Or uninstall in the Synaptic.
