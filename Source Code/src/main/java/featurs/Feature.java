/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import be.vanoosten.esa.tools.ESAtester_EN;
import com.github.andrewoma.dexx.collection.Pair;
import core.SqlConnection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import core.SerializeHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import util.StandardDeviation;


/**
 *
 * @author parastoo
 */
public class Feature {
    public final String dataset ;
    public final String rs;
    public final int docCount;
    public final int start;
    public final int end;
    public BufferedWriter resultwriter;
    public BufferedWriter logwriter;
    public BufferedWriter entitywriter;
    public String filename;
    public String logfilename;
    public SerializeHashMap SerializeHashMap;
    public int publicvectorSize = 10;
    public int privatevectorSize = 100;
    public double p_e_C_Down = 0;
    public int collectionFrequency;
    
    public HashSet<Pair<String,String>> queryDocRelatedness = new HashSet<>();
    public HashMap<String, Double> predictions = new HashMap<>();
    public HashMap<String,Integer> collection  = new HashMap<>();
    public static HashMap<String,String> entityTitle = new HashMap<>() ;
    public HashMap<String , HashMap<String,Integer>> docs = new HashMap<>();
    public HashMap<String , HashMap<String,Integer>> docsrelated = new HashMap<>();
    public HashMap<String,String> queriesEntity = new HashMap<>();
    public HashMap<String,String> queriesTitle = new HashMap<>();
    public Map<String ,Map<String,Float>> colelementVectors = new HashMap<>();
    public Map<String ,Map<String,Float>> colelementSpecialVectors = new HashMap<>();
    public Map<String,Double> p_q_d_ForALLRelatedDocs = new HashMap<>();
    public Map<String,Double> p_q_d_TFIDF_ForALLRelatedDocs = new HashMap<>();
    public Map<String,Double> p_q_d_Down_ForALLRelatedDocs = new HashMap<>();
    public HashMap<String, Double> p_e_C_up_ForALLEntities = new HashMap<>();
    public Map<String, Double> IDF_ForALLEntities = new HashMap<>();
    public HashMap<Pair<String,String>,Float> docScore = new HashMap<>();
    public ESAtester_EN ee = new ESAtester_EN();
    public Map<String,Double> p_e_d_ForALLEntities = new HashMap<>();
    public Map<String,Double> centered_Weights = new HashMap<>();
    public Map<String,Double> p_e_d_down_ForALLEntities = new HashMap<>();
    public Map<String,Double> tf_ForALLEntities = new HashMap<>();
    public Map<String,Double> idfentity_ForALLEntities = new HashMap<>();
    public String withoutTitleEntities;
    
    
    
    public Feature(String dataset ,String rs ,int docCount, int start,int end,String feature){
        this.dataset = dataset;
        this.rs = rs;
        this.docCount = docCount;
        this.start = start;
        this.end = end;
        try {
            this.filename= "./crossfold/"+dataset+"/result/"+this.rs+"-"+dataset+"-"+this.docCount+"docs-percentEntities-F"+feature+".txt";
            this.logfilename = "./crossfold/"+dataset+"/log/"+this.rs+"-"+dataset+"-"+this.docCount+"docs-log-F"+feature+".txt";
            this.withoutTitleEntities = "./crossfold/"+dataset+"/log/"+this.rs+"-"+dataset+"-"+this.docCount+"docs-emptytitleentities-F"+feature+".txt";
            File f = new File(filename);
            File flog = new File(logfilename);
            
            logwriter = new BufferedWriter(new FileWriter(flog));
            entitywriter = new BufferedWriter(new FileWriter(withoutTitleEntities));
            if(!f.exists())
                resultwriter = new BufferedWriter(
                    new FileWriter(f));
        } 
        catch (IOException ex) {
        }
        this.SerializeHashMap = new SerializeHashMap(dataset, rs, docCount, start, end);
    }
    
    public void run() throws IOException, SQLException{}
    
    protected void createEntitiyTitle() throws SQLException {
        if(entityTitle.size() == 0){
            entityTitle = new HashMap<>();
            String  query = "SELECT entity, title from entitytitle";
            ResultSet resultset2 = SqlConnection.select(query);


            while(resultset2.next()){

                String entity = resultset2.getString(1);
                String title  = resultset2.getString(2);

                if(!entityTitle.containsKey(entity))
                    entityTitle.put(entity, title);

            }
        }
    }

    protected HashMap<String, Integer> createTopEntitiesRelatedToDoc(HashMap<String, Integer> dochash) {
        HashMap<String, Integer> result = new HashMap<>();
//        HashMap<String, Integer> dochash = docs.get(docTitle);
        for(String et : dochash.keySet()){
            try {
                Map<String,Float> v;
                if(!colelementVectors.containsKey(et)){
                    v = SerializeHashMap.deserializeHashMap(et);
                    colelementVectors.put(et, v);
                    
                }
                else{
                    v = colelementVectors.get(et);
                }
                for(String e: v.keySet()){
                    if(result.containsKey(e))
                        result.put(e, result.get(e)+1);
                    else
                        result.put(e, 1);
                }
            } catch (Exception ex) {
                System.out.println(et+" has a problem in create top entities");
            }
        }
        for(String e: dochash.keySet()){
                    if(result.containsKey(e))
                        result.put(e, result.get(e)+dochash.get(e));
                    else
                        result.put(e, dochash.get(e));
        }
        return result;
    }
    
    protected HashMap<String, Integer> createHashMap(String doc) throws IOException {
        String[] entities = doc.split(",");
        HashMap<String , Integer> result = new HashMap<>();
        for(String entity : entities){
            if(entityTitle.containsKey(entity)){
                String title = entityTitle.get(entity);
                if(!result.containsKey(title))
                    result.put(title, 1);
                else
                    result.put(title, result.get(title)+1);
                if(!collection.containsKey(title))
                    collection.put(title, 1);
                else
                    collection.put(title, collection.get(title)+1);
            }
            else{
                entitywriter.write(entity+"\n");
                entitywriter.flush();
            }
        }
        return result;
    }
    
    protected void createCollection() throws SQLException, IOException {
        String query = "SELECT q.id,q.entity,d.title, d.entity,rs.document_id,rs.score,q.title,rs.rank from "+rs+"_"+dataset+" rs left join document_"+dataset+
                        " d on rs.document_id = d.title " +
                        "left join query_"+dataset+" q on rs.query_id = q.id" +
                        " where q.id  is not null and rs.rank <= "+docCount; 
        ResultSet resultset = SqlConnection.select(query);
        while(resultset.next()){
            if(resultset.getString(4)!= null){
                if(!docs.containsKey(resultset.getString(3)))
                {
                    HashMap<String,Integer> dochash = createHashMap(resultset.getString(4));
                    docs.put(resultset.getString(3),dochash);
                    docsrelated.put(resultset.getString(3),createTopEntitiesRelatedToDoc(docs.get(resultset.getString(3))));
                }

                queryDocRelatedness.add(new Pair<String,String>(resultset.getString(1),resultset.getString(3)));
                queriesEntity.put(resultset.getString(1), resultset.getString(2));
                queriesTitle.put(resultset.getString(1), resultset.getString(7));
                docScore.put(new Pair<String,String>(resultset.getString(1),resultset.getString(3)),Float.parseFloat( resultset.getString(6)));
            }
        }
        for (String entity :collection.keySet()) {
                collectionFrequency += collection.get(entity);
        }
    }

    protected HashMap<String, Float> getEntityVector(String entity, int size) throws IOException, ParseException {
        Map<String,Float> v = new HashMap<>();
        HashMap<String,Float> eRelated = new HashMap<>();
        try{
            if(size == privatevectorSize){
                if(!colelementSpecialVectors.containsKey(entity)){
                    v = SerializeHashMap.deserializeSpecialHashMap(entity);
                    v = reduceHashmapSize(v, publicvectorSize);
                    colelementSpecialVectors.put(entity, v);
                }
                else{
                    v = colelementSpecialVectors.get(entity);
                }
                
                if(v.size() == 0){
                    size = publicvectorSize;
                }
            }
            else if(size == publicvectorSize){
                if(!colelementVectors.containsKey(entity)){
                    v = SerializeHashMap.deserializeHashMap(entity);
                    v = reduceHashmapSize(v, publicvectorSize);
                    colelementVectors.put(entity, v);
                }
                else{
                    v = colelementVectors.get(entity);
                }
            }
            for(String e: v.keySet()){
                eRelated.put(e, v.get(e));
            }
            eRelated.put(entity,(float) 1);
        }
        catch(Exception ex){
            System.out.println(entity+" with size of " + size + " has a problem");
        }
        return eRelated;
    }
    
    protected LinkedHashMap<String, Float> reduceHashmapSize( Map<String, Float> passedMap, double size){
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Float> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapKeys, Collections.reverseOrder());
        Collections.sort(mapValues, Collections.reverseOrder());
        
        LinkedHashMap<String , Float> sortedMap = new LinkedHashMap<>();
        LinkedHashMap<String , Float> finalMap = new LinkedHashMap<>();
        Iterator<Float> valueIt = mapValues.iterator();
        
        while(valueIt.hasNext() ){
            float val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();
            
            while(keyIt.hasNext()){
                String key = keyIt.next();
                float comp1 = passedMap.get(key);
                float comp2 = val;
                
                if(comp1 == comp2){
                    keyIt.remove();
                    sortedMap.put(key, val);
                    
                    break;
                }
            }
        }
        int counter = 0;
        
        Iterator it = sortedMap.entrySet().iterator();
        while(it.hasNext() &&  counter<size  ){
            Map.Entry pair = (Map.Entry) it.next();
            finalMap.put((String)pair.getKey(), (Float)pair.getValue());
            counter++;
            it.remove();
            
        }
        return finalMap;
    }
    
    protected ArrayList<String> getRelatedDocsToQuery(String queryId) {
        ArrayList<String> docsTitle = new ArrayList<>();
        for (Pair p : queryDocRelatedness) {
            if(p.component1().toString().equalsIgnoreCase(queryId)){
                docsTitle.add(p.component2().toString());
            }
        }
        return docsTitle;
    }
    
    protected HashMap<String, Integer> getTitlesOfQueryEntities(String queryId) throws IOException {
        HashMap<String, Integer> result = new HashMap<>();
        for(String qEntity:queriesEntity.get(queryId).split(",") ){
            qEntity = qEntity.trim();
            if(entityTitle.containsKey(qEntity)){
                String qtitle = entityTitle.get(qEntity);
                if(result.containsKey(qtitle))
                    result.put(qtitle,result.get(qtitle));
                else
                    result.put(qtitle, 1);  
                
                if(collection.containsKey(qtitle))
                    collection.put(qtitle, collection.get(qtitle)+1);
                    
                else
                    collection.put(qtitle, 1);
                
                try{
                    Map<String,Float> qvector = SerializeHashMap.deserializeHashMap(qtitle);
                    colelementVectors.put(qtitle,qvector);
                }
                catch(Exception ex){
                    System.out.println(queryId + " does not have any entities!");
                }
            }
            else{
                entitywriter.write(qEntity+"\n");
                entitywriter.flush();
            }

        }
        return result;
    }
    
    protected double calculateP_e_d( String docTitle, String entityTitle) {
        
        double result = 0;
        float value = 0;
        if(p_e_d_ForALLEntities.containsKey((entityTitle+"_"+docTitle))){
            result = p_e_d_ForALLEntities.get((entityTitle+"_"+docTitle));
        }
        else{
            result=0;
//            try{
//                logwriter.write("Starting to calculate calculateP_e_d where doctitle is :"+docTitle+" and entityTitle is : "+entityTitle +"\n");
//                logwriter.flush();
//                HashMap<String, Integer> docsRelatedHash = docsrelated.get(docTitle);
//                logwriter.write("size of doc's entities is :"+docsRelatedHash.size()+"\n");
//                logwriter.flush();
//                HashMap<String,Float> eRelated = getEntityVector(entityTitle, privatevectorSize);
//                for (String e : eRelated.keySet()) {
//                    if(docsRelatedHash.containsKey(e)){
//                        value += ee.CalcSimilarity(entityTitle, e)* docsRelatedHash.get(e);
//                        logwriter.write("value for "+e+", "+entityTitle+" is: "+value+"\n");
//                        logwriter.flush();
//                    }
//                }
//                double up = value;
//                logwriter.write("up is: "+up+"\n");
//                logwriter.flush();
//                double down = 0;
//                down = getOwnDocSimilarity(docTitle);
//                if(up == 0 || down == 0 )
//                    result = 0;
//                else
//                    result = up / down;
//                logwriter.write("down is: "+down+"\n");
//                logwriter.write("result is: "+result+"\n\n");
//                logwriter.flush();
//                if(Double.isNaN(result) || Double.isInfinite(result))
//                    result = 0;
//            }
//            catch(Exception ex){
//
//                System.out.println(ex.getMessage());
//                ex.printStackTrace();
//                result = 0;
//            }
            if(result !=0)
                p_e_d_ForALLEntities.put((entityTitle+"_"+docTitle), result);
        }
        
        return result;
    } 
    
    protected double calculateP_e_C( String entityTitle) {

        double result = 0;
        float value = 0;
        if(p_e_d_ForALLEntities.containsKey((entityTitle+"_"+"Collection"))){
            result = p_e_d_ForALLEntities.get((entityTitle+"_"+"Collection"));
        }
        else{
            double up = 0;
            try{
                logwriter.write("Starting to calculate calculateP_e_C for collection and entityTitle is : "+entityTitle +"\n");
                logwriter.flush();
                if(!p_e_C_up_ForALLEntities.containsKey(entityTitle)){
                    HashMap<String,Float> eRelated = getEntityVector(entityTitle, privatevectorSize);

                    for (String e:eRelated.keySet()) {
                        if(collection.containsKey(e)){
                            value += ee.CalcSimilarity(entityTitle, e)* collection.get(e);
                            logwriter.write("value for "+e+", "+entityTitle+" is: "+value+"\n");
                            logwriter.flush();
                        }
                    }
                    up = value;
                    p_e_C_up_ForALLEntities.put(entityTitle, up);
                }
                else{
                     up = p_e_C_up_ForALLEntities.get(entityTitle);
                }
                logwriter.write("up is: "+up+"\n");
                logwriter.flush();
                double down = 0;
                if(!p_e_d_down_ForALLEntities.containsKey("Collection")){
                    if(p_e_C_Down == 0){
                        for(String re :collection.keySet()){
                            try{
                                HashMap<String,Float> eRelated = getEntityVector(re, publicvectorSize);
                                double value2 = 0;
                                for (String e : eRelated.keySet()) {

                                    if(collection.containsKey(e)){
                                        value2 += ee.CalcSimilarity(re, e)* collection.get(e);
                                    }
                                }
                                down += value2 * collection.get(re);

                            }
                            catch(Exception ex){
                                System.out.println(ex.getMessage());
                                ex.printStackTrace();
                                result = 0;
                            }

                        }
                        p_e_C_Down=down;  
                    }
                    else{
                        down = p_e_C_Down;
                    }
                    p_e_d_down_ForALLEntities.put("Collection",down);
                }
                else{
                    down = p_e_d_down_ForALLEntities.get("Collection");
                }
                result = up / down;
                logwriter.write("down is: "+down+"\n");
                logwriter.write("result is: "+result+"\n\n");
                logwriter.flush();
                if(Double.isNaN(result) || Double.isInfinite(result))
                    result = 0;
            }
            catch(Exception ex){

                System.out.println(ex.getMessage());
                ex.printStackTrace();
                result = 0;
            }
//            if(result != 0)
                p_e_d_ForALLEntities.put((entityTitle+"_"+"Collection"), result);
        }
        
        return result;
    } 
    
    protected double calculateQLSimilarity(HashMap<String, Integer> doc1, String doc2title) throws IOException {
        logwriter.write("Starting to calculate calculateQLSimilarity for : "+doc2title+"\n");
        logwriter.flush();
        double result = 0;
        for (String entry :doc1.keySet()) {
            double value;
            if(doc2title.equals("Collection"))
                value = calculateP_e_C(entry) * doc1.get(entry);
            else
                value = calculateSmoothedP_e_d(doc2title,entry) * doc1.get(entry);
            logwriter.write("p_q_d for qt: "+entry+" and doctitle: "+doc2title+"is: "+value+"\n");
            logwriter.flush();
            if(value != 0)
                result += Math.log(value);
        }
        logwriter.write("p_q_d for all query and doctitle: "+doc2title+"is: "+result+"\n\n");
        logwriter.flush();
        if(Double.isNaN(result) || Double.isInfinite(result))
            result = 0;
        return result;
    }
    
    protected double calculateExpandSimilarity(HashMap<String, Integer> doc1, String doc2title) throws IOException {
        logwriter.write("Starting to calculate calculateQLSimilarity for : "+doc2title+"\n");
        logwriter.flush();
        double result = 0;
        doc1 = createTopEntitiesRelatedToDoc(doc1);
        for (String entry :doc1.keySet()) {
            double value;
            if(doc2title.equals("Collection"))
                value = calculateP_e_MatchedC(entry);
            else
                value = calculateP_e_MatchedD(doc2title,entry);
            logwriter.write("p_q_d for qt: "+entry+" and doctitle: "+doc2title+"is: "+value+"\n");
            logwriter.flush();
            if(value!=0)
                result += value;
        }
        logwriter.write("p_q_d for all query and doctitle: "+doc2title+"is: "+result+"\n\n");
        logwriter.flush();
        HashMap<String, Integer> doc2;
        if(doc2title.equals("Collection"))
            doc2 = collection;
        else
            doc2 = docs.get(doc2title);
        if(doc1.size()!=0 || doc2.size()!=0){
            result = result/(doc1.size()+doc2.size());
        }
        else
            result =0;
        if(Double.isNaN(result) || Double.isInfinite(result))
            result = 0;
        return result;
    }
    
    protected double calculateCenteredSimilarity(HashMap<String, Integer> doc1, String doc1title, String doc2title) throws IOException {
        logwriter.write("Starting to calculate calculateQLSimilarity for : "+doc2title+"\n");
        logwriter.flush();
        double result = 0;
        
        if(doc2title.equals("Collection"))
            result = calculateCenteredSimCollection(doc1,doc2title);
        else if(doc1title.equals("query"))
            result = calculateCenteredSimQuery(doc1,doc2title);
        else
            result = calculateCenteredSimDocument(doc1,doc1title,doc2title);
        
        
        return result;
    }
    
    protected double calculateWiserSimilarity(HashMap<String, Integer> doc1, String doc1title, String doc2title) throws IOException {
        logwriter.write("Starting to calculate calculatewiserSimilarity for : "+doc2title+"\n");
        logwriter.flush();
        double result = 0;
        if(doc2title.equals("Collection")){
            result = calculateWiserCollection(doc1);
        }
        else
            result = calculateWiserDoc(doc1,doc1title,doc2title);
        logwriter.write("p_q_d for all query and doctitle: "+doc2title+"is: "+result+"\n\n");
        logwriter.flush();
        if(Double.isNaN(result) || Double.isInfinite(result))
            result = 0;
        return result;
    }
    
    protected void calculateP_q_D( HashMap<String,Integer> query,  ArrayList<String> docsTitle) {

        try{
            logwriter.write("Starting to calculate calculateP_q_D \n");
            logwriter.flush();
            if(p_q_d_ForALLRelatedDocs.isEmpty()){
                for (String docTitle : docsTitle) {
                    double p_q_d = 1;
                    for (String qt: query.keySet()) {
                        double p_e_d = calculateSmoothedP_e_d(docTitle,qt);
                        if(p_e_d !=0)
                            p_q_d *= p_e_d;
                    }
                    if(p_q_d == 1)
                        p_q_d = 0;
                    if(Double.isNaN(p_q_d) || Double.isInfinite(p_q_d))
                        p_q_d = 0;
                    p_q_d_ForALLRelatedDocs.put(docTitle, p_q_d);
                }
            }
            
        }catch(Exception ex){
            
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            
        }
    }
    
    protected double calculateP_e_q(HashMap<String,Integer> query, ArrayList<String> docsTitle,String entityName) {
        double result = 0;
        try{
            logwriter.write("Starting to calculate calculateP_e_q where e is: "+entityName+ "\n");
            logwriter.flush();
            calculateP_q_D(query, docsTitle);
            
            for (String docTitle : docsTitle) {
                double p_e_d_C = calculateSmoothedP_e_d(docTitle,entityName);
                if(Double.isNaN(p_e_d_C) || Double.isInfinite(p_e_d_C))
                    p_e_d_C = 0;
                result += ( p_e_d_C * p_q_d_ForALLRelatedDocs.get(docTitle));
                
            }
            logwriter.write("result for P_e_q is: "+result+ "\n\n");
            logwriter.flush();
        }catch(Exception ex){
            
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            
        }
        if(Double.isNaN(result) || Double.isInfinite(result))
            result = 0;
        return result;
    }
    
    protected double calculateSmoothedP_e_d( String docTitle, String entityName) throws IOException {
        logwriter.write("Starting to calculate calculateP_e_d_C where doctitle is: "+docTitle+ " and entityname is: "+entityName+"\n");
        logwriter.flush();
        double p_e_d = calculateP_e_d(docTitle, entityName);
        if(Double.isNaN(p_e_d) || Double.isInfinite(p_e_d))
            p_e_d = 0;
        double p_e_C = calculateP_e_C(entityName);
        if(Double.isNaN(p_e_C) || Double.isInfinite(p_e_C))
            p_e_C = 0;
        double result = (0.6 * p_e_d)+(0.4 * p_e_C);
        logwriter.write("result for P_e_d_C is: "+result+ "\n\n");
        logwriter.flush();
        if(Double.isNaN(result) || Double.isInfinite(result))
            result = 0;
        return result;
    }
    
    protected double calculateTFIDF(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2, String doc2title) {
        double result = 0;
        try{
            for (String dtitle1 : doc1.keySet()) {
                result += calculateEntityTfIdf(dtitle1, doc2) * doc1.get(dtitle1);
            }

            double doc1Frequency = getFrequency(doc1); 
            double doc2Frequency = getFrequency(doc2); 
            
            if(doc1Frequency !=0 && doc2Frequency !=0)
                result = result / (doc1Frequency * doc2Frequency);
            else
                result = 0;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        if(Double.isNaN(result) || Double.isInfinite(result))
            result = 0;
        return result;
    }
    
    protected double calculateEntityTfIdf(String dtitle, HashMap<String, Integer> doc) {
        double tf = calculateEntityTF(dtitle,doc);
        double idf = calculateEntityIDF(dtitle);
        return tf*idf;
    }
    
    protected double calculateEntityTF(String title, HashMap<String, Integer> doc) {
        
        double result = 0;
        
        try{
            logwriter.write("Starting to calculate calculateTF \n");
            logwriter.flush();
            
            double tf = doc.get(title);
            result = Math.log(1+tf);
            if(Double.isNaN(result) || Double.isInfinite(result))
                result = 0;
            logwriter.write("result is: "+result+"\n\n");
            logwriter.flush();
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
            ex.printStackTrace();
            result = 0;
        }
        
        return result;
    }

    protected double calculateEntityIDF(String title) {
        double result = 0;
        float value = 0;
        
        try{
            logwriter.write("Starting to calculate calculateIDF:"+title+" \n");
            logwriter.flush();
            if(!idfentity_ForALLEntities.containsKey(title)){
                
                double df = collection.get(title);
                int colFreq = getFrequency(collection);
                if(df ==0 )
                    df ++;
                result = Math.log(colFreq/df);

                idfentity_ForALLEntities.put(title, result);
            }
            else{
                 result = idfentity_ForALLEntities.get(title);
            }
            if(Double.isNaN(result) || Double.isInfinite(result))
                result = 0;
            logwriter.write("result is: "+result+"\n\n");
            logwriter.flush();
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
            ex.printStackTrace();
            result = 0;
        }
        
        return result;
    }

    
    protected double calculateCosineSimilarity(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2, String doc2title) {
        double result = 0;
        try{
            for (String dtitle1 : doc1.keySet()) {
                result += calculateP_e_d_TfIdf(dtitle1, doc2, doc2title) * doc1.get(dtitle1);
            }

            int doc1Frequency = getFrequency(doc1); 
            int doc2Frequency = getFrequency(doc2); 
            
            if(doc1Frequency !=0 && doc2Frequency !=0)
                result = result / (doc1Frequency * doc2Frequency);
            else
                result = 0;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        if(Double.isNaN(result) || Double.isInfinite(result))
            result = 0;
        return result;
    }

    protected double calculateTF(String title, HashMap<String, Integer> doc, String doctitle) {
        
        double result = 0;
        float value = 0;
        if(tf_ForALLEntities.containsKey((title+"_"+doctitle))){
            result = tf_ForALLEntities.get((title+"_"+doctitle));
        }
        else{
            try{
                logwriter.write("Starting to calculate calculateTF \n");
                logwriter.flush();
                HashMap<String,Float> eRelated = getEntityVector(title, privatevectorSize);
                for (String e : eRelated.keySet()) {
                    if(doc.containsKey(e)){
                        value += ee.CalcSimilarity(title, e)* doc.get(e);
                        logwriter.write("value for "+e+", "+title+" is: "+value+"\n");
                        logwriter.flush();
                    }
                }
                double tf = value;
                result = Math.log(1+tf);
                if(Double.isNaN(result) || Double.isInfinite(result))
                    result = 0;
                logwriter.write("result is: "+result+"\n\n");
                logwriter.flush();
            }
            catch(Exception ex){

                System.out.println(ex.getMessage());
                ex.printStackTrace();
                result = 0;
            }
//            if(result != 0)
                tf_ForALLEntities.put((title+"_"+doctitle), result);
        }
        return result;
    }

    protected double calculateIDF(String title) {
        double result = 0;
        float value = 0;
        if(IDF_ForALLEntities.containsKey(title)){
            result = IDF_ForALLEntities.get(title);
        }
        else{
            try{
                logwriter.write("Starting to calculate calculateIDF:"+title+" \n");
                logwriter.flush();
                if(!IDF_ForALLEntities.containsKey(title)){
                    HashMap<String,Float> eRelated = getEntityVector(title, privatevectorSize);

                    for (String e:eRelated.keySet()) {
                        if(collection.containsKey(e)){
                            value += ee.CalcSimilarity(title, e)* collection.get(e);
                            logwriter.write("value for "+e+", "+title+" is: "+value+"\n");
                            logwriter.flush();
                        }
                    }
                    double df = value;
                    int colFreq = getFrequency(collection);
                    if(df ==0 )
                        df ++;
                    result = Math.log(colFreq/df);

                    IDF_ForALLEntities.put(title, result);
                }
                else{
                     result = IDF_ForALLEntities.get(title);
                }
                if(Double.isNaN(result) || Double.isInfinite(result))
                    result = 0;
                logwriter.write("result is: "+result+"\n\n");
                logwriter.flush();
            }
            catch(Exception ex){

                System.out.println(ex.getMessage());
                ex.printStackTrace();
                result = 0;
            }
//            if(result != 0)
                IDF_ForALLEntities.put(title, result);
        }
        
        return result;
    }

    protected int getFrequency(HashMap<String, Integer> hm) {
        int result = 0;
        for(String entry: hm.keySet())
            result += hm.get(entry);
        return result;
    }

    protected double calculateP_e_C_TfIdf(String title) {
        double tf = calculateTF(title,collection, "collection");
        double idf = calculateIDF(title);
        return tf*idf;
    }

    protected double calculateP_e_d_TfIdf(String dtitle, HashMap<String, Integer> doc,String doctitle) {
        double tf = calculateTF(dtitle,doc,doctitle);
        double idf = calculateIDF(dtitle);
        return tf*idf;
    }

    protected double getOwnDocSimilarity(String docTitle) {
        double result = 0;
        HashMap<String, Integer> docsRelatedHash = docsrelated.get(docTitle);
        HashMap<String,Float> eRelated = new HashMap<>();
        if(p_e_d_down_ForALLEntities.containsKey(docTitle)){
             result = p_e_d_down_ForALLEntities.get(docTitle);
        }
        else {
            if (!p_q_d_Down_ForALLRelatedDocs.containsKey(docTitle)) {
                for (String re : docsRelatedHash.keySet()) {
                    try {
                        double value2 = 0;
                        eRelated = getEntityVector(re, publicvectorSize);
                        for (String e : eRelated.keySet()) {
                            if (docsRelatedHash.containsKey(e)) {
                                value2 += ee.CalcSimilarity(re, e) * docsRelatedHash.get(e);
                            }
                        }
                        result += value2 * docsRelatedHash.get(re);
                    } catch (Exception ex) {

                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                p_q_d_Down_ForALLRelatedDocs.put(docTitle, result);
            } else {
                result = p_q_d_Down_ForALLRelatedDocs.get(docTitle);
            }
            p_e_d_down_ForALLEntities.put(docTitle, result);
        }
        return result;
    }

    protected double calculateP_e_MatchedD(String docTitle, String entityTitle) {
        double result = 0;
        try{
            logwriter.write("Starting to calculate calculateP_e_d where doctitle is :"+docTitle+" and entityTitle is : "+entityTitle +"\n");
            logwriter.flush();
            HashMap<String, Integer> docsRelatedHash = docs.get(docTitle);
            logwriter.write("size of doc's entities is :"+docsRelatedHash.size()+"\n");
            logwriter.flush();
            double bestSimilarity = 0;
            for (String e : docsRelatedHash.keySet()) {
                double similarity = ee.CalcSimilarity2(entityTitle, e);
                if(similarity >= bestSimilarity){
                    bestSimilarity = similarity;
                    result = bestSimilarity;
                }
            }
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
            ex.printStackTrace();
            result = 0;
        }
        
        
        return result;
    }

    private double calculateP_e_MatchedC(String entityTitle) {
        
        double result = 0;
       
        try{
            logwriter.write("Starting to calculate calculateP_e_C for collection and entityTitle is : "+entityTitle +"\n");
            logwriter.flush();
            double bestsimilarity = 0;
            for (String e:collection.keySet()) {
                double similarity = ee.CalcSimilarity2(entityTitle, e);
                if(similarity >= bestsimilarity){
                    bestsimilarity = similarity;
                    result = bestsimilarity;
                }
            }
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
            ex.printStackTrace();
            result = 0;
        }
        return result;
    }

    protected double calculateCenteredSimQuery(HashMap<String, Integer> query, String docTitle) {
        
        double result = 0;
        double queryPart = 0;
        double docPart = 0;
        try{
            logwriter.write("Starting to calculate calculateCenteredSimQuery for collection and entityTitle is : "+docTitle +"\n");
            logwriter.flush();
            HashMap<String, Integer> doc = docs.get(docTitle);
            HashMap<String, Pair<String,Double>> bestQuery = findBestOfQuery(query,doc);
            HashMap<String,Double> queryWeights = findWeightsOfQuery(query);
            HashMap<String, Pair<String,Double>> bestDoc = findBestOfDocumentUsingQuery(doc,query,bestQuery);
            HashMap<String,Double> docWeights = findWeightsOfDocument(doc,docTitle);
            for (String qentry: bestQuery.keySet()) {
                Pair<String,Double> value = bestQuery.get(qentry);
                queryPart += queryWeights.get(qentry)* value.component2();
                logwriter.write("queryPart : "+queryPart+" for qentity: "+qentry +"\n");
                logwriter.flush();
            }
            queryPart = ((double)1/2)*((double)queryPart /StandardDeviation.sumHash(queryWeights));
            
            for (String dentry: bestDoc.keySet()) {
                Pair<String,Double> value = bestDoc.get(dentry);
                docPart += docWeights.get(dentry)* value.component2();
                logwriter.write("queryPart : "+docPart+" for qentity: "+dentry +"\n");
                logwriter.flush();
            }
            docPart = ((double)1/2)*((double)docPart /StandardDeviation.sumHash(docWeights));
            
            result = queryPart + docPart;
            logwriter.write("result of calculateCenteredSimQuery: "+result +"\n");
            logwriter.flush();
            
        }
        catch(Exception ex){
        System.out.println(ex.getMessage());
            ex.printStackTrace();
            result = 0;
        }
        return result;
    }

    protected double calculateCenteredSimCollection(HashMap<String, Integer> query, String collectionName) {
        double result = 0;
        double queryPart = 0;
        double docPart = 0;
        try{
            logwriter.write("Starting to calculate calculateCenteredSimCollection for collection  is \n");
            logwriter.flush();
            HashMap<String, Pair<String,Double>> bestQuery = findBestOfQuery(query,collection);
            HashMap<String,Double> queryWeights = findWeightsOfQuery(query);
            HashMap<String, Pair<String,Double>> bestDoc = findBestOfCollectionUsingQuery(query);
            HashMap<String,Double> docWeights = findWeightsOfDocument(collection,collectionName);
            for (String qentry: bestQuery.keySet()) {
                Pair<String,Double> value = bestQuery.get(qentry);
                queryPart += queryWeights.get(qentry)* value.component2();
                logwriter.write("queryPart : "+queryPart+" for qentity: "+qentry +"\n");
                logwriter.flush();
                
            }
            queryPart = ((double)1/2)*((double)queryPart /StandardDeviation.sumHash(queryWeights));
            logwriter.write(" final queryPart : "+queryPart+" \n");
            logwriter.flush();
            for (String dentry: bestDoc.keySet()) {
                Pair<String,Double> value = bestDoc.get(dentry);
                docPart += docWeights.get(dentry)* value.component2();
                logwriter.write("docpart : "+docPart+" for qentity: "+dentry +"\n");
                logwriter.flush();
            }
            docPart = ((double)1/2)*((double)docPart /StandardDeviation.sumHash(docWeights));
            logwriter.write(" final docpart : "+docPart+" \n");
            logwriter.flush();
            result = queryPart + docPart;
            logwriter.write("result of calculateCenteredSimQuery: "+result +"\n");
            logwriter.flush();
        }
        catch(Exception ex){
        System.out.println(ex.getMessage());
            ex.printStackTrace();
            result = 0;
        }
        return result;
    }

    protected double calculateCenteredSimDocument(HashMap<String, Integer> doc1, String doc1title, String doc2title) {
        double result = 0;
        double queryPart = 0;
        double docPart = 0;
        try{
            logwriter.write("Starting to calculate calculateCenteredSimDocument for doc and entityTitle is : "+doc2title +"\n");
            logwriter.flush();
            HashMap<String, Integer> doc2 = docs.get(doc2title);
            HashMap<String, Pair<String,Double>> bestDoc1 = findBestOfDocument(doc1,doc2);
            HashMap<String,Double> doc1Weights = findWeightsOfDocument(doc1,doc1title);
            HashMap<String, Pair<String,Double>> bestDoc2 = findBestOfDocument(doc2,doc1);
            HashMap<String,Double> doc2Weights = findWeightsOfDocument(doc2,doc2title);
            for (String d1entry: bestDoc1.keySet()) {
                Pair<String,Double> value = bestDoc1.get(d1entry);
                queryPart += doc1Weights.get(d1entry)* value.component2();
                logwriter.write("queryPart : "+queryPart+" for qentity: "+d1entry +"\n");
                logwriter.flush();
            }
            queryPart = ((double)1/2)*((double)queryPart /StandardDeviation.sumHash(doc1Weights));
            
            for (String d2entry: bestDoc2.keySet()) {
                Pair<String,Double> value = bestDoc2.get(d2entry);
                docPart += doc2Weights.get(d2entry)* value.component2();
                logwriter.write("queryPart : "+docPart+" for qentity: "+d2entry +"\n");
                logwriter.flush();
                
            }
            docPart = ((double)1/2)*((double)docPart /StandardDeviation.sumHash(doc2Weights));
            
            result = queryPart + docPart;
            
        }
        catch(Exception ex){
        System.out.println(ex.getMessage());
            ex.printStackTrace();
            result = 0;
        }
        return result;
    }

    private HashMap<String, Pair<String,Double>> findBestOfQuery(HashMap<String, Integer> query, HashMap<String, Integer> doc) {
        HashMap<String, Pair<String,Double>>  bestQuery = new HashMap<>();
        try {
            logwriter.write("starting findBestOfQuery \n");
            logwriter.flush();
            for (String entry:query.keySet()) {
            
                String valueName = "";
                double bestValue = 0;
                HashMap<String,Float> eRelated = getEntityVector(entry, privatevectorSize);
                for (String e:eRelated.keySet()) {
                    if(doc.containsKey(e)){
                        double value = ee.CalcSimilarity(entry, e);
                        if(value > bestValue){
                            bestValue = value;
                            valueName = e;
                        }
                        
                    }
                }
                if(bestValue !=0){
                    bestQuery.put(entry, new Pair<String,Double>(valueName,bestValue));
                    logwriter.write("best for : "+entry+" is: "+valueName +"\n");
                    logwriter.flush();
                }
            
            }
        } catch (IOException ex) {
                Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bestQuery;
    }

    private HashMap<String, Double> findWeightsOfDocument(HashMap<String, Integer> document, String docTitle) {
        HashMap<String,Double> weights = new HashMap<>();
        try {
            logwriter.write("starting findWeightsOfDocument \n");
            logwriter.flush();
            for (String entry1:document.keySet()) {
                double weight = 1;
                if(centered_Weights.containsKey(entry1+"_"+docTitle)){
                    weight = centered_Weights.get(entry1+"_"+docTitle);
                }
                else{
                    HashMap<String,Float> eRelated = getEntityVector(entry1, privatevectorSize);
                    for (String e:eRelated.keySet()) {
                        if(document.containsKey(e) && !entry1.equals(e) ){
                            weight += ee.CalcSimilarity(entry1, e);
                        }
                    }
                    weight = (double)weight / document.size();
                    if(weight != 0)
                        centered_Weights.put(entry1+"_"+docTitle, weight);
                        logwriter.write("weight: "+ entry1+" value: "+weight+" \n");
                        logwriter.flush();
                }
                weights.put(entry1, weight);
            }
        } catch (Exception ex) {
            Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        }
        return weights;
    }
    
    private HashMap<String, Double> findWeightsOfQuery(HashMap<String, Integer> query) {
        HashMap<String,Double> weights = new HashMap<>();
        try {
            logwriter.write("starting findWeightsOfQuery \n");
            logwriter.flush();
            for (String entry1:query.keySet()) {
                double weight = (double)1/ query.size();
                weights.put(entry1, weight);
            }
        } catch (Exception ex) {
            Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        }
        return weights;
    }

    private HashMap<String, Pair<String,Double>> findBestOfDocumentUsingQuery( HashMap<String, Integer> doc,HashMap<String, Integer> query, HashMap<String, Pair<String,Double>> bestQuery) {
        HashMap<String, Pair<String,Double>> best = new HashMap<>();
        try {
            logwriter.write("starting findBestOfCollectionUsingQuery \n");
            logwriter.flush();
            for (String entry:doc.keySet()) {
            
                double bestvalue = 0;
                String valueName = "";
                HashMap<String,Float> eRelated = getEntityVector(entry, privatevectorSize);
                for (String e:eRelated.keySet()) {
                    if(query.containsKey(e)){
                        double value = ee.CalcSimilarity(entry, e);
                        if(value > bestvalue){
                            bestvalue = value;
                            valueName = e;
                        }
                        
                    }
                }
                if(bestvalue !=0){
                    best.put(entry, new Pair<String,Double>(valueName,bestvalue));
                    logwriter.write("best for : "+entry+" is: "+valueName +"\n");
                    logwriter.flush();
                }
                else{
                    for(String qentry : bestQuery.keySet()) {
                        Pair<String,Double> value = bestQuery.get(qentry);
                        if(value.component1().equals(entry)){
                            if(value.component2() > bestvalue){
                                bestvalue = value.component2();
                                valueName = qentry;
                            }
                        }
                    }
                    if(bestvalue !=0){
                        best.put(entry, new Pair<String,Double>(valueName,bestvalue));
                        logwriter.write("best for : "+entry+" is: "+valueName +"\n");
                        logwriter.flush();
                    }
                }
            
            }
        } catch (IOException ex) {
                Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        }
        return best;
    }

    private HashMap<String, Pair<String,Double>> findBestOfCollectionUsingQuery(HashMap<String,Integer > query) {
        HashMap<String, Pair<String,Double>> best = new HashMap<>();
        try {
            logwriter.write("starting findBestOfCollectionUsingQuery \n");
            logwriter.flush();
            for (String entry:query.keySet()) {
                
                double bestvalue = 0;
                String valueName = "";
                String key = "";
                
                HashMap<String,Float> eRelated = getEntityVector(entry, privatevectorSize);
                for (String e:eRelated.keySet()) {
                    if(collection.containsKey(e)){
                        double value = ee.CalcSimilarity(entry, e);
                        if(value !=0 && !best.containsKey(e)){
                            best.put(e, new Pair<String,Double>(entry,value));
                            logwriter.write("best for : "+entry+" is: "+e +"\n");
                            logwriter.flush();
                        }
                        else if(value !=0 && !best.containsKey(e)){
                            if(value> best.get(e).component2()){
                                best.put(e, new Pair<String,Double>(entry,value));
                                logwriter.write("best for : "+entry+" is: "+e +"\n");
                                logwriter.flush();
                            }
                        }
                        
                    }
                }
                
            }
        } catch (Exception ex) {
                Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        }
        return best;
    }

    private HashMap<String, Pair<String, Double>> findBestOfDocument(HashMap<String, Integer> doc1, HashMap<String, Integer> doc2) {
        HashMap<String, Pair<String,Double>> best = new HashMap<>();
        try {
            logwriter.write("starting findBestOfDocument \n");
            logwriter.flush();
            for (String entry:doc1.keySet()) {
            
                double bestvalue = 0;
                String valueName = "";
                HashMap<String,Float> eRelated = getEntityVector(entry, privatevectorSize);
                for (String e:eRelated.keySet()) {
                    if(doc2.containsKey(e)){
                        double value = ee.CalcSimilarity(entry, e);
                        if(value > bestvalue){
                            bestvalue = value;
                            valueName = e;
                        }
                        
                    }
                }
                if(bestvalue !=0){
                    best.put(entry, new Pair<String,Double>(valueName,bestvalue));
                    logwriter.write("best for : "+entry+" is: "+valueName +"\n");
                    logwriter.flush();
                }
                
            
            }
        } catch (IOException ex) {
                Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        }
        return best;
    }

    private double calculateWiserCollection(HashMap<String, Integer> doc1) {
        double result = 0;
        try{
            for(String entry:doc1.keySet()){
                HashMap<String,Float> eRelated = getEntityVector(entry, privatevectorSize);
                for(String e:eRelated.keySet()){
                    if(collection.containsKey(e)){
                        result += calculateP_e_C(e)*calculateRel(entry,e,collection) * doc1.get(entry);
                    }
                }
                result = result * (1/(double)(0.1*doc1.size()));
            }
        }
        catch(Exception ex){
            result = 0;
        }
        return result;
    }

    private double calculateWiserDoc(HashMap<String, Integer> doc1, String doc1title, String doc2title) {
        double result = 0;
        try{
            HashMap<String, Integer> doc2 = docsrelated.get(doc2title);
            for(String entry:doc1.keySet()){
                HashMap<String,Float> eRelated = getEntityVector(entry, privatevectorSize);
                for(String e:eRelated.keySet()){
                    if(doc2.containsKey(e)){
                        result += calculateP_e_d(doc2title,e)*calculateRel(entry,e,doc2) * doc1.get(entry);
                    }
                }
                result = result * (1/(double)(0.1*doc1.size()));
            }
        }
        catch(Exception ex){
            result = 0;
        }
        return result;
    }

    private double calculateRel(String entry, String e, HashMap<String, Integer> doc2) {
        double result = 0;
        try{
            if(doc2.containsKey(entry))
            {
                result = doc2.get(entry) * ee.CalcSimilarity(entry, e) * calculateEntityIDF(entry);
            }
        }
        catch(Exception ex){
            result = 0;
        }
        return result;
    }

    

    

    
}
