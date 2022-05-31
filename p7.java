import java.io.*;
import java.util.Scanner;

public class p7 {
	public static void main(String[] args) throws IOException{
		if(args[4] == null)
	    {
	        System.out.println("Need more command line arguments");
	        System.exit(0);
	    }
		String inFile1_Header;
		String inFile1_Path = args[0];
		File inFile1_OBJ = new File(inFile1_Path);
		Scanner inFile1 = new Scanner(inFile1_OBJ); 
		inFile1_Header = inFile1.nextLine();
		String Header_Array[] = inFile1_Header.split(" ");
		int numNodes = Integer.parseInt(Header_Array[0]);
		
		String inFile2_Header;
		String inFile2_Path = args[1];
		File inFile2_OBJ = new File(inFile2_Path);
		Scanner inFile2 = new Scanner(inFile2_OBJ); 
		inFile2_Header = inFile2.nextLine();
		
		String inFile3_Header;
		String inFile3_Path = args[2];
		File inFile3_OBJ = new File(inFile3_Path);
		Scanner inFile3 = new Scanner(inFile3_OBJ);
		inFile3_Header = inFile3.nextLine();
		String Header3_Array[] = inFile3_Header.split(" ");
		int numProcs = Integer.parseInt(Header3_Array[0]);
		
		String O1_path = args[3];
		File O1_OBJ = new File(O1_path);
		FileWriter O1 = new FileWriter(O1_OBJ);
		O1.flush();
		
		String O2_path = args[4];
		File O2_OBJ = new File(O2_path);
		FileWriter O2 = new FileWriter(O2_OBJ);
		O2.flush();
		
		if(numProcs <= 0) {
			System.out.println("need 1 or more processors");
			System.exit(0);
		}
		else if (numProcs > numNodes){
			numProcs = numNodes;
		}
		
		schedule s = new schedule(numNodes, numProcs);
		
		s.loadMatrix(inFile1);
		
		s.setMatrix();

		s.printMatrix(O2);

		s.OPEN = new node(-1,-1,s.dummy);
		
		s.currentTime = 0;
		
		s.procUsed = 0;
		
		s.totalJobTimes = s.loadJobTimeAry(inFile2);

		s.table = new int[numProcs][s.totalJobTimes];
		
		while(s.isGraphEmpty() != true) {
			
			s.printTable(O1);

			s.fillOPEN(O2);

			s.printOPEN(O2);
		
			s.fillTable();

			s.printTable(O1);

			s.currentTime++;
			
			s.deleteDoneJobs(O2);
		
			if(s.checkCycle() == true) {
				System.out.println("need 1 or more processors");
				System.exit(0);
			}
		}
		
		s.printTable(O1);
		
		inFile1.close();
		inFile2.close();
		inFile3.close();
		O1.close();
		O2.close();
	}
}
