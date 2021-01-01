/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import java.util.ArrayList;

/**
 *
 * @author parastoo
 */
public class F10_CrossEntropy extends EntityBasedFeature {
    
    public F10_CrossEntropy(String dataset, String rs, int docCount, int start, int end, String feature) {
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
                    for (String colElement :docsrelated.get(docsTitle.get(i)).keySet()) {
                        double p_e_d = calculateP_e_d(docsTitle.get(i), colElement);
                        double p_e_d_c = calculateSmoothedP_e_d(docsTitle.get(j), colElement);
                        if( p_e_d_c != 0 ){
                            score += p_e_d * Math.log(p_e_d_c);
                        }
                        
                    }
                    comparisonCount ++;
                }
            }
            for(Double entry : docsScore)
                logwriter.write("p_e_d is: "+entry+"\n");
            logwriter.write("score is: "+score+"\n");
            score = -1*score / comparisonCount;
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
