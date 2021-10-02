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
				
		if (i == urls.length || node == null) {
			return false;
		}
		
		System.err.print("Value:" + urls[i]+",Node:"+node.getValue()+",");
		
		String value = urls[i];

		URLNode child = node.getChild(value);
		URLNode wildCardChild = node.getChild("*");
		
		System.err.println("Child:" +child + "," + wildCardChild);
		
		if (wildCardChild != null) { // exist wildcard node has value equal current path		
			if (wildCardChild.isLeaf()) { // if exist leaf node *
				System.err.println("wildcard *");
				return true;
			}
			if (checkMatchURL(wildCardChild, urls,  i + 1)) { // continue with subtree wildcard
				return true;
			}
		}
		
		if (child != null) { // exist child node has value equal current path
			if (child.isLeaf() && i == urls.length - 1) {
				return true;
			}
			if (checkMatchURL(child, urls, i)) { // continue with subtree
				return true;
			}
		}
		return false;
	}
}
