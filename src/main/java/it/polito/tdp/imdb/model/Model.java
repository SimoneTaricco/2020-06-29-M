package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Director,DefaultWeightedEdge> grafo;
	private Map<Integer,Director> idMap;
	private ImdbDAO dao;
	List<Director> affini;
	
	private int pesoMigliore;
	
	private int numeroDirettori;
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	
	public void creagrafo(int anno) {
		
		this.idMap = new HashMap<Integer,Director>();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
				
		for (Director a:this.dao.listAllDirectorsYear(anno)) {
			idMap.put(a.getId(), a);
		}
		
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		for (Adiacenza a:dao.listAdiacenze(idMap, anno)) {
				Graphs.addEdge(this.grafo, a.getDir1(), a.getDir2(), a.getPeso());
		}
	}
	
	public List<Director> vertici() {
		
		TreeMap<Integer,Director> map = new TreeMap<Integer,Director>();
			
		for (Director o:this.grafo.vertexSet()) 
			map.put(o.id, o);

		return new ArrayList<Director>(map.values());
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Map<Integer,Director> getAdiacenti(Director d){
		
		Map<Integer,Director> mappa = new TreeMap<Integer,Director>(Collections.reverseOrder());
		
		for (DefaultWeightedEdge e:grafo.edgesOf(d)) {
			mappa.put((int)grafo.getEdgeWeight(e), grafo.getEdgeSource(e));
		}
		
		return mappa;
	}
		
	public List<Director> getAffini(int pesiArco, Director partenza) {
		
		this.affini = new ArrayList<Director>();
		this.numeroDirettori = 0; // grandezza con cui controllare se una soluzione trovata è più lunga di quella migliore
		List<Director> parziale = new ArrayList<Director>();
		parziale.add(partenza);

		this.pesoMigliore = 0; // valore da ottenere per la stampa in txtResult
		
		this.cerca(parziale, pesiArco, 0); // il terzo parametro è la somma del peso degli archi della parziale

		return this.affini;
	}
	
	private void cerca(List<Director> parziale, int pesiArco, int pesoTotale) {
		
		if (parziale.size() > this.numeroDirettori && pesoTotale >= pesoMigliore && pesoTotale <= pesiArco) {
			this.numeroDirettori = parziale.size();
			this.affini = new ArrayList<Director>(parziale);
			this.pesoMigliore = pesoTotale;
		}
			
		for(Director vicino: Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			
			int pesoAggiunto = (int)(grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), vicino)));
			
			if(!parziale.contains(vicino) && pesoTotale + pesoAggiunto <= pesiArco) {
				
				pesoTotale += pesoAggiunto;
				parziale.add(vicino);
				
				cerca(parziale, pesiArco, pesoTotale);
					
				pesoTotale -= pesoAggiunto;
				parziale.remove(parziale.size()-1); 
				}
			}		
	}
	
	public int getAttoriCondivisi() {
		return this.pesoMigliore;
	}

}
