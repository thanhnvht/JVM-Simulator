/*
    MIT License

    Copyright (c) 2017 Ryan French

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/

package main.com.rfrench.jvm.java;

import javafx.stage.Stage;
import main.com.rfrench.jvm.controller.SceneController;
import main.com.rfrench.jvm.ui.MainScene;

/*
    Program Title: ExecutionEngine.java
    Author: Ryan French
    Created: 19-Oct-2016
    Version: 1.0
*/

public class ExecutionEngine   
{
    private SceneController scene_controller;        
    private MethodArea method_area;
            
    private int current_method_count = 0;
    
    private int PC;
    
    private boolean branched;
    private boolean method_invoked;
    private boolean method_return;
    private boolean move_tab;
    
    private boolean program_complete = false;
    private boolean pause_program = false;
                      

    public ExecutionEngine(MainScene main_scene, MethodArea method_area, Stage primaryStage, SceneController scene_controller) 
    {                
        this.scene_controller = scene_controller;                
        this.method_area = method_area;       
                
        move_tab = false;
        
        PC = 0;      
        
        main_scene.getFXMLController().setExecutionEngine(this);
    }
       
    
    public void executeInstruction()
    {
        Method m = method_area.getMethod(current_method_count);
        
        branched = false;
        method_invoked = false;
        method_return = false;
                            
        int NUMBER_OF_OPCODES = m.getNumberOfOpcodes();
                        
        if(PC < NUMBER_OF_OPCODES)
        {            
//            main_controller.updateRegisterLabels(PC);    
            
            String bytecode = Integer.toHexString(m.getMethodOpcodes().get(PC)).toUpperCase();

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
                case ("14"):  LDC2_W();        break;
                case ("15"):  ILOAD(-1);       break;  //ILOAD
                case ("1A"):  ILOAD(0);        break;  //ILOAD_0
                case ("1B"):  ILOAD(1);        break;  //ILOAD_1
                case ("1C"):  ILOAD(2);        break;  //ILOAD_2
                case ("1D"):  ILOAD(3);        break;  //ILOAD_3
                case ("36"):  ISTORE(-1);      break;  //ISTORE
                case ("37"):  LSTORE(-1);      break;
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

            highlightLine();
                                  
            PC++;                   
        }

        else
        {             
            scene_controller.hightlightLine(current_method_count, PC);
//            main_controller.updateRegisterLabels(PC); 
            System.out.println("Program Done!!");
            program_complete = true;
        }       
    }
    

    public void resetProgram()
    {     
             
    }
      
    private void highlightLine()
    {
        // Ensures method invokation changes tab on next button press
        if(move_tab) 
        {                        
            scene_controller.changeTab(current_method_count);  
            
            move_tab = false;
        }
        
        if(method_invoked)
        {
            int previous_method_count = current_method_count - 1;

            int button_press_count = (int) scene_controller.getButtonStack().pop();            

            button_press_count++;          
            
            scene_controller.getButtonStack().push(button_press_count);            
            scene_controller.hightlightLine(previous_method_count, button_press_count);            
            scene_controller.getButtonStack().push(-1);
            
            move_tab = true;
        }
        
        else if(method_return) // FIX SO HIGHLIGHTS RETURN
        {
            int button_press_count;
                       
            System.out.println("Return");
            
            int previous_method_count = current_method_count + 1;
            
            button_press_count = (int) scene_controller.getButtonStack().pop();            

            button_press_count++;          
            
            scene_controller.getButtonStack().push(button_press_count);
            scene_controller.getButtonStack().push(button_press_count);            
            scene_controller.hightlightLine(previous_method_count, button_press_count);                       
            scene_controller.getButtonStack().pop();
            
            if(current_method_count > 0)
            {   button_press_count = (int) scene_controller.getButtonStack().pop();
                //button_press_count = (int) main_scene_controller.getButtonStack().pop();   
                button_press_count++;   
                //main_scene_controller.getButtonStack().push(button_press_count); 
                scene_controller.getButtonStack().push(button_press_count);
                
                System.out.println("current_method: " + current_method_count);
                System.out.println("button_press: " + button_press_count);
            }
            
            move_tab = true;
        }

        else if(!branched)
        {            
            int button_press = (int) scene_controller.getButtonStack().pop();
            
            button_press++;
  
            scene_controller.getButtonStack().push(button_press);
            
            int button_press_count = (int) scene_controller.getButtonStack().peek();
                    
            scene_controller.hightlightLine(current_method_count, button_press_count);
        }
        
        scene_controller.updateRegister(PC);
    }
    
    /**
     * Pushes a value between 0 and 5 onto stack
     * @param value number t push onto stack
     */
    private void ICONST(int value)
    {
        method_area.pushOperandStack(value);

        scene_controller.ICONST(Integer.toString(value));    
    }
    
    /**
     * Pushes a byte (value between 0-255) onto stack
     */
    private void BIPUSH() 
    {   
        Method m = method_area.getMethod(current_method_count);
        
        PC++;              
        
        int value = m.getMethodOpcodes().get(PC);       
                     
        method_area.pushOperandStack(value);
                         
        scene_controller.BIPUSH(Integer.toString(value));
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
        
        int offset = (first_operand + second_operand) - 1;                            
        
        PC = offset;
        
        scene_controller.GOTO(offset, m.getMethodLineNumbers(), current_method_count);
        
        branched = true;
    }

    /**
     * Add two top values on stack together. Push the result onto the stack
     */
    private void IADD() 
    {        
        int value_1 = method_area.popOperandStack();        
        int value_2 = method_area.popOperandStack();
               
        int add_result = value_1 + value_2;
        
        method_area.pushOperandStack(add_result);
                             
        scene_controller.IADD();
    }

    private void LDC2_W()
    {
        Method m = method_area.getMethod(current_method_count);
        
        PC++;
        
        int operand = m.getMethodOpcodes().get(PC);
        
        String constant_pool_entry = method_area.getConstantPool().get(operand);
        
        constant_pool_entry = constant_pool_entry.replaceAll("\\s+","");
        
        String keyword = "Long";
        int keyword_length = keyword.length();
        
        //Remove long keyword
        constant_pool_entry = constant_pool_entry.substring(constant_pool_entry.indexOf(keyword) + keyword_length);
        
        //Remove 'l' character from end of number
        constant_pool_entry = constant_pool_entry.substring(0, constant_pool_entry.length() - 1);

        int constant_pool_long = Integer.parseInt(constant_pool_entry);
        
        method_area.pushOperandStack(constant_pool_long);
        
        scene_controller.LDC2_W(constant_pool_entry);
    }
    
    private void IAND()
    {
          
    }
        
    /**
     * IF_ICMPEQ Opcode Command
     * Branch to new location in program if the two values on the
     * top of operand stack are equal
     */
    private void IF_ICMPEQ() 
    {           
    
        
//        if (stack_element_1 == stack_element_2)       
//        {
//            calculateBranch(offset);       
//        }
                       
//        main_controller.IF_ICMPEQ();
    }
    
    private void ILOAD(int frame_index) 
    {               
        Method m = method_area.getMethod(current_method_count);
        
        int value = Integer.parseInt(m.getLocalVariable(frame_index));
        
        method_area.pushOperandStack(value);
        
        scene_controller.ILOAD(frame_index);
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
 
        scene_controller.ISTORE(current_method_count, frame_index, value);
    }
    
    private void LSTORE(int frame_index)
    {
        //SHOULD STORE INTO 2 LOCAL VARS INSTEAD OF ONE
        
        Method m = method_area.getMethod(current_method_count);
                        
        if(frame_index < 0)
        {
            PC++;            
            frame_index = method_area.getMethod(current_method_count).getMethodOpcodes().get(PC); 
        }
            
        String value = Integer.toString(method_area.popOperandStack());
         
        m.setLocalVariable(frame_index, value);
 
        scene_controller.ISTORE(current_method_count, frame_index, value);
    }
    
    private void ISUB() 
    {        
        int value_1 = method_area.popOperandStack();
        int value_2 = method_area.popOperandStack();
                     
        int sub_value = value_2 - value_1;
        
        method_area.pushCallStack(sub_value);
        
        scene_controller.ISUB();
    }

    private void LDC_W()
    {

         
    }    

    private void POP() 
    {
        method_area.popCallStack();
        
        scene_controller.POP();
    }
    
    private void SWAP()
    {        
        int value_1 = method_area.popCallStack();
        int value_2 = method_area.popCallStack();    
        
        method_area.pushOperandStack(value_1);
        method_area.pushOperandStack(value_2);
    }
    
    private void DUP()
    {        
        int value = method_area.popOperandStack();
        
        method_area.pushOperandStack(value);
        
        method_area.pushOperandStack(value);
    }
    
    private void IFEQ()
    {
      
        
//        if (x == 0)            
//        {
//            PC = (PC + offset - 3);
//        }
           
    }
    
    private void IFLT()
    {
        
//        if (x < 0)            
//        {
//            PC = (PC + offset - 3);
//        }
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
                        
        scene_controller.IINC(current_method_count, frame_index, value);
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

        int value_1 = method_area.popOperandStack();                    
        int value_2 = method_area.popOperandStack();
                
        if (value_2 >= value_1)
        {
            PC = offset - 1;                    
            branched = true;            
            scene_controller.GOTO(offset, m.getMethodLineNumbers(), current_method_count);
        }
        
        scene_controller.IF_ICMPEQ();
    }
    
    private void IF_ICMPNE()
    {

//        if (x != y)
//        {
//            calculateBranch(offset);
//        }
                
//       main_controller.IF_ICMPEQ(offset, m.getMethodLineNumbers());
    }
    
    
    private void RETURN()
    {                
        int previous_method_count = current_method_count;
    
        if(current_method_count > 0)
        {
            current_method_count--;
            PC = method_area.popCallStack();
        }
                
        scene_controller.RETURN(previous_method_count);
                        
        method_return = true;                
                      
    }
    
    private void ALOAD_0()
    {                
        String reference = method_area.getMethod(current_method_count).getMethodName();
        method_area.pushOperandStack(0); //have put 0 for now. will hav to change to address of reference
        scene_controller.ALOAD_0(reference);
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

        scene_controller.INVOKESPECIAL(current_stack_size, current_method_count, max_local_var);
        
        method_area.pushCallStack(PC);
        
        PC = -1;
        
        method_invoked = true;
    }
    
    public boolean isBranch()
    {
        return branched;
    }
    
    public boolean isMethodInvoked()
    {
        return method_invoked;        
    }
    
    public boolean isMethodReturn()
    {
        return method_return;
    }
    
    public boolean isPaused()
    {
        return pause_program;
    }
    
    public void changePause()
    {
        pause_program = !pause_program;
    }
    
    public boolean isProgramComplete()
    {
        return program_complete;
    }
}
