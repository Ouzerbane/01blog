package com._blog._blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.IdDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.ReportRepo;

@Service
public class AdminService {

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private ReportRepo reportRepo;

    public String removeUserService(IdDto id, AuthEntity currentUser) {

        if (!"admin".equalsIgnoreCase(currentUser.getType())) {
            throw new CustomException("authorization", "You are not authorized to remove users");
        }

        return "User with ID " + id.getId() + " has been removed successfully.";
    }


}

// if (!"admin".equalsIgnoreCase(currentUser.getType())) {
// throw new CustomException("authorization", "You are not authorized to remove
// users");
// }
