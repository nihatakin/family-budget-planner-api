package com.royalfamily.familybudgetplannerapi.services;

import com.royalfamily.familybudgetplannerapi.domain.User;
import com.royalfamily.familybudgetplannerapi.exceptions.EtAuthException;

public interface UserService {

    User validateUser(String email, String password) throws EtAuthException;

    User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException;

}
