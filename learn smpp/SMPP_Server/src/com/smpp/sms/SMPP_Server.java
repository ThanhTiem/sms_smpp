package com.smpp.sms;

import com.seleniumsoftware.SMPPSim.SMPPSim;

public class SMPP_Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hi, this is a smpp-server :(( ");
		String[] smsArgs = new String[1];
		smsArgs[0] = "conf\\smppsim.props";
		try {
			SMPPSim.main(smsArgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
