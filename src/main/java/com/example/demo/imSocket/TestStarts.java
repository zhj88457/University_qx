package com.example.demo.imSocket;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class TestStarts implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        int port = 8887;

        Server server =new Server(port);
        server.start();
        System.out.println("项目启动中：服务已开启");
        System.out.println("等待客户端接入的端口号: " + server.getPort());

    }
}
