package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Successivo;
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
    	this.txtResult.clear();
		String s=this.txtPorzioni.getText().trim();
    	if(s.isBlank()) {
    		this.txtResult.setText("Nessuna porzione inserita!");
    		return;
    	}
    	
    	Integer k;
    	try {
    		k=Integer.parseInt(s);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Numero porzioni inserite non è un numero!");
    		return;
    	}
    	if(k<0) {
    		this.txtResult.setText("Inserito un numero negativo di porzioni");
    		return;
    	}
    	
    	String result=this.model.creaGrafo(k);
    	this.txtResult.setText(result);
    	List<Food> cibi=this.model.getVertici();
    	this.boxFood.getItems().clear();
    	this.boxFood.getItems().addAll(cibi);
    	this.btnGrassi.setDisable(false);
    	this.btnSimula.setDisable(false);
    	
    	
    }

   

	@FXML
    void doGrassi(ActionEvent event) 
	{
		this.txtResult.clear();
		Food f=this.boxFood.getValue();
		if(f==null) {
			this.txtResult.setText("Nessun cibo selezionato");
			return;
		}
		
		List<Successivo> result=this.model.getPeggiori(f);
		if(result.size()==0) {
			this.txtResult.appendText("Il cibo selezionato non ha successivi");
			return;
		}
		for(int i=0; i<result.size() && i<5 ; i++) {
			this.txtResult.appendText(result.get(i).toString()+"\n");
		}
		
    	
    }

    

	@FXML
    void doSimula(ActionEvent event) 
	{
		this.txtResult.clear();
		Food f=this.boxFood.getValue();
		if(f==null) {
			this.txtResult.appendText("Nessun cibo selezionato!");
			return;
		}
		
		String s=this.txtK.getText().trim();
    	if(s.isBlank()) {
    		this.txtResult.setText("Nessuna porzione inserita!");
    		return;
    	}
    	
    	Integer k;
    	try {
    		k=Integer.parseInt(s);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Numero porzioni inserite non è un numero!");
    		return;
    	}
    	if(k<0) {
    		this.txtResult.setText("Inserito un numero negativo di porzioni");
    		return;
    	}
		
    	if(k<1 || k>10) {
    		this.txtResult.setText("K deve essere compreso tra 1 e 10");
    		return;
    	}
    	
		String result=this.model.avviaSimulazione(k, f);
		this.txtResult.appendText(result);
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
    	this.btnSimula.setDisable(true);
    	this.btnGrassi.setDisable(true);
    }
}