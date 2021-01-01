/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author parastoo
 */
public class F08_InnerSimilarity_CosineBased extends EntityBasedFeature{
    
    public F08_InnerSimilarity_CosineBased(String dataset, String rs, int docCount, int start, int end, String feature) {
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
                    HashMap<String,Integer> document1 = docsrelated.get(docsTitle.get(i));
                    HashMap<String,Integer> document2 = docsrelated.get(docsTitle.get(j));
                    double cosineSimilarity = calculateCosineSimilarity(document1, document2, docsTitle.get(j));
                    docsScore.add(cosineSimilarity);
                    score += cosineSimilarity;
                    comparisonCount++;
                }
            }
            for(Double entry : docsScore)
                logwriter.write("p_e_d is: "+entry+"\n");
            logwriter.write("score is: "+score+"\n");
            score = score / comparisonCount;
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
