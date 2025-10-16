package com.example.ShoppingCart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.UserInterface;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.model.User;
import com.example.ShoppingCart.model.UserAddress;
import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.RegisterRequest;
import com.example.ShoppingCart.pojo.dto.UpdateUserRequest;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;
import com.example.ShoppingCart.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

/**
 * User Service Implementation - Implements all user business logic
 * Handles authentication, registration, profile updates, and session management
 */
@Service
@Transactional
public class UserImplementation implements UserInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private com.example.ShoppingCart.repository.UserAddressRepository userAddressRepository;

    /**
     * User login authentication
     * Logic: Find user by phone, verify password, create session, return user info
     */
    @Override
    public UserInfoDTO login(LoginRequest request, HttpSession session, BindingResult bindingResult) {

        // Step 1: Find user by phone number
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber().trim());

        // Step 2: If not found, the user does not exist
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // Step 3: Verify password (plain text comparison)
        if (!request.getPassword().trim().equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // Step 4: Login successful, save user information to Session
        session.setAttribute(SessionConstant.USER_ID, user.getUserId());
        UserInfoDTO userInfo = new UserInfoDTO(user);
        session.setAttribute(SessionConstant.CURRENT_USER, userInfo);

        // Step 5: Return user information
        return userInfo;
    }

    /**
     * User logout
     * Logic: Destroy entire session to clear all user data
     */
    @Override
    public void logout(HttpSession session) {
        // destroy the entire Session
        session.invalidate();
    }

    /**
     * Get current user from session
     * Logic: Retrieve user info from session, throw error if not logged in
     */
    @Override
    public UserInfoDTO getCurrentUser(HttpSession session) {
        // get user information from Session
        UserInfoDTO userInfo = (UserInfoDTO) session.getAttribute(SessionConstant.CURRENT_USER);

        if (userInfo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        return userInfo;
    }

    /**
     * Check login status
     * Logic: Verify if userId exists in session
     */
    @Override
    public boolean isLoggedIn(HttpSession session) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        return userId != null && !userId.isEmpty();
    }

    /**
     * User registration
     * Logic: Validate passwords match, check duplicates, create new user, save to database
     */
    @Override
    public UserInfoDTO register(RegisterRequest request) {
        // Step 1: Verify if the two passwords are consistent
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        // Step 2: Check if the phone number has been registered
        User existingUser = userRepository.findByPhoneNumber(request.getPhoneNumber().trim());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.PHONE_NUMBER_DUPLICATE);
        }

        // Step 3: Check if the user name has been occupied
        User existingUserName = userRepository.findByUserName(request.getUserName().trim());
        if (existingUserName != null) {
            throw new BusinessException(ErrorCode.USER_NAME_DUPLICATE);
        }

        // Step 4: Create new user
        User newUser = new User();
        newUser.setUserName(request.getUserName().trim());
        newUser.setPhoneNumber(request.getPhoneNumber().trim());

        // Step 5: Set password (plain text storage, Remove trailing spaces)
        newUser.setPassword(request.getPassword().trim());

        // Step 6: Save to database
        User savedUser = userRepository.save(newUser);

        // Step 7: Return user information
        return new UserInfoDTO(savedUser);
    }

    /**
     * Update user profile information
     * Logic: Update username, avatar, password if provided; optionally create new address
     */
    @Override
    public UserInfoDTO updateUserInfo(String userId, UpdateUserRequest request) {
        // Step 1: Query user from database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Step 2: Update user name if provided
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            // check if the user name has been occupied by other users
            User existingUser = userRepository.findByUserName(request.getUserName());
            if (existingUser != null && !existingUser.getUserId().equals(userId)) {
                throw new BusinessException(ErrorCode.USER_NAME_DUPLICATE);
            }
            user.setUserName(request.getUserName());
        }

        // Step 3: Update avatar if provided
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            user.setAvatar(request.getAvatar());
        }

        // Step 4: Update password (if the old password and new password are provided)
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            // verify old password
            if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "Please enter the old password");
            }
            if (!request.getOldPassword().equals(user.getPassword())) {
                throw new BusinessException(ErrorCode.OLD_PASSWORD_ERROR);
            }
            // set new password (plain text storage)
            user.setPassword(request.getNewPassword());
        }

        // Step 5: Save update
        User updatedUser = userRepository.save(user);

        // Step 6: Handle address creation if address fields are provided
        try {
            // If user submitted any address-related field, require all address fields to be present
            boolean anyAddressFieldProvided = (request.getReceiverName() != null && !request.getReceiverName().isEmpty())
                    || (request.getPhone() != null && !request.getPhone().isEmpty())
                    || (request.getRegion() != null && !request.getRegion().isEmpty())
                    || (request.getDetailAddress() != null && !request.getDetailAddress().isEmpty());

            if (anyAddressFieldProvided) {
                // require completeness
                boolean allPresent = request.getReceiverName() != null && !request.getReceiverName().isEmpty()
                        && request.getPhone() != null && !request.getPhone().isEmpty()
                        && request.getRegion() != null && !request.getRegion().isEmpty()
                        && request.getDetailAddress() != null && !request.getDetailAddress().isEmpty();

                if (!allPresent) {
                    throw new BusinessException(ErrorCode.PARAM_ERROR, "Please complete the shipping address (recipient, phone number, region, detailed address)");
                }

                // check current count - limit to 6 addresses
                java.util.List<UserAddress> existing = userAddressRepository.findByUser_UserId(userId);
                if (existing != null && existing.size() >= 6) {
                    throw new BusinessException(ErrorCode.PARAM_ERROR, "The maximum number of addresses has been reached, please delete one address before adding.");
                }

                // create new address
                UserAddress addr = new UserAddress();
                addr.setUser(updatedUser);
                addr.setReceiverName(request.getReceiverName());
                addr.setPhone(request.getPhone());
                addr.setRegion(request.getRegion());
                addr.setDetailAddress(request.getDetailAddress());

                // handle default address logic
                UserAddress defaultAddress = userAddressRepository.findByUser_UserIdAndIsDefaultTrue(userId);
                if (defaultAddress == null || Boolean.TRUE.equals(request.getIsDefault())) {
                    if (defaultAddress != null) {
                        defaultAddress.setDefault(false);
                        userAddressRepository.save(defaultAddress);
                    }
                    addr.setDefault(true);
                } else {
                    addr.setDefault(false);
                }

                userAddressRepository.save(addr);
            }
        } catch (BusinessException e) {
            // rethrow business exceptions so controller can show message to user
            throw e;
        } catch (Exception ignored) {
            // swallow other exceptions to avoid breaking user update on address save errors
        }

        // Step 7: Return updated user information
        return new UserInfoDTO(updatedUser);
    }
}
