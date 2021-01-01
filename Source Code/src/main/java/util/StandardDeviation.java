/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Parastoo
 */
public class StandardDeviation {
    public static double  calculateSD(List<Double> scores){
        double sum = 0.0 ,standardDeviation = 0.0;
        int length = scores.size();
        for (double num: scores) {
            sum +=num; 
        }
        double mean = sum/length;
        for (double num:scores) {
            standardDeviation += Math.pow(num-mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
    }

    public static double sum(List<Double> list) {
        double sum = 0;

        for (double line:list) {
            sum = sum + line;

        }
        return sum;
    }
    
    public static double sumWithN(List<Double> list, int n) {
        double sum = 0;

        for (int i = 0; i < n; i++) {
            
            sum = sum + list.get(i);

        }
        return sum;
    }
    
     public static double sumHash(HashMap<String,Double> list) {
        double sum = 0;

        for (String line:list.keySet()) {
            sum = sum + list.get(line);
        }
        return sum;
    }

    public static double mean(List<Double> list) {
        double average = sum(list) / (double) list.size();
        return average;
    }
    public static double meanWithN(List<Double> list,int n) {
        Collections.sort(list,Collections.reverseOrder());
        if(n> list.size())
            n= list.size();
        double average = sumWithN(list,n) / (double) n;
        return average;
    }

    public static double variance(List<Double> list) {
        double z = 0;
        int y = 0;
        double x = 0;
        for (double word : list) {
            x = (double) list.get(y)* list.get(y);
            z = z + x;

            y++;
        }


        double var = (z - (sum(list) * sum(list)) / list.size()) / (list.size()-1);
        System.out.println(var);
        return var;
    }
}
