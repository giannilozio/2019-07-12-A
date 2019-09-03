package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Map<Integer,Food> idMap;
	private Graph<Food,DefaultWeightedEdge> grafo;
	private List <Food> vertici;
	private List <PD> archi;
	private List<ConPeso> best;
	
	public Model() {
		
		dao = new FoodDao ();
		idMap = new HashMap<>();
		best = new ArrayList<>();
		vertici = new ArrayList<>();
		
	}

	public void creaGrafo(int porzioni) {
		
		grafo = new SimpleWeightedGraph<Food,DefaultWeightedEdge>(DefaultWeightedEdge.class);

		vertici = dao.getVertici(porzioni,idMap);
		Graphs.addAllVertices(grafo,vertici);
		
		archi = dao.getPesi(porzioni,idMap);
		
		for(PD p : archi)
		
			Graphs.addEdge(grafo,p.getF1(),p.getF2(),p.getPeso());
	}

	public Graph<Food, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Food, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public List<ConPeso> getBest(Food f) {

		List<Food>  neighbors = new ArrayList<>();
		best = new ArrayList<>();
		
		neighbors = Graphs.neighborListOf(grafo, f);
		
		for(Food f1 : neighbors) {
			
			DefaultWeightedEdge edge = grafo.getEdge(f, f1);
		
			double peso = grafo.getEdgeWeight(edge);
			
			ConPeso p = new ConPeso (f1,peso);
			
			best.add(p);
		}
		
	Collections.sort(best);
		return best;
	}
	
	public List<PD> getArchi() {
		return archi;
	}

	public void setArchi(List<PD> archi) {
		this.archi = archi;
	}

	public List<ConPeso> Best() {
		return best;
	}

	public void setBest(List<ConPeso> best) {
		this.best = best;
	}

	public List<Food> getVertici() {
		return vertici;
	}

	public void setVertici(List<Food> vertici) {
		this.vertici = vertici;
	}
	
}