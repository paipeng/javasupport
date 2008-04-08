package javasupport.servlet;

import javasupport.servlet.ServletRequestHelper;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.servlet.http.HttpSession;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Unit test for ServletUtils class.
 * 
 * @author zemian
 *
 */
public class ServletRequestHelperTest {

    private MockHttpServletRequest request;

    @Before
    public void createRequest() {
        request = new MockHttpServletRequest();
        request.addParameter("null", (String) null);
        request.addParameter("empty", "");

        request.addParameter("s", "foo-param");
        request.addParameter("s2", "foo-param");
        request.addParameter("foo", "bar");
        request.addParameter("foo2", "bar2");

        request.addParameter("b", "true"); //The only two true
        request.addParameter("bt", "TRUE");//The only two true
        request.addParameter("bf", "false");
        request.addParameter("bf2", "FALSE");
        request.addParameter("bjunk", "blah");
        request.addParameter("by", "YES");  //should BE FALSE!!!
        request.addParameter("bn", "NO");

        request.addParameter("n", "0");
        request.addParameter("n2", "1");
        request.addParameter("n3", "-1");
        request.addParameter("n4", String.valueOf(Integer.MAX_VALUE));
        request.addParameter("wrongN", "abc");

        request.addParameter("n4", String.valueOf(Integer.MAX_VALUE));

        request.addParameter("d", "99.9");
        request.addParameter("d2", "0.0");
        request.addParameter("d3", "-1.0");
        request.addParameter("d4", String.valueOf(Double.MAX_VALUE));

        request.setAttribute("o", new Date(2));
        request.setAttribute("o2", new Date(2));
        request.setAttribute("s", "foo-attr");
        request.setAttribute("s2", "foo-attr");
        request.setAttribute("b", Boolean.TRUE);
        request.setAttribute("n", new Integer(2));
        request.setAttribute("d", new Double(77.7));

        HttpSession session = request.getSession();
        session.setAttribute("o", new Date(3));
        session.setAttribute("o2", new Date(3));
        session.setAttribute("s", "foo-session");
        session.setAttribute("s2", "foo-session");
        session.setAttribute("b", Boolean.TRUE);
        session.setAttribute("n", new Integer(3));
        session.setAttribute("d", new Double(77.7));
    }

    @Test
    public void searchScopeForObject() {

        //should be request attribute level... 
        assertEquals(new Date(2), ServletRequestHelper.searchScope(request, "o2", new Date(-1)));

        request.removeAttribute("o2");
        //now should be request session level.
        assertEquals(new Date(3), ServletRequestHelper.searchScope(request, "o2", new Date(-1)));
    }

    @Test
    public void searchScopeForString() {
        //should be request parameter level... 
        assertEquals("foo-param", ServletRequestHelper.searchScope(request, "s2", "abc"));

        request.removeParameter("s2");
        //should be request attribute level... 
        assertEquals("foo-attr", ServletRequestHelper.searchScope(request, "s2", "abc"));

        request.removeAttribute("s2");
        //now should be requet session level.
        assertEquals("foo-session", ServletRequestHelper.searchScope(request, "s2", "abc"));
    }

    @Test
    public void sessionSetter() {
        ServletRequestHelper.setSession(request, "o-set", new Date(0));
        assertEquals(new Date(0), ServletRequestHelper.getSession(request, "o-set"));

        ServletRequestHelper.setSession(request, "s-set", "foo-session");
        assertEquals("foo-session", ServletRequestHelper.getSession(request, "s-set"));

    }

    @Test
    public void expectedSession() {
        assertEquals(new Date(3), ServletRequestHelper.getSession(request, "o"));
        assertEquals("foo-session", ServletRequestHelper.getSession(request, "s"));
    }

    @Test
    public void optionalParameters() {
        assertEquals("bar", ServletRequestHelper.getOptionalParameter(request, "not-there", "bar"));
        assertEquals("99", ServletRequestHelper.getOptionalParameter(request, "not-there", "99"));
    }

    @Test
    public void requiredParameters() {
        assertEquals("bar", ServletRequestHelper.getRequiredParameter(request, "foo"));
        assertEquals("bar2", ServletRequestHelper.getRequiredParameter(request, "foo2"));
    }

    @Test
    public void baseUrl() {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/testApp/webapp/demo");
        req.setContextPath("/testApp");
        Assert.assertEquals("http://localhost:80/testApp", ServletRequestHelper.getBaseUrl(req));

        req.setRequestURI("/testApp/demo.jsp");
        Assert.assertEquals("http://localhost:80/testApp", ServletRequestHelper.getBaseUrl(req));

        req.setRequestURI("/testApp/");
        Assert.assertEquals("http://localhost:80/testApp", ServletRequestHelper.getBaseUrl(req));

        req.setRequestURI("/testApp");
        Assert.assertEquals("http://localhost:80/testApp", ServletRequestHelper.getBaseUrl(req));
    }
}
