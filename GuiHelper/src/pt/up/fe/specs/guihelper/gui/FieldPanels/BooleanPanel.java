/*
 *  Copyright 2010 SPeCS Research Group.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.guihelper.gui.FieldPanels;

import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSwing;

/**
 *
 * @author Joao Bispo
 */
public class BooleanPanel extends FieldPanel {
//public class BooleanPanel extends FieldPanel<Boolean> {

    private static final long serialVersionUID = 1L;
    
   public BooleanPanel(String labelName) {
      label = new JLabel(labelName+":");
      checkBox = new JCheckBox();

      add(label);
      add(checkBox);
      setLayout(new FlowLayout(FlowLayout.LEFT));
   }

   public JCheckBox getCheckBox() {
      return checkBox;
   }



   @Override
   public FieldType getType() {
      return FieldType.bool;
   }


   @Override
   public FieldValue getOption() {
         return FieldValue.create(Boolean.valueOf(checkBox.isSelected()), getType());
   }

   @Override
   public void updatePanel(Object option) {
   //public void updatePanel(Boolean option) {
      //final Boolean b = (Boolean)option;
      final Boolean b = SpecsStrings.parseBoolean(option.toString());
      if(b == null) {
	  SpecsLogs.warn("Could not update panel, expecting true or false, received '"+option+"'.");
	  return;
      }

      SpecsSwing.runOnSwing(new Runnable() {

         @Override
	public void run() {
            checkBox.setSelected(b);
         }
      });
   }

   @Override
   public JLabel getLabel() {
      return label;
   }

   /**
    * INSTANCE VARIABLES
    */
   private JLabel label;
   private JCheckBox checkBox;


}
