package com.ai.commonUtils.frontParseUtils;

public class FrontParseUtils {
	
	@Override
	public String toString(){
		return "<span style=\\"+"color:#ff9445\\"+">lixj5<\\/span>";
	}
	
	public static void main(String[] args) {
		FrontParseUtils d = new FrontParseUtils();
		System.out.println(d.toString());
	}

}
