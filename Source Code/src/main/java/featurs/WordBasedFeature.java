/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featurs;

import core.SqlConnection;
import static featurs.Feature.entityTitle;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.lemurproject.galago.utility.Parameters;

/**
 *
 * @author parastoo
 */
public class WordBasedFeature extends Feature {
    
    public WordBasedFeature(String dataset, String rs, int docCount, int start, int end, String feature) {
        super(dataset, rs, docCount, start, end, feature);
    }
    
    @Override
    public void run() throws IOException, SQLException{
        if(resultwriter != null){
            createEntitiyTitle();
            for (int i =start; i<=end; i++) {
                CalculateSimilarityScore(Integer.toString(i));
            }
            resultwriter.close();
        }
            
    }

    protected double CalculateSimilarityScore(String queryId) {
        return 0;
    }
         
    
    protected Parameters getQuery(int i) throws SQLException {
        Parameters queryContent = Parameters.create();
        String query = "SELECT q.id,q.entity,q.title from query_"+dataset+" q where q.id="+i; 
        ResultSet resultset = SqlConnection.select(query);
        while(resultset.next()){
            String number = ""+i;
            String text = resultset.getString(3);
            String[] entities = resultset.getString(2).split(",");
            for (String entry: entities) {
                entry = entry.trim();
                if(entityTitle.containsKey(entry) && !entry.equals("") && !entry.equals("\\s")){
                    text += " " + entityTitle.get(entry);
                }
            }
            text = "#combine("+text+")";
            queryContent.set("number",number);
            queryContent.set("text",text);
        }
        return queryContent;
    }
}
