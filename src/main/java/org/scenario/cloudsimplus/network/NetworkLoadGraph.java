package org.scenario.cloudsimplus.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.network.switches.Switch;
import org.scenario.cloudsimplus.network.switches.AdaptedAbstractSwitch;


public class NetworkLoadGraph {
	
	/**
	 * The datacenter network might have many root switches
	 */
	private List<NetworkLoadGraphNode> rootNodes;

	private Map<String,Integer> routesWeight;
	
	public NetworkLoadGraph(List<Switch> switchList) {
		rootNodes = new ArrayList<>();
		routesWeight = new HashMap<>();
		for(Switch sw : switchList) {
			if(sw.getLevel() == 0) {
				//assuming we have one root node if not method should note 
				//return after finding first root/core switch
				NetworkLoadGraphNode root = new NetworkLoadGraphNode(sw.getName(), ((AdaptedAbstractSwitch)sw).historyList.get(((AdaptedAbstractSwitch)sw).historyList.size()-1), sw.getLevel());
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
	private void append(Switch sw, NetworkLoadGraphNode node) {
		if(sw.getLevel() == 2)
			return;
		for(Switch swi : sw.getDownlinkSwitches()) {
			//assuming we have one root node
			NetworkLoadGraphNode child  = new NetworkLoadGraphNode(swi.getName(), ((AdaptedAbstractSwitch)swi).historyList.get(((AdaptedAbstractSwitch)swi).historyList.size()-1), swi.getLevel());
			node.getChildren().add(child);
			append(swi,child);
		}
	}
	
	/**
	 * Method for testing
	 */
	public void display() {
		display(rootNodes.get(0));
	}
	
	private void display(NetworkLoadGraphNode node) {
		System.out.println("Node "+ node.getLabel()+ ": "+ node.getWeight());
		for(NetworkLoadGraphNode childNode: node.getChildren()) {
			display(childNode);
		}
		if(node.getLevel() == 2)
			return;
	}
	
	public void bestRoute() {
		bestRoute(rootNodes.get(0),rootNodes.get(0).getWeight());
	}
	
	public void bestRoute(NetworkLoadGraphNode node ,int weight) {
		if(node.getLevel() == 2) {
			routesWeight.put(node.getLabel(), node.getWeight() + weight);
			return;
		}
		for(NetworkLoadGraphNode childNode: node.getChildren()) {
			bestRoute(childNode,childNode.getWeight()+weight);
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
	
	private void updateGraph(Switch sw, NetworkLoadGraphNode node) {
		node.setWeight(((AdaptedAbstractSwitch)sw).historyList.get(((AdaptedAbstractSwitch)sw).historyList.size()-1));
		Iterator<Switch> it1 = sw.getDownlinkSwitches().iterator();
		Iterator<NetworkLoadGraphNode> it2 = node.getChildren().iterator();
		while (it1.hasNext() && it2.hasNext()) {
			updateGraph(it1.next(),it2.next());
		}
		if(node.getLevel() == 2)
			return;
	}
	
	public List<NetworkLoadGraphNode> getRootNodes() {
		return rootNodes;
	}

	public void setRootNodes(List<NetworkLoadGraphNode> rootNodes) {
		this.rootNodes = rootNodes;
	}

	public Map<String, Integer> getRoutesWeight() {
		return routesWeight;
	}

	public void setRoutesWeight(Map<String, Integer> routesWeight) {
		this.routesWeight = routesWeight;
	}
}
