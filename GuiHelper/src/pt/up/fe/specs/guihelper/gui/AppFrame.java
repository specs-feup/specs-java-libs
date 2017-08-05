/*
 * Copyright 2010 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.guihelper.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import pt.up.fe.specs.guihelper.App;
import pt.up.fe.specs.guihelper.gui.BasePanels.TabbedPane;

/**
 * Frame of the SimpleGui.
 *
 * @author Joao Bispo
 */
public class AppFrame {

    public AppFrame(App application) {
        frameTitle = "";
        tabbedPane = new TabbedPane(application);
        createGui();
        /*
              SwingUtilities.invokeLater(new Runnable() {
         @Override
            public void run() {
                //Turn off metal's use of bold fonts
        		//UIManager.put("swing.boldMetal", Boolean.FALSE);
        		createGui();
            }
        });
         *
         */
    }

    public void setFrameTitle(String frameTitle) {
        // this.frameTitle = frameTitle;
        this.mainWindow.setTitle(frameTitle);
    }

    public void setFrameIcon(Image image) {
        this.mainWindow.setIconImage(image);
    }

    public void launchGui() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                // createAndShowGUI();
                showGui();
            }
        });
    }

    /**
     * Shows the GUI. For thread safety, this method should be invoked from the event dispatch thread.
     */
    // private void createAndShowGUI() {
    private void showGui() {
        /*
        //Create and set up the window.
        JFrame frame = new JFrame(frameTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setResizable(true);
        //Add content to the window.
        frame.add(tabbedPane, BorderLayout.CENTER);
        */
        // Display the window.
        // frame.pack();
        // frame.setVisible(true);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    /**
     * Creates the GUI.
     */
    private void createGui() {
        // Create and set up the window.
        // JFrame frame = new JFrame(frameTitle);
        mainWindow = new JFrame(frameTitle);

        JFrame frame = mainWindow;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(true);
        // Add content to the window.
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Resize window
        frame.setPreferredSize(new Dimension(AppFrame.PREFERRED_WIDTH, AppFrame.PREFERRED_HEIGHT));
    }

    public JFrame getMainWindow() {
        return mainWindow;
    }

    /**
     * INSTANCE VARIABLES
     */
    private String frameTitle;
    private TabbedPane tabbedPane;
    private JFrame mainWindow;

    public static final int PREFERRED_HEIGHT = 360;
    public static final int PREFERRED_WIDTH = 560;
}
