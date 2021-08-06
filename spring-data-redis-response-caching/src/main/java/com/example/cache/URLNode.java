package com.example.cache;

import java.util.ArrayList;
import java.util.List;

public class URLNode {

	private String value;
	
	private boolean isLeaf;
	
	private List<URLNode> children;

	public URLNode(String value) {
		this.value = value;
		this.isLeaf = false;
		this.children = new ArrayList<URLNode>();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<URLNode> getChildren() {
		return children;
	}

	public void setChildren(List<URLNode> children) {
		this.children = children;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public void addChild(URLNode child) {
		this.children.add(child);
	}
	
	public URLNode getChild(String value) {
		for (URLNode child : this.children) {
			if (child.getValue().equals(value)) {
				return child;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return value;
	}
	
	
}
