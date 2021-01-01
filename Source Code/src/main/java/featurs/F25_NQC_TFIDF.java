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
 * @author Parastoo
 */
public class F25_NQC_TFIDF extends EntityBasedFeature{
    public F25_NQC_TFIDF(String dataset, String rs, int docCount, int start, int end, String feature) {
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
                HashMap<String,Integer> document = docsrelated.get(docsTitle.get(i));
                
                double p_q_d = calculateTFIDF(query,document, docsTitle.get(i));
                p_q_d_list.add(p_q_d);
                System.out.println(p_q_d);
            }
            
            
            double stdev = StandardDeviation.calculateSD(p_q_d_list);
            logwriter.write("stdev is: "+stdev+"\n");
            double p_q_C = calculateTFIDF(query,collection,"collection");
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
