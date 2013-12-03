package cz.vutbr.fit.sin.tlc;

import it.polito.appeal.traci.TrafficLight;

public abstract class BaseTrafficLightsControler implements
		ITrafficLightController {
	
	protected TrafficLight mTrafficLight;
	protected int mTime;
	
	public BaseTrafficLightsControler(TrafficLight light){
		mTrafficLight = light;
		mTime = 0;
	}

	public BaseTrafficLightsControler(TrafficLight light, int initTime){
		mTrafficLight = light;
		mTime = initTime;
	}
	
	@Override
	public void step() {
		mTime++;
	}


}
