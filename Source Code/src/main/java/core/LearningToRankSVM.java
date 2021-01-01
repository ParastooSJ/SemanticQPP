/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import query_performance_prediction.Query_performance_prediction;

/**
 *
 * @author Parastoo
 */
public class LearningToRankSVM {
    
    public String path;
    public void createSplits(String dataset) throws FileNotFoundException, IOException{
        File[] files = new File("./crossfold/"+dataset+"/normalizedresult/").listFiles();
        int length = 120;
        int fn = files.length;
        path = "./crossfold/"+dataset+"/foldsvmList/foldsvm-"+length+"-"+fn+"-"+dataset+"/";
        boolean bool = new File(path).mkdir();
        
        
        for (int i = 1; i <= length; i++) {
            createTrainTest(i, dataset);
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path+"/train.sh")));
        for (int i = 1; i <= length; i++) {
            for (int j = 1; j <= 2; j++) {
                String line = "/cygdrive/c/Users/Parastoo/Documents/NetBeansProjects/svmrank/svm_rank_learn.exe -c 0.01 "+
                        i+"/"+j+"/train.dat "+i+"/"+j+"/model.dat  &&^";
                bw.write(line+"\n");
            }
        }
        bw.close();
        
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(path+"/test.sh")));
        for (int i = 1; i <= length; i++) {
            for (int j = 1; j <= 2; j++) {
                String line = "/cygdrive/c/Users/Parastoo/Documents/NetBeansProjects/svmrank/svm_rank_classify.exe "+
                        i+"/"+j+"/test.dat "+i+"/"+j+"/model.dat "+i+"/"+j+"/prediction.txt  &&^";
                bw2.write(line+"\n");
            }
        }
//        File source = new File("./crossfold/"+dataset+"/normalizedresult/");
//        File dest = new File(path);
//        Files.copy(source,dest);
        bw2.close();
        
    }
    
    public void createTrainTest(int split, String dataset) throws FileNotFoundException{
            
        try {
            File[] files = new File("./crossfold/"+dataset+"/normalizedresult/").listFiles();
            List<HashMap<String, Double>> features = new ArrayList<>();
            
            for(File f: files){
                features.add(readPerfFromFils(f));
            }
            
            String mapFile = "./crossfold/"+dataset+"/map/map-ql-"+dataset+".txt";
            HashMap<String, Double> map = readPerfFromFile(mapFile,"map");
            
            
            List<Integer> train = new ArrayList<>();
            List<Integer> test = new ArrayList<>();
            
            HashMap<String, Double> mapTrain = new HashMap<>();
            HashMap<String, Double> mapTest = new HashMap<>();
            
            
            for (int i = 0; i < map.size()/2; ) {
                Random rand = new Random();
                int rand_int = rand.nextInt(700);
                if(map.containsKey(Integer.toString(rand_int)) && !train.contains(rand_int))
                {
                    train.add(rand_int);
                    mapTrain.put(Integer.toString(rand_int), map.get(Integer.toString(rand_int)));
                    i++;
                }
            }

            for (String key: map.keySet()) {
                int number = Integer.parseInt(key);
                if(!train.contains(number))
                {
                    test.add(number);
                    mapTest.put(Integer.toString(number), map.get(Integer.toString(number)));
                }
            }
            mapTrain = sortByValue(mapTrain);
            mapTest = sortByValue(mapTest);
            
            createFold(train,test,mapTrain,mapTest,features,split,dataset);
        } catch (IOException ex) {
            Logger.getLogger(LearningToRankSVM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static HashMap<String, Double> readPerfFromFils(File file) throws FileNotFoundException {
        ArrayList<Integer> exceptionalQueries = new ArrayList<Integer>(
            Arrays.asList(3,40,72,100,119,121,137,205,231,307,312,313,317,319,326,328,346,379,395,408,417,418,424,428,435,436,438,446,638,649,659,667,670,672,689));
        
        HashMap<String, Double> result = new HashMap<>();
        Scanner in = new Scanner(file);
        while (in.hasNextLine()){
            try{
                String qid = in.next();
                double val = in.nextDouble();
                if(!exceptionalQueries.contains(Integer.parseInt(qid)))
                    result.put(qid,val);
            }
            catch(Exception ex){
            }
        }
        in.close();
        return result;
    }
    
    public static HashMap<String, Double> readPerfFromFile (String filename, String targetMetric) throws FileNotFoundException {
        ArrayList<Integer> exceptionalQueries = new ArrayList<Integer>(
            Arrays.asList(3,40,72,100,119,121,137,205,231,307,312,313,317,319,326,328,346,379,395,408,417,418,424,428,435,436,438,446,638,649,659,667,670,672,689));
        
        HashMap<String, Double> result = new HashMap<>();
        Scanner in = new Scanner(new File(filename));
        while (in.hasNextLine()){
            String metric = in.next();
            String qid = in.next();
            if (qid.equals("all"))
                break;
            double val = in.nextDouble();
            if (metric.equals(targetMetric) && !exceptionalQueries.contains(Integer.parseInt(qid)) ){
                result.put(qid,val);
            }
        }
        in.close();
        return result;
    }

    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm){ 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Double> > list = 
               new LinkedList<Map.Entry<String, Double> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() { 
            public int compare(Map.Entry<String, Double> o1,  
                               Map.Entry<String, Double> o2) 
            { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>(); 
        double count = 1;
        for (Map.Entry<String, Double> aa : list) { 
            temp.put(aa.getKey(), count); 
            count++;
        } 
        return temp; 
    } 

    private void createFold(List<Integer> train, List<Integer> test, HashMap<String, Double> mapTrain, HashMap<String, Double> mapTest, List<HashMap<String, Double>> features, int split, String dataset) throws IOException {
        File trainFile = new File("./crossfold/train.dat");
        File testFile = new File("./crossfold/test.dat");
        
        writeInfile(trainFile, train, features, mapTrain);
        writeInfile(testFile, test, features, mapTest);
        
        createfoldFile(trainFile, testFile, train, test, 1, split, dataset);
        createfoldFile(testFile, trainFile, test, train,2, split, dataset);
        
    }

    private void writeInfile(File file, List<Integer> list, List<HashMap<String, Double>> features, HashMap<String, Double> map) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (Integer entry : list) {
            double mapValued = map.get(Integer.toString(entry));
            int mapValue = (int) mapValued;
            String line ="" + mapValue +" qid:1 ";
            int featureNo = 1;
            for (HashMap<String, Double> feature: features) {
                line += featureNo + ":" + feature.get(Integer.toString(entry)) + " ";
                featureNo ++;
            }
            bw.write(line+"\n");
        }
        bw.close();
    }

    private void createfoldFile(File file1, File file2, List<Integer> train, List<Integer> test, int i, int split, String dataset) throws IOException {
        String dir = path+split+"/";
        boolean bool = new File(dir).mkdir();
        dir = path+split+"/"+i+"/";
        bool = new File(dir).mkdir();
        Files.copy(file1, new File(dir + "train.dat"));
        Files.copy(file2, new File(dir + "test.dat"));
        BufferedWriter bw  = new BufferedWriter(new FileWriter(new File(dir+"train.txt")));
        for (Integer entry:train) {
            bw.write(entry+"\n");
        }
        bw.close();
        bw  = new BufferedWriter(new FileWriter(new File(dir+"test.txt")));
        for (Integer entry:test) {
            bw.write(entry+"\n");
        }
        bw.close();
        bw  = new BufferedWriter(new FileWriter(new File(dir+"prediction.txt")));
        bw.close();
        bw  = new BufferedWriter(new FileWriter(new File(dir+"model.dat")));
        bw.close();
        
    }
}
