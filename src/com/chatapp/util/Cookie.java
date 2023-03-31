/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatapp.util;

import com.chatapp.pojos.User;

/**
 *
 * @author Helanka
 */
public class Cookie {
    private static User loginUser;

    public static User getLoginUser() {
        return loginUser;
    }

    public static void setLoginUser(User loginUser) {
        Cookie.loginUser = loginUser;
    }
    
}
