package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adjacence;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController 
{
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtPorzioni;

    @FXML
    private TextField txtK;

    @FXML
    private Button btnAnalisi;

    @FXML
    private Button btnGrassi;

    @FXML
    private Button btnSimula;

    @FXML
    private ComboBox<Food> boxFood;

    @FXML
    private TextArea txtResult;
    
	private Model model;


	@FXML
    void doCreaGrafo(ActionEvent event) 
	{
    	String inputPortions = this.txtPorzioni.getText();
    	
    	if(inputPortions == null || inputPortions.isBlank())
    	{
    		this.txtResult.setText("Errore: inserire un valore intero di porzioni");
    		return;
    	}
    	
    	int numPortions;
    	
    	try
		{
        	numPortions = Integer.parseInt(inputPortions);
		}
		catch(NumberFormatException nfe)
		{
			this.txtResult.setText(String.format(
					"Errore di formato: \"%s\" non è un intero valido.\nInserire un valore intero di porzioni",
					inputPortions));
    		return;
		}
    	
    	if(numPortions < 1) 
    	{
    		this.txtResult.setText("Errore: inserire un valore intero maggiore o uguale a 1");
    		return;
    	}
    	
    	this.model.createGraph(numPortions);
    	
    	//UI
    	String output = this.printGraphInfo();
    	this.txtResult.setText(output);
    	
    	this.boxFood.getItems().clear();
    	List<Food> orderedFoods = this.model.getOrderedFoods();
    	
    	if(orderedFoods != null)
    		this.boxFood.getItems().addAll(orderedFoods);
    }

    private String printGraphInfo()
	{
    	int numVertices = this.model.getNumVertices();
    	
		StringBuilder sb = new StringBuilder();
		
		if(numVertices == 0)
		{
			sb.append("Errore: grafo vuoto. Input troppo alto");
			return sb.toString();
		}
		
		sb.append("Grafo creato");
		sb.append("\n# Vertci: ").append(this.model.getNumVertices());
		sb.append("\n# Archi: ").append(this.model.getNumEdges());

		return sb.toString();
	}

	@FXML
    void doGrassi(ActionEvent event) 
	{
    	Food selectedFood = this.boxFood.getValue();
    	
    	if(selectedFood == null) 
    	{
    		this.txtResult.setText("Errore: selezionare un cibo dal menù a tendina");
    		return;
    	}
    	
    	List<Adjacence> minimumAdjacentFoods = this.model.getMinimumAdjacentFoodsOf(selectedFood);
    	
    	if(minimumAdjacentFoods == null) 
    	{
    		this.txtResult.setText("Errore: creare prima il grafo!");
    		return;
    	}
    	
    	String output = this.printOrderedMinimumAdjacences(selectedFood, minimumAdjacentFoods);
    	this.txtResult.setText(output);
    }

    private String printOrderedMinimumAdjacences(Food selected, List<Adjacence> minimumAdjacences)
	{
    	if(minimumAdjacences.isEmpty())
    		return "Il cibo selezionato (\"" + selected.toString() + "\") non presenta alcun cibo adiacente";
    	
    	StringBuilder sb = new StringBuilder();
    	int num = minimumAdjacences.size();
    	
    	for(Adjacence a : minimumAdjacences)
    	{
    		if(sb.length() > 0)
    			sb.append("\n");
    		
    		sb.append(a.getFood2()).append("  -  ").append(String.format("%.3f", a.getWeight()));
    	}
    	
    	if(num == 1)
    		sb.insert(0, "L'unico cibo con differenza grassi minore rispetto a \"" + 
					selected.toString() + "\" è:\n\n");
    	else
    		sb.insert(0, "I "+num+" cibi con differenza grassi minore rispetto a \"" + 
    						selected.toString() + "\" sono:\n\n");
    	
    	if(num != 5) 
    	{
    		sb.append("\n(Il cibo selezionato presenta meno di 5 cibi adiacenti)");
    	}
    	
    	return sb.toString();
	}

	@FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...");
    }

    @FXML
    void initialize() 
    {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnGrassi != null : "fx:id=\"btnGrassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) 
    {
    	this.model = model;
    }
}