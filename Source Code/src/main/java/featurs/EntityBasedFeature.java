/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author parastoo
 */
public class EntityBasedFeature extends Feature {
    
    public EntityBasedFeature(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }
    
    @Override
    public void run() throws SQLException, IOException{
        if(resultwriter != null){
            
            loadHashmaps();
            createEntitiyTitle();
            createCollection();
            logwriter.write("entityTitleSize: "+ entityTitle.size()+"\n");
            logwriter.write("collectionSize: "+ collection.size()+"\n");
            ArrayList<Integer> exceptionalQueries = new ArrayList<Integer>(
            Arrays.asList(3,40,72,119,121,137,205,231,307,312,313,317,319,326,328,346,379,395,408,417,418,424,428,435,436,438,446,638,649,659,667,670,689));
            for (int i =start; i<=end; i++) {
                if(!exceptionalQueries.contains(i)){
                    logwriter.write("Starting to calculate query: "+i+"\n");
                    p_q_d_ForALLRelatedDocs = new HashMap<>();
                    p_q_d_Down_ForALLRelatedDocs = new HashMap<>();
                    p_q_d_TFIDF_ForALLRelatedDocs = new HashMap<>();
                    CalculateSimilarityScore(Integer.toString(i));
                    logwriter.write("****************************************************************************************\n");
                    logwriter.flush();
                }
            }
            resultwriter.close();
            logwriter.close();
            entitywriter.close();
            saveHashmaps();
            
        }
    }

    protected double CalculateSimilarityScore(String queryId) {
        return 0;
    }

    private void loadHashmaps() {
        File f = new File(SerializeHashMap.hashmapAddress);
        if( f.list().length > 0){
            p_e_d_ForALLEntities = SerializeHashMap.deserializeHashMapEtities(SerializeHashMap.hashmapAddress);
            System.out.println("size is:"+p_e_d_ForALLEntities.size() );
        }

        File down_f = new File(SerializeHashMap.downhashmapAddress);
        if( down_f.list().length > 0){
            p_e_d_down_ForALLEntities = SerializeHashMap.deserializeHashMapEtities(SerializeHashMap.downhashmapAddress);
            System.out.println("size is:"+p_e_d_down_ForALLEntities.size() );
        }
        
        File tf_f = new File(SerializeHashMap.tfhashmapAddress);
        if( tf_f.list().length > 0){
            tf_ForALLEntities = SerializeHashMap.deserializeHashMapEtities(SerializeHashMap.tfhashmapAddress);
        }
        
        File idf_f = new File(SerializeHashMap.idfhashmapAddress);
        if( idf_f.list().length > 0){
            IDF_ForALLEntities = SerializeHashMap.deserializeHashMapEtities(SerializeHashMap.idfhashmapAddress);
        }
        
        File centered_f = new File(SerializeHashMap.centeredhashmapAddress);
        if( centered_f.list().length > 0){
            centered_Weights = SerializeHashMap.deserializeHashMapEtities(SerializeHashMap.centeredhashmapAddress);
        }
        
    }

    private void saveHashmaps() {
        SerializeHashMap.seializeMapEntities(p_e_d_ForALLEntities, SerializeHashMap.hashmapAddress);
        SerializeHashMap.seializeMapEntities(p_e_d_down_ForALLEntities, SerializeHashMap.downhashmapAddress);
        SerializeHashMap.seializeMapEntities(tf_ForALLEntities, SerializeHashMap.tfhashmapAddress);
        SerializeHashMap.seializeMapEntities(IDF_ForALLEntities, SerializeHashMap.idfhashmapAddress);
        SerializeHashMap.seializeMapEntities(centered_Weights, SerializeHashMap.centeredhashmapAddress);
    }
    
}
