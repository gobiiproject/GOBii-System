package org.gobiiproject.gobiiclient.generic;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Phil on 5/19/2017.
 */
public class TestServerHandlerGet extends AbstractHandler {

    @Override
    public void handle(String target,
                       Request request,
                       HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException, ServletException {

        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        request.setHandled(true);
        httpServletResponse.getWriter().println("<h1>Hello World</h1>");

    }
}
