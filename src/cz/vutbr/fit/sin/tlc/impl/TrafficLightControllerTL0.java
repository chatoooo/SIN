package cz.vutbr.fit.sin.tlc.impl;

import java.io.IOException;

import net.sourceforge.jFuzzyLogic.FIS;
import it.polito.appeal.traci.LightState;
import it.polito.appeal.traci.InductionLoop;
import it.polito.appeal.traci.Repository;
import it.polito.appeal.traci.TLState;
import it.polito.appeal.traci.TrafficLight;
import cz.vutbr.fit.sin.tlc.BaseTrafficLightsControler;

public class TrafficLightControllerTL0 extends BaseTrafficLightsControler {
	
	final String FUZZY_FILE = "fcl/Crossroad.fcl";
	private FIS mFIS;
	private int mPhase = 0;
	private int mDuration = 0;
	
	private InductionLoop mLoop1_1_20;
	private InductionLoop mLoop1_1_50;
	private InductionLoop mLoop1_1_100;
	
	private InductionLoop mLoop1_2_20;
	private InductionLoop mLoop2_0_20;
	
	private InductionLoop mLoop2_1_20;
	private InductionLoop mLoop2_1_50;
	private InductionLoop mLoop2_1_100;
	
	private InductionLoop mLoop2_2_20;
	private InductionLoop mLoop2_2_50;
	private InductionLoop mLoop2_2_100;
	
	private InductionLoop mLoop3_2_20;
	
	private InductionLoop mLoop3_1_20;
	private InductionLoop mLoop3_1_50;
	private InductionLoop mLoop3_1_100;
	
	private InductionLoop mLoop3_0_20;
	private InductionLoop mLoop3_0_50;
	private InductionLoop mLoop3_0_100;
	
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
	
	public TrafficLightControllerTL0(TrafficLight light, Repository<InductionLoop> repository) {
		super(light);
		init();
		
		try {
			mLoop1_1_20 = repository.getByID("loop1_1_20");
			mLoop1_1_50 = repository.getByID("loop1_1_50");
			mLoop1_1_100 = repository.getByID("loop1_1_100");
			mLoop1_2_20 = repository.getByID("loop1_2_20");
			
			mLoop2_0_20 = repository.getByID("loop2_0_20");
			mLoop2_1_20 = repository.getByID("loop2_1_20");
			mLoop2_1_50 = repository.getByID("loop2_1_50");
			mLoop2_1_100 = repository.getByID("loop2_1_100");
			mLoop2_2_20 = repository.getByID("loop2_3_20");
			mLoop2_2_50 = repository.getByID("loop2_3_50");
			mLoop2_2_100 = repository.getByID("loop2_3_100");
			
			mLoop3_0_20 = repository.getByID("loop3_0_20");
			mLoop3_0_50 = repository.getByID("loop3_0_50");
			mLoop3_0_100 = repository.getByID("loop3_0_100");
			mLoop3_1_20 = repository.getByID("loop3_1_20");
			mLoop3_1_50 = repository.getByID("loop3_1_50");
			mLoop3_1_100 = repository.getByID("loop3_1_100");
			mLoop3_2_20 = repository.getByID("loop3_2_20");

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	private int get3iQueLen(){
		return 0;
	}
	
	private int get2iQueLen(){
		return 0;
	}
	
    private int get2ito1oQueLen(){
    	return 0;
	}
	
	private int get3ito1oQueLen(){
		return 0;
	}
	
	private int get1iQueLen(){
		return 0;
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
