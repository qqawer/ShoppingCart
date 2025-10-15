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

@Service
@Transactional
public class UserImplementation implements UserInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private com.example.ShoppingCart.repository.UserAddressRepository userAddressRepository;

    @Override
    public UserInfoDTO login(LoginRequest request, HttpSession session, BindingResult bindingResult) {

        // 1. find user by phone number
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber().trim());

        // 2. if not found, the user does not exist
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 3. verify password (plain text comparison)
        if (!request.getPassword().trim().equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 4. login successful, save user information to Session
        session.setAttribute(SessionConstant.USER_ID, user.getUserId());
        UserInfoDTO userInfo = new UserInfoDTO(user);
        session.setAttribute(SessionConstant.CURRENT_USER, userInfo);

        // 5. return user information
        return userInfo;
    }

    @Override
    public void logout(HttpSession session) {
        // destroy the entire Session
        session.invalidate();
    }

    @Override
    public UserInfoDTO getCurrentUser(HttpSession session) {
        // get user information from Session
        UserInfoDTO userInfo = (UserInfoDTO) session.getAttribute(SessionConstant.CURRENT_USER);

        if (userInfo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        return userInfo;
    }

    @Override
    public boolean isLoggedIn(HttpSession session) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        return userId != null && !userId.isEmpty();
    }

    @Override
    public UserInfoDTO register(RegisterRequest request) {
        // 1. verify if the two passwords are consistent
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        // 2. check if the phone number has been registered
        User existingUser = userRepository.findByPhoneNumber(request.getPhoneNumber().trim());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.PHONE_NUMBER_DUPLICATE);
        }

        // 3. check if the user name has been occupied
        User existingUserName = userRepository.findByUserName(request.getUserName().trim());
        if (existingUserName != null) {
            throw new BusinessException(ErrorCode.USER_NAME_DUPLICATE);
        }

        // 4. create new user
        User newUser = new User();
        newUser.setUserName(request.getUserName().trim());
        newUser.setPhoneNumber(request.getPhoneNumber().trim());
        // 5. set password (plain text storage, Remove trailing spaces)
        newUser.setPassword(request.getPassword().trim());

        // 6. save to database
        User savedUser = userRepository.save(newUser);

        // 7. return user information
        return new UserInfoDTO(savedUser);
    }

    @Override
    public UserInfoDTO updateUserInfo(String userId, UpdateUserRequest request) {
        // 1. query user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. update user name
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            // check if the user name has been occupied by other users
            User existingUser = userRepository.findByUserName(request.getUserName());
            if (existingUser != null && !existingUser.getUserId().equals(userId)) {
                throw new BusinessException(ErrorCode.USER_NAME_DUPLICATE);
            }
            user.setUserName(request.getUserName());
        }

        // 3. update avatar
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            user.setAvatar(request.getAvatar());
        }

        // 4. update password (if the old password and new password are provided)
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

        // 5. save update
        User updatedUser = userRepository.save(user);

//        // 6. if the request contains address information, save or update the user address
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

                // check current count
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

                // check if there is a default address
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
        // 6. return updated user information
        return new UserInfoDTO(updatedUser);
    }
}
