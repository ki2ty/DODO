# Netty





## 入门



### 写一个Discard Server



> 1. 编写一个ChannelInboundHandlerAdapter的子类，重写ChannelRead方法以及exceptionCaught方法
>
>    ```java
>    public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
>    
>    
>        @Override
>        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
>            //Discard the received data silently
>          	//这里的ByteBuf是一个引用计数对象，调用release()来减少引用计数，计数为0时则被显式回收
>            ((ByteBuf) msg).release();
>        }
>    
>    
>        @Override
>        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
>            cause.printStackTrace();
>            ctx.close();
>        }
>    }
>    ```
>
>    ChannelInboundHandlerAdapter是ChannelInboundHandler的实现类
>
>    ![image-20210831105813991](/Users/zlc/Library/Application Support/typora-user-images/image-20210831105813991.png)
>
>    ChannelInboundHandler定义了各种各样的事件处理方法
>
>    ```java
>    public interface ChannelInboundHandler extends ChannelHandler {
>        void channelRegistered(ChannelHandlerContext var1) throws Exception;
>    
>        void channelUnregistered(ChannelHandlerContext var1) throws Exception;
>    
>        void channelActive(ChannelHandlerContext var1) throws Exception;
>    
>        void channelInactive(ChannelHandlerContext var1) throws Exception;
>    		//读事件，当有读事件时被调用，在这个例子中接收到的Object var2的类型为ByteBuf
>        void channelRead(ChannelHandlerContext var1, Object var2) throws Exception;
>    
>        void channelReadComplete(ChannelHandlerContext var1) throws Exception;
>    
>        void userEventTriggered(ChannelHandlerContext var1, Object var2) throws Exception;
>    
>        void channelWritabilityChanged(ChannelHandlerContext var1) throws Exception;
>    		//异常事件
>        void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception;
>    }
>    ```
>
>    要时刻记得，引用计数对象的垃圾回收是handler的责任，通常会在channelRead中这样做
>
>    ```java
>        @Override
>        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
>            try {
>                //Do something with msg
>            } finally {
>                ReferenceCountUtil.release(msg);
>            }
>        }
>    ```
>
>    
>
> 2. 编写DiscardServer
>
>    ```java
>    @Slf4j
>    public class DiscardServer {
>    
>        private int port;
>    
>        public DiscardServer(int port) {
>            this.port = port;
>        }
>    
>    
>        /**
>         *  (1)     NioEventLoopGroup是一个多线程的event loop用来进行I/O操作，bossGroup用来连接请求并将其注册到workersGroup
>         *
>         *  (2)     ServerBootstrap是一个用来创建一个server的帮助类，也可以直接使用Channel创建服务器，但那将是一个tedious的过程，在大多数情况下都不需要使用这种方式
>         *
>         *  (3)     NioServerSocketChannel用来接收服务器接收到的连接请求
>         *
>         *  (4)     应用逐渐复杂后，当你需要添加更多的handler到pipeline中时就需要将这个匿名类抽象为一个top-level class
>         *
>         *  (5)     配置bossGroup中的Channel
>         *
>         *  (6)     配置workerGroup中的Channel
>         *
>         *  (7)     bind可以绑定多次，可以绑定多个端口
>         *
>         */
>        public void run() throws Exception {
>            EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
>            EventLoopGroup workerGroup = new NioEventLoopGroup();
>            try {
>                ServerBootstrap bootstrap = new ServerBootstrap();  //  (2)
>                bootstrap.group(bossGroup, workerGroup)
>                        .channel(NioServerSocketChannel.class)  //  (3)
>                        .childHandler(new ChannelInitializer<SocketChannel>() { //  (4)
>                            @Override
>                            protected void initChannel(SocketChannel socketChannel) throws Exception {
>                                socketChannel.pipeline().addLast(new DiscardServerHandler());
>                            }
>                        })
>                        .option(ChannelOption.SO_BACKLOG, 128)  //  (5)
>                        .childOption(ChannelOption.SO_KEEPALIVE, true); //  (6)
>                ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).sync();   //  (7)
>                bootstrap.bind(8082).sync();    //可以bind多个端口
>    
>    
>                f.channel().closeFuture().sync();
>            } finally {
>                workerGroup.shutdownGracefully();
>                bossGroup.shutdownGracefully();
>            }
>        }
>    
>        public static void main(String[] args) throws Exception {
>            int port = 8081;
>            if (args.length > 0) {
>                port = Integer.parseInt(args[0]);
>            }
>            new DiscardServer(port).run();
>        }
>    
>    
>    }
>    ```
>
>    
>
> 



### 尝试接收数据

>1.修改DiscardServerHandler的channelRead代码
>
>```java
>    @Override
>    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
>        ByteBuf byteBuf = (ByteBuf) msg;
>        try {
>            //Do something with msg
>//            while (byteBuf.isReadable()) {  //  (1) can be simplified to System.out.println(in.toString(CharsetUtil.UTF_8))
>//                System.out.println((char) byteBuf.readByte());
>//                System.out.flush();
>//            }
>            System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
>
>        } finally {
>            ReferenceCountUtil.release(msg);    //  (2)
>          //            byteBuf.release();
>        }
>    }
>```
>
>
>
>



### 尝试响应客户端

> 1.创建一个EchoServerHandler，将服务器接收到的数据返回给客户端
>
> ```java
> public class EchoServerHandler extends ChannelInboundHandlerAdapter {
>     
>     @Override
>     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
>         ctx.writeAndFlush(msg);		// or ctx.write()	ctx.flush()
>     }
>     
> }
> ```
>
> 







### 时间服务器











## 构建一个服务器



```Java
new ServerBootstrap()
  			//一个Selector和一个线程就认为是一个EventLoop
        .group(new NioEventLoopGroup())
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(new StringDecoder())
                        .addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
            }
        })
        .bind(8081);
```





