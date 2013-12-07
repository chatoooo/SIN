package cz.vutbr.fit.sin.tlc;

import it.polito.appeal.traci.StepAdvanceListener;


public interface ITrafficLightController extends StepAdvanceListener  {
	
	void step();
	
}
