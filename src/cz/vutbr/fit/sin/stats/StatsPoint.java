package cz.vutbr.fit.sin.stats;

public class StatsPoint<T> {
	public int time;
	public T value;
	
	public StatsPoint(int time,T value){
		this.time = time;
		this.value = value;
	}
}
