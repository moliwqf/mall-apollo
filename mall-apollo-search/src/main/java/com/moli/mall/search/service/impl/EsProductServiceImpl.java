package com.moli.mall.search.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.moli.mall.search.dao.EsProductDao;
import com.moli.mall.search.domain.EsProduct;
import com.moli.mall.search.domain.EsProductRelatedInfo;
import com.moli.mall.search.repository.EsProductRepository;
import com.moli.mall.search.service.EsProductService;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author moli
 * @time 2024-04-09 15:16:11
 * @description 搜索商品管理
 */
@Service
public class EsProductServiceImpl implements EsProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsProductServiceImpl.class);

    @Resource
    private EsProductDao esProductDao;

    @Resource
    private EsProductRepository esProductRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public int importAll() {
        // 查询所有商品
        List<EsProduct> esProductList = esProductDao.getAllEsProducts(null);
        // 保存商品
        Iterable<EsProduct> esProducts = esProductRepository.saveAll(esProductList);
        Iterator<EsProduct> iterator = esProducts.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        return count;
    }

    @Override
    public void delete(Long id) {
        esProductRepository.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        esProductRepository.deleteAllById(ids);
    }

    @Override
    public EsProduct create(Long id) {
        List<EsProduct> allEsProducts = esProductDao.getAllEsProducts(id);
        if (CollUtil.isEmpty(allEsProducts)) return null;

        EsProduct esProduct = allEsProducts.get(0);
        esProductRepository.save(esProduct);
        return esProduct;
    }

    @Override
    public Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return esProductRepository.findByNameOrSubTitleOrKeywords(keyword, keyword, keyword, pageRequest);
    }

    @Override
    public Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        // 分页
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withPageable(pageRequest);
        // 过滤
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (Objects.nonNull(brandId)) {
            boolQuery.must(QueryBuilders.termQuery("brandId", brandId));
        }
        if (Objects.nonNull(productCategoryId)) {
            boolQuery.must(QueryBuilders.termQuery("productCategoryId", productCategoryId));
        }
        nativeSearchQueryBuilder.withFilter(boolQuery);

        // 搜索
        if (StrUtil.isEmpty(keyword)) {
            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
        } else {
            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("name", keyword),
                    ScoreFunctionBuilders.weightFactorFunction(10)));
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("subTitle", keyword),
                    ScoreFunctionBuilders.weightFactorFunction(5)));
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("keywords", keyword),
                    ScoreFunctionBuilders.weightFactorFunction(2)));

            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders.size()];
            filterFunctionBuilders.toArray(builders);

            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                    .setMinScore(2);

            nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
        }

        // 排序
        if (sort == 1) {
            //按新品从新到旧
            nativeSearchQueryBuilder.withSort(Sort.by("id").descending());
        } else if (sort == 2) {
            //按销量从高到低
            nativeSearchQueryBuilder.withSort(Sort.by("sale").descending());
        } else if (sort == 3) {
            //按价格从低到高
            nativeSearchQueryBuilder.withSort(Sort.by("price").ascending());
        } else if (sort == 4) {
            //按价格从高到低
            nativeSearchQueryBuilder.withSort(Sort.by("price").descending());
        } else {
            //按相关度
            nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        }

        nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();

        LOGGER.info("DSL:{}", Objects.requireNonNull(searchQuery.getQuery()));

        SearchHits<EsProduct> results = elasticsearchTemplate.search(searchQuery, EsProduct.class);

        if (results.getTotalHits() <= 0) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }

        List<EsProduct> page = results.stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new PageImpl<>(page, pageRequest, results.getTotalHits());
    }

    @Override
    public Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        List<EsProduct> esProductList = esProductDao.getAllEsProducts(id);

        if (CollUtil.isEmpty(esProductList)) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }

        EsProduct target = esProductList.get(0);

        String keyword = target.getName();
        Long brandId = target.getBrandId();
        Long productCategoryId = target.getProductCategoryId();

        //根据商品标题、品牌、分类进行搜索
        List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("name", keyword),
                ScoreFunctionBuilders.weightFactorFunction(8)));
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("subTitle", keyword),
                ScoreFunctionBuilders.weightFactorFunction(2)));
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("keywords", keyword),
                ScoreFunctionBuilders.weightFactorFunction(2)));
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("brandId", brandId),
                ScoreFunctionBuilders.weightFactorFunction(5)));
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("productCategoryId", productCategoryId),
                ScoreFunctionBuilders.weightFactorFunction(3)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders.size()];
        filterFunctionBuilders.toArray(builders);
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
                .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                .setMinScore(2);

        // 过滤相同商品
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.mustNot(QueryBuilders.termQuery("id", id));

        //构建查询条件
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(functionScoreQueryBuilder);
        builder.withFilter(boolQuery);
        builder.withPageable(pageRequest);

        NativeSearchQuery searchQuery = builder.build();
        LOGGER.info("DSL:{}", Objects.requireNonNull(searchQuery.getQuery()));
        SearchHits<EsProduct> searchHits = elasticsearchTemplate.search(searchQuery, EsProduct.class);

        if (CollUtil.isEmpty(searchHits)) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }

        List<EsProduct> searchProductList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new PageImpl<>(searchProductList, pageRequest, searchHits.getTotalHits());
    }

    @Override
    public EsProductRelatedInfo searchRelatedInfo(String keyword) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        if (StrUtil.isEmpty(keyword)) {
            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
        } else {
            nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keyword, "name", "subTitle", "keywords"));
        }

        // 聚合搜索品牌名称
        nativeSearchQueryBuilder.withAggregations(
                AggregationBuilders.terms("brandNames").field("brandName"),
                AggregationBuilders.terms("productCategoryNames").field("productCategoryName"),
                AggregationBuilders.nested("allAttrValues", "attrValueList")
                        .subAggregation(
                                AggregationBuilders.filter("productAttrs", QueryBuilders.termQuery("attrValueList.type", 1))
                                        .subAggregation(
                                                AggregationBuilders.terms("attrIds").field("attrValueList.productAttributeId")
                                                        .subAggregation(
                                                                AggregationBuilders.terms("attrValues").field("attrValueList.value")
                                                        ).subAggregation(
                                                                AggregationBuilders.terms("attrNames").field("attrValueList.name"))
                                        )
                        )
        );

        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();

        SearchHits<EsProduct> searchHits = elasticsearchTemplate.search(searchQuery, EsProduct.class);

        return convertProductRelatedInfo(searchHits);
    }

    /**
     * 将返回结果转换为对象
     */
    private EsProductRelatedInfo convertProductRelatedInfo(SearchHits<EsProduct> response) {
        EsProductRelatedInfo productRelatedInfo = new EsProductRelatedInfo();

        Map<String, Aggregation> aggregationMap = ((Aggregations) Objects.requireNonNull(response.getAggregations()).aggregations()).asMap();

        // 设置品牌名
        Aggregation brandNames = aggregationMap.get("brandNames");
        List<String> brandNameList = new ArrayList<>();
        for (int i = 0; i < ((Terms) brandNames).getBuckets().size(); i++) {
            brandNameList.add(((Terms) brandNames).getBuckets().get(i).getKeyAsString());
        }
        productRelatedInfo.setBrandNames(brandNameList);

        //设置分类
        Aggregation productCategoryNames = aggregationMap.get("productCategoryNames");
        List<String> productCategoryNameList = new ArrayList<>();
        for (int i = 0; i < ((Terms) productCategoryNames).getBuckets().size(); i++) {
            productCategoryNameList.add(((Terms) productCategoryNames).getBuckets().get(i).getKeyAsString());
        }
        productRelatedInfo.setProductCategoryNames(productCategoryNameList);

        // 设置参数
        Aggregation productAttrs = aggregationMap.get("allAttrValues");
        List<? extends Terms.Bucket> attrIds = ((ParsedLongTerms) ((ParsedFilter) ((ParsedNested) productAttrs).getAggregations().get("productAttrs")).getAggregations().get("attrIds")).getBuckets();
        List<EsProductRelatedInfo.ProductAttr> attrList = new ArrayList<>();
        for (Terms.Bucket attrId : attrIds) {
            EsProductRelatedInfo.ProductAttr attr = new EsProductRelatedInfo.ProductAttr();
            attr.setAttrId((Long) attrId.getKey());
            List<String> attrValueList = new ArrayList<>();
            List<? extends Terms.Bucket> attrValues = ((ParsedStringTerms) attrId.getAggregations().get("attrValues")).getBuckets();
            List<? extends Terms.Bucket> attrNames = ((ParsedStringTerms) attrId.getAggregations().get("attrNames")).getBuckets();
            for (Terms.Bucket attrValue : attrValues) {
                attrValueList.add(attrValue.getKeyAsString());
            }
            attr.setAttrValues(attrValueList);
            if (!CollectionUtils.isEmpty(attrNames)) {
                String attrName = attrNames.get(0).getKeyAsString();
                attr.setAttrName(attrName);
            }
            attrList.add(attr);
        }
        productRelatedInfo.setProductAttrs(attrList);
        return productRelatedInfo;
    }
}
