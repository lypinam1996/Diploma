package com.diploma.law.controllers;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

public class ClassNameHere
{

    public boolean infoBox(String infoMessage, String titleBar) {
        boolean res = true;
        Button c = new Button();
        JTextPane jTextPane = new JTextPane();
        jTextPane.setText(infoMessage);
        jTextPane.setSize(new Dimension(400,200));
        jTextPane.setPreferredSize(new Dimension(400,200));
        jTextPane.setFont(new Font("Arial", Font.BOLD, 40));
        int result = JOptionPane.showConfirmDialog(c, jTextPane, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            res = true;
        }
        if (result == JOptionPane.NO_OPTION){
            res = false;
        }
        return res;
    }
}