/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package query_performance_prediction;

import com.github.andrewoma.dexx.collection.Pair;
import core.*;
import core.LearningToRankSVM;
import core.SerializeHashMap;
import core.SqlConnection;
import featurs.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import util.StandardDeviation;
import util.TTest;


/**
 *
 * @author Parastoo
 */
public class Query_performance_prediction {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
        
                
        SqlConnection.getInstance();
        Query_performance_prediction q = new Query_performance_prediction(); 
        LearningToRankSVM ltrsvm = new LearningToRankSVM();
        NeuralNetworkMLP mlp = new NeuralNetworkMLP();
        NNMLP_NotRandom nr = new NNMLP_NotRandom();
        MLP onesplit = new MLP();
            
       
        String dataset = "2009";
        String rs= "ql";
        int docCount = 10;
        int start = 1 ;
        int end = 200 ;
        
//        F01_NQC_QlBased f1_2009 = new F01_NQC_QlBased(dataset, rs, docCount, start, end, "01");
//        F02_AverageScore_QlBased f2_2009 = new F02_AverageScore_QlBased(dataset, rs, docCount, start, end, "02");
//        F03_Clarity_EntityBased f3_2009 = new F03_Clarity_EntityBased(dataset, rs, docCount, start, end, "03");
//        F04_InnerSimilarity_EntityBased f4_2009 = new F04_InnerSimilarity_EntityBased(dataset, rs, docCount, start, end, "04");
//        F05_WIG_QlBase f5_2009 = new F05_WIG_QlBase(dataset, rs, docCount, start, end, "05");
//        F06_AverageScore_CosineBased f6_2009 = new F06_AverageScore_CosineBased(dataset, rs, docCount, start, end, "06");
//        F07_NQC_CosineBased f7_2009 = new F07_NQC_CosineBased(dataset, rs, docCount, start, end, "07");
//        F08_InnerSimilarity_CosineBased f8_2009 = new F08_InnerSimilarity_CosineBased(dataset, rs, docCount, start, end, "08");
//        F09_WIG_CosineBased f9_2009 = new F09_WIG_CosineBased(dataset, rs, docCount, start, end, "09");
//        F10_CrossEntropy f10_2009 = new F10_CrossEntropy(dataset, rs, docCount, start, end, "10");
//        F11_Clarity_AbstractBased f11_2009 = new F11_Clarity_AbstractBased(dataset, rs, docCount, start, end, "11");
//        F12_Qf_AbstractBased f12_2009 = new F12_Qf_AbstractBased(dataset, rs, docCount, start, end, "12");
//        F13_SMV_QlBased f13_2009 = new F13_SMV_QlBased(dataset, rs, docCount, start, end, "13");
//        F14_Sigma_QlBased f14_2009 = new F14_Sigma_QlBased(dataset, rs, docCount, start, end, "14");
//        F15_UEF_NQC_QlBased f15_2009 = new F15_UEF_NQC_QlBased(dataset, rs, docCount, start, end, "15");
//        F16_UEF_AvgScore_CosineBased f16_2009 = new F16_UEF_AvgScore_CosineBased(dataset, rs, docCount, start, end, "16");
//        F17_UEF_Clarity_AbstractBased f17_2009 = new F17_UEF_Clarity_AbstractBased(dataset, rs, docCount, start, end, "17");
//        F18_UEF_Clarity_EntityBased f18_2009 = new F18_UEF_Clarity_EntityBased(dataset, rs, docCount, start, end, "18");
//        F19_UEF_InnerSim_CosineBased f19_2009 = new F19_UEF_InnerSim_CosineBased(dataset, rs, docCount, start, end, "19");
//        F20_UEF_NQC_CosineBased f20_2009 = new F20_UEF_NQC_CosineBased(dataset, rs, docCount, start, end, "20");
//        F21_UEF_WIG_QlBased f21_2009 = new F21_UEF_WIG_QlBased(dataset, rs, docCount, start, end, "21");
//        F22_UEF_WIG_CosineBased f22_2009 = new F22_UEF_WIG_CosineBased(dataset, rs, docCount, start, end, "22");
//        F23_UEF_AvgSimilarity f23_2009 = new F23_UEF_AvgSimilarity(dataset, rs, docCount, start, end, "23");
//        F24_UEF_InnerSim_EntityBased f24_2009 = new F24_UEF_InnerSim_EntityBased(dataset, rs, docCount, start, end, "24");
//        F25_NQC_TFIDF f25_2009 = new F25_NQC_TFIDF(dataset, rs, docCount, start, end, "25");
//        F26_NQC_Abstractbased f26_2009 = new F26_NQC_Abstractbased(dataset, rs, docCount, start, end, "26");
//        F27_WIG_Abstractbased f27_2009 = new F27_WIG_Abstractbased(dataset, rs, docCount, start, end, "27");
//        F28_WIG_TFIDF f28_2009 = new F28_WIG_TFIDF(dataset, rs, docCount, start, end, "28");
//        F29_UEF_NQC_Abstractbased f29_2009 = new F29_UEF_NQC_Abstractbased(dataset, rs, docCount, start, end, "29");
//        F30_UEF_WIG_Abstractbased f30_2009 = new F30_UEF_WIG_Abstractbased(dataset, rs, docCount, start, end, "30");
//        F31_NQC_ExpandSim f31_2009 = new F31_NQC_ExpandSim(dataset, rs, docCount, start, end, "31");
//        F32_WIG_ExpandSim f32_2009 = new F32_WIG_ExpandSim(dataset, rs, docCount, start, end, "32");
//        F33_InnerSimilarity_ExpandSim f33_2009 = new F33_InnerSimilarity_ExpandSim(dataset, rs, docCount, start, end, "33");
//        F34_UEF_NQC_ExpandSim f34_2009 = new F34_UEF_NQC_ExpandSim(dataset, rs, docCount, start, end, "34");
//        F35_UEF_WIG_ExpandSim f35_2009 = new F35_UEF_WIG_ExpandSim(dataset, rs, docCount, start, end, "35");
//        F36_UEF_Inner_ExpandSim f36_2009 = new F36_UEF_Inner_ExpandSim(dataset, rs, docCount, start, end, "36");
//        F37_UEF_NQC_TFIDF f37_2009 = new F37_UEF_NQC_TFIDF(dataset, rs, docCount, start, end, "37");
//        F38_UEF_WIG_TFIDF f38_2009 = new F38_UEF_WIG_TFIDF(dataset, rs, docCount, start, end, "38");
//        F39_Inner_TFIDF f39_2009 = new F39_Inner_TFIDF(dataset, rs, docCount, start, end, "39");
//        F40_UEF_Inner_TFIDF f40_2009 = new F40_UEF_Inner_TFIDF(dataset, rs, docCount, start, end, "40");
//        F41_NQC_CenteredSim f41_2009 = new F41_NQC_CenteredSim(dataset, rs, docCount, start, end, "41");
//        F42_WIG_CenteredSim f42_2009 = new F42_WIG_CenteredSim(dataset, rs, docCount, start, end, "42");
//        F43_Inner_CenteredSim f43_2009 = new F43_Inner_CenteredSim(dataset, rs, docCount, start, end, "43");
//        F44_UEF_NQC_CenteredSim f44_2009 = new F44_UEF_NQC_CenteredSim(dataset, rs, docCount, start, end, "44");
//        F45_UEF_WIG_CenteredSim f45_2009 = new F45_UEF_WIG_CenteredSim(dataset, rs, docCount, start, end, "45");
//        F46_UEF_Inner_CenteredSim f46_2009 = new F46_UEF_Inner_CenteredSim(dataset, rs, docCount, start, end, "46");
//        F47_NQC_Wiser f47_2009 = new F47_NQC_Wiser(dataset, rs, docCount, start, end, "47");
//        F48_WIG_Wiser f48_2009 = new F48_WIG_Wiser(dataset, rs, docCount, start, end, "48");
//        F49_InnerSim_Wiser f49_2009 = new F49_InnerSim_Wiser(dataset, rs, docCount, start, end, "49");
//        F50_UEF_NQC_Wiser f50_2009 = new F50_UEF_NQC_Wiser(dataset, rs, docCount, start, end, "50");
//        F51_UEF_WIG_Wiser f51_2009 = new F51_UEF_WIG_Wiser(dataset, rs, docCount, start, end, "51");
//        F52_UEF_InnerSim_Wiser f52_2009 = new F52_UEF_InnerSim_Wiser(dataset, rs, docCount, start, end, "52");
//        F53_query_doc_Qlsimilarity f53_2009 = new F53_query_doc_Qlsimilarity(dataset, rs, docCount, start, end, "53");
//        F54_query_document_CenteredSimilarity f54_2009 = new F54_query_document_CenteredSimilarity(dataset, rs, docCount, start, end, "54");
//        F55_query_document_ExpandSimilarity f55_2009 = new F55_query_document_ExpandSimilarity(dataset, rs, docCount, start, end, "55");
//        F56_query_document_WiserSimilarity f56_2009 = new F56_query_document_WiserSimilarity(dataset, rs, docCount, start, end, "56");
//        F57_UEF_NQC f57_2009 = new F57_UEF_NQC(dataset, rs, docCount, start, end, "57");

//        q.runfeature(rs, dataset, docCount, start, end, "01",f1_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "02",f2_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "03",f3_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "04",f4_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "05",f5_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "06",f6_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "07",f7_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "08",f8_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "09",f9_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "10",f10_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "11",f11_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "12",f12_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "13",f13_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "14",f14_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "15",f15_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "16",f16_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "17",f17_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "18",f18_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "19",f19_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "20",f20_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "21",f21_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "22",f22_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "23",f23_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "24",f24_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "25",f25_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "26",f26_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "27",f27_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "28",f28_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "29",f29_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "30",f30_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "31",f31_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "32",f32_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "33",f33_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "34",f34_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "35",f35_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "36",f36_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "37",f37_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "38",f38_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "39",f39_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "40",f40_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "41",f41_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "42",f42_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "43",f43_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "44",f44_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "45",f45_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "46",f46_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "47",f47_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "48",f48_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "49",f49_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "50",f50_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "51",f51_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "52",f52_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "53",f53_2009); 
//        q.runfeature(rs, dataset, docCount, start, end, "54",f54_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "55",f55_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "56",f56_2009);
//        q.runfeature(rs, dataset, docCount, start, end, "57",f57_2009);

//        q.runFeature_NumberOfEntity(dataset, rs);
//        q.NormalizeFeatures(rs, dataset);
//        q.testAll(rs, dataset);
//        onesplit.createSplits(dataset);
//        q.NormalizeFeatures(rs, dataset);
//        mlp.createSplits(dataset);
//        nr.createSplits(dataset, start, end);
//        ltrsvm.createSplits(dataset);
//        q.EvalAll(rs, dataset);
//        q.EvalAllNN(rs, dataset);
//        q.EvalAllNNRandom(rs, dataset);

        dataset = "2012";
        rs= "ql";
        docCount = 10;
        start = 201;
        end = 250;
        
//        F01_NQC_QlBased f1_2012 = new F01_NQC_QlBased(dataset, rs, docCount, start, end, "01");
//        F02_AverageScore_QlBased f2_2012 = new F02_AverageScore_QlBased(dataset, rs, docCount, start, end, "02");
//        F03_Clarity_EntityBased f3_2012 = new F03_Clarity_EntityBased(dataset, rs, docCount, start, end, "03");
//        F04_InnerSimilarity_EntityBased f4_2012 = new F04_InnerSimilarity_EntityBased(dataset, rs, docCount, start, end, "04");
//        F05_WIG_QlBase f5_2012 = new F05_WIG_QlBase(dataset, rs, docCount, start, end, "05");
//        F06_AverageScore_CosineBased f6_2012 = new F06_AverageScore_CosineBased(dataset, rs, docCount, start, end, "06");
//        F07_NQC_CosineBased f7_2012 = new F07_NQC_CosineBased(dataset, rs, docCount, start, end, "07");
//        F08_InnerSimilarity_CosineBased f8_2012 = new F08_InnerSimilarity_CosineBased(dataset, rs, docCount, start, end, "08");
//        F09_WIG_CosineBased f9_2012 = new F09_WIG_CosineBased(dataset, rs, docCount, start, end, "09");
//        F10_CrossEntropy f10_2012 = new F10_CrossEntropy(dataset, rs, docCount, start, end, "10");
//        F11_Clarity_AbstractBased f11_2012 = new F11_Clarity_AbstractBased(dataset, rs, docCount, start, end, "11");
//        F12_Qf_AbstractBased f12_2012 = new F12_Qf_AbstractBased(dataset, rs, docCount, start, end, "12");
//        F13_SMV_QlBased f13_2012 = new F13_SMV_QlBased(dataset, rs, docCount, start, end, "13");
//        F14_Sigma_QlBased f14_2012 = new F14_Sigma_QlBased(dataset, rs, docCount, start, end, "14");
//        F15_UEF_NQC_QlBased f15_2012 = new F15_UEF_NQC_QlBased(dataset, rs, docCount, start, end, "15");
//        F16_UEF_AvgScore_CosineBased f16_2012 = new F16_UEF_AvgScore_CosineBased(dataset, rs, docCount, start, end, "16");
//        F17_UEF_Clarity_AbstractBased f17_2012 = new F17_UEF_Clarity_AbstractBased(dataset, rs, docCount, start, end, "17");
//        F18_UEF_Clarity_EntityBased f18_2012 = new F18_UEF_Clarity_EntityBased(dataset, rs, docCount, start, end, "18");
//        F19_UEF_InnerSim_CosineBased f19_2012 = new F19_UEF_InnerSim_CosineBased(dataset, rs, docCount, start, end, "19");
//        F20_UEF_NQC_CosineBased f20_2012 = new F20_UEF_NQC_CosineBased(dataset, rs, docCount, start, end, "20");
//        F21_UEF_WIG_QlBased f21_2012 = new F21_UEF_WIG_QlBased(dataset, rs, docCount, start, end, "21");
//        F22_UEF_WIG_CosineBased f22_2012 = new F22_UEF_WIG_CosineBased(dataset, rs, docCount, start, end, "22");
//        F23_UEF_AvgSimilarity f23_2012 = new F23_UEF_AvgSimilarity(dataset, rs, docCount, start, end, "23");
//        F24_UEF_InnerSim_EntityBased f24_2012 = new F24_UEF_InnerSim_EntityBased(dataset, rs, docCount, start, end, "24");
//        F25_NQC_TFIDF f25_2012 = new F25_NQC_TFIDF(dataset, rs, docCount, start, end, "25");
//        F26_NQC_Abstractbased f26_2012 = new F26_NQC_Abstractbased(dataset, rs, docCount, start, end, "26");
//        F27_WIG_Abstractbased f27_2012 = new F27_WIG_Abstractbased(dataset, rs, docCount, start, end, "27");
//        F28_WIG_TFIDF f28_2012 = new F28_WIG_TFIDF(dataset, rs, docCount, start, end, "28");
//        F29_UEF_NQC_Abstractbased f29_2012 = new F29_UEF_NQC_Abstractbased(dataset, rs, docCount, start, end, "29");
//        F30_UEF_WIG_Abstractbased f30_2012 = new F30_UEF_WIG_Abstractbased(dataset, rs, docCount, start, end, "30");
//        F31_NQC_ExpandSim f31_2012 = new F31_NQC_ExpandSim(dataset, rs, docCount, start, end, "31");
//        F32_WIG_ExpandSim f32_2012 = new F32_WIG_ExpandSim(dataset, rs, docCount, start, end, "32");
//        F33_InnerSimilarity_ExpandSim f33_2012 = new F33_InnerSimilarity_ExpandSim(dataset, rs, docCount, start, end, "33");
//        F34_UEF_NQC_ExpandSim f34_2012 = new F34_UEF_NQC_ExpandSim(dataset, rs, docCount, start, end, "34");
//        F35_UEF_WIG_ExpandSim f35_2012 = new F35_UEF_WIG_ExpandSim(dataset, rs, docCount, start, end, "35");
//        F36_UEF_Inner_ExpandSim f36_2012 = new F36_UEF_Inner_ExpandSim(dataset, rs, docCount, start, end, "36");
//        F37_UEF_NQC_TFIDF f37_2012 = new F37_UEF_NQC_TFIDF(dataset, rs, docCount, start, end, "37");
//        F38_UEF_WIG_TFIDF f38_2012 = new F38_UEF_WIG_TFIDF(dataset, rs, docCount, start, end, "38");
//        F39_Inner_TFIDF f39_2012 = new F39_Inner_TFIDF(dataset, rs, docCount, start, end, "39");
//        F40_UEF_Inner_TFIDF f40_2012 = new F40_UEF_Inner_TFIDF(dataset, rs, docCount, start, end, "40");
//        F41_NQC_CenteredSim f41_2012 = new F41_NQC_CenteredSim(dataset, rs, docCount, start, end, "41");
//        F42_WIG_CenteredSim f42_2012 = new F42_WIG_CenteredSim(dataset, rs, docCount, start, end, "42");
//        F43_Inner_CenteredSim f43_2012 = new F43_Inner_CenteredSim(dataset, rs, docCount, start, end, "43");
//        F44_UEF_NQC_CenteredSim f44_2012 = new F44_UEF_NQC_CenteredSim(dataset, rs, docCount, start, end, "44");
//        F45_UEF_WIG_CenteredSim f45_2012 = new F45_UEF_WIG_CenteredSim(dataset, rs, docCount, start, end, "45");
//        F46_UEF_Inner_CenteredSim f46_2012 = new F46_UEF_Inner_CenteredSim(dataset, rs, docCount, start, end, "46");
//        F47_NQC_Wiser f47_2012 = new F47_NQC_Wiser(dataset, rs, docCount, start, end, "47");
//        F48_WIG_Wiser f48_2012 = new F48_WIG_Wiser(dataset, rs, docCount, start, end, "48");
//        F49_InnerSim_Wiser f49_2012 = new F49_InnerSim_Wiser(dataset, rs, docCount, start, end, "49");
//        F50_UEF_NQC_Wiser f50_2012 = new F50_UEF_NQC_Wiser(dataset, rs, docCount, start, end, "50");
//        F51_UEF_WIG_Wiser f51_2012 = new F51_UEF_WIG_Wiser(dataset, rs, docCount, start, end, "51");
//        F52_UEF_InnerSim_Wiser f52_2012 = new F52_UEF_InnerSim_Wiser(dataset, rs, docCount, start, end, "52");
//        F53_query_doc_Qlsimilarity f53_2012 = new F53_query_doc_Qlsimilarity(dataset, rs, docCount, start, end, "53");
//        F54_query_document_CenteredSimilarity f54_2012 = new F54_query_document_CenteredSimilarity(dataset, rs, docCount, start, end, "54");
//        F55_query_document_ExpandSimilarity f55_2012 = new F55_query_document_ExpandSimilarity(dataset, rs, docCount, start, end, "55");
//        F56_query_document_WiserSimilarity f56_2012 = new F56_query_document_WiserSimilarity(dataset, rs, docCount, start, end, "56");
//        F57_UEF_NQC f57_2012 = new F57_UEF_NQC(dataset, rs, docCount, start, end, "57");

//        q.runfeature(rs, dataset, docCount, start, end, "01",f1_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "02",f2_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "03",f3_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "04",f4_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "05",f5_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "06",f6_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "07",f7_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "08",f8_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "09",f9_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "10",f10_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "11",f11_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "12",f12_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "13",f13_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "14",f14_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "15",f15_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "16",f16_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "17",f17_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "18",f18_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "19",f19_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "20",f20_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "21",f21_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "22",f22_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "23",f23_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "24",f24_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "25",f25_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "26",f26_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "27",f27_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "28",f28_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "29",f29_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "30",f30_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "31",f31_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "32",f32_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "33",f33_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "34",f34_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "35",f35_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "36",f36_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "37",f37_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "38",f38_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "39",f39_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "40",f40_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "41",f41_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "42",f42_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "43",f43_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "44",f44_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "45",f45_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "46",f46_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "47",f47_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "48",f48_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "49",f49_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "50",f50_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "51",f51_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "52",f52_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "53",f53_2012); 
//        q.runfeature(rs, dataset, docCount, start, end, "54",f54_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "55",f55_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "56",f56_2012);
//        q.runfeature(rs, dataset, docCount, start, end, "57",f57_2012);
        

//        q.runFeature_NumberOfEntity(dataset, rs);
//        q.NormalizeFeatures(rs, dataset);
//        q.testAll(rs, dataset);
//        q.NormalizeFeatures(rs, dataset);
//        mlp.createSplits(dataset);
//        ltrsvm.createSplits(dataset);
//        q.EvalAll(rs, dataset);
//        q.EvalAllNN(rs, dataset);
        
        dataset = "robust";
        rs= "ql";
        docCount = 10;
        start =301 ;
        end = 700;
        
//        F01_NQC_QlBased f1_robust = new F01_NQC_QlBased(dataset, rs, docCount, start, end, "01");
//        F02_AverageScore_QlBased f2_robust = new F02_AverageScore_QlBased(dataset, rs, docCount, start, end, "02");
//        F03_Clarity f3_robust = new F03_Clarity(dataset, rs, docCount, start, end, "03");
//        F04_InnerSimilarity_EntityBased f4_robust = new F04_InnerSimilarity_EntityBased(dataset, rs, docCount, start, end, "04");
//        F05_WIG_QlBase f5_robust = new F05_WIG_QlBase(dataset, rs, docCount, start, end, "05");
//        F06_AverageScore_CosineBased f6_robust = new F06_AverageScore_CosineBased(dataset, rs, docCount, start, end, "06");
//        F07_NQC_CosineBased f7_robust= new F07_NQC_CosineBased(dataset, rs, docCount, start, end, "07");
//        F08_InnerSimilarity_CosineBased f8_robust = new F08_InnerSimilarity_CosineBased(dataset, rs, docCount, start, end, "08");
//        F09_WIG_CosineBased f9_robust = new F09_WIG_CosineBased(dataset, rs, docCount, start, end, "09");
//        F10_CrossEntropy f10_robust = new F10_CrossEntropy(dataset, rs, docCount, start, end, "10");
//        F11_Clarity_AbstractBased f11_robust = new F11_Clarity_AbstractBased(dataset, rs, docCount, start, end, "11");
//        F12_Qf_AbstractBased f12_robust = new F12_Qf_AbstractBased(dataset, rs, docCount, start, end, "12");
//        F13_SMV_QlBased f13_robust = new F13_SMV_QlBased(dataset, rs, docCount, start, end, "13");
//        F14_Sigma_QlBased f14_robust = new F14_Sigma_QlBased(dataset, rs, docCount, start, end, "14");
//        F15_UEF_NQC_QlBased f15_robust = new F15_UEF_NQC_QlBased(dataset, rs, docCount, start, end, "15");
//        F16_UEF_AvgScore_CosineBased f16_robust = new F16_UEF_AvgScore_CosineBased(dataset, rs, docCount, start, end, "16");
//        F17_UEF_Clarity_AbstractBased f17_robust = new F17_UEF_Clarity_AbstractBased(dataset, rs, docCount, start, end, "17");
//        F18_UEF_Clarity_EntityBased f18_robust = new F18_UEF_Clarity_EntityBased(dataset, rs, docCount, start, end, "18");
//        F19_UEF_InnerSim_CosineBased f19_robust = new F19_UEF_InnerSim_CosineBased(dataset, rs, docCount, start, end, "19");
//        F20_UEF_NQC_CosineBased f20_robust = new F20_UEF_NQC_CosineBased(dataset, rs, docCount, start, end, "20");
//        F21_UEF_WIG_QlBased f21_robust = new F21_UEF_WIG_QlBased(dataset, rs, docCount, start, end, "21");
//        F22_UEF_WIG_CosineBased f22_robust = new F22_UEF_WIG_CosineBased(dataset, rs, docCount, start, end, "22");
//        F23_UEF_AvgSimilarity f23_robust = new F23_UEF_AvgSimilarity(dataset, rs, docCount, start, end, "23");
//        F24_UEF_InnerSim_EntityBased f24_robust = new F24_UEF_InnerSim_EntityBased(dataset, rs, docCount, start, end, "24");
//        F25_NQC_TFIDF f25_robust = new F25_NQC_TFIDF(dataset, rs, docCount, start, end, "25");
//        F26_NQC_Abstractbased f26_robust = new F26_NQC_Abstractbased(dataset, rs, docCount, start, end, "26");
//        F27_WIG_Abstractbased f27_robust = new F27_WIG_Abstractbased(dataset, rs, docCount, start, end, "27");
//        F28_WIG_TFIDF f28_robust = new F28_WIG_TFIDF(dataset, rs, docCount, start, end, "28");
//        F29_UEF_NQC_Abstractbased f29_robust = new F29_UEF_NQC_Abstractbased(dataset, rs, docCount, start, end, "29");
//        F30_UEF_WIG_Abstractbased f30_robust = new F30_UEF_WIG_Abstractbased(dataset, rs, docCount, start, end, "30");
//        F31_NQC_ExpandSim f31_robust = new F31_NQC_ExpandSim(dataset, rs, docCount, start, end, "31");
//        F32_WIG_ExpandSim f32_robust = new F32_WIG_ExpandSim(dataset, rs, docCount, start, end, "32");
//        F33_InnerSimilarity_ExpandSim f33_robust = new F33_InnerSimilarity_ExpandSim(dataset, rs, docCount, start, end, "33");
//        F34_UEF_NQC_ExpandSim f34_robust = new F34_UEF_NQC_ExpandSim(dataset, rs, docCount, start, end, "34");
//        F35_UEF_WIG_ExpandSim f35_robust = new F35_UEF_WIG_ExpandSim(dataset, rs, docCount, start, end, "35");
//        F36_UEF_Inner_ExpandSim f36_robust = new F36_UEF_Inner_ExpandSim(dataset, rs, docCount, start, end, "36");
//        F37_UEF_NQC_TFIDF f37_robust = new F37_UEF_NQC_TFIDF(dataset, rs, docCount, start, end, "37");
//        F38_UEF_WIG_TFIDF f38_robust = new F38_UEF_WIG_TFIDF(dataset, rs, docCount, start, end, "38");
//        F39_Inner_TFIDF f39_robust = new F39_Inner_TFIDF(dataset, rs, docCount, start, end, "39");
//        F40_UEF_Inner_TFIDF f40_robust = new F40_UEF_Inner_TFIDF(dataset, rs, docCount, start, end, "40");
//        F41_NQC_CenteredSim f41_robust = new F41_NQC_CenteredSim(dataset, rs, docCount, start, end, "41");
//        F42_WIG_CenteredSim f42_robust = new F42_WIG_CenteredSim(dataset, rs, docCount, start, end, "42");
//        F43_Inner_CenteredSim f43_robust = new F43_Inner_CenteredSim(dataset, rs, docCount, start, end, "43");
//        F44_UEF_NQC_CenteredSim f44_robust = new F44_UEF_NQC_CenteredSim(dataset, rs, docCount, start, end, "44");
//        F45_UEF_WIG_CenteredSim f45_robust = new F45_UEF_WIG_CenteredSim(dataset, rs, docCount, start, end, "45");
//        F46_UEF_Inner_CenteredSim f46_robust = new F46_UEF_Inner_CenteredSim(dataset, rs, docCount, start, end, "46");
//        F47_NQC_Wiser f47_robust = new F47_NQC_Wiser(dataset, rs, docCount, start, end, "47");
//        F48_WIG_Wiser f48_robust = new F48_WIG_Wiser(dataset, rs, docCount, start, end, "48");
//        F49_InnerSim_Wiser f49_robust = new F49_InnerSim_Wiser(dataset, rs, docCount, start, end, "49");
//        F50_UEF_NQC_Wiser f50_robust = new F50_UEF_NQC_Wiser(dataset, rs, docCount, start, end, "50");
//        F51_UEF_WIG_Wiser f51_robust = new F51_UEF_WIG_Wiser(dataset, rs, docCount, start, end, "51");
//        F52_UEF_InnerSim_Wiser f52_robust = new F52_UEF_InnerSim_Wiser(dataset, rs, docCount, start, end, "52");
//        F53_query_doc_Qlsimilarity f53_robust = new F53_query_doc_Qlsimilarity(dataset, rs, docCount, start, end, "53");
//        F54_query_document_CenteredSimilarity f54_robust = new F54_query_document_CenteredSimilarity(dataset, rs, docCount, start, end, "54");
//        F55_query_document_ExpandSimilarity f55_robust = new F55_query_document_ExpandSimilarity(dataset, rs, docCount, start, end, "55");
//        F56_query_document_WiserSimilarity f56_robust = new F56_query_document_WiserSimilarity(dataset, rs, docCount, start, end, "56");
//        F57_UEF_NQC f57_robust = new F57_UEF_NQC(dataset, rs, docCount, start, end, "57");
        
//        q.runfeature(rs, dataset, docCount, start, end, "01",f1_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "02",f2_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "03",f3_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "04",f4_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "05",f5_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "06",f6_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "07",f7_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "08",f8_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "09",f9_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "10",f10_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "11",f11_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "12",f12_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "13",f13_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "14",f14_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "15",f15_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "16",f16_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "17",f17_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "18",f18_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "19",f19_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "20",f20_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "21",f21_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "22",f22_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "23",f23_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "24",f24_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "25",f25_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "26",f26_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "27",f27_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "28",f28_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "29",f29_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "30",f30_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "31",f31_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "32",f32_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "33",f33_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "34",f34_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "35",f35_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "36",f36_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "37",f37_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "38",f38_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "39",f39_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "40",f40_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "41",f41_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "42",f42_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "43",f43_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "44",f44_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "45",f45_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "46",f46_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "47",f47_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "48",f48_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "49",f49_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "50",f50_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "51",f51_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "52",f52_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "53",f53_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "54",f54_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "55",f55_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "56",f56_robust);
//        q.runfeature(rs, dataset, docCount, start, end, "57",f57_robust);
        

//        q.runFeature_NumberOfEntity(dataset, rs);
//        q.NormalizeFeatures(rs, dataset);
        q.testAll(rs, dataset);
//        nr.createSplits(dataset, start, end);
//        q.NormalizeFeatures(rs, dataset);
//        mlp.createSplits(dataset);
//        ltrsvm.createSplits(dataset);
//        q.EvalAll(rs, dataset);
//        q.EvalAllNN(rs, dataset);
//         q.EvalAllNNRandom(rs, dataset);
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
    
    public static HashMap<String, Double> readPerfFromFile (String filename, String targetMetric) throws FileNotFoundException {
        HashMap<String, Double> result = new HashMap<>();
        Scanner in = new Scanner(new File(filename));
        while (in.hasNextLine()){
            String metric = in.next();
            String qid = in.next();
            if (qid.equals("all"))
                break;
            double val = in.nextDouble();
            if (metric.equals(targetMetric)){
                result.put(qid,val);
            }
        }
        in.close();
        return result;
    }
    
    public static double [] evalAll(List <Pair<String, Double>> a, List <Pair<String, Double>> b) {
        double[] result = new double[3];
        result[0] = computeSpearmannCorrelation(a, b);
        result[1] = computePearsonCorrelation(a, b);
        result[2] = computeKendallsTauDistance(a, b);
        return  result;
    }
    
    public static double computeSpearmannCorrelation(List <Pair<String, Double>> a, List <Pair<String, Double>> b) {
        Comparator comparator = new Comparator<Pair<String, Double>>(){
            @Override
            public int compare(Pair<String, Double> p1, Pair<String, Double> p2) {
                return p2.component2().compareTo(p1.component2());
            }
        };
        Collections.sort(a, comparator);
        Collections.sort(b, comparator);

        Map <String, Integer> first = getMapRank(a);
        for (int i=0; i<a.size(); i++) {
            String q_i = a.get(i).component1();

        }
        for (int i=0; i<b.size(); i++) {
            String q_i = b.get(i).component1();

        }
        Map <String, Integer> second = getMapRank(b);

        double cov = 0, var1 = 0, var2 = 0;
        for (int i=0; i<a.size(); i++) {
            for (int j=i+1; j<a.size(); j++) {
                String q_i = a.get(i).component1();
                String q_j = a.get(j).component1();
                cov += ((first.get(q_i) - first.get(q_j)) * (second.get(q_i) - second.get(q_j)));
                var1 += ((first.get(q_i) - first.get(q_j)) * (first.get(q_i) - first.get(q_j)));
                var2 += ((second.get(q_i) - second.get(q_j)) * (second.get(q_i) - second.get(q_j)));
            }
        }
        return cov / Math.sqrt(var1 * var2);
    }
    
    public static double computePearsonCorrelation(List <Pair<String, Double>> a, List <Pair<String, Double>> b) {
        Map <String, Double> first = pairToMap(a);
        Map <String, Double> second = pairToMap(b);

        double cov = 0, var1 = 0, var2 = 0;
        for (int i=0; i<a.size(); i++) {
            for (int j=i+1; j<a.size(); j++) {
                String q_i = a.get(i).component1();
                String q_j = a.get(j).component1();
                cov += ((first.get(q_i) - first.get(q_j)) * (second.get(q_i) - second.get(q_j)));
                var1 += ((first.get(q_i) - first.get(q_j)) * (first.get(q_i) - first.get(q_j)));
                var2 += ((second.get(q_i) - second.get(q_j)) * (second.get(q_i) - second.get(q_j)));
            }
        }
        return cov / Math.sqrt(var1 * var2);
    }
    
    public static double computeKendallsTauDistance(List <Pair<String, Double>> a, List <Pair<String, Double>> b) {
        Map <String, Double> first = pairToMap(a);
        Map <String, Double> second = pairToMap(b);

        int concordant = 0, discordant = 0;
        for (int i=0; i<a.size(); i++) {
            for (int j=i+1; j<a.size(); j++) {
                String q_i = a.get(i).component1();
                String q_j = a.get(j).component1();
                if ((first.get(q_i) - first.get(q_j))*(second.get(q_i) - second.get(q_j)) < 0)
                    discordant++;
                if ((first.get(q_i) - first.get(q_j))*(second.get(q_i) - second.get(q_j)) > 0)
                    concordant++;
            }
        }
        double z = (double)(a.size()*(a.size()-1))/2.;
        return (double)(concordant-discordant)/z;
    }
    
    public static Map <String, Integer> getMapRank (List <Pair<String, Double>> sortedList) {
        Map <String, Integer> result = new HashMap <> ();
        int rank = 0;
        double previousScore = -1000;
        for (int i=0; i<sortedList.size(); i++) {
            if (sortedList.get(i).component2() != previousScore) {
                rank++;
                previousScore = sortedList.get(i).component2();
            }
            result.put(sortedList.get(i).component1(), rank);
        }
        return result;
    }
    
    public static Map <String, Double> pairToMap (List <Pair<String, Double>> list) {
        Map <String, Double> result = new HashMap <> ();
        for (int i=0; i<list.size(); i++) {
            result.put(list.get(i).component1(), list.get(i).component2());
        }
        return result;
    }

    private static double computePvalue(List<Pair<String, Double>> trainMap, List<Pair<String, Double>> trainSet) {
        TTest ttest = new TTest();
        double[] map = new double[trainMap.size()];
        double[] set = new double[trainSet.size()];
        int i = 0;
        for (Pair p :trainMap) {
            map[i] = (double) p.component2();
            i++;
        }
        i=0;
        for (Pair p :trainSet) {
            set[i] = (double) p.component2();
            i++;
        }
        double pvalue = ttest.pairedTTest(map,set);
        return pvalue;
    }

    
    
    public void runfeature(String rs, String dataset, int documentNumber, int start, int end, String fname, Feature f){
        
        try {
            f.run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    
    
    public void testAll(String rs, String dataset) throws IOException{
        File[] files = new File("./crossfold/"+dataset+"/result/").listFiles();
        File dir = new File("./crossfold/"+dataset+"/correlation/");
        FileUtils.cleanDirectory(dir);
        for(File f : files){
            try {
                String fname = f.getName();
                String resultPath = "./crossfold/"+dataset+"/correlation/correlation-"+fname;
                testOne(rs, dataset, resultPath, "./crossfold/"+dataset+"/result/"+fname,fname);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    public List<Double> testOne(String rs, String dataset, String resultPath,String fname,String name) throws UnsupportedEncodingException, FileNotFoundException, IOException{
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(
                          new FileOutputStream(resultPath), "UTF8"));
        BufferedWriter bw_difficult = new BufferedWriter( new OutputStreamWriter(
                          new FileOutputStream(resultPath+"-difficult.txt"), "UTF8"));
        BufferedWriter bw_easy = new BufferedWriter( new OutputStreamWriter(
                          new FileOutputStream(resultPath+"-easy.txt"), "UTF8"));
        String mapFile = "./crossfold/"+dataset+"/map/map-"+rs+"-"+dataset+".txt";
        double threshold = 0.05;
        
        List<Double> results = new ArrayList<>();
        HashMap<String, Double> map =  Query_performance_prediction.readPerfFromFile(mapFile,"map");
        
        List <Pair<String, Double>> trainMap = new ArrayList<>();
        List <Pair<String, Double>> trainMap_easy = new ArrayList<>();
        List <Pair<String, Double>> trainMap_difficult = new ArrayList<>();
        
        System.out.println("file: "+fname);
        HashMap<String,Double> predictions = Query_performance_prediction.readPerfFromFils(fname);
        List <Pair<String, Double>> trainSet = new ArrayList<>();
        List <Pair<String, Double>> trainSetEasy = new ArrayList<>();
        List <Pair<String, Double>> trainSetDifficult = new ArrayList<>();

        for(String k:predictions.keySet()){
            double score = 0;
            score = predictions.get(k);
            trainSet.add(new Pair<String,Double>(k,score));
        }
        for(Pair p : trainSet){
            if(map.containsKey(p.component1().toString())){
                double value = map.get(p.component1().toString());
                String key = p.component1().toString();
                trainMap.add(new Pair<String,Double>(key,value));
                if(value <= threshold){
                    trainSetDifficult.add(p);
                    trainMap_difficult.add(new Pair<String,Double>(key,value));
                }
                else if(value>threshold){
                    trainSetEasy.add(p);
                    trainMap_easy.add(new Pair<String,Double>(key,value));
                }
            }
            else{
                trainMap.add(new Pair<String,Double>(p.component1().toString(),0.0));
            }
        }
        
        
        Compare(trainMap, trainSet, dataset, name);
        double pearson = Query_performance_prediction.evalAll(trainMap, trainSet)[1];
        double kendall  = Query_performance_prediction.evalAll(trainMap, trainSet)[2];
        double spearman = Query_performance_prediction.evalAll(trainMap, trainSet)[0];
        
        double pearson_easy = Query_performance_prediction.evalAll(trainMap_easy, trainSetEasy)[1];
        double kendall_easy  = Query_performance_prediction.evalAll(trainMap_easy, trainSetEasy)[2];
        double spearman_easy = Query_performance_prediction.evalAll(trainMap_easy, trainSetEasy)[0];
        
        double pearson_difficult = Query_performance_prediction.evalAll(trainMap_difficult, trainSetDifficult)[1];
        double kendall_difficult  = Query_performance_prediction.evalAll(trainMap_difficult, trainSetDifficult)[2];
        double spearman_difficult = Query_performance_prediction.evalAll(trainMap_difficult, trainSetDifficult)[0];

        results.add(pearson);
        results.add(kendall);
        results.add(spearman);

        bw.write("pearson is: "+pearson+"\n");
        bw.write("kandal is:"+kendall+"\n");
        bw.write("spearman is:"+spearman+"\n");
        
        bw_easy.write("pearson is: "+pearson_easy+"\n");
        bw_easy.write("kandal is:"+kendall_easy+"\n");
        bw_easy.write("spearman is:"+spearman_easy+"\n");
        
        bw_difficult.write("pearson is: "+pearson_difficult+"\n");
        bw_difficult.write("kandal is:"+kendall_difficult+"\n");
        bw_difficult.write("spearman is:"+spearman_difficult+"\n");

        bw.close();
        bw_difficult.close();
        bw_easy.close();
        return results;
    }
    
    public List<Double> testOne_TTest(String rs, String dataset, String resultPath,String fname,String name) throws UnsupportedEncodingException, FileNotFoundException, IOException{
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(
                          new FileOutputStream(resultPath), "UTF8"));
        String mapFile = "./crossfold/"+dataset+"/map/map-"+rs+"-"+dataset+".txt";
        String clarityFile = "./crossfold/"+dataset+"/result/ql-"+dataset+"-10docs-percentEntities-F53-clarity.txt";
        String nqcFile = "./crossfold/"+dataset+"/result/ql-"+dataset+"-10docs-percentEntities-F54-nqc.txt";
        String qfFile = "./crossfold/"+dataset+"/result/ql-"+dataset+"-10docs-percentEntities-F55-qf.txt";
        String smvFile = "./crossfold/"+dataset+"/result/ql-"+dataset+"-10docs-percentEntities-F56-smv.txt";
        String uefFile = "./crossfold/"+dataset+"/result/ql-"+dataset+"-10docs-percentEntities-F57-uef.txt";
        String wigFile = "./crossfold/"+dataset+"/result/ql-"+dataset+"-10docs-percentEntities-F58-wig.txt";
        List<Double> results = new ArrayList<>();
        HashMap<String, Double> map =  Query_performance_prediction.readPerfFromFile(mapFile,"map");
        
        List <Pair<String, Double>> trainMap = new ArrayList<>();
        List <Pair<String, Double>> trainClarity = new ArrayList<>();
        List <Pair<String, Double>> trainNQC = new ArrayList<>();
        List <Pair<String, Double>> trainWIG = new ArrayList<>();
        List <Pair<String, Double>> trainSMV = new ArrayList<>();
        List <Pair<String, Double>> trainQF = new ArrayList<>();
        List <Pair<String, Double>> trainUEF = new ArrayList<>();
        System.out.println("file: "+fname);
        HashMap<String,Double> predictions = Query_performance_prediction.readPerfFromFils(fname);
        HashMap<String, Double> clarity =  Query_performance_prediction.readPerfFromFils(clarityFile);
        HashMap<String, Double> nqc =  Query_performance_prediction.readPerfFromFils(nqcFile);
        HashMap<String, Double> qf =  Query_performance_prediction.readPerfFromFils(qfFile);
        HashMap<String, Double> smv =  Query_performance_prediction.readPerfFromFils(smvFile);
        HashMap<String, Double> uef =  Query_performance_prediction.readPerfFromFils(uefFile);
        HashMap<String, Double> wig =  Query_performance_prediction.readPerfFromFils(wigFile);
        List <Pair<String, Double>> trainSet = new ArrayList<>();

        for(String k:predictions.keySet()){
            double score = 0;
            score = predictions.get(k);
            trainSet.add(new Pair<String,Double>(k,score));
        }
        for(Pair p : trainSet){
            if(map.containsKey(p.component1().toString())){
                double value = map.get(p.component1().toString());
                String key = p.component1().toString();
                trainMap.add(new Pair<String,Double>(key,value));
            }
            else{
                trainMap.add(new Pair<String,Double>(p.component1().toString(),0.0));
            }
        }
        
        for(Pair p : trainSet){
            if(clarity.containsKey(p.component1().toString())){
                double value = clarity.get(p.component1().toString());
                String key = p.component1().toString();
                trainClarity.add(new Pair<String,Double>(key,value));
            }
            else{
                trainClarity.add(new Pair<String,Double>(p.component1().toString(),0.0));
            }
        }
        for(Pair p : trainSet){
            if(nqc.containsKey(p.component1().toString())){
                double value = nqc.get(p.component1().toString());
                String key = p.component1().toString();
                trainNQC.add(new Pair<String,Double>(key,value));
            }
            else{
                trainNQC.add(new Pair<String,Double>(p.component1().toString(),0.0));
            }
        }
        for(Pair p : trainSet){
            if(qf.containsKey(p.component1().toString())){
                double value = qf.get(p.component1().toString());
                String key = p.component1().toString();
                trainQF.add(new Pair<String,Double>(key,value));
            }
            else{
                trainQF.add(new Pair<String,Double>(p.component1().toString(),0.0));
            }
        }
        for(Pair p : trainSet){
            if(smv.containsKey(p.component1().toString())){
                double value = smv.get(p.component1().toString());
                String key = p.component1().toString();
                trainSMV.add(new Pair<String,Double>(key,value));
            }
            else{
                trainSMV.add(new Pair<String,Double>(p.component1().toString(),0.0));
            }
        }
        for(Pair p : trainSet){
            if(uef.containsKey(p.component1().toString())){
                double value = uef.get(p.component1().toString());
                String key = p.component1().toString();
                trainUEF.add(new Pair<String,Double>(key,value));
            }
            else{
                trainUEF.add(new Pair<String,Double>(p.component1().toString(),0.0));
            }
        }
        for(Pair p : trainSet){
            if(wig.containsKey(p.component1().toString())){
                double value = wig.get(p.component1().toString());
                String key = p.component1().toString();
                trainWIG.add(new Pair<String,Double>(key,value));
            }
            else{
                trainWIG.add(new Pair<String,Double>(p.component1().toString(),0.0));
            }
        }
        Compare(trainMap, trainSet, dataset, name);
        double pearson = Query_performance_prediction.evalAll(trainMap, trainSet)[1];
        double kendall  = Query_performance_prediction.evalAll(trainMap, trainSet)[2];
        double spearman = Query_performance_prediction.evalAll(trainMap, trainSet)[0];
        double pvalue = Query_performance_prediction.computePvalue(trainMap, trainSet);
        double pvalueClarity = Query_performance_prediction.computePvalue(trainClarity, trainSet);
        double pvalueNQC = Query_performance_prediction.computePvalue(trainNQC, trainSet);
        double pvalueWIG = Query_performance_prediction.computePvalue(trainWIG, trainSet);
        double pvalueSMV = Query_performance_prediction.computePvalue(trainSMV, trainSet);
        double pvalueQF = Query_performance_prediction.computePvalue(trainQF, trainSet);
        double pvalueUEF = Query_performance_prediction.computePvalue(trainUEF, trainSet);

        results.add(pearson);
        results.add(kendall);
        results.add(spearman);
        results.add(pvalue);
        results.add(pvalueClarity);
        results.add(pvalueNQC);
        results.add(pvalueWIG);
        results.add(pvalueSMV);
        results.add(pvalueQF);
        results.add(pvalueUEF);

        bw.write("pearson is: "+pearson+"\n");
        bw.write("kandal is:"+kendall+"\n");
        bw.write("spearman is:"+spearman+"\n");
        bw.write("pvalue is:"+pvalue+"\n");
        bw.write("pvalue-Clarity is:"+pvalueClarity+"\n");
        bw.write("pvalue_NQC is:"+pvalueNQC+"\n");
        bw.write("pvalue_WIG is:"+pvalueWIG+"\n");
        bw.write("pvalue_SMV is:"+pvalueSMV+"\n");
        bw.write("pvalue_QF is:"+pvalueQF+"\n");
        bw.write("pvalue_UEF is:"+pvalueUEF+"\n");

        bw.close();
        return results;
    }
    
    public void EvalAll(String rs, String dataset) {
        File[] files = new File("./crossfold/"+dataset+"/foldsvmList/").listFiles();
        for(File f : files){
            try {
                String fname = f.getName();
                if(fname.equals("foldsvm-120-14-2012"))
                    System.out.println("here");
                String dir = "./crossfold/"+dataset+"/foldsvmList/"+fname+"/";
                evalOne(rs, dataset, dir);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        
    }
    
    public void EvalAllNN(String rs, String dataset) throws IOException {
        File[] files = new File("./crossfold/"+dataset+"/foldneuralList/").listFiles();
        for(File f : files){
            try {
                String fname = f.getName();
                String dir = "./crossfold/"+dataset+"/foldneuralList/"+fname+"/";
                evalNNOne(rs, dataset, dir);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    public void EvalAllNNRandom(String rs, String dataset) throws IOException {
        File[] files = new File("./crossfold/"+dataset+"/foldneuralList/").listFiles();
        for(File f : files){
            try {
                String fname = f.getName();
                String dir = "./crossfold/"+dataset+"/foldneuralList/"+fname+"/";
                evalNNOneRandom(rs, dataset, dir);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    
    public void NormalizeFeatures(String rs, String dataset) throws IOException{
         File[] files = new File("./crossfold/"+dataset+"/result/").listFiles();
        File dir = new File("./crossfold/"+dataset+"/normalizedresult/");
        FileUtils.cleanDirectory(dir);
        for(File f : files){
            try {
                String fname = f.getName();
                String resultPath = "./crossfold/"+dataset+"/normalizedresult/normalizedresult-"+fname;
                normalizeOne(rs, dataset, resultPath, "./crossfold/"+dataset+"/result/"+fname);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void normalizeOne(String rs, String dataset, String resultPath,String fname) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(
                          new FileOutputStream(resultPath), "UTF8"));
        List<Double> results = new ArrayList<>();
        HashMap<String,Double> predictions = Query_performance_prediction.readPerfFromFils(fname);

        for(String k:predictions.keySet()){
            double score = 0;
            score = predictions.get(k);
            results.add(score);
        }
        StandardDeviation sd = new StandardDeviation();
        double mean = sd.mean(results);
        double standardDeviation = sd.calculateSD(results);
        if(standardDeviation !=0){
            for (String k:predictions.keySet()) {
                double score = 0;
                score = predictions.get(k);
                double newScore = (score - mean) / standardDeviation;
                predictions.put(k, newScore);
                bw.write(k+" "+newScore+"\n");
            }
        }
        bw.flush();
        bw.close();
     
    }

    

    private void evalNNOne(String rs, String dataset, String dir) {
        int count =0;
        try{
            File[] files = new File(dir).listFiles();
            int length = 60;
//            if(files.length>40)
//                length =60;
//            if(files.length>100)
//                length =120;
            List<Double> finalPearsons = new ArrayList<>();
            List<Double> finalKendalls = new ArrayList<>();
            List<Double> finalPvalues = new ArrayList<>();
            List<Double> finalPvalues_clarity = new ArrayList<>();
            List<Double> finalPvalues_nqc = new ArrayList<>();
            List<Double> finalPvalues_qf = new ArrayList<>();
            List<Double> finalPvalues_smv = new ArrayList<>();
            List<Double> finalPvalues_uef = new ArrayList<>();
            List<Double> finalPvalues_wig = new ArrayList<>();
                
            
            for (int i = 1; i <= length; i++) {
                for (int j = 1; j <= 2; j++) {
                    BufferedReader br1 = new BufferedReader(new FileReader(new File(dir+i+"/"+j+"/test.txt")));
                    BufferedReader br2 = new BufferedReader(new FileReader(new File(dir+i+"/"+j+"/prediction.txt")));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(dir+i+"/"+j+"/predictionWithQueries.txt")));
                    String query = "";
                    count = 1;
                    while((query =br1.readLine())!= null)
                    {
                        query=query.trim();
                        String prediction;
                        if((prediction =br2.readLine())!= null)
                            prediction = prediction.trim();
                        else
                            System.out.println("exception");
                        bw.write(query+" "+prediction+"\n");
                        count ++;
                    }
                    br1.close();
                    br2.close();
                    bw.close();
                }
                
            }
            
            for (int i = 1; i <= length; i++) {
                List<Double> eachFoldPearsons = new ArrayList<>();
                List<Double> eachFoldKendalls = new ArrayList<>();
                List<Double> eachFoldPvalues = new ArrayList<>();
                List<Double> eachFoldPvalues_clarity = new ArrayList<>();
                List<Double> eachFoldPvalues_nqc = new ArrayList<>();
                List<Double> eachFoldPvalues_qf = new ArrayList<>();
                List<Double> eachFoldPvalues_smv = new ArrayList<>();
                List<Double> eachFoldPvalues_uef = new ArrayList<>();
                List<Double> eachFoldPvalues_wig = new ArrayList<>();
                for (int j = 1; j <= 2; j++) {
                    String resultPath = "./crossfold/temp.txt";
                    String fname = dir+i+"/"+j+"/predictionWithQueries.txt";
                    List<Double> results = testOne(rs, dataset, resultPath, fname,fname);
                    if(!results.get(0).isNaN() && !results.get(1).isNaN()){
                        eachFoldPearsons.add(results.get(0));
                        eachFoldKendalls.add(results.get(1));
                        eachFoldPvalues.add(results.get(3));
                        eachFoldPvalues_clarity.add(results.get(4));
                        eachFoldPvalues_nqc.add(results.get(5));
                        eachFoldPvalues_qf.add(results.get(6));
                        eachFoldPvalues_smv.add(results.get(7));
                        eachFoldPvalues_uef.add(results.get(8));
                        eachFoldPvalues_wig.add(results.get(9));
                    }
                }
                if( StandardDeviation.mean(eachFoldPearsons) <=1 && !Double.isNaN(StandardDeviation.mean(eachFoldPearsons))){

                    finalPearsons.add(StandardDeviation.mean(eachFoldPearsons));
                    finalKendalls.add(StandardDeviation.mean(eachFoldKendalls));
                    finalPvalues.add(StandardDeviation.mean(eachFoldPvalues));
                    if (!Double.isNaN(StandardDeviation.mean(eachFoldPvalues_clarity)))
                        finalPvalues_clarity.add(StandardDeviation.mean(eachFoldPvalues_clarity));
                    if (!Double.isNaN(StandardDeviation.mean(eachFoldPvalues_nqc)))
                        finalPvalues_nqc.add(StandardDeviation.mean(eachFoldPvalues_nqc));
                    if (!Double.isNaN(StandardDeviation.mean(eachFoldPvalues_qf)))
                        finalPvalues_qf.add(StandardDeviation.mean(eachFoldPvalues_qf));
                    if (!Double.isNaN(StandardDeviation.mean(eachFoldPvalues_smv)))
                        finalPvalues_smv.add(StandardDeviation.mean(eachFoldPvalues_smv));
                    if (!Double.isNaN(StandardDeviation.mean(eachFoldPvalues_uef)))
                        finalPvalues_uef.add(StandardDeviation.mean(eachFoldPvalues_uef));
                    if (!Double.isNaN(StandardDeviation.mean(eachFoldPvalues_wig)))
                        finalPvalues_wig.add(StandardDeviation.mean(eachFoldPvalues_wig));
                }
            }
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(
                          new FileOutputStream(dir+"/finalresults.txt"), "UTF8"));
            
            bw.write("pearson:"+StandardDeviation.meanWithN(finalPearsons,30)+"\n");
            bw.write("kendall:"+StandardDeviation.meanWithN(finalKendalls, 30)+"\n");
            bw.write("pvalue:"+StandardDeviation.meanWithN(finalPvalues,30)+"\n");
            bw.write("pvalue_clarity:"+StandardDeviation.meanWithN(finalPvalues_clarity,finalPvalues.size())+"\n");
            bw.write("pvalue_nqc:"+StandardDeviation.meanWithN(finalPvalues_nqc,finalPvalues.size())+"\n");
            bw.write("pvalue_qf:"+StandardDeviation.meanWithN(finalPvalues_qf,finalPvalues.size())+"\n");
            bw.write("pvalue_smv:"+StandardDeviation.meanWithN(finalPvalues_smv,finalPvalues.size())+"\n");
            bw.write("pvalue_uef:"+StandardDeviation.meanWithN(finalPvalues_uef,finalPvalues.size())+"\n");
            bw.write("pvalue_wig:"+StandardDeviation.meanWithN(finalPvalues_wig,finalPvalues.size())+"\n");
            bw.write("size:"+finalPvalues.size()+"\n");
            
            bw.close();
        }
        catch(Exception ex){
            
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println(count);
    }

    private void evalNNOneRandom(String rs, String dataset, String dir) {
        try{
            File[] files = new File(dir).listFiles();
            
            List<Double> finalPearsons = new ArrayList<>();
            List<Double> finalKendalls = new ArrayList<>();
            List<Double> finalPvalues = new ArrayList<>();
            
            for (int i = 1; i <= 1; i++) {
                for (int j = 1; j <= 4; j++) {
                    BufferedReader br1 = new BufferedReader(new FileReader(new File(dir+i+"/"+j+"/test.txt")));
                    BufferedReader br2 = new BufferedReader(new FileReader(new File(dir+i+"/"+j+"/prediction.txt")));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(dir+i+"/"+j+"/predictionWithQueries.txt")));
                    String query = "";
                    while((query =br1.readLine())!= null)
                    {
                        query = query.trim();
                        String prediction = br2.readLine().trim();
                        bw.write(query+" "+prediction+"\n");
                    }
                    br1.close();
                    br2.close();
                    bw.close();
                }
                
            }
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(
                          new FileOutputStream(dir+"/finalresults.txt"), "UTF8"));
            for (int i = 1; i <= 1; i++) {
                List<Double> eachFoldPearsons = new ArrayList<>();
                List<Double> eachFoldKendalls = new ArrayList<>();
                List<Double> eachFoldPvalues = new ArrayList<>();
                
                for (int j = 1; j <= 4; j++) {
                    String resultPath = "./crossfold/temp.txt";
                    String fname = dir+i+"/"+j+"/predictionWithQueries.txt";
                    List<Double> results = testOne(rs, dataset, resultPath, fname,fname);
                    if(!results.get(0).isNaN() && !results.get(1).isNaN()){
                        eachFoldPearsons.add(results.get(0));
                        eachFoldKendalls.add(results.get(1));
                        eachFoldPvalues.add(results.get(3));
                    }
                     
            
                    bw.write("pearson:"+results.get(0)+"\n");
                    bw.write("kendall:"+results.get(1)+"\n");
                    bw.write("pvalue:"+results.get(3)+"\n");
                    bw.write("----------\n");
                }
                
            }
           
            
            bw.close();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
  
    }
    
    private void evalOne(String rs, String dataset, String dir) {
       try{
            File[] files = new File(dir).listFiles();
            int length = 30;
            if(files.length>40)
                length =60;
            if(files.length>70)
                length =120;
            List<Double> finalPearsons = new ArrayList<>();
            List<Double> finalKendalls = new ArrayList<>();
            List<Double> finalPvalues = new ArrayList<>();
            for (int i = 1; i <= length; i++) {
                for (int j = 1; j <= 2; j++) {
                    BufferedReader br1 = new BufferedReader(new FileReader(new File(dir+i+"/"+j+"/test.txt")));
                    BufferedReader br2 = new BufferedReader(new FileReader(new File(dir+i+"/"+j+"/prediction.txt")));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(dir+i+"/"+j+"/predictionWithQueries.txt")));
                    String query = "";
                    while((query =br1.readLine())!= null)
                    {
                        query=query.trim();
                        String prediction = br2.readLine().trim();
                        bw.write(query+" "+prediction+"\n");
                    }
                    br1.close();
                    br2.close();
                    bw.close();
                }
                
            }
            
            for (int i = 1; i <= length; i++) {
                List<Double> eachFoldPearsons = new ArrayList<>();
                List<Double> eachFoldKendalls = new ArrayList<>();
                List<Double> eachFoldPvalues = new ArrayList<>();
                for (int j = 1; j <= 2; j++) {
                    String resultPath = "./crossfold/temp.txt";
                    String fname = dir+i+"/"+j+"/predictionWithQueries.txt";
                    List<Double> results = testOne(rs, dataset, resultPath, fname,fname);
                    if(!results.get(0).isNaN() && !results.get(1).isNaN()){
                        eachFoldPearsons.add(results.get(0));
                        eachFoldKendalls.add(results.get(1));
                        eachFoldPvalues.add(results.get(3));
                    }
                   
                }
//                if( StandardDeviation.mean(eachFoldPvalues) <=0.01 && finalPvalues.size()<30 && StandardDeviation.mean(eachFoldPearsons) <=1){
                if( finalPvalues.size()<30 ){
                
                finalPearsons.add(StandardDeviation.mean(eachFoldPearsons));
                finalKendalls.add(StandardDeviation.mean(eachFoldKendalls));
                finalPvalues.add(StandardDeviation.mean(eachFoldPvalues));
                }
            }
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(
                          new FileOutputStream(dir+"/finalresults.txt"), "UTF8"));
            
            bw.write("pearson:"+StandardDeviation.mean(finalPearsons)+"\n");
            bw.write("kendall:"+StandardDeviation.mean(finalKendalls)+"\n");
            bw.write("pvalue:"+StandardDeviation.mean(finalPvalues)+"\n");
            bw.write("size:"+finalPvalues.size()+"\n");
            
            bw.close();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    HashMap<String, Double> rank(List <Pair<String, Double>> a){
        Map <String, Double> input = pairToMap(a);
        List<Map.Entry<String, Double> > list = 
               new LinkedList<Map.Entry<String, Double> >(input.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() { 
            public int compare(Map.Entry<String, Double> o1,  
                               Map.Entry<String, Double> o2) 
            { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Double> result = new LinkedHashMap<String, Double>(); 
        int i=1;
        for (Map.Entry<String, Double> aa : list) { 
            result.put(aa.getKey(), (double) i); 
            i++;
        } 
        return result;
    }
    
    
    void Compare(List <Pair<String, Double>> map, List <Pair<String, Double>> list, String dataset , String fname) throws IOException{
        String filePath_difficult_difficult = "./crossfold/"+dataset+"/analysis/analysis-"+fname+"-difficult-difficult.txt";
        String filePath_difficult_easy = "./crossfold/"+dataset+"/analysis/analysis-"+fname+"-difficult-easy.txt";
        String filePath_easy_difficult = "./crossfold/"+dataset+"/analysis/analysis-"+fname+"-easy-difficult.txt";
        String filePath_easy_easy = "./crossfold/"+dataset+"/analysis/analysis-"+fname+"-easy-easy.txt";
        
        BufferedWriter bw_dd = new BufferedWriter(new FileWriter(new File(filePath_difficult_difficult)));
        BufferedWriter bw_de = new BufferedWriter(new FileWriter(new File(filePath_difficult_easy)));
        BufferedWriter bw_ed = new BufferedWriter(new FileWriter(new File(filePath_easy_difficult)));
        BufferedWriter bw_ee = new BufferedWriter(new FileWriter(new File(filePath_easy_easy)));
        
        HashMap<String, Double> rankedMap = rank(map);
        HashMap<String, Double> rankedList = rank(list);
        
        Map <String, Double> mapvalue = pairToMap(map);
        
        HashMap<String, Pair<Double,Double>> difficult_difficult  = new HashMap<>();
        HashMap<String, Pair<Double,Double>> difficult_easy  = new HashMap<>();
        HashMap<String, Pair<Double,Double>> easy_difficult  = new HashMap<>();
        HashMap<String, Pair<Double,Double>> easy_easy  = new HashMap<>();
        
        double threshold = 0.05;
        double thresholdDifficult = (double)rankedMap.size() /4;
        double thresholdEasy = ((double)rankedMap.size()) /2;
        
        if(fname.equals("ql-robust-10docs-percentEntities-F33.txt"))
            System.out.println("here");
        
        int dif_total = 0;
        int easy_total = 0;
        for(String listItem : rankedList.keySet()){
            if(rankedMap.containsKey(listItem)){
               double rank_in_map = rankedMap.get(listItem);
               double map_val = mapvalue.get(listItem);
               double rank_in_list = rankedList.get(listItem);
               double distance = Math.abs(rank_in_map - rank_in_list);
               
               if(map_val<= threshold){
                   dif_total++;
                   if(distance <= thresholdDifficult ){
                       difficult_difficult.put(listItem, new Pair<Double,Double>(rank_in_map,rank_in_list));
                       bw_dd.write("difficult_difficult: "+listItem +": Map: "+rank_in_map +" Predictor: "+ rank_in_list+": "+mapvalue.get(listItem)+"\n");
                       bw_dd.flush();
                       
                   }
                   else if(distance > thresholdDifficult ){
                       difficult_easy.put(listItem, new Pair<Double,Double>(rank_in_map,rank_in_list));
                       bw_de.write("difficult_easy     : "+listItem +": Map: "+rank_in_map +" Predictor: "+ rank_in_list+": "+mapvalue.get(listItem)+"\n");
                       bw_de.flush();
                   }
               }
               
               if(map_val > threshold){
                   easy_total++;
                   if(distance <= thresholdDifficult ){
                       easy_easy.put(listItem, new Pair<Double,Double>(rank_in_map,rank_in_list));
                       bw_ee.write("easy_easy          : "+listItem +": Map: "+rank_in_map +" Predictor: "+ rank_in_list+": "+mapvalue.get(listItem)+"\n");
                       bw_ee.flush();
                   }
                   else if(distance > thresholdDifficult ){
                       easy_difficult.put(listItem, new Pair<Double,Double>(rank_in_map,rank_in_list));
                       bw_ed.write("easy_difficult     : "+listItem +": Map: "+rank_in_map +" Predictor: "+ rank_in_list+": "+mapvalue.get(listItem)+"\n");
                       bw_ed.flush();
                   }
               }
            }
            
        }
        bw_ee.close();
        bw_ed.close();
        bw_de.close();
        bw_dd.write("easy_easy: "+ (double)easy_easy.size()/(double)rankedMap.size()+"\n");
        bw_dd.write("easy_difficult: "+(double)easy_difficult.size()/(double)rankedMap.size()+"\n");
        bw_dd.write("difficult_easy: "+(double)difficult_easy.size()/(double)rankedMap.size()+"\n");
        bw_dd.write("difficult_difficult: "+(double)difficult_difficult.size()/(double)rankedMap.size()+"\n");
        bw_dd.write("difficult percentage: "+(double)dif_total/(double)rankedMap.size()+"\n");
        bw_dd.write("easy percentage: "+(double)easy_total/(double)rankedMap.size()+"\n");
        bw_dd.write("difficulty perormance percentage: "+(double)difficult_difficult.size()/(double)dif_total+"\n");
        bw_dd.write("easy perormance percentage: "+(double)easy_easy.size()/(double)easy_total+"\n");
        bw_dd.close();
        
        
    }
 
    void runFeature_NumberOfEntity(String dataset, String rs) throws IOException{
        File[] files = new File("./crossfold/"+dataset+"/result/").listFiles();
        ArrayList<Integer> moreThanOneEntities = new ArrayList<Integer>(Arrays.asList(2,7,17,18,29,32,44,46,49,50,56,57,62,102,104,105,107,109,117,120,125,126,127,128,129,141,146,147,148,149,150,157,175,181,183,189,192,194,197,16,87,95,101,106,112,114,200,203,207,210,214,216,217,218,220,222,223,225,227,230,234,238,239,240,242,243,244,248,228,232,236,246,301,304,305,306,308,310,314,315,316,320,321,323,325,329,330,331,332,335,333,336,338,339,342,343,344,345,347,350,351,352,353,354,355,356,357,358,359,360,361,365,369,370,371,372,373,374,375,377,378,380,382,383,384,385,386,388,389,391,397,398,401,404,405,407,409,411,413,414,415,419,420,421,422,423,425,426,427,430,431,432,433,434,437,439,442,443,445,448,450,601,602,603,604,606,607,609,610,612,613,614,615,616,617,618,620,621,623,624,625,627,629,631,632,633,634,635,636,637,639,640,641,643,644,647,650,651,652,653,654,656,657,660,661,662,663,664,665,666,668,671,673,674,675,676,679,680,681,682,683,685,686,690,691,692,693,694,695,696,698,700));
        for(File f : files){
            try {
                
                String fname = f.getName();
                if(!fname.contains("Mul") && !fname.contains("Sin")){
                    String resultPath1 = "./crossfold/"+dataset+"/result/singleEntity-"+fname;
                    String resultPath2 = "./crossfold/"+dataset+"/result/MultipleEntity-"+fname;
                    HashMap<String,Double> predictions = Query_performance_prediction.readPerfFromFils("./crossfold/"+dataset+"/result/"+fname);
                    BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File(resultPath1)));
                    BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(resultPath2)));

                    for(String key: predictions.keySet()){
                        if(!moreThanOneEntities.contains(Integer.parseInt(key))){
                            bw1.write(key+ " "+ predictions.get(key));
                            bw1.write("\n");
                        }
                        else if(moreThanOneEntities.contains(Integer.parseInt(key))){
                            bw2.write(key+ " "+ predictions.get(key));
                            bw2.write("\n");
                        }
                    }
                    bw1.close();
                    bw2.close();
                }
                
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}

    

