package dii.vrp.tp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom2.Document;
import org.jdom2.Element;

import dii.vrp.util.XMLParser;

public class WindowClarkAndWright {
	
	private JFrame mainWindow;
	private ArrayList<VRPRoute> routes;
	private Document xml;
	private double[][] coordinates;
	
	public WindowClarkAndWright(ArrayList<VRPRoute> routes,String path) {
		this.routes = routes;
		this.xml=XMLParser.parse(path);
		initCoords();
		initWindow();
		
	}
	
	private  void initWindow(){
		mainWindow = new JFrame("Clark and Wright");
		
		mainPanel main = new mainPanel();
		main.setLayout(new FlowLayout());
		main.add(new JLabel(routes.size()-1+" different Routes"));
		double cost=0;
		for (VRPRoute route : routes) {
			cost +=route.getCost();
		}
		main.add(new JLabel(" | Total Cost :"+cost));
		mainWindow.add(main);
		mainWindow.pack();
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
	private  class mainPanel extends JPanel {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int ratio = 10;
		
		@Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            addRoutes(g);
            addNodes(g);
        }
		
		 public void addNodes(Graphics g) {
	           for (int i = 0; i < routes.size(); i++) {
	        	   for (int j = 0; j < routes.get(i).size(); j++) {
	        		   g.setColor(Color.red);
	        		   g.drawOval((int)coordinates[routes.get(i).get(j)][0]*ratio-2, (int)coordinates[routes.get(i).get(j)][1]*ratio-2, 4, 4);
	        		   g.setColor(Color.blue);
	        		   g.drawString(routes.get(i).get(j)+"", (int)coordinates[routes.get(i).get(j)][0]*ratio, (int)coordinates[routes.get(i).get(j)][1]*ratio+15);
	        	   }
	           }
	        }
		
        public void addRoutes(Graphics g) {
           for (int i = 0; i < routes.size(); i++) {
        	   for (int j = 0; j < routes.get(i).size()-1; j++) {
        		   Graphics2D g2 = (Graphics2D) g;
        		   g2.setStroke(new BasicStroke(3));
        		   g2.draw(new Line2D.Float((int)coordinates[routes.get(i).get(j)][0]*ratio, (int)coordinates[routes.get(i).get(j)][1]*ratio, (int)coordinates[routes.get(i).get(j+1)][0]*ratio, (int)coordinates[routes.get(i).get(j+1)][1]*ratio));
        		   //g.drawLine((int)coordinates[routes.get(i).get(j)][0]*ratio, (int)coordinates[routes.get(i).get(j)][1]*ratio, (int)coordinates[routes.get(i).get(j+1)][0]*ratio, (int)coordinates[routes.get(i).get(j+1)][1]*ratio);
        	   }
           }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 1000);
        }
    }

	private void initCoords(){
		Element nodes=xml.getRootElement().getChild("network").getChild("nodes");

		coordinates=new double[nodes.getChildren().size()][2];
		Iterator<Element> it=nodes.getChildren().iterator();
		while(it.hasNext()){
			Element node=it.next();
			int type=Integer.valueOf(node.getAttributeValue("type"));
			int id=type==0?0:Integer.valueOf(node.getAttributeValue("id"));
			Element cx=node.getChild("cx");
			Element cy=node.getChild("cy");
			coordinates[id][0]=Double.valueOf(cx.getValue());
			coordinates[id][1]=Double.valueOf(cy.getValue());
		}
	}
	
}
