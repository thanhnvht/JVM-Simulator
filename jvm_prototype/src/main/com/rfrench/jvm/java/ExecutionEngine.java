
package main.com.rfrench.jvm.java;

import controller.com.rfrench.jvm.java.MainSceneController;
import javafx.event.ActionEvent;
import ui.com.rfrench.jvm.java.MainScene;

/*
    Program Title: ExecutionEngine.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class ExecutionEngine 
{
    private final int HEX = 16;
    
    private MainSceneController main_scene_controller;
    
    private MethodArea method_area;
    private ClassLoader class_loader;
        
    private int current_method_count = 0;
    
    private Register LV;
    private int PC;
    private Register SP;
    private Register CPP;
    
    private int LV_size;     
            
    /**
     * Constructor for InstructionSet Logic
     * @param m
     * @param main_scene_controller
     * @param mem
     * @param class_loader 
     */
    public ExecutionEngine(MainScene m, MainSceneController main_scene_controller, MethodArea mem, ClassLoader class_loader) 
    {                
        this.main_scene_controller = main_scene_controller;
        
        this.method_area = mem;
                
        this.class_loader = class_loader;
              
        LV_size = class_loader.getMethods().get(current_method_count).getLocalSize();
            
        PC = 0;
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

        //Change PC to index into memory_opcodes in Method Class
            
        int NUMBER_OF_OPCODES = class_loader.getMethods().get(current_method_count).getNumberOfOpcodes();
                        
        if(PC < NUMBER_OF_OPCODES)
        {            
            main_scene_controller.updateRegisterLabels(PC, SP, LV, CPP);    

            String bytecode = Integer.toHexString(class_loader.getMethods().get(current_method_count).getMethodOpcodes().get(PC)).toUpperCase();

            System.out.println(bytecode);
            
            //class_loader.getByteCodeDetails().get(HEX)

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
                case ("2A"):  ALOAD_0();       break;
                case ("B7"):  INVOKESPECIAL(); break;
                case ("B8"):  INVOKESTATIC (); break;
            }                         

            PC++;                           
        }

        else
        {                 
         //   main_scene_controller.getMainScene().getAssembly().highlightLine(current_line); 
            main_scene_controller.updateRegisterLabels(PC, SP, LV, CPP); 

            System.out.println("Program Done!!");
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
        PC = 0;      
        
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
        if(value <= 0 || value >= 5)
        {
            method_area.pushOperandStack(value);
            
            //method_area.pushStackMem(SP, value);
        
            main_scene_controller.ICONST(Integer.toString(value));
        }
        
        else
        {
            System.out.println("Invalid ICONST Value : " + value);
        }       
    }
    
    /**
     * Pushes a byte (value between 0-255) onto stack
     */
    private void BIPUSH() 
    {        
        PC++;              
        
        int value = method_area.getMethod(current_method_count).getMethodOpcodes().get(PC);       
                        
        method_area.pushStackMem(SP, value);                        
        
        main_scene_controller.BIPUSH(Integer.toString(value));       
    }
    
    /**
     * Move current position in program according to the value
     * of the two parameters
     */
    private void GOTO() 
    {
        Method m = method_area.getMethod(current_method_count);
        
        PC++;
                                
        int first_operand = m.getMethodOpcodes().get(PC); 
                
        PC++;
        
        int second_operand = m.getMethodOpcodes().get(PC);
        
        int offset = first_operand + second_operand;    
        
        calculateBranch(offset);
    }

    /**
     * Add two top values on stack together. Push the result onto the stack
     */
    private void IADD() 
    {        
        int x = method_area.popStackMem(SP);
        
        int y = method_area.popStackMem(SP);
        
        int add_result = x + y;
        
        method_area.pushStackMem(SP, add_result);
                             
        main_scene_controller.IADD();                         
    }

    private void IAND() //NEED TO DO UI CHANGES
    {
        int x = method_area.popStackMem(SP);        
        
        int y = method_area.popStackMem(SP);    
                       
        int value = (x == y) ? 1 : 0;       
        
        method_area.pushStackMem(SP, value);             
    }
        
    /**
     * IF_ICMPEQ Opcode Command
     * Branch to new location in program if the two values on the
     * top of operand stack are equal
     */
    private void IF_ICMPEQ() 
    {           
        PC++;
        
        int first_operand = method_area.getMethodMemoryElement(PC);       
        
        PC++;
        
        int second_operand = method_area.getMethodMemoryElement(PC);
        
        int offset = first_operand + second_operand;         

        int stack_element_1 = method_area.popStackMem(SP);        
        
        int stack_element_2 = method_area.popStackMem(SP);        
        
        if (stack_element_1 == stack_element_2)       
        {
            calculateBranch(offset);       
        }
                       
        main_scene_controller.IF_ICMPEQ();
    }
    
    private void ILOAD(int frame_index) 
    {               
        Method m = method_area.getMethod(current_method_count);
        
        int value = Integer.parseInt(m.getLocalVariable(frame_index));
        
        method_area.pushOperandStack(value);
        
        main_scene_controller.ILOAD(frame_index);                                         
    }
    
    private void IOR() 
    {

    }
    
    private void ISTORE(int frame_index)
    {   
        Method m = method_area.getMethod(current_method_count);
                        
        if(frame_index < 0)
        {
            PC++;            
            frame_index = method_area.getMethod(current_method_count).getMethodOpcodes().get(PC); 
        }
            
        String value = Integer.toString(method_area.popOperandStack());
         
        m.setLocalVariable(frame_index, value);
         
                        
        main_scene_controller.ISTORE(frame_index, value);                             
    }
    
    private void ISUB() 
    {        
        int x = method_area.popStackMem(SP);       
        
        int y = method_area.popStackMem(SP);    
        
        int sub_value = y - x;
        
        method_area.pushStackMem(SP, sub_value);              
        
        main_scene_controller.ISUB();
    }

    private void LDC_W()
    {

         
    }    

    private void POP() 
    {
        method_area.pushStackMem(SP, 0);       
        
        main_scene_controller.POP();
    }
    
    private void SWAP()
    {
        int x = method_area.popStackMem(SP);        
        
        int y = method_area.popStackMem(SP);        
        
        method_area.pushStackMem(SP, x);        
        
        method_area.pushStackMem(SP, y);                                      
    }
    
    private void DUP()
    {
        int x = method_area.popStackMem(SP);        
        method_area.pushStackMem(SP, x);                       
    }
    
    private void IFEQ()
    {
        PC++;
        
        int offset = method_area.getMethodMemoryElement(PC);
        
        PC++;
        
        offset += method_area.getMethodMemoryElement(PC);
        
        int x = method_area.popStackMem(SP);        
        
        if (x == 0)            
        {
            PC = (PC + offset - 3);
        }
           
    }
    
    private void IFLT()
    {
        PC++;
        
        int offset = method_area.getMethodMemoryElement(PC);
        
        PC++;
        
        offset += method_area.getMethodMemoryElement(PC);
        
        int x = method_area.popStackMem(SP);
        
        if (x < 0)            
        {
            PC = (PC + offset - 3);
        }
    }
    
    private void IINC()
    {
        Method m = method_area.getMethod(current_method_count);
        
        PC++;
                
        int frame_index = method_area.getMethod(current_method_count).getMethodOpcodes().get(PC); 
        
        PC++;      
        
        int second_operand = m.getMethodOpcodes().get(PC);
        
        int current_value = Integer.parseInt(m.getLocalVariable(frame_index));
        
        String value = Integer.toString(current_value + second_operand);
        
        m.setLocalVariable(frame_index, value);
                        
        main_scene_controller.IINC(frame_index, value);                        
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
        Method m = method_area.getMethod(current_method_count);
                
        PC++;
                
        int offset = m.getMethodOpcodes().get(PC);
        
        PC++;   
        
        offset += m.getMethodOpcodes().get(PC);

        int x = method_area.popOperandStack();        
            
        int y = method_area.popOperandStack();
                
        if (y >= x)
        {
            calculateBranch(offset);
        }
        
        main_scene_controller.IF_ICMPEQ(); //CHANGE? SAME THiNGs happen to stack so...
    }
    
    private void IF_ICMPNE()
    {
        PC++;
        
        int offset = method_area.getMethodMemoryElement(PC);
        
        PC++;
        
        offset += method_area.getMethodMemoryElement(PC);        
        
        
        int x = method_area.popStackMem(SP);        
        
        int y = method_area.popStackMem(SP);        
        
        if (x != y)
        {
            calculateBranch(offset);
        }
                
        main_scene_controller.IF_ICMPEQ(); //CHANGE? SAME THiNGs happen to stack so...
    }
    
    private void calculateBranch(int offset)
    {
        PC = offset - 1; //-1 to negate the PC increment at end of execution                                     
    }
    
    private void RETURN()
    {
        Method old_method = method_area.getMethod(current_method_count);
        
        int max_local_var = old_method.getLocalSize();
        
        if(current_method_count > 0)
        {
            current_method_count--;
            PC = method_area.popCallStack();
        }
                
        main_scene_controller.RETURN(current_method_count, max_local_var);
                      
    }
    
    private void ALOAD_0()
    {
        
        
        String reference = method_area.getMethod(current_method_count).getLocalVariable(0);
        
        method_area.pushOperandStack(0); //have put 0 for now. will hav to change to address of reference
        
        main_scene_controller.ALOAD_0("0");
        
        System.out.println(reference);
    }
    
    private void INVOKESPECIAL()
    {
        INVOKESTATIC();      
    }
    
    private void INVOKESTATIC()
    {
        Method old_method = method_area.getMethod(current_method_count);
                
        PC++;
        int first_operand = old_method.getMethodOpcodes().get(PC);
        PC++;
        int second_operand = old_method.getMethodOpcodes().get(PC);
        
        //JVM Specifcation states Constant Pool start index is 1 rather than 0, hence -1 to value
        int value = (first_operand + second_operand) - 1; 
                        
        String symbolic_reference = method_area.getConstantPool().get(value);
                
        current_method_count++;
        
        Method new_method = method_area.getMethod(current_method_count);
        
        int current_stack_size = method_area.getOperandStackSize();             
        
        for(int i = 0; i < current_stack_size; i++)
        {
            int s_value = method_area.popOperandStack();
            
            String stack_value = Integer.toString(s_value);
            
            new_method.setLocalVariable(i, stack_value);
        }
        
        int max_local_var = new_method.getLocalSize();
        
        main_scene_controller.INVOKESPECIAL(current_stack_size, current_method_count, max_local_var);
        
        method_area.pushCallStack(PC);
        
        PC = -1;
    }
}
