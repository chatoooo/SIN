package cz.vutbr.fit.sin.tlc;

import it.polito.appeal.traci.StepAdvanceListener;

/**
 * Rozhrani radice svetelne krizovatky.
 */
public interface ITrafficLightController extends StepAdvanceListener  {
	
	/**
	 * Jeden krok rizeni (simulacni krok) krizovatky.
	 */
	void step();
	
}
