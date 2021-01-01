/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import java.util.ArrayList;
import java.util.HashMap;
import util.StandardDeviation;

/**
 *
 * @author parastoo
 */
public class F31_NQC_ExpandSim extends EntityBasedFeature{
    
    public F31_NQC_ExpandSim(String dataset, String rs, int docCount, int start, int end, String feature) {
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
                
                double p_q_d = calculateExpandSimilarity(query,docsTitle.get(i));
                p_q_d_list.add(p_q_d);
            }
            for (Double entry : p_q_d_list) 
                logwriter.write(entry+"\n");
            
            double stdev = StandardDeviation.calculateSD(p_q_d_list);
            logwriter.write("stdev is: "+stdev+"\n");
            
            double p_q_C = calculateExpandSimilarity(query, "Collection");
            logwriter.write("p_q_C is: "+p_q_C+"\n");
            
            if(stdev != 0)
                score = stdev / p_q_C;
            else
                score = 0;
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
