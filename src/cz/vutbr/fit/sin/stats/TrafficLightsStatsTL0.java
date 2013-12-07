package cz.vutbr.fit.sin.stats;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import it.polito.appeal.traci.Edge;
import it.polito.appeal.traci.Repository;
import it.polito.appeal.traci.StepAdvanceListener;

public class TrafficLightsStatsTL0 implements StepAdvanceListener {
	
	private Edge mEdgeZid;
	private Edge mEdgeZid0;
	private Edge mEdgeVin;
	private Edge mEdgeVin0;
	private Edge mEdgeMal;
	private Edge mEdgeMal0;
	private int mTime; 
	
	private final List<StatsPoint<Integer>> mZidStat = new LinkedList<StatsPoint<Integer>>();
	private final List<StatsPoint<Integer>> mMalStat = new LinkedList<StatsPoint<Integer>>();
	private final List<StatsPoint<Integer>> mVinStat = new LinkedList<StatsPoint<Integer>>();
	
	
	public TrafficLightsStatsTL0(Repository<Edge> repository, int initTime) {
		try {
			mEdgeMal = repository.getByID("3i");
			mEdgeZid = repository.getByID("2i");
			mEdgeVin = repository.getByID("1i");
			
			mEdgeMal0 = repository.getByID("3i.150");
			mEdgeZid0 = repository.getByID("2i.150");
			mEdgeVin0 = repository.getByID("1i.185");
			
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
		
		try {
			int mal, vin, zid;
			
						
			mal = mEdgeMal.getLastStepVehicleNumber() + mEdgeMal0.getLastStepVehicleNumber();
			
			zid = mEdgeZid.getLastStepVehicleNumber() + mEdgeZid0.getLastStepVehicleNumber();

			vin = mEdgeVin.getLastStepVehicleNumber() + mEdgeVin0.getLastStepVehicleNumber();

			mZidStat.add(new StatsPoint<Integer>(mTime, zid));
			mVinStat.add(new StatsPoint<Integer>(mTime, vin));
			mMalStat.add(new StatsPoint<Integer>(mTime, mal));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTime++;
	}
	
	

	@Override
	public void nextStep(double arg0) {
		step();
	}
}
