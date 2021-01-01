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
 * @author Parastoo
 */
public class F03_Clarity_EntityBased extends EntityBasedFeature{
    public F03_Clarity_EntityBased(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }
    
     
    @Override
    protected double CalculateSimilarityScore(String queryId) {
        double score = 0;
        try {
            ArrayList<String> docsTitle = getRelatedDocsToQuery(queryId);
            HashMap<String,Integer> query = getTitlesOfQueryEntities(queryId);
            
            for (String colElement :collection.keySet()) {
                double tempraryScore = 0;
                
                if(query.size()>0){
                    logwriter.write("Starting entity : "+colElement+"\n");
                    double p_e_q = calculateP_e_q(query,docsTitle,colElement);
                    double p_e_C = calculateP_e_C(colElement);
                    if (p_e_C != 0 && p_e_q !=0 )
                        tempraryScore = p_e_q * Math.log(p_e_q/p_e_C);
                    if(Double.isNaN(tempraryScore) || Double.isInfinite(tempraryScore))
                        tempraryScore = 0;
                    logwriter.write("p_e_q is: "+p_e_q+"\n");
                    logwriter.write("p_e_C is: "+p_e_C+"\n");
                    logwriter.write("tempraryScore is: "+tempraryScore+"\n");
                    logwriter.write("-----------------------------------------------------------------------------------------\n");
                    score += tempraryScore;
                }
            }
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
