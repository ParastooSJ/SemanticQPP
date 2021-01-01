/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import baselines.Clarity;
import baselines.NQC;
import baselines.PerformancePredictor;
import baselines.QF;
import core.SqlConnection;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lemurproject.galago.core.retrieval.Retrieval;
import org.lemurproject.galago.core.retrieval.RetrievalFactory;
import org.lemurproject.galago.utility.Parameters;

/**
 *
 * @author parastoo
 */
public class F12_Qf_AbstractBased extends WordBasedFeature{
   
    public HashMap<String,String> EntityAbstract ;

    public F12_Qf_AbstractBased(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }

    @Override
    protected double CalculateSimilarityScore(String queryId){
        double score = 0;
        try {
            Parameters query = getQuery(Integer.parseInt(queryId));
            String colPath = "C:\\Users\\Parastoo\\Documents\\NetBeansProjects\\explicit-semantic-analysis-master(1)\\explicit-semantic-analysis-master\\index\\colIndex\\";
            Retrieval retrieval = RetrievalFactory.instance(Arrays.asList(colPath), Parameters.create());
            query.set("fbTerm", 100);
            query.set("fbOrigWeight", 0.5);
            query.set("fbDocs", 20);
            query.set("numDocs", 50);
            query.get("sampleSize", 100);
            query.set("StdevApproach", "fixed");
            query.set("NormalizeStdev", false);
            String queryText = query.getString("text");
            PerformancePredictor qpp = new QF(retrieval);
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
