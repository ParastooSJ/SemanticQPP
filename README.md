# A semantic approach to post-retrieval query performance prediction
The importance of query performance prediction has been widely acknowledged in the literature, especially for query expansion, refinement, and interpolating different retrieval approaches. This paper proposes a novel semantics-based query performance prediction approach based on estimating semantic similarities between queries and documents. We introduce three post-retrieval predictors, namely (1) semantic distinction, (2) semantic query drift, and (3) semantic cohesion based on (1) the semantic similarity of a query to the top-ranked documents compared to the whole collection, (2) the estimation of non-query related aspects of the retrieved documents using semantic measures, and (3) the semantic cohesion of the retrieved documents. We assume that queries and documents are modeled as sets of entities from a knowledge graph, e.g., DBPedia concepts, instead of bags of words. With this assumption, semantic similarities between two texts are measured based on the relatedness between entities, which are learned from the contextual information represented in the knowledge graph. We empirically illustrate these predictors’ effectiveness, especially when term-based measures fail to quantify query performance prediction hypotheses correctly. We report our findings on the proposed predictors’ performance and their interpolation on three standard collections, namely ClueWeb09-B, ClueWeb12-B, and Robust04. We show that the proposed predictors are effective across different datasets in terms of Pearson and Kendall correlation coefficients between the predicted performance and the average precision measured by relevance judgments.



## Citation
If you find the code and data useful, please cite our paper.
```
@article{jafarzadeh2022semantic,
  title={A semantic approach to post-retrieval query performance prediction},
  author={Jafarzadeh, Parastoo and Ensan, Faezeh},
  journal={Information Processing \& Management},
  volume={59},
  number={1},
  pages={102746},
  year={2022},
  publisher={Elsevier}
}
```
