package cz.vutbr.fit.sin.tlc.impl;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

import net.sourceforge.jFuzzyLogic.FIS;
import it.polito.appeal.traci.LightState;
import it.polito.appeal.traci.InductionLoop;
import it.polito.appeal.traci.Repository;
import it.polito.appeal.traci.TLState;
import it.polito.appeal.traci.TrafficLight;
import cz.vutbr.fit.sin.tlc.BaseTrafficLightsControler;

/**
 * Trida implementujici radic krizovatky.
 * Jedna se o krizovatku na Brnenskem okruhu pred Zidenickymi kasarnami s prijezdem z Vinohran.
 */
public class TrafficLightControllerTL0 extends BaseTrafficLightsControler {
	
	/**
	 * Pocet hodnot pro vypocet kouzaveho prumeru obsazeni indukcnich smycek.
	 */
	final static int MOVING_AVG_VALUES = 40;
	
	/**
	 * Soubor s fuzzy logikou 
	 */
	final static String FUZZY_FILE = "fcl/Crossroad3.fcl";
	
	/**
	 * Trida zpracovavajici fuzzy logiku
	 */
	private FIS mFis;
	
	/**
	 * Index svetelne faze na semaforech.
	 */
	private int mPhase = -1;
	
	/**
	 * Zbyvajici pocet casovych jednotek, aktualni svetelne faze.
	 */
	private int mDuration = 0;
	
	/**
	 * Priznak aktivace rizeni krizovatky.
	 */
	private boolean mEnabled = true;
	
	/**
	 * Indukcni smycky na 1. prijezdu (od Vinohrad) do krizovatky.
	 * Znaceni r_l_d, kde r - cislo silnice, l - cislo pruhu, d - vzdalenost od krozovatky
	 */
	private InductionLoop mLoop1_1_20;
	private InductionLoop mLoop1_1_50;
	private InductionLoop mLoop1_1_100;	
	private InductionLoop mLoop1_2_20;
	
	/**
	 * Indukcni smycky na 2. prijezdu (z Zidenic) do krizovatky.
	 */
	private InductionLoop mLoop2_0_20;
	private InductionLoop mLoop2_0_50;
	private InductionLoop mLoop2_0_100;
	private InductionLoop mLoop2_1_20;
	private InductionLoop mLoop2_1_50;
	private InductionLoop mLoop2_1_100;
	private InductionLoop mLoop2_2_20;
	
	/**
	 * Indukcni smycky na 3. prijezdu (z Malomeric) do krizovatky.
	 */
	private InductionLoop mLoop3_0_20;
	private InductionLoop mLoop3_0_50;
	private InductionLoop mLoop3_0_100;
	private InductionLoop mLoop3_1_20;
	private InductionLoop mLoop3_1_50;
	private InductionLoop mLoop3_1_100;
	private InductionLoop mLoop3_2_20;
	
	/**
	 * Pocet hodnot obsazenosti indukcnich smycek pro vypocet prumerne obsazenosti. 
	 */
	private int mMeanValues = 0;
	
	/**
	 * Obsazenost cidel na 1. prijezdu.
	 */
	private Queue<Double> mLoop1_1_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop1_1_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop1_1_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop1_2_20Occupancy = new ArrayDeque<Double>();
	
	/**
	 * Obsazenost cidel na 2. prijezdu.
	 */
	private Queue<Double> mLoop2_0_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_0_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_0_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_1_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_1_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_1_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop2_2_20Occupancy = new ArrayDeque<Double>();
	
	/**
	 * Obsazenost cidel na 3. prijezdu.
	 */
	private Queue<Double> mLoop3_0_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_0_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_0_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_1_20Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_1_50Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_1_100Occupancy = new ArrayDeque<Double>();
	private Queue<Double> mLoop3_2_20Occupancy= new ArrayDeque<Double>();
	
	/**
	 * Svetelne faze pro semafory.
	 */
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
	
	/**
	 * Konstruktor 
	 * @param light Rizena sada semaforu.
	 * @param repository Repozitar indkucnich smycek rizene krizovatky.
	 */
	public TrafficLightControllerTL0(TrafficLight light, Repository<InductionLoop> repository) {
		super(light);
		// inicializace fuzzy
		initFuzzy();
		// inicializace indkucnich smycek
		initLoops(repository);
		
	}
	
	/**
	 * Konstruktor s uvedenim pocatecniho casu rizeni.
	 * @param light Rizena sada semaforu.
	 * @param repository Repozitar indkucnich smycek rizene krizovatky.
	 * @param initTime Pocatecni cas rizeni.
	 */
	public TrafficLightControllerTL0(TrafficLight light, Repository<InductionLoop> repository, int initTime) {
		super(light, initTime);
		initFuzzy();
		initLoops(repository);
	}
	
	/**
	 * Povoleni fuzzy rizeni krizovatky.
	 */
	public void enable(){
		mEnabled = true;
	}
	
	/**
	 * Zakazani fuzzy rizeni krizovatky.
	 */
	public void disable(){
		mEnabled = false;
	}
	
	/**
	 * Ziskani indukcnich smycek z reporitare.
	 * @param repository Repositar indukcnich smycek.
	 */
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
	
	/**
	 * Inicializace fuzzy rizeni
	 */
	private void initFuzzy() {
		// nacteni souboru s fuzzy control language
		mFis = FIS.load(FUZZY_FILE);
		
		if(mFis == null) {
			System.err.println("Soubor s definicí fuzzy nenalezen.");
			return;
		}
		
		// nastavime provni svetelnou fazi na semaforech
		setTrafficLights(PHASES[0]);
	}
	
	/**
	 * Nastavim svetelou fazi na semaforech.
	 * @param lightsState Svetelna faze.
	 */
	private void setTrafficLights(LightState[] lightsState) {
		TLState tlState = new TLState(lightsState);
		try {
			mTrafficLight.changeLightsState(tlState);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Vypocet prumeru z poslednich x hodnot.
	 * @param queue
	 * @return
	 */
	private static double movingAverange(Queue<Double> queue) {
		double sum = 0;
		for(Double d : queue) {
			if(d > 0) {
				sum += d;
			}
		}
		
		return sum/MOVING_AVG_VALUES;
	}
	
	/**
	 * Zaznamena obsazeni indukcnich smycek.
	 */
	private void recordOccupancy() {
		if(mMeanValues == MOVING_AVG_VALUES) {
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
			
			mMeanValues--;
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
			
			mMeanValues++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Obsazenost indukcnich smycek ve smeru rovne na 3. prijezdu.
	 * @return Obsazenot indukcnich cidel.
	 */
	private double get3iOccupancy() {
	    double occupancy3_0_20 = movingAverange(mLoop3_0_20Occupancy);
	    double occupancy3_0_50 = movingAverange(mLoop3_0_50Occupancy);
	    double occupancy3_0_100 = movingAverange(mLoop3_0_100Occupancy);
	    double occupancy3_1_20 = movingAverange(mLoop3_1_20Occupancy);
	    double occupancy3_1_50 = movingAverange(mLoop3_1_50Occupancy);
	    double occupancy3_1_100 = movingAverange(mLoop3_1_100Occupancy);
	    
	    double occupancy3_0 = occupancy3_0_20 + occupancy3_0_50 + occupancy3_0_100;
	    double occupancy3_1 = occupancy3_1_20 + occupancy3_1_50 + occupancy3_1_100;
		
		return (occupancy3_0 + occupancy3_1) / 6;
	}
	
	/**
	 * Obsazenost indukcnich smycek ve smeru rovne na 2. prijezdu.
	 * @return Obsazenot indukcnich cidel.
	 */
	private double get2iOccupancy() {
		double occupancy2_1_20 = movingAverange(mLoop2_1_20Occupancy);
	    double occupancy2_0_50 = movingAverange(mLoop2_0_50Occupancy);
	    double occupancy2_0_100 = movingAverange(mLoop2_0_100Occupancy);
	    double occupancy2_2_20 = movingAverange(mLoop2_2_20Occupancy);
	    double occupancy2_1_50 = movingAverange(mLoop2_0_50Occupancy);
	    double occupancy2_1_100 = movingAverange(mLoop2_0_100Occupancy);
	    
	    double occupancy2_0 = occupancy2_1_20 + occupancy2_0_50 + occupancy2_0_100;
	    double occupancy2_1 = occupancy2_2_20 + occupancy2_1_50 + occupancy2_1_100;
		
		return (occupancy2_0 + occupancy2_1) / 6;
	}
	
	/**
	 * Obsazenost indukcnich smycek na 2. prijezdu odbocka na silnici c. 1.
	 * @return Obsazenot indukcnich cidel.
	 */
    private double get2ito1oOccupancy() {
    	double occupancy2_0_20 = movingAverange(mLoop2_2_20Occupancy);
    	double occupancy2_0_50 = movingAverange(mLoop2_0_50Occupancy);
	    double occupancy2_0_100 = movingAverange(mLoop2_0_100Occupancy);
	    
	    return (occupancy2_0_20 + occupancy2_0_50 + occupancy2_0_100) / 3;
	}
	
    /**
	 * Obsazenost indukcnich smycek na 3. prijezdu odbocka na silnici c. 1.
	 * @return Obsazenot indukcnich cidel.
	 */
	private double get3ito1oOccupancy() {
	    double occupancy3_1_50 = movingAverange(mLoop3_1_50Occupancy);
	    double occupancy3_1_100 = movingAverange(mLoop3_1_100Occupancy);
	    double occupancy3_2_20 = movingAverange(mLoop3_2_20Occupancy);
		
		return (occupancy3_2_20 + occupancy3_1_50 + occupancy3_1_100) / 3;
	}
	
	/**
	 * Obsazenost indukcnich smycek ve smeru rovne na 1. prijezdu.
	 * @return Obsazenot indukcnich cidel.
	 */
	private double get1iOccupancy(){
		double occupancy1_1_20 = movingAverange(mLoop1_1_20Occupancy);
	    double occupancy1_1_50 = movingAverange(mLoop1_1_50Occupancy);
	    double occupancy1_1_100 = movingAverange(mLoop1_1_100Occupancy);
	    double occupancy1_2_20 = movingAverange(mLoop1_2_20Occupancy);
		
	    double occupancy1_1_20_avg = (occupancy1_1_20 + occupancy1_2_20) / 2;
	    
		return (occupancy1_1_20_avg + occupancy1_1_50 + occupancy1_1_100) / 3;
	}
	
	/**
	 * Nastavi dobu faze zelene pro Vinohrady -> Zidenice.
	 */
	private void phaseVinZid() {
		double vVinToZid = mFis.getVariable("gVinToZid").defuzzify();
		mDuration = (int) Math.round(vVinToZid);
		if(mDuration == 0) {
			mDuration = 1;
		}
	}
	
	/**
	 * Nastavi dobu faze zelene pro Zidenice -> Malomerice a naopak.
	 */
	private void phaseZidMal() {
		double vZidToMal = mFis.getVariable("gZidToMal").defuzzify();
		double vMalToZid = mFis.getVariable("gMalToZid").defuzzify();
		if(vZidToMal > vMalToZid) {
			mDuration = (int) Math.round(vZidToMal);
		}
		else {
			mDuration = (int) Math.round(vMalToZid);
		}
		if(mDuration == 0) {
			mDuration = 1;
		}
	}
	
	/**
	 * Nastavi dobu faze zelene pro Malomerice -> odbocka Vinohrady.
	 */
	private void phaseZidVin() {
		double vMalToVin = mFis.getVariable("gMalToVin").defuzzify();
		mDuration = (int) Math.round(vMalToVin);
		if(mDuration == 0) {
			mDuration = 1;
		}
	}
	
	/**
	 * Provede jeden krok simulace.
	 */
	@Override
	public void step(){
		super.step();
		
		if(!mEnabled){
			return;
		}
		// zaznamenani obsazenosti indkucnich smycek
		recordOccupancy();
		
		if(mDuration == 0) {
			// ziskane obsazenom jednotlivych smeru
			double occupacy2i = get2iOccupancy();
			double occupacy3i = get3iOccupancy();
			double occupacy1i = get1iOccupancy();
			double occupacy2ito1 = get2ito1oOccupancy();
			double occupacy3ito1 = get3ito1oOccupancy();
			
			// nastavime vstuoni hodnoty pro fuzzyfikaci
	        mFis.setVariable("zidToMal", occupacy2i);
	        mFis.setVariable("malToZid", occupacy3i);
	        mFis.setVariable("vinToZid", occupacy1i);
	        mFis.setVariable("malToVin", occupacy3ito1);
			
	        // Evaluate Fuzzy
	        mFis.evaluate();
	        
			switch(mPhase) {
				case -1:
					phaseVinZid();
					mPhase = 0;
					break;
				case 0:	// vinToZid - green
					if(occupacy2i == 0 && occupacy2ito1 == 0 && occupacy3i == 0 && occupacy3ito1 == 0) {
						// v ostatnich smerech zadna auta => nebudeme memit svetelnou fazi
						phaseVinZid();
						mPhase = 0;
					}
					else {
						mDuration = YELLOW_TIME;
						mPhase = 1;
					}
					break;
				case 1: // vinToZid - yellow, zidToVin - green 
					phaseZidMal();
					mPhase = 2;
					break;
				case 2:	// zidToMal - green, malToZid - green, malToVin - green
					if(occupacy1i == 0 && occupacy3ito1 == 0) {
						// vinToZid a malToVin zadne auto => nebudeme menit fazi
						phaseZidMal();
						mPhase = 1;
					}
					else {
						mDuration = YELLOW_TIME;
						mPhase = 3;
					}
					break;
				case 3: // zidToMal - yellow, malToZid - green, malToVin - green
					if(occupacy3ito1 == 0) {
						mDuration = YELLOW_TIME;
						mPhase = 5;
					}
					else {
						phaseZidVin();
						mPhase = 4;
					}
					break;
				case 4:	// zidToMal - red, malToZid - green, malToVin - green
					if(occupacy1i == 0) {
						phaseZidMal();
						mPhase = 2;
					}
					else {
						mDuration = YELLOW_TIME;
						mPhase = 5;
					}
					break;
				case 5: // zidToMal - red, malToZid - yellow	
					phaseVinZid();
					mPhase = 0;
					break;
			}
			// vyber svetelne faze a jeji nastaveni
			LightState[] lightsState = PHASES[mPhase];
			setTrafficLights(lightsState);
		}
		else {
			mDuration--;
		}
	}

}
