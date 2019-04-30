package com.forkexec.pts.domain;

public class Credit {
	
	private int value;
	private int tag;
	
	public Credit(){
		this.tag=0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getTag() {
		return tag;
	}

	public void setTag() {
		this.tag = this.tag+1;
	}

}
