/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.kotteletfisk.todo_exer;

public enum ListCategory {
    DEFAULT,
    LOW,
    MID,
    HIGH;

    public static ListCategory parseFromStr(String x) throws IllegalArgumentException {
        switch (x) {
            case "1" -> {
                return ListCategory.LOW;
            }
            case "2" -> {
                return ListCategory.MID;
            }
            case "3" -> {
                return ListCategory.HIGH;
            }
            default -> {
                return ListCategory.DEFAULT;
            }
        }
    } 
}
