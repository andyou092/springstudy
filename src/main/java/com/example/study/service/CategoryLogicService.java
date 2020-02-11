package com.example.study.service;

import com.example.study.model.entity.Category;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.CategoryApiRequest;
import com.example.study.model.network.response.CategoryApiResponse;
import com.example.study.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryLogicService extends BaseService<CategoryApiRequest, CategoryApiResponse, Category> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Header<CategoryApiResponse> create( Header<CategoryApiRequest> request) {

        System.out.println("read 서비스 들어온다");
        CategoryApiRequest body = request.getData();

        Category category = Category.builder()
                .type(body.getType())
                .title(body.getTitle())
                .build();
        Category newCategory =    categoryRepository.save(category);

        return Header.OK(response(newCategory));
    }

    @Override
    public Header<CategoryApiResponse> read( Long id) {

        return categoryRepository.findById(id)
                .map(this::response)//현재 클래스에서 response 메서드를 사용 하겠다는 뜻
                .map(Header::OK)
                .orElseGet(()->Header.ERROR("데이터없음"));
    }

    @Override
    public Header<CategoryApiResponse> update( Header<CategoryApiRequest> request) {
        System.out.println("서비스 update 들어옴");
        CategoryApiRequest body = request.getData();
        return categoryRepository.findById(body.getId())
                .map(category -> {
                    category
                            .setId(body.getId())
                            .setType(body.getType())
                            .setTitle(body.getTitle());
                    return  category;
                })
                .map(changeCategory->categoryRepository.save(changeCategory))
                .map(newCategory-> response(newCategory))
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("UPDATE데이터 없음"));

    }//update

    @Override
    public Header delete(Long id) {
        System.out.println("delete들어온다");
        return  categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return Header.OK();
                })
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<List<CategoryApiResponse>> search(Pageable pageable) {
        Page<Category> categorys =categoryRepository.findAll(pageable);

        List<CategoryApiResponse> categoryApiResponseList = categorys.stream()
                .map(category-> response(category))
                .collect(Collectors.toList());

        return Header.OK(categoryApiResponseList);
    }

    public CategoryApiResponse response(Category category){
        CategoryApiResponse body = CategoryApiResponse.builder()
                .id(category.getId())
                .type(category.getType())
                .title(category.getTitle())
                .build();

        return body;
    }


}
