package com.forkexec.pts.domain;

public class Credit {
	
	private int tag;
	private int val;
	

	public Credit() {
		setTag(1);
		setVal(0);
		

	}
	
	public int getTag() {
		return tag;
	}

	public void setTag(int maxTag) {
		this.tag = maxTag;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int maxVal) {
		this.val = maxVal;
	}

	
	

}
