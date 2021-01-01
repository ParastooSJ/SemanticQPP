/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import be.vanoosten.esa.tools.ESAtester_EN;
import com.github.andrewoma.dexx.collection.Pair;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author parastoo
 */
public class SerializeHashMap {
    private final String dataset ;
    private final String rs;
    private final int docCount;
    private final int start;
    private final int end;
    private static int randomEntity =100000000;
    
    
    public static HashMap<String,String> titleEntity = new HashMap<>() ;
    public ESAtester_EN ee ;
    public static final String filename= "C:\\Users\\Parastoo\\Documents\\NetBeansProjects\\SimilartyhashMapsEntity\\SimilartyhashMapsEntity\\";
    public static final String filenameSpecialEntities= "C:\\Users\\Parastoo\\Documents\\NetBeansProjects\\SimilartyhashMapsSpecialEntity\\SimilartyhashMapsSpecialEntity\\";
    public String hashmapAddress;
    public String downhashmapAddress;
    public String tfhashmapAddress;
    public String idfhashmapAddress;
    public String centeredhashmapAddress;
    public SerializeHashMap(String dataset ,String rs ,int docCount, int start,int end){
        this.dataset = dataset;
        this.rs = rs;
        this.docCount = docCount;
        this.start = start;
        this.end = end;
        this.hashmapAddress = "./crossfold/"+dataset+"/hashmap/";
        this.downhashmapAddress = "./crossfold/"+dataset+"/down_hashmap/";
        this.tfhashmapAddress = "./crossfold/"+dataset+"/tf_hashmap/";
        this.idfhashmapAddress = "./crossfold/"+dataset+"/idf_hashmap/";
        this.centeredhashmapAddress = "./crossfold/"+dataset+"/centered_hashmap/";
        try {
            createTitleEntity();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        this.ee = new ESAtester_EN();
        
    }
    
    public void serun() throws IOException, SQLException{
        createTitleEntity();
        calcAndSer();
    }
    
    
    private void createTitleEntity() throws SQLException {
        if(titleEntity.isEmpty()){ 
            
            titleEntity = new HashMap<>();
            String  query = "SELECT title, entity  from entitytitle";
            ResultSet resultset2 = SqlConnection.select(query);


            while(resultset2.next()){

                String entity = resultset2.getString(1);
                String title  = resultset2.getString(2);

                if(!titleEntity.containsKey(entity))
                    titleEntity.put(entity, title);

            }
        }
    }

    private void calcAndSer() throws UnsupportedEncodingException, FileNotFoundException, IOException {
       
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(
                      new FileOutputStream("./data/notSerializedEntities.txt"), "UTF8"));
        
        for(String title :titleEntity.keySet()){
            
            try {
                String entity = titleEntity.get(title);
                title= title.trim();
                boolean check = new File(filename, entity+".ser").exists();
                if(!check){
                    bw.write(entity+"\n");
                    bw.flush();
                    Map<String,Float> result= ee.getSimilartyVector(title);
                    seializeMap(result, filename+entity);
                }
            }
            catch (Exception ex) {
                
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                bw.write(title+"\n");
                bw.flush();
            
            }   
        }
        bw.close();
    }
    
    private void seializeMap(Map<String, Float> map, String address) {
       try{
           
           FileOutputStream fileOut = new FileOutputStream(address+".ser");
           ObjectOutputStream out = new ObjectOutputStream(fileOut);
           out.writeObject(map);
           out.close();
           fileOut.close();
       } 
       catch(Exception ex){
           
            System.out.println(ex.getMessage());
            ex.printStackTrace();
       }
    }
    
    public void seializeMapEntities(Map<String, Double> map, String address) {
       try{
           if(map.size()>0){
                FileOutputStream fileOut = new FileOutputStream(address+"hashmap.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(map);
                out.close();
                fileOut.close();
           }
       } 
       catch(Exception ex){
           
            System.out.println(ex.getMessage());
            ex.printStackTrace();
       }
    }
    
    public Map<String,Double> deserializeHashMapEtities(String address){
        Map<String, Double> result =null;
        try{
            FileInputStream filein = new FileInputStream(address+"hashmap.ser");

            ObjectInputStream in = new ObjectInputStream(filein);
            result = (Map<String,Double>) in.readObject();
            in.close();
            filein.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return result;
    }
    
    public  Map<String,Float> deserializeHashMap(String address) {
        Map<String,Float> result =null;
        String number="";
        try{
            if(titleEntity.containsKey(address)){
                number = titleEntity.get(address);
                try{
                    FileInputStream filein = new FileInputStream(filename + number + ".ser");

                    ObjectInputStream in = new ObjectInputStream(filein);
                    result = (Map<String,Float>) in.readObject();
                    in.close();
                    filein.close();
                }
                catch(Exception ex){
//                    Map<String,Float> sim= ee.getSimilartyVector(address);
//                    seializeMap(sim,filename+titleEntity.get(address));
//                    result =sim;
                    result = new HashMap<>();

                }

            }
            else{
                result = new HashMap<>();
//                reinsertEntityTitle(address);
//                Map<String,Float> sim= ee.getSimilartyVector(address);
//                seializeMap(sim,filename+titleEntity.get(address));
//                result =sim;
            }
        }
        catch(Exception ex){
            System.out.println(address+" has a problem in deserializeHashMap");
        }
        return result;
            
        
    }
    public  Map<String,Float> deserializeSpecialHashMap(String address) {
            Map<String,Float> result =null;
            String number="";
            try{
                if(titleEntity.containsKey(address)){
                    number = titleEntity.get(address);
                    try{
                        FileInputStream filein = new FileInputStream(filenameSpecialEntities + number + ".ser");
                    
                        ObjectInputStream in = new ObjectInputStream(filein);
                        result = (Map<String,Float>) in.readObject();
                        in.close();
                        filein.close();
                    }
                    catch(Exception ex){
//                        Map<String,Float> sim= ee.getSimilartyVector(address);
//                        seializeMap(sim,filenameSpecialEntities+titleEntity.get(address));
//                        result =sim;
result =  new HashMap<>();


                    }
                }
                else{
                    result = new HashMap<>();
//                    reinsertEntityTitle(address);
//                    Map<String,Float> sim= ee.getSimilartyVector(address);
//                    seializeMap(sim,filenameSpecialEntities+titleEntity.get(address));
//                    result =sim;
                }
            }
            catch(Exception ex){
                System.out.println(address+" has a problem in deserializeSpecialHashMap");
            }
            return result;
            
        
    }
    
    public void reinsertEntityTitle(String title) throws SQLException{
        MysqlDataSource dataSource ;
        Connection connection;
        Statement statement;
        dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("137428sAm");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("query_performance_prediction");
        dataSource.setUseUnicode(true);
        dataSource.setCharacterEncoding("UTF-8");
        

        connection = dataSource.getConnection();
        statement = connection.createStatement();

        while(titleEntity.containsValue(""+randomEntity)){
            randomEntity ++;
        }
        PreparedStatement updateSales = connection.prepareStatement(
            "insert into entitytitle (id,entity,title,oldid) values(?,?,?,?)");

        updateSales.setString(1, Integer.toString(randomEntity));
        updateSales.setString(2,Integer.toString(randomEntity));
        updateSales.setString(3, title);
        updateSales.setString(4,Integer.toString(randomEntity));
        titleEntity.put(title, Integer.toString(randomEntity));
        updateSales.executeUpdate();  
        randomEntity++;      
        connection.close();
        

    }
}
