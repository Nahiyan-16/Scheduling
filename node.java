
public class node {
	int jobID;
	int jobTime;
	node next;
	public node(int ID, int time, node n) {
		jobID = ID;
		jobTime = time;
		next = n;
	}
}
