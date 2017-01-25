/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.rfrench.jvm.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*
    Program Title: UIMenu.java
    Author: Ryan French
    Created: 07-Nov-2016
    Version: 1.0
*/

public class UIMenu {
    
    private MenuBar menu_bar;
    
    public UIMenu(Stage primaryStage)
    {
        menu_bar = new MenuBar();
        menu_bar.prefWidthProperty().bind(primaryStage.widthProperty());
        Menu file_menu = new Menu("File");
        menu_bar.getMenus().add(file_menu);
        MenuItem open_file_item = new MenuItem("Open");
        
        open_file_item.setOnAction((ActionEvent event) -> 
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
        
        });
        
        file_menu.getItems().add(open_file_item);
    }       
    
    public MenuBar getMenuBar()
    {
        return menu_bar;
    }
}
