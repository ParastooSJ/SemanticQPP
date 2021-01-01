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
public class F33_InnerSimilarity_ExpandSim extends EntityBasedFeature {
    public F33_InnerSimilarity_ExpandSim(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }

    
    @Override
    protected double CalculateSimilarityScore(String queryId) {
        double score = 0;
        try {
            ArrayList<String> docsTitle = getRelatedDocsToQuery(queryId);
            ArrayList<Double> docsScore = new ArrayList<>();
            
            int comparisonCount = 0;
            for (int i = 0; i < docsTitle.size(); i++) {
                for (int j = i+1; j < docsTitle.size(); j++) {
                    double p_d_d = 0;
                    HashMap<String,Integer> doc1 = docs.get(docsTitle.get(i));
                    p_d_d += calculateExpandSimilarity(doc1, docsTitle.get(j));
                    docsScore.add(p_d_d);
                    comparisonCount++;
                }
            }
            logwriter.write("score is: "+score+"\n");
            score = StandardDeviation.sum(docsScore) / comparisonCount;
            if(Double.isNaN(score) || Double.isInfinite(score))
                score = 0;
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
