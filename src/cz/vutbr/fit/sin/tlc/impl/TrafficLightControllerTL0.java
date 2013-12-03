package cz.vutbr.fit.sin.tlc.impl;


import java.io.IOException;

import it.polito.appeal.traci.TLState;
import it.polito.appeal.traci.TrafficLight;
import cz.vutbr.fit.sin.tlc.BaseTrafficLightsControler;

public class TrafficLightControllerTL0 extends BaseTrafficLightsControler {
	
	
	
	public TrafficLightControllerTL0(TrafficLight light) {
		super(light);
	}
	
	public TrafficLightControllerTL0(TrafficLight light,int initTime) {
		super(light,initTime);
	}
	
	@Override
	public void step(){
		super.step();
		try {
			TLState state = mTrafficLight.queryReadState().get();
			System.out.println(state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
