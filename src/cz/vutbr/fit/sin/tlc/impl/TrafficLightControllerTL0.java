package cz.vutbr.fit.sin.tlc.impl;


import java.io.IOException;

import it.polito.appeal.traci.InductionLoop;
import it.polito.appeal.traci.Repository;
import it.polito.appeal.traci.TLState;
import it.polito.appeal.traci.TrafficLight;
import cz.vutbr.fit.sin.tlc.BaseTrafficLightsControler;

public class TrafficLightControllerTL0 extends BaseTrafficLightsControler {
	
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
	
	public TrafficLightControllerTL0(TrafficLight light, Repository<InductionLoop> repository) {
		super(light);
		
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

}
