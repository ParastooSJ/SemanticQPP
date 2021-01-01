/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author parastoo
 */
public class F09_WIG_CosineBased extends EntityBasedFeature{
    
    public F09_WIG_CosineBased(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }
    
    @Override
    protected double CalculateSimilarityScore(String queryId) {
        double score = 0;
        try {
            ArrayList<String> docsTitle = getRelatedDocsToQuery(queryId);
            HashMap<String,Integer> query = getTitlesOfQueryEntities(queryId);
            ArrayList<Double> docsScore = new ArrayList<>();
            
            
            double p_q_C = calculateCosineSimilarity(query,collection,"collection");
            
            for (int i = 0; i < docsTitle.size(); i++) {
                HashMap<String,Integer> document = docsrelated.get(docsTitle.get(i));
                double p_q_d = calculateCosineSimilarity(query, document,docsTitle.get(i) );
                docsScore.add(p_q_d);
                score += p_q_d - p_q_C;
                
            }
            for(Double entry : docsScore)
                logwriter.write("p_e_d is: "+entry+"\n");
            
            score = score/(docsTitle.size());
            score = score/Math.sqrt(query.size());
            if(Double.isNaN(score) || Double.isInfinite(score))
                score = 0;
            logwriter.write("score is: "+score+"\n");
            predictions.put(queryId,score);
            resultwriter.write(queryId+ " "+ score);
            resultwriter.write("\n");
            
            resultwriter.flush();
        } 
        catch (Exception ex) {
            
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return score;
    }
    
}
