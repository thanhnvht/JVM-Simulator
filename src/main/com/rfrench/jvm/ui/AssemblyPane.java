/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import main.com.rfrench.jvm.java.JVMClass;
import static main.com.rfrench.jvm.java.Main.JSON_FILE_PATH;
import main.com.rfrench.jvm.java.MethodArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
    Program Title: UIAssemblyPane.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class AssemblyPane 
{                   
    private ArrayList<ArrayList<Label>> java_program_labels_list;    
    private ArrayList<Label> java_program_labels;
            
    private int NUMBER_OF_METHODS;
   
    private ArrayList<ListView> java_program_listview_list;
    private ListView java_program_listview;
        
    private TabPane bytecode_pane;
    private ArrayList<Tab> bytecode_tab_list;
    private Tab bytecode_tab;
           
    private MethodArea method_area;
    
    private JVMClass current_class;
        
    
    public AssemblyPane(TabPane bytecode_pane, MethodArea method_area)
    {                          
        this.bytecode_pane = bytecode_pane;
        this.method_area = method_area;
                
        current_class = method_area.getJVMClass(0);
        
        NUMBER_OF_METHODS = current_class.getNumberOfMethods();
        
        setupTabs();
        setupListView();                                                       
        setupLabels();   
        setupTooltips();
    }
    
    private void setupListView()
    {
        java_program_listview_list = new ArrayList<ListView>();
                
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            java_program_listview = new ListView();
            java_program_listview.setMinWidth(MainScene.WIDTH_TENTH * 30);
            java_program_listview.setMinHeight(MainScene.HEIGHT_TENTH * 70);             
            java_program_listview_list.add(java_program_listview);
        }   
    }        
    
    public void addNewAssembly(TabPane bytecode_pane, MethodArea method_area, int class_number)
    {    
        this.bytecode_pane = bytecode_pane;
        
        current_class = method_area.getJVMClass(class_number);
        NUMBER_OF_METHODS = current_class.getNumberOfMethods();
        
        setupTabs();
        setupListView();
        setupLabels();
        setupTooltips();
    }
    
    private void setupLabels()
    {      
        java_program_labels_list = new ArrayList<ArrayList<Label>>();                
        
        for (int i = 0; i < NUMBER_OF_METHODS; i++) 
        {
            ArrayList<String> bytecode_text = current_class.getMethod(i).getMethodBytecode();
                        
            ArrayList<String> line_numbers = current_class.getMethod(i).getMethodLineNumbers();

            ListView listview = java_program_listview_list.get(i);
            
            Tab tab = bytecode_tab_list.get(i);
            
            tab.setContent(listview);
                                    
            int NUMBER_OF_BYTECODES = bytecode_text.size();

            java_program_labels = new ArrayList<Label>();

            for (int j = 0; j < NUMBER_OF_BYTECODES; j++) 
            {
                String line_number = line_numbers.get(j);

                String bytecode = bytecode_text.get(j);                                                                
                
                Label new_label = new Label(line_number + "\t" + bytecode);
                    
                java_program_labels.add(new_label);
                
                listview.getItems().add(java_program_labels.get(j));
            }
            
            java_program_labels_list.add(java_program_labels);
        }
    }
    
    private void setupTooltips()
    {
                            
        JSONArray bytecode_json_array = getByteCodeTooltipDetails();

        HashMap bytecode_details_map = method_area.getBytecodeDetails();

        for (int i = 0; i < NUMBER_OF_METHODS; i++) 
        {

            int NUMBER_OF_BYTECODES = current_class.getMethod(i).getMethodBytecode().size();

            for (int j = 0; j < NUMBER_OF_BYTECODES; j++) {

                String word = current_class.getMethod(i).getMethodBytecode().get(j);

                //Remove Operand from Bytecode
                if (word.indexOf(' ') != - 1) 
                {
                    word = word.substring(0, word.indexOf(' '));
                }
                
                if (bytecode_details_map.containsKey(word)) 
                {
                    //Gather Tooltip text from JSON
                    assignToolTip(bytecode_details_map, bytecode_json_array, word, i, j);
                }
            }
        }      
    }
    
    private void assignToolTip(HashMap bytecode_details_map, JSONArray bytecode_json_array, String word, int method_count, int line_number)
    {
        final String ATTRIBUTE_NAME = "Tooltip";
        
        Tooltip tooltip = new Tooltip();

        int bytecode_index = (int) bytecode_details_map.get(word);

        JSONObject bytecode_element = (JSONObject) bytecode_json_array.get(bytecode_index);

        String tooltip_text = (String) bytecode_element.get(ATTRIBUTE_NAME);

        tooltip.setText(tooltip_text);

        Label l = java_program_labels_list.get(method_count).get(line_number);

        l.setTooltip(tooltip);
    }
    
    private JSONArray getByteCodeTooltipDetails()
    {
        JSONArray bytecode_json_array = null;
        
        try
        {                     
            JSONParser parser = new JSONParser();            

            Object obj = parser.parse(new InputStreamReader(getClass().getResourceAsStream(JSON_FILE_PATH)));

            JSONObject jsonObject = (JSONObject) obj;            

            bytecode_json_array = (JSONArray) jsonObject.get("bytecodes");
            
        }
        
        catch(IOException | ParseException e)
        {
            System.out.println("Error - JSON Tooltips");
            
            e.printStackTrace();
        }
        
        return bytecode_json_array;
    }
    
    private void setupTabs()
    {

        bytecode_tab_list = new ArrayList<Tab>();
        
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            bytecode_tab = new Tab();
            
            String method_name = current_class.getMethod(i).getMethodName();
            
            bytecode_tab.setText(method_name);
            bytecode_tab_list.add(bytecode_tab);
            bytecode_pane.getTabs().add(bytecode_tab);
        }
        
        bytecode_pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }                          
    
    public ListView getListView()
    {
        return java_program_listview;
    }
    
    public TabPane getTabPane()
    {
        return bytecode_pane;
    }       
    
    public void highlightLine(int index)
    {        
        java_program_listview.getSelectionModel().select(index);
    }    
    
    public ListView getCurrentListView(int current_method)
    {
        return java_program_listview_list.get(current_method);
    }
}
