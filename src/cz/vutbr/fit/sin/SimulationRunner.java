package cz.vutbr.fit.sin;

import it.polito.appeal.traci.InductionLoop;
import it.polito.appeal.traci.Repository;
import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.TrafficLight;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import cz.vutbr.fit.sin.stats.TrafficLightsStatsTL0;
import cz.vutbr.fit.sin.tlc.ITrafficLightController;
import cz.vutbr.fit.sin.tlc.impl.TrafficLightControllerTL0;

public class SimulationRunner {

	private final int SUMO_PORT = 56789;
	private Process mSumoProcess;
	
	private boolean mGui;
	
	private TrafficLightsStatsTL0 mStaticStats;
	private TrafficLightsStatsTL0 mIntelligentStats;
	
	private static final Logger mLog = Logger.getLogger(SumoTraciConnection.class);
	
	public SimulationRunner(boolean gui){
		mGui = gui;
	}
	
	public void runControlled() {
		if(startSumo()) {
			try {
				SumoTraciConnection con = new SumoTraciConnection(InetAddress.getLoopbackAddress(), SUMO_PORT);
				
				TrafficLight lights = con.getTrafficLightRepository().getByID("0");
				Repository<InductionLoop> loops = con.getInductionLoopRepository();
				ITrafficLightController controler = new TrafficLightControllerTL0(lights, loops);
				mIntelligentStats = new TrafficLightsStatsTL0(con.getEdgeRepository(),0);
				
				con.addStepAdvanceListener(mIntelligentStats);
				con.addStepAdvanceListener(controler);
				for(int i = 0;i<2700;i++){
					con.nextSimStep();
					processBuffers();
				}
				con.removeStepAdvanceListener(mIntelligentStats);
				con.removeStepAdvanceListener(controler);
				con.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			stopSumo();
		}
		
	}
	
	
	public void runStatic() {
		if(startSumo()) {
			try {
				SumoTraciConnection con = new SumoTraciConnection(InetAddress.getLoopbackAddress(), SUMO_PORT);
				mStaticStats = new TrafficLightsStatsTL0(con.getEdgeRepository(),0);
				
				con.addStepAdvanceListener(mStaticStats);
				for(int i = 0;i<2700;i++){
					con.nextSimStep();
					processBuffers();
				}
				con.removeStepAdvanceListener(mStaticStats);
				con.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			stopSumo();
		}
		
	}
	
	private void processBuffers(){
		
	}
	
	public TrafficLightsStatsTL0 getStatsIntelligent(){
		return mIntelligentStats;
	}
	
	public TrafficLightsStatsTL0 getStatsStatic(){
		return mStaticStats;
	}
	
	private boolean startSumo() {
		
		String[] argsSumo;
		if(mGui){
			argsSumo = new String[] { 
				"sumo/sumo-0.19.0/bin/sumo-gui", 
				"-c", "sumo/sim.sumocfg", 
				"--remote-port", Integer.toString(SUMO_PORT)
				};
		}else{
			
			argsSumo = new String[] { 
					"sumo/sumo-0.19.0/bin/sumo", 
					"-c", "sumo/sim.sumocfg", 
					"--remote-port", Integer.toString(SUMO_PORT),
					};
		}
		
		try {
			mSumoProcess = Runtime.getRuntime().exec(argsSumo);
			
			StreamLogger errStreamLogger = new StreamLogger(mSumoProcess.getErrorStream(), "SUMO-err:", mLog);
			StreamLogger outStreamLogger = new StreamLogger(mSumoProcess.getInputStream(), "SUMO-out:", mLog);
			new Thread(errStreamLogger, "StreamLogger-SUMO-err").start();
			new Thread(outStreamLogger, "StreamLogger-SUMO-out").start();
            
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private void stopSumo() {
		try {
			mSumoProcess.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
