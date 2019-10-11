 
/*
 * Alifa Faruk
 * Lab 2 Scheduler 
 * October 11, 2019
 */
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

 /*
 * actual schdueler class that contains all methodes
 * 
 * 
 */


public class Scheduler {
	
	 public static int randomOS(int base, int randomNum) throws FileNotFoundException {
	java.io.File random= new java.io.File("random-numbers.txt");
	Scanner rand= new Scanner(random);
	int result=0;
	int returnVal=0;
	for(int i=0; i<randomNum; i++) {
		result=rand.nextInt();
 	}
	randomNum++;
 	returnVal=1+(result%base);
 
	rand.close();
	return returnVal;
}
/*
 * ALGORITHM: FCFS
 * 	 
 */
public static void FCFS(boolean verbose,ArrayList<Process> list, int numProcesses ) throws FileNotFoundException {
	System.out.println("FIRST COME FIRST SERVE");
	  int totalCPUTime=0;
	  int randomNum=1;
	System.out.print("The original output is: " + numProcesses+ " ");
	int AVGIO=0;
	for(int i=0;i<numProcesses;i++) {
		list.get(i).processdetails();
 	}
	System.out.println();
	Collections.sort(list);
	System.out.print("The sort output is: " + numProcesses + " ");

	for(Process iterate: list) {
		 
		iterate.processdetails();
	}
	System.out.println();
 
 	  ArrayList<Process> terminated= new ArrayList<Process>();
	     HashMap<Process, Integer> runinng  = new HashMap<Process, Integer>();

	  int cycleNum=0;
    
      
     HashMap<Process, Integer> waitTime  = new HashMap<Process, Integer>();
     HashMap<Process, Integer> finishTime  = new HashMap<Process, Integer>();
   
     HashMap<Process, Integer> IOTime  = new HashMap<Process, Integer>();
  CopyOnWriteArrayList<Process> blocked= new CopyOnWriteArrayList<Process>();
	  
	    Queue<Process> readyQ = new LinkedList<Process>(); 
		ArrayList<Integer> AVGwait=new ArrayList<Integer>();
		ArrayList<Integer> IOUtilization=new ArrayList<Integer>();

	   Queue<Process> unstartedQ = new LinkedList<Process>(); 

 
	 
	
	int finishingTime=0;
 
	double contextSwitching=0; 
	//Current running process
	Process running=null;
	for(int i=0; i<list.size();i++) {
		
			list.get(i).currentState="unstarted";
			unstartedQ.add(list.get(i));
		
	}
	
	if(verbose==true) {
		System.out.println("This detailed printout gives the state and remaining burst for each process\n");
		System.out.print("Before cycle \t" + cycleNum + ":\t");
		for (int  iterator=0; iterator<list.size();iterator++) {
			//System.out.println("Here 0000 ");
			list.get(iterator).cycleStatus();
			System.out.print("\t  ");
		}
		System.out.print("\n");
	}
	
	
	while (terminated.size()!=list.size()) {

		 
		
		for(Process iterator: unstartedQ) {
			if (iterator.A==cycleNum) {
			int in=	list.indexOf(iterator);
			list.get(in).currentState="ready";
			
			readyQ.add(iterator);
			iterator.setCurrentState("ready");
			
			waitTime.put(iterator, 0);
			}
			else {
				
			}
		}
		cycleNum++;
		
		 
		if(running==null ){
			contextSwitching++;
 			running=readyQ.poll();
			 			String state= "running";
			if(running!=null && running.getRuntime()==0) {
			int randomBurst= randomOS(running.getB(), randomNum);
			 randomNum++;
			running.setburst(randomBurst);
			running.setIOBurst(randomBurst);
			 
		int x=0;
		for(Process iterator: list) {
		if(iterator.getID()==running.getID()) {
			iterator.setCurrentState("running");
			runinng.put(running,randomBurst);
		}
	}
			}
	

		}
		if(!readyQ.isEmpty()) {
			for(Process process: readyQ) {
				waitTime.put(process, waitTime.get(process) + 1);
			}
		}
		
		if(verbose==true) {
			System.out.print("Before cycle \t" + cycleNum + ":\t");
			for(Process iterator: list) {
				iterator.cycleStatus();
				System.out.print("\t");
			}
			System.out.print("\n");
		}
		
		if(running!=null) {
			totalCPUTime++;
			running.setC(running.getC()-1);
			running.setburst(running.getBurst()-1);
		}
		

		if(!blocked.isEmpty()) {
			AVGIO++;
			ArrayList<Process> ready= new ArrayList<Process>();
			for(Process iterator: blocked) {
				 
				iterator.setIOBurst(iterator.getIOBurst()-1);
				
			 
				IOTime.put(iterator, IOTime.get(iterator)+1);
				if(iterator.getIOBurst()<=0) {
					ready.add(iterator);
			
				}}
			
			Collections.sort(ready);
			for(Process in :ready ) { 
				       in.setCurrentState("ready");
						blocked.remove(in);
						readyQ.add(in);
					 
						for(Process process: list) {
							if(process.getID()==in.getID()) {
								process.setCurrentState("ready");
								
						}
						
						
						//terminated.add(iterator);		
					}
				}
				
			
		}	 
		
		for(Process iterator: readyQ) {
			int in= list.indexOf(iterator);
			list.get(in).waitingTime++;
		}
		
	
		if(running!=null && running.getC()!=0 && running.getBurst()<=0) {
		 
		 
			 
			int val=0;
		
			for(Process iterator: list) {
				if(iterator.getID()==running.getID()) {
					iterator.setCurrentState("blocked");
					 
					if(IOTime.containsKey(iterator)) {
					 
					}
					else {
					IOTime.put(iterator, 0);
					}
					val=runinng.get(iterator)*iterator.getM();
 					iterator.setIOBurst(val);
 				}
			}
				
 			running.setIOBurst(val);
	
			blocked.add(running);
			running=null;
 		}
		else if(running!=null && running.getC()==0 ){
			for(Process iterator: list) {
				if(iterator.getID()==running.getID()) {
					iterator.setCurrentState("terminated");
					iterator.setburst(0);
				
				}
				}
			terminated.add(running);
			finishTime.put(running, cycleNum);
			running=null;
			
	} 
		finishingTime++;
	}
	ArrayList<Integer> AVGturnaroundTime= new ArrayList<Integer>();
	System.out.println("\nThe scheduling algorithm used was First Come First Served\n");
	int counter=0;
	for(Process values: list) {
 		System.out.println("Process "+ counter + ": ");
	 	System.out.print("\t(A, B, C, M) =" +" " );
	 	 values.processdetails();
		for(Process proc: finishTime.keySet()) {
			if (proc.getID()==values.getID()) {
				System.out.println("\n\tFinishing Time: " + finishTime.get(proc));
				System.out.println("\tTurnaround Time: "+  (finishTime.get(proc)- proc.getA()));
				AVGturnaroundTime.add( (finishTime.get(proc)- proc.getA()));
			}
		}
		
	
		for(Process proc: IOTime.keySet()) {
 			if (proc.getID()==values.getID()) {
				System.out.println("\tI/O Time: " + IOTime.get(proc));
				IOUtilization.add(IOTime.get(proc));
			}
		
		
		}
		for(Process proc: waitTime.keySet()) {
 			if (proc.getID()==values.getID()) {
				System.out.println("\tWaiting Time: " + waitTime.get(proc));
				AVGwait.add(waitTime.get(proc));
			}
		}
		
		System.out.println();
		
		counter++;
		}
	int o=0;
	for(Process l: list) {
		o+=l.getCPUtime();
		 
	}
	System.out.print("\nSummary Data:");
	System.out.println("\n\tFinishing Time: " + finishingTime);
 	System.out.println("\tCPU Utilization: " + ((double)totalCPUTime/(double)(cycleNum)));
 	int IOU=0;
	double ioUtilization=0;
	
	for(int i =0; i<IOUtilization.size();i++) {
		IOU+=IOUtilization.get(i);
	}
	ioUtilization=(double)AVGIO/((double) cycleNum);
	System.out.println("\tI/O Utilization:: " + ioUtilization);
	System.out.println("\tThroughput: " + ((double)((double)100*list.size()/(double)(cycleNum))) + " processes per hundred cycles");
	int avg=0;
	double TAT1=0;
	for(int i =0; i<AVGturnaroundTime.size();i++) {
		avg+=AVGturnaroundTime.get(i);
	}
	TAT1= (double)avg/(double)list.size();
	
	System.out.println("\tAverage turnaround time: " + TAT1);
	int avgWait=0;
	double AVGW=0;
	for(int i =0; i<AVGwait.size();i++) {
		avgWait+=AVGwait.get(i);
	}
	AVGW=(double)avgWait/(double) list.size();
	System.out.println("\tAverage Wait time: " + AVGW);
	
	
	
}

/*
 * ALGORITHM: ROUND ROBIN
 * 	 
 */
public static void RR(boolean verbose, ArrayList<Process> list, int numProcesses) throws FileNotFoundException {
	int randomNum=1;
	System.out.println("ROUND ROBIN");

	System.out.print("The original output is: " + numProcesses+ " ");

	for(int i=0;i<numProcesses;i++) {
		list.get(i).processdetails();
	 
	}
	System.out.println();
	int AVGIO=0;
	Collections.sort(list);
	System.out.print("The sort output is: " + numProcesses + " ");

	for(Process iterate: list) {
		 
		iterate.processdetails();
	}
	System.out.println();
	ArrayList<Process> terminated= new ArrayList<Process>();
    HashMap<Process, Integer> runinng  = new HashMap<Process, Integer>();

 int cycleNum=0;

 
HashMap<Process, Integer> waitTime  = new HashMap<Process, Integer>();
HashMap<Process, Integer> finishTime  = new HashMap<Process, Integer>();

HashMap<Process, Integer> IOTime  = new HashMap<Process, Integer>();
CopyOnWriteArrayList<Process> blocked= new CopyOnWriteArrayList<Process>();
 
   Queue<Process> readyQ = new LinkedList<Process>(); 
	ArrayList<Integer> AVGwait=new ArrayList<Integer>();
	ArrayList<Integer> IOUtilization=new ArrayList<Integer>();

  Queue<Process> unstartedQ = new LinkedList<Process>(); 


int CPUTime=0;

int finishingTime=0;
int quantum=2;
double contextSwitching=0;
 Process running=null;
for(int i=0; i<list.size();i++) {
	
		list.get(i).currentState="unstarted";
		unstartedQ.add(list.get(i));
	
}

if(verbose==true) {
	System.out.println("This detailed printout gives the state and remaining burst for each process\n");
	System.out.print("Before cycle \t" + cycleNum + ":\t");
	for (int  iterator=0; iterator<list.size();iterator++) {
 		list.get(iterator).cycleStatus();
		System.out.print("\t  ");
	}
	System.out.print("\n");
}

while (terminated.size()!=list.size()) {
 
	for(Process iterator: unstartedQ) {
		if (iterator.A==cycleNum) {
		int in=	list.indexOf(iterator);
		list.get(in).currentState="ready";
		 
		readyQ.add(iterator);
		iterator.setCurrentState("ready");
		 
		waitTime.put(iterator, 0);
		}
		else {
			
		}
	}
	
	cycleNum++;
	
	 
	if(running==null ){
		 
		
		contextSwitching++;
		 running=readyQ.poll();
  		if(running!=null) {
			if(running.getRuntime()<=0) {
			int randomBurst= randomOS(running.getB(), randomNum);
			randomNum++;
			running.setrunTime(randomBurst);
			running.setburst(2);
			running.setIOBurst(randomBurst);
		
 
		for(Process iterator: list) {
		if(iterator.getID()==running.getID()) {
	 		iterator.setCurrentState("running");
			runinng.put(running,randomBurst);
		}
		}
		}
		else {

			for(Process iterator: list) {
			if(iterator.getID()==running.getID()) {
				iterator.setCurrentState("running");
				 
				if(running.getRuntime()==1) {
					running.setburst(1);
				}
				else {
 			running.setburst(2);
 		}
		}

		}
		}
		}
 		
 	
 


	}

	if(!readyQ.isEmpty()&& !waitTime.isEmpty()) {
		for(Process process: readyQ) {
 			for(Process p :list) {
				if(p.getID()==process.getID()) {
					int set= p.getWaitingTime();
					p.setWaitingTime(set+1);
				}
			}
			waitTime.put(process, waitTime.get(process) + 1);
		}
 	}
	
	
	quantum--;
	if(verbose==true) {
		System.out.print("Before cycle \t" + cycleNum + ":\t");
		for(Process iterator: list) {
			iterator.cycleStatus();
			System.out.print("\t");
		}
		System.out.print("\n");
	}

 	if(running!=null) {
 		CPUTime++;
	 
		running.setrunTime(running.getRuntime()-1);
		running.setC(running.getC()-1);
		running.setburst(running.getBurst()-1);
	}
	
 	ArrayList<Process> ready= new ArrayList<Process>();
	if(!blocked.isEmpty()) {
		AVGIO++;
		for(Process iterator: blocked) {
			
			iterator.setIOBurst(iterator.getIOBurst()-1);
			
 			IOTime.put(iterator, IOTime.get(iterator)+1);
			if(iterator.getIOBurst()<=0) {
				ready.add(iterator);
 
		
			}}
		
		
		
		Collections.sort(ready);
		 		for(Process in :ready ) {
 					in.setCurrentState("ready");
					blocked.remove(in);
				 
					for(Process process: list) {
						if(process.getID()==in.getID()) {
							process.setCurrentState("ready");
							
					}
				
				}
			}
			
		
	}	

	
 	
if( quantum<=0 ) {
	 
	
	
	if(running!=null && running.getC()!=0 && running.getRuntime()<=0) {
	 
		int val=0;
	
		for(Process iterator: list) {
			if(iterator.getID()==running.getID()) {
				iterator.setCurrentState("blocked");
				 
				if(IOTime.containsKey(iterator)) {
				 
				}
				else {
				IOTime.put(iterator, 0);
				}
				val=runinng.get(iterator)*iterator.getM();
				
				iterator.setIOBurst(val);
			 
			}
		}
			
		 
		running.setIOBurst(val);

		blocked.add(running);
		running=null;
		quantum=2;
	 
	}
	else if(running!=null && running.getC()==0  ){
		for(Process iterator: list) {
			if(iterator.getID()==running.getID()) {
				iterator.setCurrentState("terminated");
				iterator.setburst(0);
			
			}
			}
		terminated.add(running);
		finishTime.put(running, cycleNum);
		running=null;
		quantum=2;
	}
	else {
		ready.add(running);
		for(Process l: list) {
			if(l.getID()==running.getID()) {
		waitTime.put(running, 0);
		}}
		running=null;
		quantum=2;
	
	
	}
	ArrayList<Process>  re=new ArrayList<Process> ();
	Collections.sort(ready);
	int [] numbers = {10, 20, 30, 40, 50};
	if(!ready.isEmpty()) {
	int smallest = ready.get(0).getID();
	Process index;
	for(Process x : ready ){
	   if (x.getID() < smallest) {
	      smallest = x.getID();
	      index=x;
	   }
	}
	 
	}
	 
	for(Process in :ready ) {
		
		in.setCurrentState("ready");
		blocked.remove(in);
		 
		for(Process process: list) {
			if(process.getID()==in.getID()) {
				process.setCurrentState("ready");
				process.setburst(0);
				
		}
		
		
	 
	}
}
	
}
	
	
	else {
		
		if(running!=null && running.getC()!=0 && running.getRuntime()<=0) {
			 
			 
			 
			int val=0;
		
			for(Process iterator: list) {
				if(iterator.getID()==running.getID()) {
					iterator.setCurrentState("blocked");
					 
					if(IOTime.containsKey(iterator)) {
					}
					else {
					IOTime.put(iterator, 0);
					}
					val=runinng.get(iterator)*iterator.getM();
					
					iterator.setIOBurst(val);
 				}
			}
				
 			running.setIOBurst(val);

			blocked.add(running);
			running=null;
			quantum=2;
 		}
		else if(running!=null && running.getC()==0 ){
			for(Process iterator: list) {
				if(iterator.getID()==running.getID()) {
					iterator.setCurrentState("terminated");
					iterator.setburst(0);
				
				}
				}
			terminated.add(running);
			finishTime.put(running, cycleNum);
			running=null;
			quantum=2;
		}	
		else if(running==null){
			quantum=2;
		}
	}

	finishingTime++;
	
	for(Process in: ready) {
		readyQ.add(in);
	}

	
}
ArrayList<Integer> AVGturnaroundTime= new ArrayList<Integer>();
System.out.println("\nThe scheduling algorithm used was Round Robin\n");
int counter=0;
for(Process values: list) {
	System.out.println("Process "+ counter + ": ");
	System.out.print("\t(A, B, C, M) =" +" " );
	 values.processdetails();
	for(Process proc: finishTime.keySet()) {
		if (proc.getID()==values.getID()) {
			System.out.println("\n\tFinishing Time: " + finishTime.get(proc));
			System.out.println("\tTurnaround Time: "+  (finishTime.get(proc)- proc.getA()));
			AVGturnaroundTime.add( (finishTime.get(proc)- proc.getA()));
		}
	
	
	
		//System.out.println(waitTime);
		
			
		}
	
	for(Process proc: IOTime.keySet()) { 
		//System.out.println(waitTime);
		if (proc.getID()==values.getID()) {
			System.out.println("\tIO Time: " + IOTime.get(proc));
			IOUtilization.add(IOTime.get(proc));
		}
	
	
	}
	System.out.println("\tWaiting Time: " + values.getWaitingTime());
	AVGwait.add(values.getWaitingTime());

	
	System.out.println();
	
	counter++;
	}
int o=0;
for(Process l: list) {
	o+=l.getCPUtime();
 }
System.out.print("\nSummary Data:");
System.out.println("\n\tFinishing Time: " + finishingTime);
 System.out.println("\tCPU Utilization: " + ((double)CPUTime/(double)(cycleNum)));
 int IOU=0;
double ioUtilization=0;

for(int i =0; i<IOUtilization.size();i++) {
	IOU+=IOUtilization.get(i);
}
ioUtilization=(double)AVGIO/((double) cycleNum);
System.out.println("\tI/O Utilization:: " + ioUtilization);
System.out.println("\tThroughput: " + ((double)((double)100*list.size()/(double)(cycleNum))) + " processes per hundred cycles");
int avg=0;
double TAT1=0;
for(int i =0; i<AVGturnaroundTime.size();i++) {
	avg+=AVGturnaroundTime.get(i);
}
TAT1= (double)avg/(double)list.size();

System.out.println("\tAverage turnaround time: " + TAT1);
int avgWait=0;
double AVGW=0;
for(int i =0; i<AVGwait.size();i++) {
	avgWait+=AVGwait.get(i);
}
AVGW=(double)avgWait/(double) list.size();
System.out.println("\tAverage Wait time: " + AVGW);

}




/*
 * ALGORITHM: SHORTEST JOB NEXT
 * 	 
 */



public static void SJF(boolean verbose,ArrayList<Process> list, int numProcesses ) throws FileNotFoundException {
	System.out.println("Shortest Job First");
	int randomNum=1;
	PriorityQueue<Process> priorityQ = new    PriorityQueue<Process>(10, new The_Comparator()); 
 	System.out.print("The original output is: " + numProcesses+ " ");

	for(int i=0;i<numProcesses;i++) {
		list.get(i).processdetails();
 	}
	System.out.println();
	Collections.sort(list);
	System.out.print("The sort output is: " + numProcesses + " ");

	for(Process iterate: list) {
		
		iterate.processdetails();
	}
	System.out.println();
 
	 
	  ArrayList<Process> terminated= new ArrayList<Process>();
	  HashMap<Process, Integer> runinng  = new HashMap<Process, Integer>();

	  int cycleNum=0;
	  int totalCPUTime=0;
   
     
     HashMap<Process, Integer> waitTime  = new HashMap<Process, Integer>();
     
     HashMap<Process, Integer> finishTime  = new HashMap<Process, Integer>();
     HashMap<Process, Integer> cycleReady  = new HashMap<Process, Integer>();

     HashMap<Process, Integer> IOTime  = new HashMap<Process, Integer>();
     CopyOnWriteArrayList<Process> blocked= new CopyOnWriteArrayList<Process>();
	  
	 Queue<Process> readyQ = new LinkedList<Process>(); 
	ArrayList<Process> ProcessesInCycle=new ArrayList<Process>();
	int AVGIO=0;
	ArrayList<Integer> IOUtilization=new ArrayList<Integer>();

	Queue<Process> unstartedQ = new LinkedList<Process>(); 

	int finishingTime=0;
 
 
	//Current running process
	Process running=null;
	for(int i=0; i<list.size();i++) {
		
			list.get(i).currentState="unstarted";
			unstartedQ.add(list.get(i));
		
	}
	
	if(verbose==true) {
		System.out.println("This detailed printout gives the state and remaining burst for each process\n");
		System.out.print("Before cycle \t" + cycleNum + ":\t");
		for (int  iterator=0; iterator<list.size();iterator++) {
			//System.out.println("Here 0000 ");
			list.get(iterator).cycleStatus();
			System.out.print("\t  ");
		}
		System.out.print("\n");
	}
	
	while (terminated.size()!=list.size()) {
		if(!ProcessesInCycle.isEmpty()) {
 				for(Process in: ProcessesInCycle) {
					in.setCycleTracker(in.getCycleTracker()+1);
				}
				ArrayList<Process> resort = new ArrayList<Process>();
				if(!priorityQ.isEmpty()) {
				for(Process in : priorityQ) {
					resort.add(in);
					
			}
			}
				priorityQ.clear();
				for(Process in: resort) {
					priorityQ.add(in);
					
				}
				 				Process[] events = priorityQ.toArray(new Process[priorityQ.size()]);
				Arrays.sort(events, priorityQ.comparator());
				for (Process e : events) {
				//    e.Cycle();
				}
				
				
			}
		
		
		for(Process iterator: unstartedQ) {
			if (iterator.A==cycleNum) {
			int in=	list.indexOf(iterator);
			list.get(in).currentState="ready";
			
			iterator.setCycleTracker(0);
			readyQ.add(iterator);
			iterator.setCurrentState("ready");
			iterator.setCycleTracker(0);
			ProcessesInCycle.add(iterator);
			cycleReady.put(iterator, 0);
			priorityQ.add(iterator);
			waitTime.put(iterator, 0);
			}
			else {
				
			}
		}
		 

		cycleNum++;
		if(running==null ){
		  
			int i=0;
			
			
 			running=priorityQ.poll();
 			 
 			int minCycle= Collections.min(cycleReady.values());
 			
 			readyQ.remove(running);
			if(running!=null &&running.getRuntime()==0) {
			int randomBurst= randomOS(running.getB(), randomNum);
			 randomNum++;
			running.setburst(randomBurst);
			running.setIOBurst(randomBurst);
			 
		int x=0;
		for(Process iterator: list) {
		if(iterator.getID()==running.getID()) {
			iterator.setCurrentState("running");
			runinng.put(running,randomBurst);
		}
	}
			}
	

		}
	 
		for(Process iterator: readyQ) {
			int in= list.indexOf(iterator);
			list.get(in).setWaitingTime(list.get(in).getWaitingTime()+1);
		}
		
		if(verbose==true) {
		
			System.out.print("Before cycle \t" + cycleNum + ":\t");
			
			for(Process iterator: list) {
				iterator.cycleStatus();
				System.out.print("\t");
			}
			System.out.print("\n");
		}
		
		if(running!=null) {
			totalCPUTime++;
			running.setC(running.getC()-1);
			running.setburst(running.getBurst()-1);
		}
		

		if(!blocked.isEmpty()) {
			AVGIO++;
			ArrayList<Process> ready= new ArrayList<Process>();
			for(Process iterator: blocked) {
 				iterator.setIOBurst(iterator.getIOBurst()-1);
				
 				IOTime.put(iterator, IOTime.get(iterator)+1);
				if(iterator.getIOBurst()<=0) {
					ready.add(iterator);
			
				}}
			
			Collections.sort(ready);
			for(Process in :ready ) {
				if(!ProcessesInCycle.contains(in)) {
				ProcessesInCycle.add(in);
				}
				       in.setCurrentState("ready");
						blocked.remove(in);
						readyQ.add(in);
						if(!priorityQ.contains(in)) {
							priorityQ.add(in);
						}
					
  						for(Process process: list) {
							if(process.getID()==in.getID()) {
								process.setCurrentState("ready");
								
						}
						
						
 					}
				}
				
			
		}	
	
	
		if(running!=null && running.getC()!=0 && running.getBurst()<=0) {
	
			int val=0;
		
			for(Process iterator: list) {
				if(iterator.getID()==running.getID()) {
					iterator.setCurrentState("blocked");
					
					if(IOTime.containsKey(iterator)) {
 					}
					else {
					IOTime.put(iterator, 0);
					}
					val=runinng.get(iterator)*iterator.getM();
					
					iterator.setIOBurst(val);
 				}
			}
				
 			running.setIOBurst(val);
	
			blocked.add(running);
			running=null;
 		}
		else if(running!=null && running.getC()==0 ){
			for(Process iterator: list) {
				if(iterator.getID()==running.getID()) {
					iterator.setCurrentState("terminated");
					iterator.setburst(0);
					ProcessesInCycle.remove(iterator);
				
				}
				}
			terminated.add(running);
			finishTime.put(running, cycleNum);
			running=null;
			
	} 
		finishingTime++;
	}
	ArrayList<Integer> AVGturnaroundTime= new ArrayList<Integer>();
	ArrayList<Integer> AVGWaitTIme= new ArrayList<Integer>();

	System.out.println("\nThe scheduling algorithm used was Shortest Job First\n");
	int counter=0;
	for(Process values: list) {
 		System.out.println("Process "+ counter + ": ");
	 	System.out.print("\t(A, B, C, M) =" +" " );
	 	 values.processdetails();
		for(Process proc: finishTime.keySet()) {
			if (proc.getID()==values.getID()) {
				System.out.println("\n\tFinishing Time: " + finishTime.get(proc));
				System.out.println("\tTurnaround Time: "+  (finishTime.get(proc)- proc.getA()));
				AVGturnaroundTime.add( (finishTime.get(proc)- proc.getA()));
			}
		}
		
		
		for(Process proc: IOTime.keySet()) {
			//System.out.println(waitTime);
			if (proc.getID()==values.getID()) {
				System.out.println("\tIO Time: " + IOTime.get(proc));
				IOUtilization.add(IOTime.get(proc));
			}
		
		
		}
		System.out.println("\tWaiting Time: " +values.getWaitingTime());
		AVGWaitTIme.add(values.getWaitingTime());
		
		System.out.println();
		
		counter++;
		}
	int o=0;
	for(Process l: list) {
		o+=l.getCPUtime();
		 
	}
	System.out.print("\nSummary Data:");
	System.out.println("\n\tFinishing Time: " + finishingTime);
 	System.out.println("\tCPU Utilization: " + ((double)totalCPUTime/(double)(cycleNum)));
 	int IOU=0;
	double ioUtilization=0;
	
	for(int i =0; i<IOUtilization.size();i++) {
		IOU+=IOUtilization.get(i);
	}
	ioUtilization=(double)AVGIO/((double) cycleNum);
	System.out.println("\tI/O Utilization:: " + ioUtilization);
	System.out.println("\tThroughput: " + ((double)((double)100*list.size()/(double)(cycleNum))) + " processes per hundred cycles");
	int avg=0;
	double TAT1=0;
	for(int i =0; i<AVGturnaroundTime.size();i++) {
		avg+=AVGturnaroundTime.get(i);
	}
	TAT1= (double)avg/(double)list.size();
	
	System.out.println("\tAverage turnaround time: " + TAT1);
	int avgWait=0;
	double AVGW=0;
	for(int i =0; i<AVGWaitTIme.size();i++) {
		avgWait+=AVGWaitTIme.get(i);
	}
	AVGW=(double)avgWait/(double) list.size();
	System.out.println("\tAverage Wait time: " + AVGW);
	
	
	
}


/*
 * ALGORITHM: HIGHEST OENALTY RATIO NEXT
 * 	 
 */
public static void HPRN(boolean verbose,ArrayList<Process> list, int numProcesses ) throws FileNotFoundException {
	System.out.println("Highest Penalty Ratio Next");
	int totalCPU=0;
	int randomNum=1;
	PriorityQueue<Process> priorityQ = new    PriorityQueue<Process>(10, new The_Comparator_Ratio()); 
 	System.out.print("The original output is: " + numProcesses+ " ");

	for(int i=0;i<numProcesses;i++) {
		list.get(i).processdetails();
 	}
	System.out.println();
	Collections.sort(list);
	System.out.print("The sort output is: " + numProcesses + " ");

	for(Process iterate: list) {
		
		iterate.processdetails();
	}
	System.out.println();
 
	 
	  ArrayList<Process> terminated= new ArrayList<Process>();
	  ArrayList<Process> InSystem= new ArrayList<Process>();
	  HashMap<Process, Integer> runinng  = new HashMap<Process, Integer>();
 
	  int cycleNum=0;
    
   
     
     HashMap<Process, Integer> waitTime  = new HashMap<Process, Integer>();
     
     HashMap<Process, Integer> finishTime  = new HashMap<Process, Integer>();
     HashMap<Process, Integer> cycleReady  = new HashMap<Process, Integer>();

     HashMap<Process, Integer> IOTime  = new HashMap<Process, Integer>();
     CopyOnWriteArrayList<Process> blocked= new CopyOnWriteArrayList<Process>();
	  
	 Queue<Process> readyQ = new LinkedList<Process>(); 
	ArrayList<Process> ProcessesInCycle=new ArrayList<Process>();
	int AVGIO=0;
	ArrayList<Integer> IOUtilization=new ArrayList<Integer>();

	Queue<Process> unstartedQ = new LinkedList<Process>(); 

	int finishingTime=0;
 
 
	//Current running process
	Process running=null;
	for(int i=0; i<list.size();i++) {
		
			list.get(i).currentState="unstarted";
			unstartedQ.add(list.get(i));
		
	}
	
	if(verbose==true) {
		System.out.println("This detailed printout gives the state and remaining burst for each process\n");
		System.out.print("Before cycle \t" + cycleNum + ":\t");
		for (int  iterator=0; iterator<list.size();iterator++) {
			//System.out.println("Here 0000 ");
			list.get(iterator).cycleStatus();
			System.out.print("\t  ");
		}
		System.out.print("\n");
	}
	
	while (terminated.size()!=list.size()) {
		if(!InSystem.isEmpty()) {
			for(Process p: InSystem) {
				 
				p.setInSystem(p.getInSystem()+1);
			}
			for(Process p: priorityQ) {
				//p.Cycle();
				//System.out.println(p.getRuning());
				//System.out.println("IN the System " + p.getInSystem());
				//System.out.println("Actually Running " + p.getRuning());
				//p.setInSystem(p.getInSystem()+1);
			}
		}
		if(!ProcessesInCycle.isEmpty()) {
			 
				for(Process in: ProcessesInCycle) {
					in.setCycleTracker(in.getCycleTracker()+1);
				}
				ArrayList<Process> resort = new ArrayList<Process>();
				if(!priorityQ.isEmpty()) {
				for(Process in : priorityQ) {
					resort.add(in);
					
			}
			}
				priorityQ.clear();
				for(Process in: resort) {
					priorityQ.add(in);
					
				}
				 
				Process[] events = priorityQ.toArray(new Process[priorityQ.size()]);
				Arrays.sort(events, priorityQ.comparator());
				for (Process e : events) {
					//e.Cycle();
				    //System.out.println( "  " + ratioTest(e, cycleNum));
				}
				
				
			}
		
		
		for(Process iterator: unstartedQ) {
			if (iterator.A==cycleNum) {
				InSystem.add(iterator);
			int in=	list.indexOf(iterator);
			list.get(in).currentState="ready";
			
			iterator.setCycleTracker(0);
			readyQ.add(iterator);
			iterator.setCurrentState("ready");
			iterator.setCycleTracker(0);
			ProcessesInCycle.add(iterator);
			cycleReady.put(iterator, 0);
			priorityQ.add(iterator);
			waitTime.put(iterator, 0);
			}
			else {
				
			}
		}
		 
		cycleNum++;
		if(running==null ){
		 
			 
			int i=0;
			
			
 			running=priorityQ.poll();
 			 
 			int minCycle= Collections.min(cycleReady.values());
 			
 			readyQ.remove(running);
			if(running!=null &&running.getRuntime()==0) {
			int randomBurst= randomOS(running.getB(), randomNum);
			randomNum++;
			running.setburst(randomBurst);
			running.setIOBurst(randomBurst);
			 
		int x=0;
		for(Process iterator: list) {
		if(iterator.getID()==running.getID()) {
			iterator.setCurrentState("running");
			runinng.put(running,randomBurst);
		}
	}
			}
	

		}
	
		for(Process iterator: readyQ) {
			int in= list.indexOf(iterator);
			list.get(in).setWaitingTime(list.get(in).getWaitingTime()+1);
		}
		
		if(verbose==true) {
		
			System.out.print("Before cycle \t" + cycleNum + ":\t");
			
			for(Process iterator: list) {
				iterator.cycleStatus();
				System.out.print("\t");
			}
			System.out.print("\n");
		}
		
		if(running!=null) {
			totalCPU++;
			running.setC(running.getC()-1);
			running.setburst(running.getBurst()-1);
			for(Process p: list) {
				if(p.getID()==running.getID())
				p.setRuning(p.getRuning()+1);
			}
			running.setRuning(running.getRuning()+1);
		}
		

		if(!blocked.isEmpty()) {
			AVGIO++;
			ArrayList<Process> ready= new ArrayList<Process>();
			for(Process iterator: blocked) {
				iterator.setBlocked(iterator.getBlocked()+1);
 				iterator.setIOBurst(iterator.getIOBurst()-1);
				
 				IOTime.put(iterator, IOTime.get(iterator)+1);
				if(iterator.getIOBurst()<=0) {
					ready.add(iterator);
			
				}}
			
			Collections.sort(ready);
			for(Process in :ready ) {
				if(!ProcessesInCycle.contains(in)) {
				ProcessesInCycle.add(in);
				}
				       in.setCurrentState("ready");
						blocked.remove(in);
						readyQ.add(in);
						if(!priorityQ.contains(in)) {
							priorityQ.add(in);
						}
					
  						for(Process process: list) {
							if(process.getID()==in.getID()) {
								process.setCurrentState("ready");
								
						}
						
						
						//terminated.add(iterator);		
					}
				}
				
			
		}	
	
	
		if(running!=null && running.getC()!=0 && running.getBurst()<=0) {
	
			int val=0;
		
			for(Process iterator: list) {
				if(iterator.getID()==running.getID()) {
					iterator.setCurrentState("blocked");
					
					if(IOTime.containsKey(iterator)) {
 					}
					else {
					IOTime.put(iterator, 0);
					}
					val=runinng.get(iterator)*iterator.getM();
					
					iterator.setIOBurst(val);
 				}
			}
				
 			running.setIOBurst(val);
	
			blocked.add(running);
			running=null;
 		}
		else if(running!=null && running.getC()==0 ){
			for(Process iterator: list) {
				if(iterator.getID()==running.getID()) {
					iterator.setCurrentState("terminated");
					iterator.setburst(0);
					ProcessesInCycle.remove(iterator);
				
				}
				}
			terminated.add(running);
			finishTime.put(running, cycleNum);
			running=null;
			
	} 
		finishingTime++;
	}
	ArrayList<Integer> AVGturnaroundTime= new ArrayList<Integer>();
	ArrayList<Integer> AVGWaitTIme= new ArrayList<Integer>();

	System.out.println("\nThe scheduling algorithm used was Highest Penalty Ratio Next\n");
	int counter=0;
	for(Process values: list) {
 		System.out.println("Process "+ counter + ": ");
	 	System.out.print("\t(A, B, C, M) =" +" " );
	 	 values.processdetails();
		for(Process proc: finishTime.keySet()) {
			if (proc.getID()==values.getID()) {
				System.out.println("\n\tFinishing Time: " + finishTime.get(proc));
				System.out.println("\tTurnaround Time: "+  (finishTime.get(proc)- proc.getA()));
				AVGturnaroundTime.add( (finishTime.get(proc)- proc.getA()));
			}
		}
		 
		
		
		for(Process proc: IOTime.keySet()) {
 			if (proc.getID()==values.getID()) {
				System.out.println("\tIO Time: " + IOTime.get(proc));
				IOUtilization.add(IOTime.get(proc));
			}
		
		
		}
		System.out.println("\tWaiting Time: " +values.getWaitingTime());
		AVGWaitTIme.add(values.getWaitingTime());
		
		System.out.println();
		
		counter++;
		}
	int o=0;
	for(Process l: list) {
		o+=l.getCPUtime();
		 
	}
	System.out.print("\nSummary Data:");
	System.out.println("\n\tFinishing Time: " + finishingTime);
 	System.out.println("\tCPU Utilization: " + (double)totalCPU/((double)cycleNum));
 	int IOU=0;
	double ioUtilization=0;
	
	for(int i =0; i<IOUtilization.size();i++) {
		IOU+=IOUtilization.get(i);
	}
	ioUtilization=(double)AVGIO/((double) cycleNum);
	System.out.println("\tI/O Utilization:: " + ioUtilization);
	System.out.println("\tThroughput: " + ((double)((double)100*list.size()/(double)(cycleNum))) + " processes per hundred cycles");
	int avg=0;
	double TAT1=0;
	for(int i =0; i<AVGturnaroundTime.size();i++) {
		avg+=AVGturnaroundTime.get(i);
	}
	TAT1= (double)avg/(double)list.size();
	
	System.out.println("\tAverage turnaround time: " + TAT1);
	int avgWait=0;
	double AVGW=0;
	for(int i =0; i<AVGWaitTIme.size();i++) {
		avgWait+=AVGWaitTIme.get(i);
	}
	AVGW=(double)avgWait/(double) list.size();
	System.out.println("\tAverage Wait time: " + AVGW);
	
	
	
	
	
}
public static void main(String args[]) throws IOException {
	ArrayList<Process> list1= new ArrayList<Process>();
	ArrayList<Process> list2= new ArrayList<Process>();
	ArrayList<Process> list3= new ArrayList<Process>();
	ArrayList<Process> list4= new ArrayList<Process>();

	//create an instance of the schduele
	
	Scheduler scheduler = new Scheduler();
	
	String fileName;
	//check if verbose detailed wanted
	boolean verbose=false;
	if (args.length==2) {;
		verbose=true;
		fileName=args[1];
	
	}
	else {
		verbose=false;
		fileName=args[0];
		
	}
	
	
	BufferedReader file= new BufferedReader(new FileReader(fileName));
	

	
	int numProcesses= Character.getNumericValue(file.read());
	 
	String process= file.readLine();
	
 	 
	int a, b, c, m;
	
	
	String[] proc=process.split("   ");
 
	for(int i=0;i<numProcesses;i++) {
		int x=0;
		String[] proccessEach=proc[i].trim().split(" ");
		 
		a=Integer.parseInt(proccessEach[x]);
		
		b=Integer.parseInt(proccessEach[x+1]);
		c=Integer.parseInt(proccessEach[x+2]);
		m=Integer.parseInt(proccessEach[x+3]);
	
		Process p1=new Process(a,b,c,m,i);
		Process p2=new Process(a,b,c,m,i);
		Process p3=new Process(a,b,c,m,i);
		Process p4=new Process(a,b,c,m,i);
		list1.add(p1);
		list2.add(p2);
		list3.add(p3);
		list4.add(p4);
		
	}


	System.out.println();
	
	FCFS(verbose, list1,numProcesses);
	System.out.println();
	RR(verbose, list2, numProcesses);
	System.out.println();
	SJF(verbose,list3,numProcesses);
	System.out.println();
	HPRN(verbose,list4,numProcesses);
}

public static ArrayList<Process> sortList(ArrayList<Process> in, Process p) {
 
		// TODO Auto-generated method stub
	Process o1=in.get(0);
	int index=0;
	for(int i=1; i<in.size();i++) {
		if(in.get(i).getCycleTracker()>p.getCycleTracker()) {
			index=i;
		}
		else if(in.get(i).getCycleTracker()<p.getCycleTracker()) {
			//o1=in.get
		}
		else if(in.get(i).getCycleTracker()==p.getCycleTracker()){
			if(in.get(i).getA()> p.getA()) {
				index=i;
			}
			else if(in.get(i).getA()==p.getA()) {
				if(in.get(i).getID()>p.getID()) {
					index=i;
				}
				
			}
		}
	}
	in.add(index, p);
		
		return in;
	
}
public  static double ratioTest(Process p, int cycleNum){
	
	//double timeInSys=(cycleNum)-p.getA();
	double runTime=p.getWaitingTime()+p.gettotalTime();
	double denominator= Math.max(1,p.getRuntime());
	double result=runTime/denominator;
		return result;
		

}
}
