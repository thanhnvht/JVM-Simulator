
package main.com.rfrench.jvm.java;

import controller.com.rfrench.jvm.java.MainSceneController;
import javafx.event.ActionEvent;
import ui.com.rfrench.jvm.java.MainScene;

/*
    Program Title: InstructionSet.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class ExecutionEngine 
{
    private final int HEX = 16;
    
    private MainSceneController main_scene_controller;
    
    private Memory mem;
    private ClassLoader class_loader;
    
    private Register LV;
    private Register PC;
    private Register SP;
    private Register CPP;
    
    private int LV_size;     
    private int current_line = 0;
            
    /**
     * Constructor for InstructionSet Logic
     * @param m
     * @param main_scene_controller
     * @param mem
     * @param class_loader 
     */
    public ExecutionEngine(MainScene m, MainSceneController main_scene_controller, Memory mem, ClassLoader class_loader) 
    {                
        this.main_scene_controller = main_scene_controller;
        this.mem = mem;
                
        this.class_loader = class_loader;
       
       //LV_size = class_loader.getMaxLocalVariable();
       
        LV_size = class_loader.getMethods().get(0).getLocalSize();
        
        PC = new Register(0);                     
        LV = new Register(2000);        
        SP = new Register(LV.get() + LV_size); 
        CPP = new Register(0);
                        
        
        main_scene_controller.getMainScene().getButton().getNextInstructionButton().setOnAction((ActionEvent event) -> 
        {
            executeInstruction();
        }); 
        
        main_scene_controller.getMainScene().getButton().getResetProgramButton().setOnAction((ActionEvent event) -> 
        {
            resetProgram();
        });                        
    }
       
    /**
     * Read next bytecode in memory
     * Execute instruction based on what has been read
     */
    public void executeInstruction()
    {
        try
        {
            //Change PC to index into memory_opcodes in Method Class
            
            int NUMBER_OF_OPCODES = class_loader.getMethods().get(0).getNumberOfOpcodes();
            
            //if(PC.get() < class_loader.getTotalMemorySpotsUsed()-1)
            
            if(PC.get() < NUMBER_OF_OPCODES)
            {            
                main_scene_controller.updateRegisterLabels(PC, SP, LV, CPP);    
                
               // main_scene_controller.getMainScene().getAssembly().highlightLine(current_line);  

                //String bytecode = Integer.toHexString(mem.getMethodMemoryElement(PC.get())).toUpperCase();

                String bytecode = Integer.toHexString(class_loader.getMethods().get(0).getMethodOpcodes().get(PC.get())).toUpperCase();
                
                System.out.println(bytecode);
                
                
                //Change this to HashMap?
                switch(bytecode)
                {
                    case ("3") :  ICONST(0);       break;  //ICONST_0
                    case ("4") :  ICONST(1);       break;  //ICONST_1
                    case ("5") :  ICONST(2);       break;  //ICONST_2
                    case ("6") :  ICONST(3);       break;  //ICONST_3
                    case ("7") :  ICONST(4);       break;  //ICONST_4
                    case ("8") :  ICONST(5);       break;  //ICONST_5
                    case ("10"):  BIPUSH();        break;                                       
                    case ("15"):  ILOAD(-1);       break;  //ILOAD
                    case ("1A"):  ILOAD(0);        break;  //ILOAD_0
                    case ("1B"):  ILOAD(1);        break;  //ILOAD_1
                    case ("1C"):  ILOAD(2);        break;  //ILOAD_2
                    case ("1D"):  ILOAD(3);        break;  //ILOAD_3
                    case ("36"):  ISTORE(-1);      break;  //ISTORE
                    case ("3B"):  ISTORE(0);       break;  //ISTORE_0
                    case ("3C"):  ISTORE(1);       break;  //ISTORE_1
                    case ("3D"):  ISTORE(2);       break;  //ISTORE_2
                    case ("3E"):  ISTORE(3);       break;  //ISTORE_3
                    case ("60"):  IADD();          break;
                    case ("64"):  ISUB();          break;                    
                    case ("9F"):  IF_ICMPEQ();     break;                                   
                    case ("A7"):  GOTO();          break;
                    case ("59"):  DUP();           break;
                    case ("7E"):  IAND();          break;
                    case ("99"):  IFEQ();          break;
                    case ("9B"):  IFLT();          break;
                    case ("84"):  IINC();          break;
    //                case ("B6"):  INVOKEVIRTUAL(); break;
    //                case ("80"):  IOR();           break;
    //                case ("AC"):  IRETURN();       break;
                    case ("13"):  LDC_W();         break;
                    case ("00"):  NOP();           break;
                    case ("57"):  POP();           break;
                    case ("5F"):  SWAP();          break;
                    case ("A2"):  IF_ICMPGE();     break;                                 
                    case ("A0"):  IF_ICMPNE();     break;
    //                case ("0xC4"):  WIDE();          break;
                    case ("B1"):  RETURN();        break;
                }                         

                PC.inc();
                current_line++;                               
            }

            //No more instructions to read in program
            else
            {                 
             //   main_scene_controller.getMainScene().getAssembly().highlightLine(current_line); 
                main_scene_controller.updateRegisterLabels(PC, SP, LV, CPP); 
            }
        }
        
        catch(IllegalArgumentException e)
        {
            System.out.println("ERROR CAUGHT !");
            e.getMessage();
        }
    }
    
    /**
     * Reset Program
     * - Resets Registers
     * - Resets GUI
     * - Memory Reloaded
     */
    public void resetProgram()
    {
        SP.set(LV.get() + LV_size);         
        PC.set(0);        
        current_line = 0;
        
        main_scene_controller.getMainScene().getAssembly().highlightLine(-1);                   
        main_scene_controller.removeAllStack();               
        main_scene_controller.updateRegisterLabels(PC, SP, LV, CPP);
        main_scene_controller.getMainScene().getAssembly().getListView().getSelectionModel().select(0);                
    }
                
    /**
     * Pushes a value between 0 and 5 onto stack
     * @param value number t push onto stack
     */
    private void ICONST(int value)
    {
        if(value < 0 || value > 5)
        {
            throw new IllegalArgumentException();
        }
        
        mem.pushStackMem(SP, value);
        
        main_scene_controller.ICONST(Integer.toString(value));
    }
    
    /**
     * Pushes a byte (value between 0-255) onto stack
     */
    private void BIPUSH() 
    {        
        PC.inc();
        
        //int value = mem.getMethodMemoryElement(PC.get()); 
        
        int value = class_loader.getMethods().get(0).getMethodOpcodes().get(PC.get());
        
        mem.pushStackMem(SP, value);
                        
        main_scene_controller.BIPUSH(Integer.toString(value));       
    }
    
    /**
     * Move current position in program according to the value
     * of the two parameters
     */
    private void GOTO() 
    {
        PC.inc();
        
        //int first_parameter = mem.getMethodMemoryElement(PC.get());
        
        int first_operand = class_loader.getMethods().get(0).getMethodOpcodes().get(PC.get());
                
        PC.inc();
        
        int second_operand = class_loader.getMethods().get(0).getMethodOpcodes().get(PC.get());
        
        int offset = first_operand + second_operand;    
        
        calculateBranch(offset);
    }

    /**
     * Add two top values on stack together. Push the result onto the stack
     */
    private void IADD() 
    {        
        int x = mem.popStackMem(SP);
        
        int y = mem.popStackMem(SP);
        
        int add_result = x + y;
        
        mem.pushStackMem(SP, add_result);
                             
        main_scene_controller.IADD();                         
    }

    private void IAND() //NEED TO DO UI CHANGES
    {
        int x = mem.popStackMem(SP);        
        
        int y = mem.popStackMem(SP);    
                       
        int value = (x == y) ? 1 : 0;       
        
        mem.pushStackMem(SP, value);             
    }
        
    /**
     * IF_ICMPEQ Opcode Command
     * Branch to new location in program if the two values on the
     * top of operand stack are equal
     */
    private void IF_ICMPEQ() 
    {           
        PC.inc();
        
        int first_parameter = mem.getMethodMemoryElement(PC.get());       
        
        PC.inc();
        
        int second_parameter = mem.getMethodMemoryElement(PC.get());
        
        int offset = first_parameter + second_parameter;         

        int stack_element_1 = mem.popStackMem(SP);        
        
        int stack_element_2 = mem.popStackMem(SP);        
        
        if (stack_element_1 == stack_element_2)       
        {
            calculateBranch(offset);       
        }
                       
        main_scene_controller.IF_ICMPEQ();
    }
    
    private void ILOAD(int frame_index) 
    {               
        if(frame_index < 0)
        {            
            PC.inc();  
            
            frame_index = mem.getMethodMemoryElement(PC.get());                
        }
                        
        int value = mem.getLocalFrameElement(LV, frame_index);        
        
        mem.pushStackMem(SP, value);
        
        main_scene_controller.ILOAD(frame_index);                                         
    }
    
    private void IOR() 
    {

    }
    
    private void ISTORE(int frame_index)
    {        
        if(frame_index < 0)
        {
            PC.inc();
            
            //frame_index = mem.getMethodMemoryElement(PC.get());
            
            frame_index = class_loader.getMethods().get(0).getMethodOpcodes().get(PC.get());
        }
            
        int value = mem.popStackMem(SP);   
        
        mem.setLocalFrameElement(LV, frame_index, value);        
                        
        main_scene_controller.ISTORE(frame_index, Integer.toString(value));                
    }
    
    private void ISUB() 
    {        
        int x = mem.popStackMem(SP);       
        
        int y = mem.popStackMem(SP);    
        
        int sub_value = y - x;
        
        mem.pushStackMem(SP, sub_value);              
        
        main_scene_controller.ISUB();
    }

    private void LDC_W()
    {
//        PC.inc();
//        int constant_pool_index = mem.getMethodMemoryElement(PC);
//        int value = mem.getConstantPool(constant_pool_index);
//        
//        mem.pushStackMem(SP, value);        
//        mem.setConstantPool(constant_pool_index, 0);
//        
//        main_stage.getStack().push(Integer.toString(value), value);
//        stack_size++;
         
    }    

    private void POP() 
    {
        mem.pushStackMem(SP, 0);       
        
        main_scene_controller.POP();
    }
    
    private void SWAP()
    {
        int x = mem.popStackMem(SP);        
        
        int y = mem.popStackMem(SP);        
        
        mem.pushStackMem(SP, x);        
        
        mem.pushStackMem(SP, y);                                      
    }
    
    private void DUP()
    {
        int x = mem.popStackMem(SP);        
        mem.pushStackMem(SP, x);                       
    }
    
    private void IFEQ()
    {
        PC.inc();
        
        int offset = mem.getMethodMemoryElement(PC.get());
        
        PC.inc();
        
        offset += mem.getMethodMemoryElement(PC.get());
        
        int x = mem.popStackMem(SP);        
        
        if (x == 0)            
        {
            PC.set(PC.get() + offset - 3);
        }
           
    }
    
    private void IFLT()
    {
        PC.inc();
        
        int offset = mem.getMethodMemoryElement(PC.get());
        
        PC.inc();
        
        offset += mem.getMethodMemoryElement(PC.get());
        
        int x = mem.popStackMem(SP);
        
        if (x < 0)            
        {
            PC.set(PC.get() + offset - 3);
        }
    }
    
    private void IINC()
    {
        PC.inc();
        
        //int frame_index = mem.getMethodMemoryElement(PC.get());
        
        int frame_index = class_loader.getMethods().get(0).getMethodOpcodes().get(PC.get());
        
        PC.inc();
        
        //int value = mem.getMethodMemoryElement(PC.get()) + mem.getLocalFrameElement(LV, frame_index); 
        
        int value = class_loader.getMethods().get(0).getMethodOpcodes().get(PC.get()) + mem.getLocalFrameElement(LV, frame_index);
        
        mem.setLocalFrameElement(LV, frame_index, value);
        
        main_scene_controller.IINC(frame_index, Integer.toString(value));                        
    }
    
    private void INVOKEVIRTUAL()
    {
        
    }
    
    private void IRETURN()
    {
        
    }
    
    private void NOP()
    {
        
    }
          
    private void WIDE()
    {
        
    }
    
    private void IF_ICMPGE()
    {
        PC.inc();
        
        //int offset = mem.getMethodMemoryElement(PC.get());
        
        int offset = class_loader.getMethods().get(0).getMethodOpcodes().get(PC.get());
        
        PC.inc();
        
        //offset += mem.getMethodMemoryElement(PC.get());      
        
        offset += class_loader.getMethods().get(0).getMethodOpcodes().get(PC.get());

        int x = mem.popStackMem(SP);        
        
        int y = mem.popStackMem(SP);        
        
        if (y >= x)
        {
            calculateBranch(offset);
        }
        
        main_scene_controller.IF_ICMPEQ(); //CHANGE? SAME THiNGs happen to stack so...
    }
    
    private void IF_ICMPNE()
    {
        PC.inc();
        
        int offset = mem.getMethodMemoryElement(PC.get());
        
        PC.inc();
        
        offset += mem.getMethodMemoryElement(PC.get());        
        
        
        int x = mem.popStackMem(SP);        
        
        int y = mem.popStackMem(SP);        
        
        if (x != y)
        {
            calculateBranch(offset);
        }
                
        main_scene_controller.IF_ICMPEQ(); //CHANGE? SAME THiNGs happen to stack so...
    }
    
    private void calculateBranch(int offset)
    {
            //PC.set(offset - 1);             
        
            System.out.println("Branch to: " + offset);
            
            //String line_number = (String)assembly_data.getProgram().get((current_line * 2) + 1);        
            
            //String test_line_number = (String)assembly_data.
            
            //line_number = line_number.replaceAll("[^0-9]", "");        
            
            //System.out.println("line number: " + line_number);
            
            //Change to get line number from another array
            
            
            
            //current_line = (int)assembly_data.getLineNumbers().get(line_number) - 1;
            
            //System.out.println("'Current Line' " + current_line);
    }
    
    private void RETURN()
    {
        
    }
}
