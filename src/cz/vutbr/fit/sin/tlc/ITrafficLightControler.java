package cz.vutbr.fit.sin.tlc;

import it.polito.appeal.traci.TLState;

public interface ITrafficLightControler {
	
	void incTime();
	TLState getState();
}
