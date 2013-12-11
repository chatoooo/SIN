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

	/**
	 * Port pro komunikaci se simulatorem SUMO
	 */
	private final int SUMO_PORT = 56789;
	
	/**
	 * Proces simulatoru SUMO.
	 */
	private Process mSumoProcess;
	
	/**
	 * Priznak spusteni simulatoru SUMO s GUI.
	 */
	private boolean mGui;
	
	/**
	 * Stastika staticky rizene krizovatky.
	 */
	private TrafficLightsStatsTL0 mStaticStats;
	
	/**
	 * Stastika krizovatky rizene fuzzy.
	 */
	private TrafficLightsStatsTL0 mIntelligentStats;
	
	/**
	 * Logovani informaci
	 */
	private static final Logger mLog = Logger.getLogger(SumoTraciConnection.class);
	
	/**
	 * @param gui Priznak spusteni SUMO s GUI.
	 */
	public SimulationRunner(boolean gui){
		mGui = gui;
	}
	
	/**
	 * Provedeni simulace krizovatky rizene fuzzy.
	 */
	public void runControlled() {
		if(startSumo()) {
			try {
				// spojeni se SUMO simulatorem
				SumoTraciConnection con = new SumoTraciConnection(InetAddress.getLoopbackAddress(), SUMO_PORT);
				
				// ziskani sady rizenych semaforu
				TrafficLight lights = con.getTrafficLightRepository().getByID("0");
				// repozitar indukcnich smycek
				Repository<InductionLoop> loops = con.getInductionLoopRepository();
				
				// vyroreni radice krizovatky
				ITrafficLightController controler = new TrafficLightControllerTL0(lights, loops);
				mIntelligentStats = new TrafficLightsStatsTL0(con.getEdgeRepository(),0);
				
				con.addStepAdvanceListener(mIntelligentStats);
				con.addStepAdvanceListener(controler);
				// provadeni simulace
				for(int i = 0;i<3600;i++){
					con.nextSimStep();
					processBuffers();
				}
				con.removeStepAdvanceListener(mIntelligentStats);
				con.removeStepAdvanceListener(controler);
				con.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			stopSumo();
		}
		
	}
	
	/**
	 * Provedeni simulace rizene krizovatky.
	 */
	public void runStatic() {
		if(startSumo()) {
			try {
				SumoTraciConnection con = new SumoTraciConnection(InetAddress.getLoopbackAddress(), SUMO_PORT);
				mStaticStats = new TrafficLightsStatsTL0(con.getEdgeRepository(),0);
				
				con.addStepAdvanceListener(mStaticStats);
				for(int i = 0;i<3600;i++){
					con.nextSimStep();
					processBuffers();
				}
				con.removeStepAdvanceListener(mStaticStats);
				con.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			stopSumo();
		}
		
	}
	
	private void processBuffers(){
		
	}
	
	/**
	 * Ziska statistiku staticky rizene krizovatky.
	 * @return Statistika o fixnim rizeni krizovatky.
	 */
	public TrafficLightsStatsTL0 getStatsIntelligent(){
		return mIntelligentStats;
	}
	
	/**
	 * Ziska statistiku fuzzy rizene krizovatky.
	 * @return Statistika o fuzzy rizeni krizovatky.
	 */
	public TrafficLightsStatsTL0 getStatsStatic(){
		return mStaticStats;
	}
	
	/**
	 * Spusteni simulatoru SUMO.
	 * @return True - uspesne spusteni, jinak false
	 */
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
	
	/**
	 * Ukonceni procesu simulatoru SUMO.
	 */
	private void stopSumo() {
		try {
			mSumoProcess.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
