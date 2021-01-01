/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import baselines.PerformancePredictor;
import baselines.UEF;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import org.lemurproject.galago.core.retrieval.Retrieval;
import org.lemurproject.galago.core.retrieval.RetrievalFactory;
import org.lemurproject.galago.utility.Parameters;

/**
 *
 * @author parastoo
 */
public class F15_UEF_NQC_QlBased extends WordBasedFeature{

    public F15_UEF_NQC_QlBased(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }

    @Override
    public double CalculateSimilarityScore(String queryId){
        double score = 0;
            try {
                String clarityPath ="./crossfold/"+dataset+"/result/"+this.rs+"-"+dataset+"-"+this.docCount+"docs-percentEntities-F01.txt";
                String rmPath ="./crossfold/"+dataset+"/rm/rm.txt";
                HashMap<String,Double> rm = readPerfFromFils(rmPath);
                HashMap<String,Double> clarity = readPerfFromFils(clarityPath);
                score = rm.get(queryId) * clarity.get(queryId);
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
         return score;
    }
    
    public static HashMap<String, Double> readPerfFromFils(String input) throws FileNotFoundException {
        HashMap<String, Double> result = new HashMap<>();
        Scanner in = new Scanner(new File(input));
        while (in.hasNextLine()){
            try{
            String qid = in.next();
            double val = in.nextDouble();
            result.put(qid,val);
            }
            catch(Exception ex){
            }
        }
        in.close();
        return result;
    }
    
}
