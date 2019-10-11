# Fall 2019 Lab2-Scheduling Operating Systems taught by Yan Shvartzshnaide.
#Project Author Alifa Faruk 

 
In this lab  simulates scheduling in order to see how the time required depends on the scheduling
algorithm and the request patterns. There are four scheduling algorithms, First Come First Serve, Shortest Job First, Round Robin, and Highest Penality Next.

This in this file there are two Java files, Process.java and Scheduler.java. The Process.java is the class that creates and tracks each process data.
The Scheduler.java is the scheduler algorithm that has all four algorithms. 

#To run this program:

Please have ALL the input files in the same direcory as the programs Process.java and Scheduler. Also, have the random-numbers.txt in the same directory as the the java files and the inputs.
The input files should be in the following formate:

              1  0 1 5 1 
              2  0 1 5 1  0 1 5 1 

Two spaces in between each process (as indicated in the lab2-spec. Example inputs based on the given outputs are given in the zipped file containing contents of this lab2.


When compiling and running, change to the directory where Scheduler.java is currently at.
To  compile the program type "javac Scheduler.java".
To run the program by typing "java Scheduler input-filename".
If you need the more detailed output (the verbose version), type "java Scheduler verbose input-filename".
All scheduling algorithms will be displayed on the screen. 

#Running Example

->javac Scheduler.java

->java Scheduler verbose input-1.txt

OR 
->javac Scheduler.java

->java Scheduler input-1.txt

