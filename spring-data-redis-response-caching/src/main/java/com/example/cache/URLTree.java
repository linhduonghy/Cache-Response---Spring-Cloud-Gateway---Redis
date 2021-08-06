package com.example.cache;

public class URLTree {

	private URLNode root;

	public URLTree() {
		root = new URLNode(null);
	}

	public URLNode getRoot() {
		return root;
	}
	
	public void insertTree(String[] url) {

		URLNode currentNode = this.root;

		for (int i = 0; i < url.length; ++i) {
			String value = url[i];
			if (value.equals("")) continue;
			URLNode child = currentNode.getChild(value);
			if (child == null) {
				child = new URLNode(value);
				currentNode.addChild(child);
			}
			if (i + 1 == url.length) {
				child.setLeaf(true);
			}

			currentNode = child;
		}
	}

	public boolean checkMatchURL(URLNode node, String[] urls, int i) {
		
		if (i == urls.length) {
			return false;
		}
		
		if (node == null) {
			return false;
		}
		
		System.err.print("Value:" + urls[i]+",Node:"+node.getValue()+",");
		
		String value = urls[i];

		URLNode child = node.getChild(value);
		URLNode wildCardChild = node.getChild("*");
		
		System.err.println("Child:" +child + "," + wildCardChild);
		
		// if exist leaf node *
		if (wildCardChild != null && wildCardChild.isLeaf()) {			
			System.err.println("wildcard *");
			return true;
		}
		
		if (i == urls.length - 1) { // end value 
			System.err.println("end");			
			if (child != null && child.isLeaf()) {
				System.err.println(child);
				return true;
			}
		}
		
		boolean c1 = checkMatchURL(child, urls, i + 1);
		boolean c2 = checkMatchURL(wildCardChild, urls, i + 1);
		
		boolean c3 = false; // /a/*/b  match /a/1/2/b  -> * = 1/2
		if (node.getValue() != null && node.getValue().equals("*")) {
			c3 = checkMatchURL(node, urls, i + 1);
		}
		return c1 || c2 || c3;		
	}
}
