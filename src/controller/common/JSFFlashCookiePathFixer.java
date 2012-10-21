package controller.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 
 * слага пътя на всички бисквитки равен на "/"
 * по този начин Flash Scope-а е достъпен и в друго поддърво на JSF web приложението
 * 
 * кодът е взет от http://stackoverflow.com/questions/11410161/jsf-2-0-flash-scope-cookie-path
 *
 */
public class JSFFlashCookiePathFixer extends HttpServletResponseWrapper {

    private final String path;

    public JSFFlashCookiePathFixer(HttpServletResponse response, String contextpath) {
        super(response);
        this.path = contextpath;
    }

    @Override
    public void addCookie(Cookie cookie) {

        // Hardcoded name from jsf-impl # com.sun.faces.context.flash.ELFlash  
        final String FLASH_COOKIE_NAME = "csfcfc";

        if (cookie.getName().equals(FLASH_COOKIE_NAME)){
            cookie.setPath(path);
        }
        super.addCookie(cookie);
    }

}

