package com.example.lab9_20216583.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class FlashUtil {

    private FlashUtil() {
    }

    public static void success(HttpServletRequest request, String mensaje) {
        request.getSession().setAttribute("flashSuccess", mensaje);
    }

    public static void error(HttpServletRequest request, String mensaje) {
        request.getSession().setAttribute("flashError", mensaje);
    }

    public static void moveToRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        Object success = session.getAttribute("flashSuccess");
        Object error = session.getAttribute("flashError");

        if (success != null) {
            request.setAttribute("flashSuccess", success);
            session.removeAttribute("flashSuccess");
        }

        if (error != null) {
            request.setAttribute("flashError", error);
            session.removeAttribute("flashError");
        }
    }
}