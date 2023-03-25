package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.UserCredentialsDTO;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface IUserService {
    User insertUser(UserCredentialsDTO userCredentialsDTO) throws EntityAlreadyExistsException;

    User updateUser(UserCredentialsDTO userCredentialsDTO) throws EntityNotFoundException;

    void deleteUser(Long id) throws EntityNotFoundException;

    List<User> getUsersByUserName(String username) throws EntityNotFoundException;
    User getUserById(Long id) throws EntityNotFoundException;

}
