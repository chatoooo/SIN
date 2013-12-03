package cz.vutbr.fit.sin;

import java.io.IOException;

import cz.vutbr.fit.sin.tlc.ITrafficLightControler;
import cz.vutbr.fit.sin.tlc.impl.TrafficLightControllerTL0;

import it.polito.appeal.traci.*;

public class MainRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SumoTraciConnection con = new SumoTraciConnection("traci.cfg", 12345);
		
		try {
			TrafficLight lights = con.getTrafficLightRepository().getByID("0");
			ITrafficLightControler controller = new TrafficLightControllerTL0(lights);
			
			while(con.get)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
