package com.royalfamily.familybudgetplannerapi.repositories;

import com.royalfamily.familybudgetplannerapi.domain.Category;
import com.royalfamily.familybudgetplannerapi.exceptions.BadRequestException;
import com.royalfamily.familybudgetplannerapi.exceptions.ResourceNotFoundException;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll(Integer userId) throws ResourceNotFoundException;

    Category findById(Integer userId, Integer categoryId) throws ResourceNotFoundException;

    Integer create(Integer userId, String title, String description) throws BadRequestException;

    void update(Integer userId, Integer categoryId, Category category) throws BadRequestException;

    void removeById(Integer userId, Integer categoryId);

}