package com.xmutca.incubator.core.web.config;

import com.xmutca.incubator.core.common.response.Receipt;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-25
 */
@Configuration
@RestController
public class ErrorPagesConfiguration implements ErrorPageRegistrar {

    /**
     * 常见错误页面的数据处理
     *
     * @param registry
     */
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
        registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
        registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(value = "/error/404")
    public Receipt error404() {
        return new Receipt(HttpStatus.NOT_FOUND.value(), "sorry, page is lost!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(value = "/error/500")
    public Receipt error500() {
        return new Receipt(HttpStatus.INTERNAL_SERVER_ERROR.value(), "sorry, unknown error has occurred!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(value = "/error/400")
    public Receipt error400() {
        return new Receipt(HttpStatus.BAD_REQUEST.value(), "sorry, current request is bad!");
    }

}
