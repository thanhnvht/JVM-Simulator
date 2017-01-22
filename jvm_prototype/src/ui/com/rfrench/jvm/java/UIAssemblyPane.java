
package ui.com.rfrench.jvm.java;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import main.com.rfrench.jvm.java.ClassLoader;
import main.com.rfrench.jvm.java.MethodArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static main.com.rfrench.jvm.java.Main.JSON_FILE_PATH;

/*
    Program Title: UIAssemblyPane.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class UIAssemblyPane 
{
    
    private final String CSS_ASSEMBLY_ID = "ASSEMBLY";
   
    
    
    private ArrayList<ArrayList<Label>> java_program_labels_list;    
    private ArrayList<Label> java_program_labels;
            
    private Label[] hex_labels;

    private final int NUMBER_OF_METHODS;
   
    private ArrayList<ListView> java_program_listview_list;
    private ListView java_program_listview;
    private ListView hex_listview;

        
    private TabPane assembly_tabpane;
    private ArrayList<Tab> bytecode_tab_list;
    private Tab bytecode_tab;
    
    private TextArea code_area;
    
    private ClassLoader class_loader;
        
    public UIAssemblyPane(ClassLoader class_loader, String[] frame_names, MethodArea M)
    {                          
        this.class_loader = class_loader;
        
        NUMBER_OF_METHODS = class_loader.getNumberOfMethods();
        
        //setupHex(class_loader, M);        
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
            java_program_listview.setMinWidth(MainScene.WIDTH_TENTH * 3);
            java_program_listview.setMinHeight(MainScene.HEIGHT_TENTH * 7);
            java_program_listview.setId(CSS_ASSEMBLY_ID);  
            
            java_program_listview_list.add(java_program_listview);
        }   
    }        
    
    private void setupLabels()
    {      
        java_program_labels_list = new ArrayList<ArrayList<Label>>();
        
        for (int i = 0; i < NUMBER_OF_METHODS; i++) 
        {
            ArrayList<String> bytecode_text = class_loader.getMethods().get(i).getMethodBytecode();

            ArrayList<String> line_numbers = class_loader.getMethods().get(i).getMethodLineNumbers();

            ListView listview = java_program_listview_list.get(i);
            
            Tab tab = bytecode_tab_list.get(i);
            
            tab.setContent(listview);
                                    
            int NUMBER_OF_BYTECODES = bytecode_text.size();

            java_program_labels = new ArrayList<Label>();

            for (int j = 0; j < NUMBER_OF_BYTECODES; j++) 
            {
                String line_number = line_numbers.get(j);

                String bytecode = bytecode_text.get(j);                                
                
                java_program_labels.add(new Label(line_number + "\t" + bytecode));
                
                listview.getItems().add(java_program_labels.get(j));
            }
            
            java_program_labels_list.add(java_program_labels);
        }
    }
    
    private void setupTooltips()
    {
        try
        {
            final String ATTRIBUTE_NAME = "Tooltip";
                     
            JSONParser parser = new JSONParser();            
            Object obj = parser.parse(new InputStreamReader(getClass().getResourceAsStream(JSON_FILE_PATH)));
            JSONObject jsonObject = (JSONObject) obj;            
            JSONArray bytecode_json_array = (JSONArray) jsonObject.get("bytecodes");
            
            HashMap bytecode_details_map = class_loader.getByteCodeDetails();
            
            for (int i = 0; i < NUMBER_OF_METHODS; i++) 
            {
                int NUMBER_OF_BYTECODES = class_loader.getMethods().get(i).getMethodBytecode().size();

                for (int j = 0; j < NUMBER_OF_BYTECODES; j++) {

                    String word = (String) class_loader.getMethods().get(i).getMethodBytecode().get(j);

                    //Remove Operand from Bytecode
                    if (word.indexOf(' ') != - 1) 
                    {
                        word = word.substring(0, word.indexOf(' '));
                    }

                    //Gather Tooltip text from JSON
                    if (bytecode_details_map.containsKey(word)) 
                    {
                        Tooltip tooltip = new Tooltip();

                        int bytecode_index = (int) bytecode_details_map.get(word);

                        JSONObject bytecode_element = (JSONObject) bytecode_json_array.get(bytecode_index);

                        String tooltip_text = (String) bytecode_element.get(ATTRIBUTE_NAME);

                        tooltip.setText(tooltip_text);
                        
                        Label l = java_program_labels_list.get(i).get(j);
                        
                        l.setTooltip(tooltip);
                    }
                }
            }      
        }
        
        catch(IOException | ParseException e)
        {
            System.out.println("Error - JSON Tooltips");
            
            e.printStackTrace();
        }
    }
    
    private void setupTabs()
    {
                
        assembly_tabpane = new TabPane();
        
        bytecode_tab_list = new ArrayList<Tab>();
        
        for(int i = 0; i < NUMBER_OF_METHODS; i++)
        {
            bytecode_tab = new Tab();
            bytecode_tab.setText("Method " + (i+1));
            bytecode_tab_list.add(bytecode_tab);
            assembly_tabpane.getTabs().add(bytecode_tab);
        }
                        
        Tab java_code_tab = new Tab();
        java_code_tab.setText("Java Code");
        setupJavaCodeField();
        java_code_tab.setContent(code_area);
                
        Tab hex_tab = new Tab();
        hex_tab.setText("Hex Code");
        hex_tab.setContent(hex_listview);
                                               
                
        assembly_tabpane.getTabs().addAll(java_code_tab, hex_tab);
        assembly_tabpane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }
        
    
    private void setupJavaCodeField()
    {
        String java_code = "*";        
        
        code_area = new TextArea();
        code_area.setText(java_code);
        code_area.setEditable(false);
        code_area.setId(CSS_ASSEMBLY_ID);
        
    }
        
//    private void setupHex(ClassLoader input_file, Memory M)
//    {
//        hex_listview = new ListView();
//        hex_listview.setId(CSS_ASSEMBLY_ID);
//        hex_labels = new Label[input_file.getNoOpcodes()];
//                
//        for(int i = 0; i < input_file.getNoOpcodes(); i++)
//        {
//            String opcode = input_file.getOpcodeProgram(i);
//            
//            if(opcode.indexOf(' ') != -1)            
//               opcode = opcode.substring(0, opcode.indexOf(' ')); 
//                        
//            int[] opcode_meta_data = (int[])input_file.getOpcodeMetaData().get(opcode);
//            
//            int mem_location = input_file.getOpcodeMemoryLocation(i);
//            
//            String hex_string = "";                                  
//            
//            for(int j = 0; j < opcode_meta_data[2]; j++)                                       
//                hex_string += "0x" + Integer.toHexString(M.getMemoryAddress(j + mem_location)).toUpperCase() + " ";
//                                                    
//            hex_labels[i] = new Label(hex_string);
//            
//            hex_listview.getItems().add(hex_labels[i]);
//        }               
//    }         
    
    public ListView getListView()
    {
        return java_program_listview;
    }
    
    public TabPane getTabPane()
    {
        return assembly_tabpane;
    }       
    
    public void highlightLine(int index)
    {        
        java_program_listview.getSelectionModel().select(index);
    }    
}
