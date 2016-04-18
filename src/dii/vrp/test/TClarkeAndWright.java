package dii.vrp.test;

import java.util.ArrayList;

import dii.vrp.data.VRPREPInstanceReader;
import dii.vrp.tp.PilotedClarkeAndWright;
import dii.vrp.tp.VRPRoute;
import dii.vrp.tp.WindowClarkAndWright;

public class TClarkeAndWright {
	public static void main(String[] args){
		//ReadCMT		
		System.out.println("ClarkeAndWright");
		// Change CMT file here 01 ->14
		String path="C:/Users/guillaume coue/Desktop/TPRoutage/tp3/data/cmt/CMT14.xml";
		VRPREPInstanceReader reader = new VRPREPInstanceReader(path);
		

		long start=System.currentTimeMillis();
		PilotedClarkeAndWright cw=new PilotedClarkeAndWright(reader.getDistanceMatrix(),reader.getDemands(),reader.getCapacity("0"));
		// Set the PILOT coeeficient
		cw.setK(40000);
		//Run Piloted Clarke and Wright
		ArrayList<VRPRoute> sol=cw.run();
		
		System.out.println("CPU = "+(System.currentTimeMillis()-start));
		//System.out.println(sol);
		
		//Display result in window, routes details are in Console
		WindowClarkAndWright win = new WindowClarkAndWright(sol,path);
		
	
		
		
	}

}
