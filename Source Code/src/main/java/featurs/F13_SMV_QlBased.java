/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import baselines.QPPUtil;
import java.util.ArrayList;
import java.util.HashMap;
import util.StandardDeviation;

/**
 *
 * @author Parastoo
 */
public class F13_SMV_QlBased extends EntityBasedFeature {
    
    public F13_SMV_QlBased(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }
    
    @Override
    protected double CalculateSimilarityScore(String queryId) {
        double score = 0;
        try {
            ArrayList<String> docsTitle = getRelatedDocsToQuery(queryId);
            HashMap<String,Integer> query = getTitlesOfQueryEntities(queryId);
            ArrayList<Double> p_q_d_list = new ArrayList<>();
            
            for (int i = 0; i < docsTitle.size(); i++) {
                
                double p_q_d = calculateQLSimilarity(query,docsTitle.get(i));
                p_q_d_list.add(p_q_d);
            }
            
            double mean = StandardDeviation.mean(p_q_d_list);
            
            for (Double score1 : p_q_d_list) {
                score += score1 * Math.abs(Math.log(score1/mean));
            }
            double p_q_C = calculateQLSimilarity(query, "Collection");
        
        
            score = score/(p_q_C*p_q_d_list.size());
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
