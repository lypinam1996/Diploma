package com.diploma.law.controllers;

import javax.swing.JOptionPane;
import java.awt.*;

public class ClassNameHere
{

    public boolean infoBox(String infoMessage, String titleBar) {
        boolean res = true;
        Button c = new Button();
        int result = JOptionPane.showConfirmDialog(c, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            res = true;
        }
        if (result == JOptionPane.NO_OPTION){
            res = false;
        }
        return res;
    }
}