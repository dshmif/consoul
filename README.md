consoul
=======
Every place I've ever worked at, not to mention school, required me to roll a commandline utility application at
some point or another. A couple years back I got tired of having to roll the same application shell over and over
again so I decided to roll a simple framework that allowed me to "quickly" get a console application up and running.
The framework mimics your run-of-the-mill web application that leverages Servlets but instead of mapping a Servlet
to a URL you map a command string to a Command implementation. Like most web application frameworks, to which Consoul
mimics, a Front Controller is used to handle finding the correct mapped implementation and basic chores before
telling the implementation to execute itself. Java 1.6 added Console API but this framework works wih 1.5 and higher.

The commands currently supported are simple synchronous commands. Which is to say that you enter a command and a
prompt isn't provided again until the command completes execution successfully or otherwise. It also provides
password-protected commands (sudo-ish). I still need to get background (async) commands implemented but the two supported
command types have handled everything I've needed to date (which is probably why async commands are still missing)
so it may be a bit before async commands drop.

This is the first time I've every open sourced anything I've worked on so be gentle ;-)
