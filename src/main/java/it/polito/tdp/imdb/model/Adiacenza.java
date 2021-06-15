package it.polito.tdp.imdb.model;

public class Adiacenza {
	
	public Director dir1;
	public Director dir2;
	public int peso;
	
	public Director getDir1() {
		return dir1;
	}
	public void setDir1(Director dir1) {
		this.dir1 = dir1;
	}
	public Director getDir2() {
		return dir2;
	}
	public void setDir2(Director dir2) {
		this.dir2 = dir2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	public Adiacenza(Director dir1, Director dir2, int peso) {
		super();
		this.dir1 = dir1;
		this.dir2 = dir2;
		this.peso = peso;
	}

	
	
	

}
