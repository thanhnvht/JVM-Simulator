
package ui.com.rfrench.jvm.java;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import main.com.rfrench.jvm.java.ClassLoader;
import main.com.rfrench.jvm.java.Memory;
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

    private ArrayList<String> bytecode_assembly_text;
    
    private ArrayList<String> bytecode_strings;
    private ArrayList<ArrayList<String>> method_bytecodes;
    
    private Label[] assembly_pane_labels;
    private Label[] hex_labels;
    
    private ListView assembly_listview;
    private ListView hex_listview;
        
    private TabPane assembly_tabpane;
    private Tab bytecode_tab;
    
    private TextArea code_area;
        
    public UIAssemblyPane(ClassLoader class_loader, String[] frame_names, Memory M)
    {                          
        //setupHex(class_loader, M);        
        setupTabs();
        setupListView();                                                       
        setupLabels(class_loader);   
        setupTooltips(class_loader);
    }
    
    private void setupListView()
    {
        assembly_listview = new ListView();
        assembly_listview.setMinWidth(MainScene.WIDTH_TENTH * 3);
        assembly_listview.setMinHeight(MainScene.HEIGHT_TENTH * 7);
        assembly_listview.setId(CSS_ASSEMBLY_ID);     
    }        
    
    private void setupLabels(ClassLoader class_loader)
    {       

        bytecode_assembly_text = class_loader.getProgram();

        bytecode_tab.setContent(assembly_listview);  
        
        assembly_pane_labels = new Label[bytecode_assembly_text.size()];

        int index = 0;

        for(int i = 0; i < class_loader.getNoOpcodes(); i++)
        {                        
            //String mem_add = "0000x" + Integer.toHexString(class_loader.getOpcodeMemoryLocation(i)).toUpperCase();
            //System.out.println(mem_add);
            //assembly_pane_labels[i] = new Label(bytecode_assembly_text.get(index) + "\t" + mem_add + "\t" + bytecode_assembly_text.get(index+1));
            assembly_pane_labels[i] = new Label(bytecode_assembly_text.get(index) + "\t" + bytecode_assembly_text.get(index+1));
            assembly_listview.getItems().add(assembly_pane_labels[i]);  
            index += 2;
        }
 
    }
    
    private void setupTooltips(ClassLoader class_loader)
    {
        try
        {
            final String ATTRIBUTE_NAME = "Tooltip";
         
            int current_line = 0;
            
            JSONParser parser = new JSONParser();            
            Object obj = parser.parse(new InputStreamReader(getClass().getResourceAsStream(JSON_FILE_PATH)));
            JSONObject jsonObject = (JSONObject) obj;            
            JSONArray bytecode_json_array = (JSONArray) jsonObject.get("bytecodes");
            
            HashMap bytecode_details_map = class_loader.getByteCodeDetails();
            
            int index = 1;
            
            final int NUMBER_OF_OPCODES = class_loader.getProgram().size() / 2;
            
            for(int i = 0; i < NUMBER_OF_OPCODES; i++)
            {                                     
                String word = (String)class_loader.getProgram().get(index);
                
                //Remove Operand from Bytecode
                if(word.indexOf(' ') != - 1)
                {
                    word = word.substring(0, word.indexOf(' '));
                }
                
                //Gather Tooltip text from JSON
                if(bytecode_details_map.containsKey(word))
                {                                        
                    Tooltip tooltip = new Tooltip();
                    
                    int bytecode_index = (int)bytecode_details_map.get(word);
                    
                    JSONObject bytecode_element = (JSONObject) bytecode_json_array.get(bytecode_index);
                    
                    String tooltip_text = (String)bytecode_element.get(ATTRIBUTE_NAME);
                    
                    tooltip.setText(tooltip_text);
                    
                    assembly_pane_labels[current_line].setTooltip(tooltip);      
                    
                    current_line++;
                }
         
                index+= 2;
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
        bytecode_tab = new Tab();
        bytecode_tab.setText("ByteCode");
        
        Tab java_code_tab = new Tab();
        java_code_tab.setText("Java Code");
        setupJavaCodeField();
        java_code_tab.setContent(code_area);
                
        Tab hex_tab = new Tab();
        hex_tab.setText("Hex Code");
        hex_tab.setContent(hex_listview);
                                
        assembly_tabpane.getTabs().addAll(bytecode_tab, java_code_tab, hex_tab);
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
        return assembly_listview;
    }
    
    public TabPane getTabPane()
    {
        return assembly_tabpane;
    }       
    
    public void highlightLine(int index)
    {        
        assembly_listview.getSelectionModel().select(index);
    }
    
//    private void writeOpcodeMemoryLocationsTest()
//    {
//        int memory_location = 0;
//        int count = 0;
//        int OPCODES_INDEX = 2;
//        
//        opcode_memory_locations = new int[NUMBER_OPCODES_PROGRAM];
//        method_start_address_array = new int[NUMBER_OF_METHODS];
//                
//        for(int i = 0; i < NUMBER_OF_METHODS; i++)
//        {
//            int method_opcodes_total = opcodes_list.get(i).size();
//            
//            method_start_address_array[i] = memory_location;
//            
//            for(int j = 0; j < method_opcodes_total; j++)
//            {
//                String word = opcodes_list.get(i).get(j);
//                
//                if(word.indexOf(' ') != -1)            
//                {
//                    word = word.substring(0, word.indexOf(' '));   
//                }
//                
//                if(opcodes.containsKey(word))
//                {
//                    opcode_memory_locations[count] = memory_location;
//                    count++;
//                    int[] opcode_meta_data = (int[])opcodes.get(word);                                               
//                    memory_location += opcode_meta_data[OPCODES_INDEX];
//                }
//                else
//                {
//                    System.out.println("Invalid Opcode: " + word);
//                }
//            }
//        }                                      
//    }  
    
}
