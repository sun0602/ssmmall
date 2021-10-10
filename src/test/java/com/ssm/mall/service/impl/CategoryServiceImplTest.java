package com.ssm.mall.service.impl;

import com.ssm.mall.service.iservice.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CategoryServiceImplTest {
    @Autowired
    private CategoryService categoryService;
    @Test
    public void childrenCategory() {
        categoryService.childrenCategory(100001).getData().forEach(System.out::println);
    }
}