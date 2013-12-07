package cz.vutbr.fit.sin.stats;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import it.polito.appeal.traci.Edge;
import it.polito.appeal.traci.Repository;
import it.polito.appeal.traci.StepAdvanceListener;

public class TrafficLightsStatsTL0 implements StepAdvanceListener {
	
	private Edge mEdgeZid;
	private Edge mEdgeVin;
	private Edge mEdgeMal;
	private int mTime; 
	
	private final List<StatsPoint<Integer>> mZidStat = new LinkedList<StatsPoint<Integer>>();
	private final List<StatsPoint<Integer>> mMalStat = new LinkedList<StatsPoint<Integer>>();
	private final List<StatsPoint<Integer>> mVinStat = new LinkedList<StatsPoint<Integer>>();
	
	
	public TrafficLightsStatsTL0(Repository<Edge> repository, int initTime) {
		try {
			mEdgeMal = repository.getByID("3i");
			mEdgeZid = repository.getByID("2i");
			mEdgeVin = repository.getByID("1i");
			mTime = initTime;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<StatsPoint<Integer>> getZid(){
		return mZidStat;
	}
	
	public List<StatsPoint<Integer>> getVin(){
		return mVinStat;
	}
	
	public List<StatsPoint<Integer>> getMal(){
		return mMalStat;
	}
	
	public void step(){
		mTime++;
		try {
			int malMax, vinMax, zidMax;
			
						
			malMax = mEdgeMal.getLastStepVehicleNumber();
			
			zidMax = mEdgeZid.getLastStepVehicleNumber();

			vinMax = mEdgeVin.getLastStepVehicleNumber();

			mZidStat.add(new StatsPoint<Integer>(mTime-1, zidMax));
			mVinStat.add(new StatsPoint<Integer>(mTime-1, vinMax));
			mMalStat.add(new StatsPoint<Integer>(mTime-1, malMax));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void nextStep(double arg0) {
		step();
	}
}
