package dii.vrp.tp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import dii.vrp.data.IDemands;
import dii.vrp.data.IDistanceMatrix;

public class PilotedClarkeAndWright  {

	
	public PilotedClarkeAndWright(IDistanceMatrix distances,IDemands demands,double capacity){
		this.n=distances.size();
		this.demands=demands;
		this.distances=distances;
		this.capacity = (int)capacity;
		
	}
	/**
	 * The distance matrix
	 */
	private final IDistanceMatrix distances;
	
	//The demands,defined in CMT file
	
	private final IDemands demands;
	
	/**
	 * The starting node
	 */
	private final int initNode =0;
	
	// Max Capacity for one route, defined in CMT file
	private final int capacity;
	
	private final int n;
	
	//PILOT coeeficient
	private  int k;
	
	
	
	// Saving object, used for saving list
	private class Saving implements Comparable<Saving>{
		public Saving(int a,int b, double value) {
			this.a = a;
			this.b=b;
			this.value=value;
		}
		public int getNode1() {
			return a;
		}
		public int getNode2() {
			return b;
		}
		public double getValue() {
			return value;
		}
		
		public int a,b;
		public double value;
		@Override
		public String toString() {
			return ""+a+"|"+b+"|"+value+"\n";
		}
		@Override
		public int compareTo(Saving o) {			
			if (this.value < o.value) {
				return -1;
			}else if(this.value > o.value){
				return 1;
			}else{
				return 0;
			}	
		}
	};
	
	// Comparator for Savings
	static final Comparator<Saving> SAVING_ORDER =new Comparator<Saving>() {	
		public int compare(Saving e1, Saving e2) {
			return e2.compareTo(e1);
		}
	};
	
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	

	public void setInitNode(int i) {
		// TODO Auto-generated method stub
	}


	public ArrayList<VRPRoute> run() {
		//Pilot Parameter, k values look head
		
		//initialise routes, one route by node from init point (tulip)
		ArrayList<VRPRoute> Routes = new ArrayList<>();
		for(int i=0;i<n;i++){
			VRPRoute route = new VRPRoute();
			route.add(initNode);
			route.add(i);
			route.setCost(2*distances.getDistance(i,initNode));
			route.setLoad(demands.getDemand(i));
			route.add(initNode);
			Routes.add(route);
		}		
		// Initialize Savings list
		ArrayList<Saving> Savings = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i!=j){
					double value = -(distances.getDistance(i, j)+
							distances.getDistance(initNode, j)+
							distances.getDistance(initNode, i))+
							((2*distances.getDistance(initNode, j))+
							(2*distances.getDistance(initNode, i)));
					if (value >1){
						Saving data = new Saving(i, j,value );
						Savings.add(data);
					}
				}
			}
		} 
		// Sort Savings List
		Collections.sort(Savings,SAVING_ORDER);
		//main Loop, define routes from init routes and savings list
		for (int i =0 ; i <Savings.size() ; i++) {

			// look head k steps in the saving list to see if a solution is less expensive
			// and starts from the same node as the current saving entry node1
			double tcost=distances.getDistance(Savings.get(i).getNode1(), Savings.get(i).getNode2());
			int swap=i;
			//look head loop
			for (int j = 0; j < k; j++) {
				//check if k next entrie in savings list refers to our current nodes
				if((i+j)<Savings.size() && (Savings.get(i+j).getNode1()==Savings.get(i).getNode1() )){
					//check if that solution would cost less
					double tmp = distances.getDistance(Savings.get(i+j).getNode1(), Savings.get(i+j).getNode2());
					if(tmp<tcost){
						tcost =tmp;
						swap=i+j;
					}
				}		
			}
			
			
			
			//swap the savings list entrie with the better one found in the look head loop
			if(swap!=i){
				Collections.swap(Savings, i, swap);
				// Insertion method, takes more cpu, gives similar results
				// wasnt worth it 
//				Saving tmp = Savings.get(swap);
//				Savings.remove(tmp);
//				Savings.add(i,tmp);
			}
			
			
			//memorise the routes that contains our 2 nodes
			VRPRoute route1 = new VRPRoute();
			VRPRoute route2 = new VRPRoute();
			for (VRPRoute route : Routes) {
				if(route.contains(Savings.get(i).getNode1())){
					route1 = route;
				}
				if(route.contains(Savings.get(i).getNode2())){
					route2 = route;
				}
			}
				
			
			//once we have the routes containing the nodes, we check if they are boths at the end of the routes
			if(route1!=route2&&route1.size()!=0&&route2.size()!=0 &&((route1.get(1)==Savings.get(i).getNode1() &&
				route2.get(route2.size()-2)==Savings.get(i).getNode2())
				||(
				route2.get(1)==Savings.get(i).getNode2() &&
				route1.get(route1.size()-2)==Savings.get(i).getNode1()))){
				//Check if Routes load will be over Capacity or not
				if(route1.getLoad()+route2.getLoad()<=capacity){
					//remove arc going through 0
					if(route1.get(1)==Savings.get(i).getNode1()){
						//remove first node
						route1.remove(0);
						//add route2 to route1
						for (int l =  route2.size()-2; l >=0; l--) {
							route1.insert(route2.get(l), 0);
						}
					}else if(route1.get(route1.size()-2)==Savings.get(i).getNode1()){
						//remove last node
						route1.remove(route1.size()-1);
						//add route2 to route1
						for (int l = 1; l < route2.size(); l++) {
							route1.add(route2.get(l));
						}	
					}
					//update route load
					route1.setLoad(route1.getLoad()+route2.getLoad());
					//update cost
					int cost=0;
					for (int j = 0; j < route1.size()-1; j++) {
						cost +=distances.getDistance(route1.get(j), route1.get(j+1));
					}
					route1.setCost(cost);
					//remove route 2 from routes
					Routes.remove(route2);
				}
			}
		}
		//Display Solution Routes
		System.out.println("Routes");
		for (VRPRoute Route : Routes) {
			System.out.println(Route.toString());
		}
		
		//Display number of nodes
		int cpt=0;
		for (int j = 0; j < Routes.size(); j++) {
			cpt+=Routes.get(j).size()-2;
		}
		System.out.println("Nb Nodes "+cpt);
		
		return Routes;
	}
	
}
