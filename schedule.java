import java.io.*;
import java.util.*;

public class schedule {
	public int numNodes;
	public int numProcs;
	public int procUsed;
	public int currentTime;
	public int totalJobTimes;
	public int[] jobTimeAry;
	public int[][] matrix;
	public int[][] table;
	public node OPEN;
	public node dummy = new node(-1,-1, null);	
	public schedule(int nodes, int procs){
		numNodes = nodes;
		numProcs = procs;
		procUsed = 0;
		currentTime = 0;
		jobTimeAry = new int[numNodes + 1];
		matrix = new int[numNodes + 1][numNodes + 1];
	}
	public void loadMatrix(Scanner inFile) {
		while (inFile.hasNextLine()) {
			String ary[] = inFile.nextLine().split(" ");
			int r = Integer.parseInt(ary[0]);
			int c = Integer.parseInt(ary[1]);
			matrix[r][c]++;
		}
	}
	public void setMatrix() {
		for(int i = 0; i < numNodes + 1; i++) {
			for(int j = 0; j < numNodes + 1; j++) {
				if(matrix[i][j] > 0) {
					matrix[i][0]++;
					matrix[0][j]++;
				}
			}
		}
		matrix[0][0] = numNodes;
		for(int i = 1; i < numNodes + 1; i++) {
			for(int j = 1; j < numNodes + 1; j++) {
				if(i == j) {
					matrix[i][j] = 1;
				}
			}
		}
	}
	public void printOPEN(FileWriter file) throws IOException {
		file.write("\n");
		if(OPEN.next != null) {
			node temp = OPEN.next;
		
		while(temp.next != null) {
			file.write("(" + temp.jobID + ", " + temp.jobTime + ") ->");
			temp = temp.next;
		}
		file.write("(" + temp.jobID + ", " + temp.jobTime +")\n");
		}
	}
	public void printMatrix(FileWriter file) throws IOException {
		for(int i = 0; i < numNodes + 1; i++) {
		for(int j = 0; j < numNodes + 1; j++) {
			int num = matrix[i][j];
			file.write("" + num + " ");
		}file.write("\n");
	}file.write("\n");
	}
	public int loadJobTimeAry(Scanner file) {
		int x = 0;
		int jobID;
		while(file.hasNextLine()) {
			String ary[] = file.nextLine().split(" ");
			jobID = Integer.parseInt(ary[0]);
			jobTimeAry[jobID] = Integer.parseInt(ary[1]);
			x = x + Integer.parseInt(ary[1]);
		}
		return x;
	}
	public void printTable(FileWriter file) throws IOException {
		file.write("============================================================\n");
		file.write("ProcUsed: " + procUsed + " CurrentTime: " + currentTime + "\n");
		file.write("Time: ");
		for(int i = 0; i <= currentTime; i++) {
			file.write("| " + i + " | ");
		}
		file.write("\n" + "------------------------------------------------------------" + "\n");
		for(int i = 1; i < numProcs + 1; i++) {
			file.write("Proc: " + i + " ");
			for(int j = 0; j <= currentTime; j++) {
					file.write("| " + table[i-1][j] + " | ");
			}
			file.write("\n" + "------------------------------------------------------------" + "\n");
		}
	}
	public void fillOPEN(FileWriter file) throws IOException {
		int findOrphan = 0;
		for(int j = 1; j < numNodes + 1; j++) {
			if(matrix[0][j] == 0 && matrix[j][j] == 1) {
				matrix[j][j] = 2;
				findOrphan = j;
				int jobID = findOrphan;
				if(jobID > 0) {
					node newNode = new node(jobID,jobTimeAry[jobID], null);
					OpenInsert(newNode);
					printOPEN(file);
				}
			}
		}
	}
	public node findSpot(node n) {
		node spot = OPEN;
		while(spot.next != null && spot.next.jobTime < n.jobTime) {
			spot = spot.next;
		}
		return spot;
	}
	public void OpenInsert(node n) {
		node spot = findSpot(n);
		n.next = spot.next;
		spot.next = n;
	}
	public int findOrphan() {
		for(int j = 1; j < numNodes + 1; j++) {
			if(matrix[0][j] == 0 && matrix[j][j] == 1) {
				matrix[j][j] = 2;
				return j; //WONT return 1,7,9,10
			}
		}
		return -1;
	}
	public void fillTable() {
		int availProc = getNextProc();
		while(availProc >= 0 && dummy.next != null && procUsed < numProcs) {		
		if(availProc >= 0) {
			node temp = dummy.next;
			dummy.next = temp.next;
			node newJob = temp;
			putJobOnTable(availProc,newJob.jobID,newJob.jobTime);
			if(availProc > procUsed) {
				procUsed++;
					}
			}
		availProc = getNextProc();
		}
	}
	public int getNextProc() {
		for(int i = 0; i < numProcs; i++) {
			if(table[i][currentTime] == 0) {
				table[i][currentTime]= -1;
				return i;
			}
		}
		return -1;
	}
	public void putJobOnTable(int proc, int ID, int jobTime){
		int Time = currentTime;
		int EndTime = Time + jobTime;
		while(Time < EndTime) {
		table[proc][Time] = ID;
		Time++;
		}
	}
	public void deleteDoneJobs(FileWriter file) throws IOException {
		int proc =  0;
		while(proc < procUsed + 1) {
		if(table[proc][currentTime] <= 0 && table[proc][currentTime - 1] > 0) {
			int jobID = table[proc][currentTime - 1];
			deleteJob(jobID);
		}
		printMatrix(file);
		proc++;
		}
	}
	public void deleteJob(int jobID) {
		matrix[jobID][jobID] = 0;
		matrix[0][0]--;
		int j = 1;
		while(j <= numNodes) {
			if(matrix[jobID][j]>0) {
				matrix[0][j]--;
			}
			j++;
		}
	}
	public boolean checkCycle() {
		boolean tableCheck = true;
		for(int i = 0; i < numProcs; i++) {
			if(table[i][currentTime-1] > 0) {
				tableCheck = false;
			}
		}
		if(dummy.next == null && matrix[0][0] != 0 && tableCheck == true) {
			return true;
		}
		return false;
	}
	public boolean isGraphEmpty() {
		if(matrix[0][0]==0) {
			return true;
		}
		else {
		return false;
				}
	}
}
