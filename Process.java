/*
*
*Alifa Faruk
*Lab2 Scheduling
*October 11, 2019
*
*/

import java.util.Comparator;
import java.util.PriorityQueue;

import sun.net.TelnetOutputStream;

public class Process implements Comparable<Process>{
	//variables of each process
	int A;
	int C;
	int M;
	int B;
	String currentState;
	int finishTime=0;
	int waitingTime=0;
	int CPUtime;
	boolean verbose;
	int burst;
	int IOtime;
	int IOburst;
	int id;
	int total;
	int quatum;
	int events;
	int runTime;
	int waitTime=0; 
	int cycleTracker=0;
	int totalTime=0;
	int cycleNum=0;
	int blockedTime=0;
	int inSystem=0;
	int Runing=0;
	int run=0;
	public int getRuning() {
		return Runing;
	}

	public int getRun() {
		return Runing;
	}
	public void setRun(int r) {
		this.run= r;
	}


	public void setRuning(int run) {
		this.Runing = run;
	}




	public int getInSystem() {
		return inSystem;
	}




	public void setInSystem(int inSystem) {
		this.inSystem = inSystem;
	}
	
	//constructor to create the actual process
	public Process(int A, int B, int C, int M, int id) {
	
		this.A=A;
		this.B= B;
		this.C=C;
		this.CPUtime=C;
		total=C;
		this.M=M;
		
		this.id=id; 
		this.quatum=2;
		this.events=0;
		this.runTime=0;
		this.totalTime=C;
	//	verbose= this.verbose;
		
	}




	//getters and setters
	public int getA() {
		return this.A;
	}public int gettotalTime() {
		return this.totalTime;
	}
	public int getID() {
		return this.id;
	}
	public void setA(int a) {
		this.A = a;
	}
	public int getBlocked() {
		return this.blockedTime;
	}
	public void setBlocked(int a) {
		this.blockedTime = a;
	}
	public int getEvents() {
		return this.events;
	}
	public void setCycleNum(int a) {
		this.cycleNum = a;
	}
	public int getCycleNum() {
		return this.cycleNum;
	}
	
	public void setEvents(int e) {
		this.events = e;
	}
	public int getQuantum() {
		return this.quatum;
	}
	public void setQuantum(int a) {
		this.quatum = a;
	}
	public int getC() {
		return this.C;
	}
	public void setC(int c) {
		this.C = c;
	}
	public int getM() {
		return M;
	}
	public void setM(int m) {
		this.M = m;
	}
	public int getB() {
		return this.B;
	}
	public void setB(int b) {
		this.B = b;
	}
	public int getRuntime() {
		return this.runTime;
	}
	public void setrunTime(int runTime) {
		this.runTime = runTime;
	}
	public String getCurrentState() {
		return this.currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public int getFinishTime() {
		return this.finishTime;
	}
	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}
	public int getWaitingTime() {
		return this.waitingTime;
	}
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	public void setburst(int val) {
		this.burst = val;
	}
	
	public int getCycleTracker() {
		return this.cycleTracker;
	}
	public void setCycleTracker(int val) {
		this.cycleTracker = val;
	}
	
	public int getBurst() {
		return this.burst;
	}
	
	
	public int getCPUtime() {
		return this.CPUtime;
	}

	public void setWaitTime(int val) {
		this.waitTime = val;
	}
	
	public int getWaitTime() {
		return this.waitTime;
	}
	



	public void setCPUtime(int cPUtime) {
		this.CPUtime = cPUtime;
	}




	public boolean isVerbose() {
		return this.verbose;
	}




	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}




	public int getIOBurst() {
		return this.IOburst;
	}




	public void setIOBurst(int val) {
		this.IOburst = val;
	}  




	public int compareTo(Process p) {
		if (p.getA() > this.A){
			return -1;
		}else if(this.A > p.getA()){
			return 1;
		}
		else if(this.A==p.getA()) {
			if (this.id>p.getID()) {
				return 1;
			}
			else {
				return -1;
			}
		}
		else if(p.getB() > this.B){
			return -1;
		}else if(this.B > p.getB()){
			return 1;
		}else if(p.getC() > this.C){
			return -1;
		}else if(this.C > p.getC()){
			return 1;
		}else if(p.getM() > this.M){
			return -1;
		}else if(this.M > p.getM()){
			return 1;
		}
		else if(this.id < p.getID()){
			return -1;
		}
		else if(this.id > p.getID()){
			return 1;
		}
		return 0;
	}
	
	/*
	 * use the processdetailes method to print out the process prograasion before every cycle
	 */
	
	public void processdetails() {
		System.out.print( "( "+ getA() +", " + getB() + ", " + this.total + ", " +getM() + ")  " );
		
	}
	
	public String toString() {
		return "Process ID: " + getID() + " ( "+ getA() +" " + getB() + " " + this.total + " " +getM() + ")";
		
		
	}
	/*
	 * used to track progression before every cycle
	 */
	public void cycleStatus() {
		if (currentState=="blocked") {
			System.out.printf(currentState + " " + this.IOburst);
		}
		else if (currentState=="unstarted") {
			System.out.printf(currentState + " " + this.burst );
		}
		else if (currentState=="ready") {
			System.out.printf(currentState + " " + this.burst+ "\t");
		}
		
		else {
		System.out.print(currentState + " " + this.burst);
		
	
}}
	
/*
 * tester method
 */
public  void Cycle() {
	System.out.println("For process " + this.getID() + "  Cycle Tracker : "+ this.cycleTracker+ "  C: " + (this.gettotalTime()-(this.getCycleTracker())));
}

/*
 * gets ratio for HPRN
 */
public  static double ratioTest(Process p, int cycleNum){
	
	double timeInSys=(cycleNum)-p.getA();
	double runTime=p.gettotalTime()-(p.getRuntime());
	double denominator= Math.max(1,runTime);
	double result=timeInSys/denominator;
		return result;
		
}
}

/*
 * used for all algorthims except sjf and hprn
 */
class The_Comparator implements Comparator<Process> { 
 
	@Override
	public int compare(Process o1, Process o2) {
		// TODO Auto-generated method stub
		double num1=o1.getC();//-(o1.getCycleTracker());
		double num2=o2.getC();//-(o2.getCycleTracker());
		
		if(num1>num2) {
			return 1;
		}
		else if(num1<num2){
			return -1;
		}
		else if(num1==num2) {
			if(o1.getA()>o2.getA()) {
				return 1;
			}
			else if(o1.getA()<o2.getA()) {
				return -1;
			}
			else if(o1.getA()==o2.getA()){
			if(o1.getID()>o2.getID()) {
				return 1;
			}
			else if(o1.getID()<o2.getID()) {
				return -1;
			}
			else {
				return 0;
			}
		}
		}
		return 0;
	} }

/*
 * 
 * Comparator used for sjf
 */
class sortSJF implements Comparator<Process> { 
	 
	@Override
	public int compare(Process o1, Process o2) {
		// TODO Auto-generated method stub
		double num1=o1.getC()-(o1.getCycleTracker());
		double num2=o2.getC()-(o2.getCycleTracker());
		
		if(num1>num2) {
			return 1;
		}
		else if(num1<num2){
			return -1;
		}
		else if(num1==num2) {
			if(o1.getA()>o2.getA()) {
				return 1;
			}
			else if(o1.getA()<o2.getA()) {
				return -1;
			}
			else if(o1.getA()==o2.getA()){
			if(o1.getID()>o2.getID()) {
				return 1;
			}
			else if(o1.getID()<o2.getID()) {
				return -1;
			}
			else {
				return 0;
			}
		}
		}
		return 0;
	} }
/*
 * 
 * comparitor used for HPRN
 * 
 */
class The_Comparator_Ratio implements Comparator<Process> { 
	//get ratio
	public  static double ratioTest(Process p){
		
		//double timeInSys=(cycleNum)-p.getA();
		double runTime=p.getInSystem();
		double denominator= Math.max(1,p.getRuning());
		double result=runTime/denominator;
			return result;
			

	}
	//compare
	@Override
	public int compare(Process o1, Process o2) {
		double ratio1=ratioTest(o1 );
		double ratio2=ratioTest(o2);
		if(ratio1>ratio2) {
			return -1;
		}
		else if(ratio1<ratio2) {
			return 1;
		}
		else if(ratio1==ratio2) {
			if(o1.getA()>o2.getA()) {
				return 1;
			}
			else if(o1.getA()<o2.getA()) {
				return -1;
			}
			else if(o1.getA()==o2.getA()){
			if(o1.getID()>o2.getID()) {
				return 1;
			}
			else if(o1.getID()<o2.getID()) {
				return -1;
			}
			else {
				return 0;
			}
		}}
		return 0;
}}
