package cz.vutbr.fit.sin.tlc.impl;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.*;
import it.polito.appeal.traci.LightState;
import it.polito.appeal.traci.InductionLoop;
import it.polito.appeal.traci.Repository;
import it.polito.appeal.traci.TLState;
import it.polito.appeal.traci.TrafficLight;
import cz.vutbr.fit.sin.tlc.BaseTrafficLightsControler;

public class TrafficLightControllerTL0 extends BaseTrafficLightsControler {
	
	final static int MOVING_AVG_VALUES = 30;
	final static String FUZZY_FILE = "fcl/Crossroad.fcl";
	private FIS mFis;
	private int mPhase = 0;
	private int mDuration = 0;
	
	private boolean mEnabled = true;
	
	private InductionLoop mLoop1_1_20;
	private InductionLoop mLoop1_1_50;
	private InductionLoop mLoop1_1_100;	
	private InductionLoop mLoop1_2_20;
	
	private InductionLoop mLoop2_0_20;
	private InductionLoop mLoop2_0_50;
	private InductionLoop mLoop2_0_100;
	private InductionLoop mLoop2_1_20;
	private InductionLoop mLoop2_1_50;
	private InductionLoop mLoop2_1_100;
	private InductionLoop mLoop2_2_20;
	
	private InductionLoop mLoop3_0_20;
	private InductionLoop mLoop3_0_50;
	private InductionLoop mLoop3_0_100;
	private InductionLoop mLoop3_1_20;
	private InductionLoop mLoop3_1_50;
	private InductionLoop mLoop3_1_100;
	private InductionLoop mLoop3_2_20;
	
	private int mCount = 0;
	
	private Queue<Double> mLoop1_1_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop1_1_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop1_1_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop1_2_20Occupancy = new ArrayDeque<Double>();
	
	private Queue<Double> mLoop2_0_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_0_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_0_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_1_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_1_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_1_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_2_20Occupancy = new ArrayDeque<Double>();
	
	private Queue<Double> mLoop3_0_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_0_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_0_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_1_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_1_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_1_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_2_20Occupancy = new ArrayDeque<Double>();
	
	private static final LightState[][] PHASES = new LightState[][] {
		// phase 0 - vinToZid - green
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
		// phase 1 - vinToZid - yellow, zidToVin - green
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
		// phase 2 - zidToMal - green, malToZid - green
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
		// phase 3 - zidToMal - yellow, malToZid - green, malToVin - green 
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
		// phase 4 - zidToMal - red, malToZid - green, malToVin - green
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
		// phase 5 - zidToMal - red, malToZid - yellow
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
		initFuzzy();
		initLoops(repository);
		
	}
	
	public TrafficLightControllerTL0(TrafficLight light, Repository<InductionLoop> repository, int initTime) {
		super(light, initTime);
		initFuzzy();
		initLoops(repository);
	}
	
	public void enable(){
		mEnabled = true;
	}
	
	public void disable(){
		mEnabled = false;
	}
	
	private void initLoops(Repository<InductionLoop> repository){
		try {
			mLoop1_1_20 = repository.getByID("loop1_1_20");
			mLoop1_1_50 = repository.getByID("loop1_1_50");
			mLoop1_1_100 = repository.getByID("loop1_1_100");
			mLoop1_2_20 = repository.getByID("loop1_2_20");
			
			mLoop2_0_20 = repository.getByID("loop2_0_20");
			mLoop2_0_50 = repository.getByID("loop2_0_50");
			mLoop2_0_100 = repository.getByID("loop2_0_100");
			mLoop2_1_20 = repository.getByID("loop2_1_20");
			mLoop2_1_50 = repository.getByID("loop2_1_50");
			mLoop2_1_100 = repository.getByID("loop2_1_100");
			mLoop2_2_20 = repository.getByID("loop2_2_20");
			
			mLoop3_0_20 = repository.getByID("loop3_0_20");
			mLoop3_0_50 = repository.getByID("loop3_0_50");
			mLoop3_0_100 = repository.getByID("loop3_0_100");
			mLoop3_1_20 = repository.getByID("loop3_1_20");
			mLoop3_1_50 = repository.getByID("loop3_1_50");
			mLoop3_1_100 = repository.getByID("loop3_1_100");
			mLoop3_2_20 = repository.getByID("loop3_2_20");		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initFuzzy() {	
		mFis = FIS.load(FUZZY_FILE);
		
		if(mFis == null) {
			System.err.println("Soubor s definic� fuzzy nenalezen.");
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
	
	private static double movingAverange(Queue<Double> queue) {
		double sum = 0;
		for(Double d : queue) {
			if(d > 0) {
				sum += d;
			}
		}
		
		return sum/MOVING_AVG_VALUES;
	}
	
	private void recordOccupancy() {
		if(mCount == MOVING_AVG_VALUES) {
			mLoop1_1_20Occupancy.remove();
			mLoop1_1_50Occupancy.remove();
			mLoop1_1_100Occupancy.remove();
			mLoop1_2_20Occupancy.remove();
			
			mLoop2_0_20Occupancy.remove();
			mLoop2_0_50Occupancy.remove();
			mLoop2_0_100Occupancy.remove();
			mLoop2_1_20Occupancy.remove();
			mLoop2_1_50Occupancy.remove();
			mLoop2_1_100Occupancy.remove();
			mLoop2_2_20Occupancy.remove();
			
			mLoop3_0_20Occupancy.remove();
			mLoop3_0_50Occupancy.remove();
			mLoop3_0_100Occupancy.remove();
			mLoop3_1_20Occupancy.remove();
			mLoop3_1_50Occupancy.remove();
			mLoop3_1_100Occupancy.remove();
			mLoop3_2_20Occupancy.remove();
			
			mCount--;
		}
		try {
			mLoop1_1_20Occupancy.add(new Double(mLoop1_1_20.getLastStepOccupancy()));
			mLoop1_1_50Occupancy.add(new Double(mLoop1_1_50.getLastStepOccupancy()));
			mLoop1_1_100Occupancy.add(new Double(mLoop1_1_100.getLastStepOccupancy()));
			mLoop1_2_20Occupancy.add(new Double(mLoop1_2_20.getLastStepOccupancy()));
			
			mLoop2_0_20Occupancy.add(new Double(mLoop2_0_20.getLastStepOccupancy()));
			mLoop2_0_50Occupancy.add(new Double(mLoop2_0_50.getLastStepOccupancy()));
			mLoop2_0_100Occupancy.add(new Double(mLoop2_0_100.getLastStepOccupancy()));
			mLoop2_1_20Occupancy.add(new Double(mLoop2_1_20.getLastStepOccupancy()));
			mLoop2_1_50Occupancy.add(new Double(mLoop2_1_50.getLastStepOccupancy()));
			mLoop2_1_100Occupancy.add(new Double(mLoop2_1_100.getLastStepOccupancy()));
			mLoop2_2_20Occupancy.add(new Double(mLoop2_2_20.getLastStepOccupancy()));
			
			mLoop3_0_20Occupancy.add(new Double(mLoop3_0_20.getLastStepOccupancy()));
			mLoop3_0_50Occupancy.add(new Double(mLoop3_0_50.getLastStepOccupancy()));
			mLoop3_0_100Occupancy.add(new Double(mLoop3_0_100.getLastStepOccupancy()));
			mLoop3_1_20Occupancy.add(new Double(mLoop3_1_20.getLastStepOccupancy()));
			mLoop3_1_50Occupancy.add(new Double(mLoop3_1_50.getLastStepOccupancy()));
			mLoop3_1_100Occupancy.add(new Double(mLoop3_1_100.getLastStepOccupancy()));
			mLoop3_2_20Occupancy.add(new Double(mLoop3_2_20.getLastStepOccupancy()));
			
			mCount++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private double get3iOccupancy() {
	    double occupancy3_0_20 = movingAverange(mLoop3_0_20Occupancy);
	    double occupancy3_0_50 = movingAverange(mLoop3_0_50Occupancy);
	    double occupancy3_0_100 = movingAverange(mLoop3_0_100Occupancy);
	    double occupancy3_1_20 = movingAverange(mLoop3_0_20Occupancy);
	    double occupancy3_1_50 = movingAverange(mLoop3_0_50Occupancy);
	    double occupancy3_1_100 = movingAverange(mLoop3_0_100Occupancy);
	    
	    double occupancy3_0 = occupancy3_0_20 + occupancy3_0_50 + occupancy3_0_100;
	    double occupancy3_1 = occupancy3_1_20 + occupancy3_1_50 + occupancy3_1_100;
		
		return (occupancy3_0 + occupancy3_1) / 6;
	}
	
	private double get2iOccupancy() {
		double occupancy2_0_20 = movingAverange(mLoop2_0_20Occupancy);
	    double occupancy2_0_50 = movingAverange(mLoop2_0_50Occupancy);
	    double occupancy2_0_100 = movingAverange(mLoop2_0_100Occupancy);
	    double occupancy2_1_20 = movingAverange(mLoop2_1_20Occupancy);
	    double occupancy2_1_50 = movingAverange(mLoop2_1_50Occupancy);
	    double occupancy2_1_100 = movingAverange(mLoop2_1_100Occupancy);
	    
	    double occupancy2_0 = occupancy2_0_20 + occupancy2_0_50 + occupancy2_0_100;
	    double occupancy2_1 = occupancy2_1_20 + occupancy2_1_50 + occupancy2_1_100;
		
		return (occupancy2_0 + occupancy2_1) / 6;
	}
	
    private double get2ito1oOccupancy() {
    	double occupancy2_0_50 = movingAverange(mLoop2_0_50Occupancy);
	    double occupancy2_0_100 = movingAverange(mLoop2_0_100Occupancy);
	    double occupancy2_1_50 = movingAverange(mLoop2_1_50Occupancy);
	    double occupancy2_1_100 = movingAverange(mLoop2_1_100Occupancy);
	    double occupancy2_2_20 = movingAverange(mLoop2_2_20Occupancy);
	    
	    double occupancy2_0 = occupancy2_0_50 + occupancy2_0_100;
	    double occupancy2_1 = occupancy2_1_50 + occupancy2_1_100;
	    
	    return (occupancy2_2_20 + occupancy2_0 + occupancy2_1) / 5;
	}
	
	private double get3ito1oOccupancy() {
	    double occupancy3_0_50 = movingAverange(mLoop3_0_50Occupancy);
	    double occupancy3_0_100 = movingAverange(mLoop3_0_100Occupancy);
	    double occupancy3_1_50 = movingAverange(mLoop3_0_50Occupancy);
	    double occupancy3_1_100 = movingAverange(mLoop3_0_100Occupancy);
	    double occupancy3_2_20 = movingAverange(mLoop3_2_20Occupancy);
	    
	    double occupancy3_0 = occupancy3_0_50 + occupancy3_0_100;
	    double occupancy3_1 = occupancy3_1_50 + occupancy3_1_100;
		
		return (occupancy3_2_20 + occupancy3_0 + occupancy3_1) / 5;
	}
	
	private double get1iOccupancy(){
		double occupancy1_1_20 = movingAverange(mLoop1_1_20Occupancy);
	    double occupancy1_1_50 = movingAverange(mLoop1_1_50Occupancy);
	    double occupancy1_1_100 = movingAverange(mLoop1_1_100Occupancy);
	    double occupancy1_2_20 = movingAverange(mLoop1_2_20Occupancy);
		
	    double occupancy1_1_20_avg = (occupancy1_1_20 + occupancy1_2_20) / 2;
	    
		return (occupancy1_1_20_avg + occupancy1_1_50 + occupancy1_1_100) / 3;
	}
	
	@Override
	public void step(){
		super.step();
		
		if(!mEnabled){
			return;
		}
		recordOccupancy();
		
		if(mDuration == 0) {
			// Set inputs for Fuzzyfication
	        mFis.setVariable("zidToMal", get2iOccupancy());
	        mFis.setVariable("malToZid", get3iOccupancy());
	        mFis.setVariable("vinToZid", get1iOccupancy());
			
	        // Evaluate Fuzzy
	        mFis.evaluate();
	        
	        double vZidToMal, vMalToZid, vVinToZid;
	        
			switch(mPhase) {
				case 0:	// vinToZid - green
					mDuration = 3;
					mPhase = 1;
					break;
				case 1: // vinToZid - yellow, zidToVin - green
					vZidToMal = mFis.getVariable("gZidToMal").defuzzify();
					vMalToZid = mFis.getVariable("gMalToZid").defuzzify();
					
					if(vZidToMal > vMalToZid) {
						mDuration = (int)vZidToMal;
					}
					else {
						mDuration = (int)vMalToZid;
					}
					
					mPhase = 2;
					break;
				case 2:	// zidToMal - green, malToZid - green
					mDuration = 3;
					mPhase = 3;
					break;
				case 3: // zidToMal - yellow, malToZid - green, malToVin - green 
					vMalToZid = mFis.getVariable("gMalToZid").defuzzify();
					mDuration = (int)(vMalToZid * 0.2);
					if(mDuration == 0) {
						mDuration = 1;
					}
					mPhase = 4;
					break;
				case 4:	// zidToMal - red,  malToZid - green, malToVin - green
					mDuration = 3;
					mPhase = 5;
					break;
				case 5: // zidToMal - red, malToZid - yellow
					vVinToZid = mFis.getVariable("gVinToZid").defuzzify();
					mDuration = (int)vVinToZid;
					mPhase = 0;
					break;
			}
			LightState[] lightsState = PHASES[mPhase];
			setTrafficLights(lightsState);
			
//			try {
//				TLState state = mTrafficLight.queryReadState().get();
//				System.out.println(state);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		else {
			mDuration--;
		}
	}

}
