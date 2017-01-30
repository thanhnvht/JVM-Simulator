
package main.com.rfrench.jvm.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.com.rfrench.jvm.java.ExecutionEngine;

/*
    Program Title: UIMenu.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/


//Icons made by "http://www.flaticon.com/authors/madebyoliver" 
//Icons made by "http://www.flaticon.com/authors/google"  
//Icons made by "http://www.freepik.com" 
public class UIMenu {
    
    private MenuBar menu_bar;   
    private ExecutionEngine execution_engine;
    
    private Menu next_part_button;
    private Menu play_button;
    private Menu pause_button;
    
    public UIMenu(Stage primaryStage)
    {
        menu_bar = new MenuBar();
        menu_bar.prefWidthProperty().bind(primaryStage.widthProperty());

        
        addPlayButton();
        addPauseButton();
        addNextInstructionButton();

    }       
    
    private void addNextInstructionButton()
    {
        next_part_button = new Menu();
        ImageView next_part_icon = new ImageView("/main/com/rfrench/jvm/resources/images/forward-button.png");
        Tooltip.install(next_part_icon, new Tooltip("Next instruction"));
        next_part_button.setGraphic(next_part_icon);               
                
        menu_bar.getMenus().add(next_part_button);        
    }
    
    private void addPlayButton()
    {
        play_button = new Menu();
        ImageView next_part_icon = new ImageView("/main/com/rfrench/jvm/resources/images/play-button.png");
        play_button.setGraphic(next_part_icon);
        
        menu_bar.getMenus().add(play_button);
    }
    
    private void addPauseButton()
    {
        pause_button = new Menu();
        ImageView next_part_icon = new ImageView("/main/com/rfrench/jvm/resources/images/pause-button.png");
        pause_button.setGraphic(next_part_icon);
        
        menu_bar.getMenus().add(pause_button);
    }
    
    public MenuBar getMenuBar()
    {
        return menu_bar;
    }
    
    public Menu getNextInstructionButton()
    {
        return next_part_button;
    }
    
    public Menu getPlayButton()
    {
        return play_button;
    }
    
    public Menu getPauseButton()
    {
        return pause_button;
    }
}
