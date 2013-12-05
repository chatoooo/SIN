package cz.vutbr.fit.sin.tlc.impl;

import java.io.IOException;

import net.sourceforge.jFuzzyLogic.FIS;
import it.polito.appeal.traci.LightState;
import it.polito.appeal.traci.TLState;
import it.polito.appeal.traci.TrafficLight;
import cz.vutbr.fit.sin.tlc.BaseTrafficLightsControler;

public class TrafficLightControllerTL0 extends BaseTrafficLightsControler {
	
	final String FUZZY_FILE = "fcl/Crossroad.fcl";
	private FIS mFIS;
	private int mPhase = 0;
	private int mDuration = 0;
	
	private static final LightState[][] PHASES = new LightState[][] {
		// phase 0
		new LightState[] {
				LightState.RED,
				LightState.RED,
				LightState.RED,
				LightState.RED,
				LightState.RED,
				LightState.RED,
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL
		},
		// phase 1
		new LightState[] {
				LightState.GREEN_NODECEL,
				LightState.RED,
				LightState.RED,
				LightState.RED,
				LightState.RED,
				LightState.RED,
				LightState.GREEN_NODECEL,
				LightState.YELLOW,
				LightState.YELLOW
		},
		// phase 2
		new LightState[] {
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL,
				LightState.GREEN,
				LightState.GREEN,
				LightState.RED,
				LightState.RED
		},
		// phase 3
		new LightState[] {
				LightState.YELLOW,
				LightState.YELLOW,
				LightState.YELLOW,
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL,
				LightState.GREEN,
				LightState.GREEN,
				LightState.RED,
				LightState.RED
		},
		// phase 4
		new LightState[] {
				LightState.RED,
				LightState.RED,
				LightState.RED,
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL,
				LightState.GREEN_NODECEL,
				LightState.RED,
				LightState.RED
		},
		// phase 5
		new LightState[] {
				LightState.RED,
				LightState.RED,
				LightState.RED,
				LightState.YELLOW,
				LightState.YELLOW,
				LightState.YELLOW,
				LightState.GREEN_NODECEL,
				LightState.RED,
				LightState.RED
		},
	};
	
	public TrafficLightControllerTL0(TrafficLight light) {
		super(light);
		init();
	}
	
	public TrafficLightControllerTL0(TrafficLight light, int initTime) {
		super(light, initTime);
	}
	
	private void init() {
		
		mFIS = FIS.load(FUZZY_FILE);
		
		if(mFIS == null) {
			System.err.println("Soubor s definicí fuzzy nenalezen.");
			return;
		}
		
		setTrafficLights(PHASES[0]);
	}
	
	private void setTrafficLights(LightState[] lightsState) {
		TLState tlState = new TLState(lightsState);
		try {
			mTrafficLight.changeLightsState(tlState);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void step(){
		super.step();
		
		if(mDuration == 0) {			
			switch(mPhase) {
				case 0:
					
					mPhase = 1;
					break;
				case 1:
					mPhase = 2;
					break;
				case 2:
					
					mPhase = 3;
					break;
				case 3:
					mPhase = 4;
					break;
				case 4:
					mPhase = 5;
					break;
				case 5:
					mPhase = 0;
					break;
			}
			mDuration = 5;
			LightState[] lightsState = PHASES[mPhase];
			setTrafficLights(lightsState);
			try {
				TLState state = mTrafficLight.queryReadState().get();
				System.out.println(state);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			mDuration--;
		}
	}

}
