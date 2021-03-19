package com.gdxsoft.weixin;

public class TestQy {
	QyConfig qy;
	public TestQy() {
		String corpId="wx4bc4a7f9fdf51b9d";
		String corpsecret="V6DVGP3un4_6Dt9jJMepouP4aW-cR0j2g0wlTAjyWQEwERfGVHh1jZeLXEpbklCt";
		
		  qy=QyConfig.instance(corpId, corpsecret); 
	}
	
	public void createDepartment(){
		int a=qy.createWxDepartment("技术部", 1, 1, 20);
		int a1=qy.createWxDepartment("技术部-设计", 20, 1, 30);
		int a2=qy.createWxDepartment("技术部-编码", 20, 10, 40);
		System.out.println(a);
		System.out.println(a1);
		System.out.println(a2);
		
	}

	public static void main(String[] args) {
		TestQy t=new TestQy();
		t.createDepartment();
		

	}

}
