/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import baselines.Clarity;
import baselines.PerformancePredictor;
import java.util.Arrays;
import org.lemurproject.galago.core.retrieval.Retrieval;
import org.lemurproject.galago.core.retrieval.RetrievalFactory;
import org.lemurproject.galago.utility.Parameters;

/**
 *
 * @author Parastoo
 */
public class F11_Clarity_AbstractBased extends WordBasedFeature{
    
    public F11_Clarity_AbstractBased(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }
    
    @Override
    protected double CalculateSimilarityScore(String queryId){
        double score = 0;
        try {
            Parameters query = getQuery(Integer.parseInt(queryId));
            String colPath = "C:\\Users\\Parastoo\\Documents\\NetBeansProjects\\explicit-semantic-analysis-master(1)\\explicit-semantic-analysis-master\\index\\colIndex\\";
            Retrieval retrieval = RetrievalFactory.instance(Arrays.asList(colPath), Parameters.create());
            query.set("fbTerm", 1000);
            query.set("fbOrigWeight", 0.5);
            query.set("fbDocs", 20);
            query.set("numDocs", 1000);
            query.get("sampleSize", 100);
            query.set("StdevApproach", "fixed");
            query.set("NormalizeStdev", false);
            String queryText = query.getString("text");
            PerformancePredictor qpp = new Clarity(retrieval);
            score = qpp.predict(queryText, query);
            
            if(Double.isNaN(score) || Double.isInfinite(score))
                score = 0;
            predictions.put(""+queryId,score);
            resultwriter.write(queryId+ " "+ score);
            resultwriter.write("\n");
            
            resultwriter.flush();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } 
        return  score;
         
    }
    
}
