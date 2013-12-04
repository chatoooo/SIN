package cz.vutbr.fit.sin;

import java.io.IOException;
import java.net.InetAddress;

import cz.vutbr.fit.sin.tlc.ITrafficLightController;
import cz.vutbr.fit.sin.tlc.impl.TrafficLightControllerTL0;

import it.polito.appeal.traci.*;

public class MainRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		try {
			SumoTraciConnection con = new SumoTraciConnection(InetAddress.getLoopbackAddress(),56789);
			TrafficLight lights = con.getTrafficLightRepository().getByID("0");
			ITrafficLightController controler = new TrafficLightControllerTL0(lights);

			for(int i = 0;i<900;i++){
				con.nextSimStep();
				int current_step = con.getCurrentSimStep();
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

	}

}
