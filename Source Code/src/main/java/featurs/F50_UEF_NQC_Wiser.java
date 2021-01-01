/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author parastoo
 */
public class F50_UEF_NQC_Wiser extends WordBasedFeature{
    public F50_UEF_NQC_Wiser(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }
    
    @Override
    public double CalculateSimilarityScore(String queryId){
        double score = 0;
            try {
                String clarityPath ="./crossfold/"+dataset+"/result/"+this.rs+"-"+dataset+"-"+this.docCount+"docs-percentEntities-F47.txt";
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
