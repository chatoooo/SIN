package cz.vutbr.fit.sin.tlc;

import it.polito.appeal.traci.StepAdvanceListener;
import it.polito.appeal.traci.TrafficLight;

/**
 * Abstraktni trida pro radic svetelne krizovatky.
 */
public abstract class BaseTrafficLightsControler implements
		ITrafficLightController, StepAdvanceListener {
	
	/**
	 * Doba oranzove na semaforu.
	 */
	protected final static int YELLOW_TIME = 3;
	
	/**
	 * Rizena sada semaforu krizovatky.
	 */
	protected TrafficLight mTrafficLight;
	
	/**
	 * Aktualni krok simulace
	 */
	protected int mTime;
	
	/**
	 * @param light Sada rizenych semaforu krizovatky.
	 */
	public BaseTrafficLightsControler(TrafficLight light){
		mTrafficLight = light;
		mTime = 0;
	}

	/**
	 * Konstruktor s pocatecnim casem simulace.
	 * @param light Sada rizenych semaforu krizovatky.
	 * @param initTime Pocatecni cas simulace.
	 */
	public BaseTrafficLightsControler(TrafficLight light, int initTime){
		mTrafficLight = light;
		mTime = initTime;
	}
	
	/**
	 * Provede jeden simulacni krok v rizeni.
	 */
	@Override
	public void step() {
		mTime++;
	}

	/**
	 * Implementace rozhrani StepAdvanceListener pro provedeni kroku simulace.
	 */
	public void nextStep(double arg0){
		step();
	}
}
