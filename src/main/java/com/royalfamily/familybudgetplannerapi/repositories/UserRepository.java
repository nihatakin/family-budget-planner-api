package com.royalfamily.familybudgetplannerapi.repositories;

import com.royalfamily.familybudgetplannerapi.domain.User;
import com.royalfamily.familybudgetplannerapi.exceptions.EtAuthException;

public interface UserRepository {

    Integer create(String firstName, String lastName, String email, String password) throws EtAuthException;

    User findByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);

}
