
package ui.com.rfrench.jvm.java;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import main.com.rfrench.jvm.java.ClassLoader;
import main.com.rfrench.jvm.java.Memory;

/*
    Program Title: UIAssemblyPane.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class UIAssemblyPane 
{
    
    private final String CSS_ASSEMBLY_ID = "ASSEMBLY";
    
    private HashMap bytecode_tool_tips;
    private final int TOOLTIP_HASHMAP_SIZE = 255;

    private ArrayList<String> bytecode;
    
    private Label[] assembly_pane_labels;
    private Label[] hex_labels;
    
    private ListView assembly_listview;
    private ListView hex_listview;
        
    private TabPane assembly_tabpane;
    private Tab bytecode_tab;
    
    private TextArea code_area;
        
    public UIAssemblyPane(ClassLoader class_loader, String[] frame_names, Memory M)
    {                  
        
        setupHex(class_loader, M);
        
        setupTabs();
        initialiseToolTipHashMap();
        setupListView();  
                                             
        
        setupLabels(class_loader);        
    }
    
    private void setupListView()
    {
        assembly_listview = new ListView();
        assembly_listview.setMinWidth(MainScene.WIDTH_TENTH * 3);
        assembly_listview.setMinHeight(MainScene.HEIGHT_TENTH * 7);
        assembly_listview.setId(CSS_ASSEMBLY_ID);     
    }
    
    private void initialiseToolTipHashMap()
    {
        bytecode_tool_tips = new HashMap(TOOLTIP_HASHMAP_SIZE);
        
        bytecode_tool_tips.put("ICONST_0", "ICONST_0 - Load the value 0 onto the stack");
        bytecode_tool_tips.put("ICONST_1", "ICONST_1 - Load the value 1 onto the stack");
        bytecode_tool_tips.put("ICONST_2", "ICONST_2 - Load the value 2 onto the stack");
        bytecode_tool_tips.put("ICONST_3", "ICONST_3 - Load the value 3 onto the stack");
        bytecode_tool_tips.put("ICONST_4", "ICONST_4 - Load the value 4 onto the stack");
        bytecode_tool_tips.put("ICONST_5", "ICONST_5 - Load the value 5 onto the stack");
        bytecode_tool_tips.put("BIPUSH", "BIPUSH - Push a value onto the stack");
        bytecode_tool_tips.put("ILOAD", "ILOAD - Load a value from given index of Local Variable Frame");
        bytecode_tool_tips.put("ILOAD_0", "ILOAD_0 - Load a value from Local Variable Frame 0");
        bytecode_tool_tips.put("ILOAD_1", "ILOAD_1 - Load a value from Local Variable Frame 1");
        bytecode_tool_tips.put("ILOAD_2", "ILOAD_2 - Load a value from Local Variable Frame 2");
        bytecode_tool_tips.put("ILOAD_3", "ILOAD_3 - Load a value from Local Variable Frame 3");
        bytecode_tool_tips.put("ISTORE", "ISTORE - Store Value from top of Stack into given index of Local Variable Frame");
        bytecode_tool_tips.put("ISTORE_0", "ISTORE_0 - Store Value from top of Stack into Local Variable Frame 0");
        bytecode_tool_tips.put("ISTORE_1", "ISTORE_1 - Store Value from top of Stack into Local Variable Frame 1");
        bytecode_tool_tips.put("ISTORE_2", "ISTORE_2 - Store Value from top of Stack into Local Variable Frame 2");
        bytecode_tool_tips.put("ISTORE_3", "ISTORE_3 - Store Value from top of Stack into Local Variable Frame 3");
        bytecode_tool_tips.put("IADD", "IADD - Add together the 2 integers on the top of Stack. Push the Result onto the Stack");
        bytecode_tool_tips.put("ISUB", "ISUB - Sub together the 2 integers on the top of Stack. Push the Result onto the Stack");
        bytecode_tool_tips.put("IF_ICMPEQ", "IF_ICMPEQ - Branch to given line number if the variables on the top of Stack are equal");
        bytecode_tool_tips.put("GOTO", "GOTO - Branch to the given line number");
        bytecode_tool_tips.put("DUP", "DUP - Duplicate the value on the top of the Stack");
        bytecode_tool_tips.put("IAND", "IAND - Perform AND operation on the 2 values on the top of Stack");
        bytecode_tool_tips.put("IFEQ", "IFEQ - If value is 0, branch to the line number given");
        bytecode_tool_tips.put("IFLT", "IFLT - If value is less than 0, branch to the line number given");
        bytecode_tool_tips.put("IINC", "IINC - Increment the chosen local variable by given number");
        bytecode_tool_tips.put("LDC_W", "LDC_W - Push constant from constant pool onto Stack");
        bytecode_tool_tips.put("NOP", "NOP - Perform no operation");
        bytecode_tool_tips.put("POP", "POP - Discard the top value on the Stack");
        bytecode_tool_tips.put("SWAP", "SWAP - Swap the top 2 values on the Stack");
        bytecode_tool_tips.put("IF_ICMPGQ", "");
        bytecode_tool_tips.put("IF_ICMPGE", "IF_ICMPGE - If first value is greater than or equal to the second value, then branch");
        bytecode_tool_tips.put("IF_ICMPNE", "IF_ICMPNE - If the two values are not equal, then branch");
        bytecode_tool_tips.put("WIDE", "WIDE - Used for when the opcode is 16-bits long");
        bytecode_tool_tips.put("RETURN", "RETURN - Return void from method");
    }
    
    private void setupLabels(ClassLoader class_loader)
    {          
        bytecode = class_loader.getProgram();
                
        bytecode_tab.setContent(assembly_listview);  
        
        class_loader.printProgramBytecode();
        
        System.out.println("bytecode.size() : " + bytecode.size());
        
        assembly_pane_labels = new Label[bytecode.size()];
        
        int index = 0;
        
        for(int i = 0; i < class_loader.getNoOpcodes(); i++)
        {                        
            String mem_add = "0000x" + Integer.toHexString(class_loader.getOpcodeMemoryLocation(i)).toUpperCase();                                    
            assembly_pane_labels[i] = new Label(bytecode.get(index) + "\t" + mem_add + "\t" + bytecode.get(index+1));
            assembly_listview.getItems().add(assembly_pane_labels[i]);  
            index += 2;
        }
                                
        int line = 0;
        
        for(int i = 0; i < class_loader.getNoOpcodes(); i++)
        {     
            
            String word = class_loader.getOpcodeProgram(i);
                        
            if(bytecode_tool_tips.containsKey(word))
            {                                   
                Tooltip tool_tip = new Tooltip();
                tool_tip.setText((String)bytecode_tool_tips.get(word));        
                assembly_pane_labels[line].setTooltip(tool_tip);      
                line++;
            }            
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
        
    private void setupHex(ClassLoader input_file, Memory M)
    {
        hex_listview = new ListView();
        hex_listview.setId(CSS_ASSEMBLY_ID);
        hex_labels = new Label[input_file.getNoOpcodes()];
                
        for(int i = 0; i < input_file.getNoOpcodes(); i++)
        {
            String opcode = input_file.getOpcodeProgram(i);
            
            if(opcode.indexOf(' ') != -1)            
               opcode = opcode.substring(0, opcode.indexOf(' ')); 
                        
            int[] opcode_meta_data = (int[])input_file.getOpcodeMetaData().get(opcode);
            
            int mem_location = input_file.getOpcodeMemoryLocation(i);
            
            String hex_string = "";                                  
            
            for(int j = 0; j < opcode_meta_data[2]; j++)                                       
                hex_string += "0x" + Integer.toHexString(M.getMemoryAddress(j + mem_location)).toUpperCase() + " ";
                                                    
            hex_labels[i] = new Label(hex_string);
            
            hex_listview.getItems().add(hex_labels[i]);
        }               
    }         
    
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
    
}
