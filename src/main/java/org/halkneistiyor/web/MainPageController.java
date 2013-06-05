package org.halkneistiyor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/5/13
 */
@Controller
public class MainPageController
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String test()
    {
        return "index";
    }
}
