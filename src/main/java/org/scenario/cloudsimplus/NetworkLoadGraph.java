package org.scenario.cloudsimplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.network.switches.Switch;
import org.scenario.cloudsimplus.switches.AdaptedAbstractSwitch;


public class NetworkLoadGraph {
	
	/**
	 * The datacenter network might have many root switches
	 */
	private List<Node> rootNodes;

	private Map<String,Integer> routesWeight;
	
	public NetworkLoadGraph(List<Switch> switchList) {
		rootNodes = new ArrayList<>();
		routesWeight = new HashMap<>();
		for(Switch sw : switchList) {
			if(sw.getLevel() == 0) {
				//assuming we have one root node if not method should note 
				//return after finding first root/core switch
				Node root = new Node(sw.getName(), ((AdaptedAbstractSwitch)sw).historyList.get(((AdaptedAbstractSwitch)sw).historyList.size()-1), sw.getLevel());
				rootNodes.add(root);
				append(sw,root);
				return;
			}
		}
	}
	
	/**
	 * Append a node representing a switch inside a datacenter to the graph 
	 * @param sw switch
	 * @param node node that will be weighted with sw load
	 */
	private void append(Switch sw, Node node) {
		if(sw.getLevel() == 2)
			return;
		for(Switch swi : sw.getDownlinkSwitches()) {
			//assuming we have one root node
			Node child  = new Node(swi.getName(), ((AdaptedAbstractSwitch)swi).historyList.get(((AdaptedAbstractSwitch)swi).historyList.size()-1), swi.getLevel());
			node.children.add(child);
			append(swi,child);
		}
	}
	
	/**
	 * Method for testing
	 */
	public void display() {
		display(rootNodes.get(0));
	}
	
	private void display(Node node) {
		System.out.println("Node "+ node.label + ": "+ node.weight);
		for(Node childNode: node.children) {
			display(childNode);
		}
		if(node.getLevel() == 2)
			return;
	}
	
	public void bestRoute() {
		bestRoute(rootNodes.get(0),rootNodes.get(0).weight);
	}
	
	public void bestRoute(Node node ,int weight) {
		if(node.getLevel() == 2) {
			routesWeight.put(node.getLabel(), node.getWeight() + weight);
			return;
		}
		for(Node childNode: node.children) {
			bestRoute(childNode,childNode.weight+weight);
		}
	}
	
	public void updateGraph(List<Switch> switchList) {
		for(Switch sw : switchList) {
			if(sw.getLevel() == 0) {
				updateGraph(sw, rootNodes.get(0));
				return;
			}
		}
	}
	
	private void updateGraph(Switch sw, Node node) {
		node.weight = ((AdaptedAbstractSwitch)sw).historyList.get(((AdaptedAbstractSwitch)sw).historyList.size()-1);
		Iterator<Switch> it1 = sw.getDownlinkSwitches().iterator();
		Iterator<Node> it2 = node.children.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			updateGraph(it1.next(),it2.next());
		}
		if(node.getLevel() == 2)
			return;
	}
	
	public List<Node> getRootNodes() {
		return rootNodes;
	}

	public void setRootNodes(List<Node> rootNodes) {
		this.rootNodes = rootNodes;
	}

	public Map<String, Integer> getRoutesWeight() {
		return routesWeight;
	}

	public void setRoutesWeight(Map<String, Integer> routesWeight) {
		this.routesWeight = routesWeight;
	}

	public class Node {
		private String label;
		private int level;
		private int weight;
		private List<Node> children;
		  
		  public Node(String label, int weight, int level) {
			  this.label = label;
			  this.weight = weight;
			  this.level = level;
			  children = new ArrayList<>();
		  }

		public String getLabel() {
			return label;
		}
		
		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}		  		  
	}

}
