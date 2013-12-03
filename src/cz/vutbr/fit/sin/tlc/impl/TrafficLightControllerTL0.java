package cz.vutbr.fit.sin.tlc.impl;


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
	public TLState getState() {
		// TODO Auto-generated method stub
		return null;
	}

}
