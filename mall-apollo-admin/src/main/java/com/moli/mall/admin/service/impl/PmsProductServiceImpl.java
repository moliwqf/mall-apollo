package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.*;
import com.moli.mall.admin.dto.PmsProductParams;
import com.moli.mall.admin.dto.PmsProductQueryParams;
import com.moli.mall.admin.service.PmsProductService;
import com.moli.mall.admin.vo.PmsProductResultVo;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.*;
import com.moli.mall.mbg.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.JDBCType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author moli
 * @time 2024-04-02 15:38:45
 * @description 产品服务实现层
 */
@Service
public class PmsProductServiceImpl implements PmsProductService {

    @Resource
    private PmsProductMapper pmsProductMapper;

    @Resource
    private PmsProductCategoryMapper pmsProductCategoryMapper;

    @Resource
    private PmsProductLadderMapper pmsProductLadderMapper;

    @Resource
    private PmsProductLadderDao pmsProductLadderDao;

    @Resource
    private PmsProductFullReductionMapper pmsProductFullReductionMapper;

    @Resource
    private PmsProductFullReductionDao pmsProductFullReductionDao;

    @Resource
    private PmsMemberPriceMapper pmsMemberPriceMapper;

    @Resource
    private PmsMemberPriceDao pmsMemberPriceDao;

    @Resource
    private PmsSkuStockMapper pmsSkuStockMapper;

    @Resource
    private PmsSkuStockDao pmsSkuStockDao;

    @Resource
    private PmsProductAttributeValueMapper pmsProductAttributeValueMapper;

    @Resource
    private PmsProductAttributeValueDao pmsProductAttributeValueDao;

    @Resource
    private CmsSubjectProductRelationMapper cmsSubjectProductRelationMapper;

    @Resource
    private CmsSubjectProductRelationDao cmsSubjectProductRelationDao;

    @Resource
    private CmsPrefrenceAreaProductRelationMapper cmsPrefrenceAreaProductRelationMapper;

    @Resource
    private CmsPrefrenceAreaProductRelationDao cmsPrefrenceAreaProductRelationDao;

    @Override
    public List<PmsProduct> list(PmsProductQueryParams productQueryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample productExample = getProductExample(productQueryParam);
        return pmsProductMapper.selectByExample(productExample);
    }

    @Override
    public PmsProductResultVo getUpdateInfo(Long id) {

        // 根据id获取商品信息
        PmsProduct pmsProduct = pmsProductMapper.selectByPrimaryKey(id);
        if (Objects.isNull(pmsProduct)) {
            return null;
        }
        PmsProductResultVo productResultVo = BeanCopyUtil.copyBean(pmsProduct, PmsProductResultVo.class);
        assert productResultVo != null;

        // 查询商品打折信息
        CompletableFuture<List<PmsProductLadder>> laddersFuture = CompletableFuture.supplyAsync(() -> {
            PmsProductLadderExample pmsProductLadderExample = new PmsProductLadderExample();
            pmsProductLadderExample.createCriteria().andProductIdEqualTo(id);
            return pmsProductLadderMapper.selectByExample(pmsProductLadderExample);
        }).whenComplete((pmsProductLadders, e) -> productResultVo.setProductLadderList(pmsProductLadders));


        // 查询商品满减价格信息
        CompletableFuture<List<PmsProductFullReduction>> reductionsFuture = CompletableFuture.supplyAsync(() -> {
            PmsProductFullReductionExample pmsProductFullReductionExample = new PmsProductFullReductionExample();
            pmsProductFullReductionExample.createCriteria().andProductIdEqualTo(id);
            return pmsProductFullReductionMapper.selectByExample(pmsProductFullReductionExample);
        }).whenComplete((pmsProductFullReductions, e) -> productResultVo.setProductFullReductionList(pmsProductFullReductions));

        // 查询商品会员价格信息
        CompletableFuture<List<PmsMemberPrice>> memberPriceFuture = CompletableFuture.supplyAsync(() -> {
            PmsMemberPriceExample pmsMemberPriceExample = new PmsMemberPriceExample();
            pmsMemberPriceExample.createCriteria().andProductIdEqualTo(id);
            return pmsMemberPriceMapper.selectByExample(pmsMemberPriceExample);
        }).whenComplete((pmsMemberPrices, e) -> productResultVo.setMemberPriceList(pmsMemberPrices));

        // 查询商品的sku库存信息
        CompletableFuture<List<PmsSkuStock>> skuStocksFuture = CompletableFuture.supplyAsync(() -> {
            PmsSkuStockExample pmsSkuStockExample = new PmsSkuStockExample();
            pmsSkuStockExample.createCriteria().andProductIdEqualTo(id);
            return pmsSkuStockMapper.selectByExample(pmsSkuStockExample);
        }).whenComplete((pmsSkuStocks, e) -> productResultVo.setSkuStockList(pmsSkuStocks));

        // 查询商品参数及自定义规格属性
        CompletableFuture<List<PmsProductAttributeValue>> valuesFuture = CompletableFuture.supplyAsync(() -> {
            PmsProductAttributeValueExample pmsProductAttributeValueExample = new PmsProductAttributeValueExample();
            pmsProductAttributeValueExample.createCriteria().andProductIdEqualTo(id);
            return pmsProductAttributeValueMapper.selectByExample(pmsProductAttributeValueExample);
        }).whenComplete((pmsProductAttributeValues, e) -> productResultVo.setProductAttributeValueList(pmsProductAttributeValues));

        // 查询专题和商品关系
        CompletableFuture<List<CmsSubjectProductRelation>> subjectProductRelationsFuture = CompletableFuture.supplyAsync(() -> {
            CmsSubjectProductRelationExample cmsSubjectProductRelationExample = new CmsSubjectProductRelationExample();
            cmsSubjectProductRelationExample.createCriteria().andProductIdEqualTo(id);
            return cmsSubjectProductRelationMapper.selectByExample(cmsSubjectProductRelationExample);
        }).whenComplete((cmsSubjectProductRelations, e) -> productResultVo.setSubjectProductRelationList(cmsSubjectProductRelations));

        // 查询优选专区和商品的关系
        CompletableFuture<List<CmsPrefrenceAreaProductRelation>> prefrenceAreaProductRelationsFuture = CompletableFuture.supplyAsync(() -> {
            CmsPrefrenceAreaProductRelationExample cmsPrefrenceAreaProductRelationExample = new CmsPrefrenceAreaProductRelationExample();
            cmsPrefrenceAreaProductRelationExample.createCriteria().andProductIdEqualTo(id);
            return cmsPrefrenceAreaProductRelationMapper.selectByExample(cmsPrefrenceAreaProductRelationExample);
        }).whenComplete((cmsPrefrenceAreaProductRelations, e) -> productResultVo.setPrefrenceAreaProductRelationList(cmsPrefrenceAreaProductRelations));

        // 查询商品分类的父分类id
        CompletableFuture<Long> cateParentIdFuture = CompletableFuture.supplyAsync(() -> pmsProductCategoryMapper.selectByPrimaryKey(productResultVo.getProductCategoryId()).getParentId())
                .whenComplete((parentId, e) -> productResultVo.setCateParentId(parentId));

        try {
            CompletableFuture.allOf(
                    laddersFuture,
                    reductionsFuture,
                    memberPriceFuture,
                    skuStocksFuture,
                    valuesFuture,
                    subjectProductRelationsFuture,
                    prefrenceAreaProductRelationsFuture,
                    cateParentIdFuture
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            AssetsUtil.fail("异步执行获取商品信息失败");
        }

        return productResultVo;
    }

    @Override
    @Transactional
    public int update(Long id, PmsProductParams productParam) {
        // 查询是否存在该商品
        PmsProduct rawProduct = pmsProductMapper.selectByPrimaryKey(id);
        if (Objects.isNull(rawProduct)) {
            AssetsUtil.fail("商品不存在!!");
        }

        // 更新产品信息
        productParam.setId(id);
        PmsProduct newProduct = BeanCopyUtil.copyBean(productParam, PmsProduct.class);
        int count = pmsProductMapper.updateByPrimaryKey(newProduct);

        CompletableFuture<Void> laddersFuture = CompletableFuture.runAsync(() -> {
            // 商品阶梯价格设置
            // 根据productId删除对对应的商品阶梯价格
            PmsProductLadderExample pmsProductLadderExample = new PmsProductLadderExample();
            pmsProductLadderExample.createCriteria().andProductIdEqualTo(id);
            pmsProductLadderMapper.deleteByExample(pmsProductLadderExample);
            // 添加新的价格区间
            List<PmsProductLadder> productLadderList = productParam.getProductLadderList();
            if (!CollectionUtils.isEmpty(productLadderList)) {
                productLadderList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                pmsProductLadderDao.insertList(productLadderList);
            }
        });


        CompletableFuture<Void> reductionsFuture = CompletableFuture.runAsync(() -> {
            // 商品满减价格设置
            // 根据productId删除对应的商品满减价格
            PmsProductFullReductionExample pmsProductFullReductionExample = new PmsProductFullReductionExample();
            pmsProductFullReductionExample.createCriteria().andProductIdEqualTo(id);
            pmsProductFullReductionMapper.deleteByExample(pmsProductFullReductionExample);
            // 添加新的商品满减价格
            List<PmsProductFullReduction> productFullReductionList = productParam.getProductFullReductionList();
            if (!CollectionUtils.isEmpty(productFullReductionList)) {
                productFullReductionList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                pmsProductFullReductionDao.insertList(productFullReductionList);
            }
        });

        CompletableFuture<Void> memberPriceFuture = CompletableFuture.runAsync(() -> {
            // 商品会员价格设置
            // 将商品会员价格之前的数据进行删除
            PmsMemberPriceExample pmsMemberPriceExample = new PmsMemberPriceExample();
            pmsMemberPriceExample.createCriteria().andProductIdEqualTo(id);
            pmsMemberPriceMapper.deleteByExample(pmsMemberPriceExample);
            // 添加新的数据
            List<PmsMemberPrice> memberPriceList = productParam.getMemberPriceList();
            if (!CollectionUtils.isEmpty(memberPriceList)) {
                memberPriceList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                pmsMemberPriceDao.insertList(memberPriceList);
            }
        });

        CompletableFuture<Void> skuStocksFuture = CompletableFuture.runAsync(() -> {
            // 商品的sku库存信息
            // 将原先的sku信息进行删除
            PmsSkuStockExample pmsSkuStockExample = new PmsSkuStockExample();
            pmsSkuStockExample.createCriteria().andProductIdEqualTo(id);
            pmsSkuStockMapper.deleteByExample(pmsSkuStockExample);
            // 添加新的sku信息
            List<PmsSkuStock> skuStockList = productParam.getSkuStockList();
            if (!CollectionUtils.isEmpty(skuStockList)) {
                skuStockList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                pmsSkuStockDao.insertList(skuStockList);
            }
        });

        CompletableFuture<Void> valuesFuture = CompletableFuture.runAsync(() -> {
            // 商品参数及自定义规格属性
            // 删除之前的自定义规格属性
            PmsProductAttributeValueExample pmsProductAttributeValueExample = new PmsProductAttributeValueExample();
            pmsProductAttributeValueExample.createCriteria().andProductIdEqualTo(id);
            pmsProductAttributeValueMapper.deleteByExample(pmsProductAttributeValueExample);
            // 添加新的规格属性
            List<PmsProductAttributeValue> productAttributeValueList = productParam.getProductAttributeValueList();
            if (!CollectionUtils.isEmpty(productAttributeValueList)) {
                productAttributeValueList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                pmsProductAttributeValueDao.insertList(productAttributeValueList);
            }
        });

        CompletableFuture<Void> subjectProductRelationsFuture = CompletableFuture.runAsync(() -> {
            // 专题和商品关系
            // 删除之前的关系
            CmsSubjectProductRelationExample cmsSubjectProductRelationExample = new CmsSubjectProductRelationExample();
            cmsSubjectProductRelationExample.createCriteria().andProductIdEqualTo(id);
            cmsSubjectProductRelationMapper.deleteByExample(cmsSubjectProductRelationExample);
            // 添加新的关系
            List<CmsSubjectProductRelation> subjectProductRelationList = productParam.getSubjectProductRelationList();
            if (!CollectionUtils.isEmpty(subjectProductRelationList)) {
                subjectProductRelationList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                cmsSubjectProductRelationDao.insertList(subjectProductRelationList);
            }
        });
        CompletableFuture<Void> prefrenceAreaProductRelationsFuture = CompletableFuture.runAsync(() -> {
            // 优选专区和商品的关系
            // 删除之前的关系
            CmsPrefrenceAreaProductRelationExample cmsPrefrenceAreaProductRelationExample = new CmsPrefrenceAreaProductRelationExample();
            cmsPrefrenceAreaProductRelationExample.createCriteria().andProductIdEqualTo(id);
            cmsPrefrenceAreaProductRelationMapper.deleteByExample(cmsPrefrenceAreaProductRelationExample);
            // 添加新的关系
            List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList = productParam.getPrefrenceAreaProductRelationList();
            if (!CollectionUtils.isEmpty(prefrenceAreaProductRelationList)) {
                prefrenceAreaProductRelationList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                cmsPrefrenceAreaProductRelationDao.insertList(prefrenceAreaProductRelationList);
            }
        });

        try {
            CompletableFuture.allOf(
                    laddersFuture,
                    reductionsFuture,
                    memberPriceFuture,
                    skuStocksFuture,
                    valuesFuture,
                    subjectProductRelationsFuture,
                    prefrenceAreaProductRelationsFuture
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            AssetsUtil.fail("异步执行更新商品操作异常");
        }

        return count;
    }

    @Override
    @Transactional
    public int create(PmsProductParams productParam) {
        if (Objects.isNull(productParam)) {
            AssetsUtil.fail("添加商品信息为空，无法添加!!");
        }
        // 添加产品信息
        productParam.setId(null);
        PmsProduct newProduct = BeanCopyUtil.copyBean(productParam, PmsProduct.class);
        int count = pmsProductMapper.insertSelective(newProduct);
        assert newProduct != null;
        Long id = newProduct.getId();

        CompletableFuture<Void> laddersFuture = CompletableFuture.runAsync(() -> {
            // 商品阶梯价格设置
            // 添加价格区间
            List<PmsProductLadder> productLadderList = productParam.getProductLadderList();
            if (!CollectionUtils.isEmpty(productLadderList)) {
                productLadderList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                pmsProductLadderDao.insertList(productLadderList);
            }
        });


        CompletableFuture<Void> reductionsFuture = CompletableFuture.runAsync(() -> {
            // 商品满减价格设置
            // 添加商品满减价格
            List<PmsProductFullReduction> productFullReductionList = productParam.getProductFullReductionList();
            if (!CollectionUtils.isEmpty(productFullReductionList)) {
                productFullReductionList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                pmsProductFullReductionDao.insertList(productFullReductionList);
            }
        });

        CompletableFuture<Void> memberPriceFuture = CompletableFuture.runAsync(() -> {
            // 商品会员价格设置
            // 添加会员价格信息
            List<PmsMemberPrice> memberPriceList = productParam.getMemberPriceList();
            if (!CollectionUtils.isEmpty(memberPriceList)) {
                memberPriceList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                pmsMemberPriceDao.insertList(memberPriceList);
            }
        });

        CompletableFuture<Void> skuStocksFuture = CompletableFuture.runAsync(() -> {
            // 商品的sku库存信息
            // 添加sku信息
            List<PmsSkuStock> skuStockList = productParam.getSkuStockList();
            if (!CollectionUtils.isEmpty(skuStockList)) {
                for (int i = 0; i < skuStockList.size(); i++) {
                    PmsSkuStock item = skuStockList.get(i);
                    item.setId(null);
                    item.setProductId(id);
                    // 处理sku码
                    handleSkuCode(item, id, i);
                }
                pmsSkuStockDao.insertList(skuStockList);
            }
        });

        CompletableFuture<Void> valuesFuture = CompletableFuture.runAsync(() -> {
            // 商品参数及自定义规格属性
            // 添加规格属性信息
            List<PmsProductAttributeValue> productAttributeValueList = productParam.getProductAttributeValueList();
            if (!CollectionUtils.isEmpty(productAttributeValueList)) {
                productAttributeValueList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                pmsProductAttributeValueDao.insertList(productAttributeValueList);
            }
        });

        CompletableFuture<Void> subjectProductRelationsFuture = CompletableFuture.runAsync(() -> {
            // 专题和商品关系
            // 添加专题和商品关系
            List<CmsSubjectProductRelation> subjectProductRelationList = productParam.getSubjectProductRelationList();
            if (!CollectionUtils.isEmpty(subjectProductRelationList)) {
                subjectProductRelationList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                cmsSubjectProductRelationDao.insertList(subjectProductRelationList);
            }
        });
        CompletableFuture<Void> prefrenceAreaProductRelationsFuture = CompletableFuture.runAsync(() -> {
            // 优选专区和商品的关系
            // 添加专区和商品的关系
            List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList = productParam.getPrefrenceAreaProductRelationList();
            if (!CollectionUtils.isEmpty(prefrenceAreaProductRelationList)) {
                prefrenceAreaProductRelationList.stream().peek(item -> {
                    item.setId(null);
                    item.setProductId(id);
                });
                cmsPrefrenceAreaProductRelationDao.insertList(prefrenceAreaProductRelationList);
            }
        });

        try {
            CompletableFuture.allOf(
                    laddersFuture,
                    reductionsFuture,
                    memberPriceFuture,
                    skuStocksFuture,
                    valuesFuture,
                    subjectProductRelationsFuture,
                    prefrenceAreaProductRelationsFuture
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            AssetsUtil.fail("异步执行更新商品操作异常");
        }

        return count;
    }

    @Override
    @Transactional
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        PmsProduct statusProduct = new PmsProduct();
        statusProduct.setDeleteStatus(deleteStatus);
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andIdIn(ids);
        return pmsProductMapper.updateByExample(statusProduct, pmsProductExample);
    }

    @Override
    @Transactional
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        PmsProduct statusProduct = new PmsProduct();
        statusProduct.setRecommandStatus(recommendStatus);
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andIdIn(ids);
        return pmsProductMapper.updateByExample(statusProduct, pmsProductExample);
    }


    @Override
    @Transactional
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        PmsProduct statusProduct = new PmsProduct();
        statusProduct.setVerifyStatus(verifyStatus);
        statusProduct.setDetailDesc(detail);
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andIdIn(ids);
        return pmsProductMapper.updateByExample(statusProduct, pmsProductExample);
    }

    @Override
    @Transactional
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        PmsProduct statusProduct = new PmsProduct();
        statusProduct.setPublishStatus(publishStatus);
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andIdIn(ids);
        return pmsProductMapper.updateByExample(statusProduct, pmsProductExample);
    }

    @Override
    @Transactional
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        PmsProduct statusProduct = new PmsProduct();
        statusProduct.setNewStatus(newStatus);
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andIdIn(ids);
        return pmsProductMapper.updateByExample(statusProduct, pmsProductExample);
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        PmsProductExample pmsProductExample = new PmsProductExample();
        if (StrUtil.isNotEmpty(keyword)) {
            String query = "%" + keyword + "%";
            pmsProductExample.createCriteria().andNameLike(query).andDeleteStatusEqualTo(0);
            pmsProductExample.or().andDeleteStatusEqualTo(0).andProductSnLike(query);
        }
        return pmsProductMapper.selectByExample(pmsProductExample);
    }

    /**
     * 处理sku码
     *
     * @param skuStock  sku存储信息
     * @param productId 产品id
     */
    private void handleSkuCode(PmsSkuStock skuStock, Long productId, Integer index) {
        if (StrUtil.isNotEmpty(skuStock.getSkuCode())) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String skuCode = sdf.format(new Date()) +
                String.format("%04d", productId) +
                String.format("%03d", index + 1);
        skuStock.setSkuCode(skuCode);
    }

    /**
     * 根据查询参数获取查询语句
     */
    private PmsProductExample getProductExample(PmsProductQueryParams productQueryParams) {
        PmsProductExample productExample = new PmsProductExample();
        if (Objects.isNull(productQueryParams)) return productExample;

        PmsProductExample.Criteria criteria = productExample.createCriteria();

        if (!Objects.isNull(productQueryParams.getPublishStatus())) {
            criteria.andPublishStatusEqualTo(productQueryParams.getPublishStatus());
        }
        if (!Objects.isNull(productQueryParams.getVerifyStatus())) {
            criteria.andVerifyStatusEqualTo(productQueryParams.getVerifyStatus());
        }
        if (StrUtil.isNotEmpty(productQueryParams.getKeyword())) {
            criteria.andNameLike("%" + productQueryParams.getKeyword() + "%");
        }
        if (StrUtil.isNotEmpty(productQueryParams.getProductSn())) {
            criteria.andProductSnEqualTo(productQueryParams.getProductSn());
        }
        if (!Objects.isNull(productQueryParams.getProductCategoryId())) {
            criteria.andProductCategoryIdEqualTo(productQueryParams.getProductCategoryId());
        }
        if (!Objects.isNull(productQueryParams.getBrandId())) {
            criteria.andBrandIdEqualTo(productQueryParams.getBrandId());
        }
        return productExample;
    }
}