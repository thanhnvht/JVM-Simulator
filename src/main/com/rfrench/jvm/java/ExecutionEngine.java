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

import java.util.ArrayList;
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
    
    private int PC;
    
    private boolean branched;
    private int current_method_count = 0;
    private Heap heap;
    private MethodArea method_area;
    private boolean method_invoked;
    private boolean method_return;
    private boolean move_tab;
    private boolean pause_program = false;
    
    private boolean program_complete = false;
    private SceneController scene_controller;
                      

    public ExecutionEngine(MainScene main_scene, MethodArea method_area, Heap heap) 
    {                
        this.scene_controller = main_scene.getFXMLController();      
        
        this.method_area = method_area;       
        this.heap = heap;
        
        move_tab = false;
        
        PC = 0;      
        
        main_scene.getFXMLController().setExecutionEngine(this);
    }
    private void ALOAD_0()
    {
        //String reference = method_area.getMethod(current_method_count).getMethodName();
        String reference = method_area.getMethod(current_method_count).getLocalVariable(0);
        
        System.out.println("Reference: " + reference);
        
        method_area.pushOperandStack(0); //have put 0 for now. will hav to change to address of reference
        scene_controller.ALOAD_0(reference);
    }
    
    private void BASTORE()
    {
        IASTORE();    
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
    private void CALOAD()
    {
        IALOAD();
    }
    private void CASTORE()
    {
        IASTORE();
    }
    private void DUP()
    {
        int value = method_area.popOperandStack();
        
        method_area.pushOperandStack(value);              
        
        method_area.pushOperandStack(value);
        
        scene_controller.DUP();
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
                             
        String arithmetic_symbol = "+";
        scene_controller.IARITHMETIC(arithmetic_symbol);
    }
    private void IALOAD()
    {
        int index = method_area.popOperandStack();
        int array_reference = method_area.popOperandStack();
        
        int array_value = heap.getElement(index, array_reference);
        method_area.pushOperandStack(array_value);
        
        String array_value_string = Integer.toString(array_value);
        scene_controller.arrayLoad(array_value_string);
    }
    

    private void IAND()
    {

    }
    private void IASTORE()
    {
        int value = method_area.popOperandStack();
        int index = method_area.popOperandStack();
        int array_reference = method_area.popOperandStack();
        
        heap.setElement(value, index, array_reference);
        
        scene_controller.arrayStore();
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
    private void IDIV()
    {
        int value_1 = method_area.popOperandStack();
        int value_2 = method_area.popOperandStack();
        
        int div_result = value_2 / value_1;
        
        method_area.pushOperandStack(div_result);
        
        String arithmetic_symbol = "/";
        scene_controller.IARITHMETIC(arithmetic_symbol);
    }
            
    private void IFEQ()
    {
        int offset = branchGetOffset();
        int number_to_pop = 1;        
        int[] operand_stack_values = getValuesStack(number_to_pop);
                
        if (operand_stack_values[0] == 0)
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionZero();
    }
    private void IFGE()
    {
        int offset = branchGetOffset();
        int number_to_pop = 1;        
        int[] operand_stack_values = getValuesStack(number_to_pop);
        
        if (operand_stack_values[0] <= 0)
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionZero();       
    }
    private void IFGT()
    {
        int offset = branchGetOffset();
        int number_to_pop = 1;        
        int[] operand_stack_values = getValuesStack(number_to_pop);
        
        if (operand_stack_values[0] > 0)
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionZero();
    }
    
    private void IFLE()
    {
        int offset = branchGetOffset();
        int number_to_pop = 1;        
        int[] operand_stack_values = getValuesStack(number_to_pop);
                
        if (operand_stack_values[0] <= 0)
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionZero();
    }
    private void IFLT()
    {
        int offset = branchGetOffset();
        int number_to_pop = 1;        
        int[] operand_stack_values = getValuesStack(number_to_pop);
        
        if (operand_stack_values[0] < 0)
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionZero();
    }
    private void IFNE()
    {
        int offset = branchGetOffset();
        int number_to_pop = 1;        
        int[] operand_stack_values = getValuesStack(number_to_pop);
        
        if (operand_stack_values[0] != 0)
        {
            branchTrue(offset);
            
        }
        
        scene_controller.branchComparisionZero();
    }
        
    private void IF_ICMPEQ() 
    {           
        int offset = branchGetOffset();

        int number_to_pop = 2;
        
        int[] operand_stack_values = getValuesStack(number_to_pop);
                
        if (operand_stack_values[1] == operand_stack_values[0])
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionNonZero();
    }
    
    
    private void IF_ICMPGE()
    {                                   
        int offset = branchGetOffset();

        int number_to_pop = 2;
        
        int[] operand_stack_values = getValuesStack(number_to_pop);
                
        if (operand_stack_values[1] >= operand_stack_values[0])
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionNonZero();
    }
        
    private void IF_ICMPGT()
    {
        int offset = branchGetOffset();

        int number_to_pop = 2;
        
        int[] operand_stack_values = getValuesStack(number_to_pop);
                
        if (operand_stack_values[1] > operand_stack_values[0])
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionNonZero();
    }
    
    private void IF_ICMPLE()
    {
        int offset = branchGetOffset();

        int number_to_pop = 2;
        
        int[] operand_stack_values = getValuesStack(number_to_pop);
                
        if (operand_stack_values[1] <= operand_stack_values[0])
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionNonZero();
    }
    
    private void IF_ICMPLT()
    {
        int offset = branchGetOffset();

        int number_to_pop = 2;
        
        int[] operand_stack_values = getValuesStack(number_to_pop);
                
        if (operand_stack_values[1] < operand_stack_values[0])
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionNonZero();
    }
    private void IF_ICMPNE()
    {
        int offset = branchGetOffset();
        
        int number_to_pop = 2;
        
        int[] operand_stack_values = getValuesStack(number_to_pop);
        
        if (operand_stack_values[1] != operand_stack_values[0])
        {
            branchTrue(offset);
        }
        
        scene_controller.branchComparisionNonZero();
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
        
                
    private void ILOAD(int frame_index) 
    {                       
        Method m = method_area.getMethod(current_method_count); 
        
        System.out.println("ILOAD: frame index: " + frame_index);
        
        int value = Integer.parseInt(m.getLocalVariable(frame_index));
        
        method_area.pushOperandStack(value);
        
        scene_controller.ILOAD(frame_index);
    }
    private void IMUL()
    {
        int value_1 = method_area.popOperandStack();
        int value_2 = method_area.popOperandStack();
        
        int mul_result = value_2 * value_1;
        
        method_area.pushOperandStack(mul_result);
        
        String arithmetic_symbol = "*";
        scene_controller.IARITHMETIC(arithmetic_symbol);
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
    private void INVOKEVIRTUAL()
    {
        
    }
    
    private void IOR() 
    {

    }
    private void IREM()
    {
        int value_1 = method_area.popOperandStack();
        int value_2 = method_area.popOperandStack();
        
        int remainder_result = value_2 % value_1;
        
        method_area.pushOperandStack(remainder_result);
        
        String arithmetic_symbol = "%";
        scene_controller.IARITHMETIC(arithmetic_symbol);
    }
    private void IRETURN()
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
    private void ISUB()
    {
        int value_1 = method_area.popOperandStack();
        int value_2 = method_area.popOperandStack();
        
        int sub_value = value_2 - value_1;
        
        method_area.pushOperandStack(sub_value);
        
        String arithmetic_symbol = "-";
        scene_controller.IARITHMETIC(arithmetic_symbol);
    }    
    private void LASTORE()
    {
        IASTORE();
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

    private void LDC_W()
    {

         
    }    
    private void LOOKUPSWITCH()
    {
        ArrayList<Integer> current_method_opcodes = method_area.getMethod(current_method_count).getMethodOpcodes();
        ArrayList<Integer> switch_cases = new ArrayList<Integer>();
        ArrayList<Integer> switch_branches = new ArrayList<Integer>();
        
        PC++;
        int number_of_switch_cases = current_method_opcodes.get(PC); // -1 for the hidden default switch case
        
        setupSwitchTables(number_of_switch_cases, switch_cases, switch_branches);      
        
        checkSwitchTable(number_of_switch_cases, switch_cases, switch_branches);
        
        scene_controller.LOOKUPSWITCH(switch_cases, switch_branches);
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
    private void NEWARRAY()
    {
        final int ARRAY_SIZE = method_area.popOperandStack();
        
        ArrayList<Integer> current_method_opcodes = method_area.getMethod(current_method_count).getMethodOpcodes();
        
        PC++;
        final int ARRAY_TYPE_CODE = current_method_opcodes.get(PC);
        
        int array_reference = heap.addArray(ARRAY_TYPE_CODE, ARRAY_SIZE);
        method_area.pushOperandStack(array_reference);
        
        String array_reference_string = "Array Ref: " + Integer.toString(array_reference);
        scene_controller.NEWARRAY(array_reference_string);
    }    
    
    
    private void NOP()
    {
        
    }
    private void POP()
    {
        method_area.popCallStack();
        
        scene_controller.POP();
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
    private void SIPUSH()
    {
        Method m = method_area.getMethod(current_method_count);
        
        PC++;
        
        int value = m.getMethodOpcodes().get(PC);
        PC++;
        value += m.getMethodOpcodes().get(PC);
        
        method_area.pushOperandStack(value);
        
        scene_controller.BIPUSH(Integer.toString(value));
    }
    private void SWAP()
    {
        int value_1 = method_area.popCallStack();
        int value_2 = method_area.popCallStack();
        
        method_area.pushOperandStack(value_1);
        method_area.pushOperandStack(value_2);
    }
    private void WIDE()
    {
        
    }
    private int branchGetOffset()
    {
        Method m = method_area.getMethod(current_method_count);             
        
        PC++;
        int offset = m.getMethodOpcodes().get(PC);
        
        PC++;
        offset += m.getMethodOpcodes().get(PC);
        
        return offset;
    }
    private void branchTrue(int offset)
    {
        Method m = method_area.getMethod(current_method_count);
        PC = offset - 1;
        branched = true;
        scene_controller.GOTO(offset, m.getMethodLineNumbers(), current_method_count);
    }
    public void changePause()
    {
        pause_program = !pause_program;
    }
    
    
    private void checkSwitchTable(int number_of_switch_cases, ArrayList<Integer> switch_cases, ArrayList<Integer> switch_branches)
    {
        int switch_condition_value = method_area.popOperandStack(); 
        
        boolean switch_case_found = false;     
                        
        for(int i = 0; i < number_of_switch_cases - 1; i++)
        {
            int switch_case = switch_cases.get(i);
            
            if(switch_case == switch_condition_value)
            {
                int switch_branch_value = switch_branches.get(i) - 1;
                PC = switch_branch_value;
                switch_case_found = true;
            }
        }
        
        if(!switch_case_found)
        {
            int switch_branch_value = switch_branches.get(number_of_switch_cases - 1);
            switch_branch_value -= 1;
            PC = switch_branch_value;
        }
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
            String bytecode = Integer.toHexString(m.getMethodOpcodes().get(PC)).toUpperCase();
            
            //Change this to HashMap?
            switch(bytecode)
            {
                case ("2") :  ICONST(-1);      break;  //ICONST_M1
                case ("3") :  ICONST(0);       break;  //ICONST_0
                case ("4") :  ICONST(1);       break;  //ICONST_1
                case ("5") :  ICONST(2);       break;  //ICONST_2
                case ("6") :  ICONST(3);       break;  //ICONST_3
                case ("7") :  ICONST(4);       break;  //ICONST_4
                case ("8") :  ICONST(5);       break;  //ICONST_5
                case ("10"):  BIPUSH();        break;
                case ("11"):  SIPUSH();        break;
                case ("14"):  LDC2_W();        break;
                case ("15"):  ILOAD(-1);       break;  //ILOAD
                case ("1A"):  ILOAD(0);        break;  //ILOAD_0
                case ("1B"):  ILOAD(1);        break;  //ILOAD_1
                case ("1C"):  ILOAD(2);        break;  //ILOAD_2
                case ("1D"):  ILOAD(3);        break;  //ILOAD_3
                case ("2E"):  IALOAD();        break;
                case ("34"):  CALOAD();        break;
                case ("36"):  ISTORE(-1);      break;  //ISTORE
                case ("37"):  LSTORE(-1);      break;
                case ("3B"):  ISTORE(0);       break;  //ISTORE_0
                case ("3C"):  ISTORE(1);       break;  //ISTORE_1
                case ("3D"):  ISTORE(2);       break;  //ISTORE_2
                case ("3E"):  ISTORE(3);       break;  //ISTORE_3
                case ("4F"):  IASTORE();       break;
                case ("50"):  LASTORE();       break;
                case ("54"):  BASTORE();       break;
                case ("55"):  CASTORE();       break;
                case ("60"):  IADD();          break;
                case ("64"):  ISUB();          break;
                case ("68"):  IMUL();          break;
                case ("6C"):  IDIV();          break;
                case ("70"):  IREM();          break;
                case ("A7"):  GOTO();          break;
                case ("59"):  DUP();           break;
                case ("84"):  IINC();          break;
                case ("7E"):  IAND();          break;
                case ("99"):  IFEQ();          break;
                case ("9A"):  IFNE();          break;
                case ("9B"):  IFLT();          break;
                case ("9C"):  IFGE();          break;
                case ("9D"):  IFGT();          break;
                case ("9E"):  IFLE();          break;
//                case ("B6"):  INVOKEVIRTUAL(); break;
//                case ("80"):  IOR();           break;
//                case ("AC"):  IRETURN();       break;
                case ("13"):  LDC_W();         break;
                case ("00"):  NOP();           break;
                case ("57"):  POP();           break;
                case ("5F"):  SWAP();          break;
                case ("A2"):  IF_ICMPGE();     break;
                case ("A3"):  IF_ICMPGT();     break;
                case ("A4"):  IF_ICMPLE();     break;
                case ("A1"):  IF_ICMPLT();     break;
                case ("A0"):  IF_ICMPNE();     break;
                case ("9F"):  IF_ICMPEQ();     break;
//                case ("0xC4"):  WIDE();          break;
                case ("B1"):  RETURN();        break;
                case ("2A"):  ALOAD_0();       break;
                case ("B7"):  INVOKESPECIAL(); break;
                case ("B8"):  INVOKESTATIC();  break;
                case ("BC"):  NEWARRAY();      break;
                case ("AB"):  LOOKUPSWITCH();  break;
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
    private int[] getValuesStack(int number_to_pop)
    {
        int[] popped_values = new int[number_to_pop];
        
        for(int i = 0; i < number_to_pop; i++)
        {
            popped_values[i] = method_area.popOperandStack();
        }
        
        return popped_values;
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
    
    
    public boolean isProgramComplete()
    {
        return program_complete;
    }
    public void resetProgram()
    {
        
    }
    private void setupSwitchTables(int number_of_switch_cases, ArrayList<Integer> switch_cases, ArrayList<Integer> switch_branches)
    {
        ArrayList<Integer> current_method_opcodes = method_area.getMethod(current_method_count).getMethodOpcodes();
        
        final int NUMBER_OF_PADDED_ENTRIES = 3;
        
        for(int i = 0; i < number_of_switch_cases; i++)
        {
            PC += NUMBER_OF_PADDED_ENTRIES;
            PC++;
            int switch_case = current_method_opcodes.get(PC);
            switch_cases.add(switch_case);
            
            PC += NUMBER_OF_PADDED_ENTRIES;
            PC++;
            int switch_branch_value = current_method_opcodes.get(PC);
            switch_branches.add(switch_branch_value);
        }
    }
    
}
