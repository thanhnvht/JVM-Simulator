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

package main.com.rfrench.jvm.ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;


public class BytecodeInfoPane 
{
    private TitledPane bytecode_info_pane;
    private ListView bytecode_info_listview;
        
    public BytecodeInfoPane(TitledPane bytecode_info_pane)
    {        
        this.bytecode_info_pane = bytecode_info_pane;
        setupListView();        
        setupPane();                                              
    }
    
    private void setupPane()
    {   
        bytecode_info_pane.setContent(bytecode_info_listview);        
        //bytecode_info_pane.setVvalue(1.0);
    }
    
    private void setupListView()
    {
        bytecode_info_listview = new ListView();
        //bytecode_info_listview.setMinHeight(MainScene.HEIGHT_TENTH * 40);
        bytecode_info_listview.setMinWidth(MainScene.WIDTH_TENTH * 100);     
                
        Label l = new Label("**************");
        
        bytecode_info_listview.getItems().add(l);
    }
    
    public void addByteCodeInfo(String bytecode_info_text)
    {
        int size = bytecode_info_listview.getItems().size();
        bytecode_info_listview.getItems().add(new Label(bytecode_info_text));
        bytecode_info_listview.scrollTo(size - 1);
    }
    
    public TitledPane getPane()
    {
        return bytecode_info_pane;
    }
}
