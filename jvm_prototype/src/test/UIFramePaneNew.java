/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 *
 * @author Ryan
 */
public class UIFramePaneNew 
{
    private ListView frame_listview;
    
    public UIFramePaneNew()
    {
        frame_listview = new ListView();
        frame_listview.setMinSize(200, 100);  

        frame_listview.getItems().add(new Label("Test"));
        
    }
    
    public ListView getFrameListView()
    {
        return frame_listview;
    }
}
