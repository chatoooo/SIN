package cz.vutbr.fit.sin;

import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.TrafficLight;

import java.io.IOException;
import java.net.InetAddress;

import cz.vutbr.fit.sin.tlc.ITrafficLightController;
import cz.vutbr.fit.sin.tlc.impl.TrafficLightControllerTL0;

public class SimulationRunner {

	private final int SUMO_PORT = 56789;
	private Process mSumoProcess;
	
	public void run() {
		if(startSumo()) {
			try {
				SumoTraciConnection con = new SumoTraciConnection(InetAddress.getLoopbackAddress(), SUMO_PORT);
				TrafficLight lights = con.getTrafficLightRepository().getByID("0");
				ITrafficLightController controler = new TrafficLightControllerTL0(lights);
					
				for(int i = 0;i<900;i++){
					con.nextSimStep();
					controler.step();
				}
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
	
	private boolean startSumo() {
		String[] argsSumo = new String[] { 
				"sumo/sumo-0.19.0/bin/sumo-gui.exe", 
				"-c", "sumo/sim.sumocfg", 
				"--remote-port", Integer.toString(SUMO_PORT)
				};
		
		try {
			mSumoProcess = Runtime.getRuntime().exec(argsSumo);
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
