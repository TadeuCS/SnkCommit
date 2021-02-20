package com.tcs.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.TextField;

/**
 *
 * @author Tadeu-PC
 */
public class OUtils {

    public static String onlyDigitsValue(TextField field, Integer length) {
        String result = field.getText();
        maxField(field, length);
        if (result == null) {
            return null;
        }
        return result.replaceAll("[^0-9]", "");
    }

    public static String fieldToUPPER(TextField field) {
        field.textProperty().addListener((ov, oldValue, newValue) -> {
            field.setText(newValue.toUpperCase());
        });
        return field.getText();
    }

    public static String fieldToLOWER(TextField field) {
        field.textProperty().addListener((ov, oldValue, newValue) -> {
            field.setText(newValue.toLowerCase());
        });
        return field.getText();
    }

    public static void maxField(TextField textField, Integer length) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null || newValue.length() > length) {
                textField.setText(oldValue);
            }
        }
        );
    }
    
    public static Date addDays(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }
    public static Date addDays(int days, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }
}
