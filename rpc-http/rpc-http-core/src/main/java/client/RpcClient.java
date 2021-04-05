package client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.home.core.Filter;
import com.home.core.LoadBalancer;
import com.home.core.Router;
import com.home.core.entity.RpcRequest;
import com.home.core.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * RPC客户端
 */
@Slf4j
public final class RpcClient {

    static {
        ParserConfig.getGlobalInstance().addAccept("com.home");
    }

    public static <T, filters> T createFromRegistry(final Class<T> serviceClass, final String zkUrl, Router router, LoadBalancer loadBalance, Filter filter) {

        // curator Provider list from zk
        List<String> invokers = new ArrayList<>();
        // 1. 简单：从zk拿到服务提供的列表
        // 2. 挑战：监听zk的临时节点，根据事件更新这个list（注意，需要做个全局map保持每个服务的提供者List）

        // router, loadbalance
        List<String> urls = router.route(invokers);
        String url = loadBalance.select(urls);

        return (T) create(serviceClass, url, filter);

    }

    public static <T> T create(final Class<T> serviceClass, final String url, Filter... filters) {

        // JDK动态代理
        return (T) Proxy.newProxyInstance(
                RpcClient.class.getClassLoader(),
                new Class[]{serviceClass},
                new RpcInvocationHandler(serviceClass, url, filters)
        );

    }


    public static class RpcInvocationHandler implements InvocationHandler {

        public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;
        private final String url;
        private final Filter[] filters;

        public <T> RpcInvocationHandler(Class<T> serviceClass, String url, Filter... filters) {
            this.serviceClass = serviceClass;
            this.url = url;
            this.filters = filters;
        }

        // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpc是xml自定义序列化、反序列化
        // int byte char float double long bool
        // [], data class
        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {

            RpcRequest request = new RpcRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);

            if (null != filters) {
                for (Filter filter : filters) {
                    if (!filter.filter(request)) {
                        return null;
                    }
                }
            }

            RpcResponse response = post(request, url);
            return JSON.parse(response.getResult().toString());
        }

        /**
         * 请求提供者的接口获取相关数据信息
         *
         * @param req
         * @param url
         * @return
         * @throws IOException
         */
        private RpcResponse post(RpcRequest req, String url) throws IOException {
            String reqJson = JSON.toJSONString(req);

            log.info("req json: {}",reqJson);

            // 1.可以复用client
            // 2.尝试使用httpclient或者netty client
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSONTYPE, reqJson))
                    .build();
            String respJson = client.newCall(request).execute().body().string();

            log.info("resp json: {}",respJson);
            return JSON.parseObject(respJson, RpcResponse.class);
        }
    }
}
