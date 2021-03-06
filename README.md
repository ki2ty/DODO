# Netty





## 入门



### 写一个Discard Server



> 1. 编写一个ChannelInboundHandlerAdapter的子类，重写ChannelRead方法以及exceptionCaught方法
>
>    
>
>
> ```java
>     public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
>        @Override
>        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
>            //Discard the received data silently
>           //这里的ByteBuf是一个引用计数对象，调用release()来减少引用计数，计数为0时则被显式回收
>            ((ByteBuf) msg).release();
>        }
>        @Override
>        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
>            cause.printStackTrace();
>            ctx.close();
>        }
>     }
> 
> ```
>
>   
>
> 2. ChannelInboundHandlerAdapter是ChannelInboundHandler的实现类
>
>    
>
> 3. ChannelInboundHandler定义了各种各样的事件处理方法
>
> 
>
>    ```java
> 	 	public interface ChannelInboundHandler extends ChannelHandler {
>         void channelRegistered(ChannelHandlerContext var1) throws Exception;
> 
>         void channelUnregistered(ChannelHandlerContext var1) throws Exception;
> 
>         void channelActive(ChannelHandlerContext var1) throws Exception;
> 
>         void channelInactive(ChannelHandlerContext var1) throws Exception;
>         //读事件，当有读事件时被调用，在这个例子中接收到的Object var2的类型为ByteBuf
>         void channelRead(ChannelHandlerContext var1, Object var2) throws Exception;
> 
>         void channelReadComplete(ChannelHandlerContext var1) throws Exception;
> 
>         void userEventTriggered(ChannelHandlerContext var1, Object var2) throws Exception;
> 
>         void channelWritabilityChanged(ChannelHandlerContext var1) throws Exception;
>         //异常事件
>         void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception;
> 	 	}
>    ```
>
>    
>
> 4. 要时刻记得，引用计数对象的垃圾回收是handler的责任，通常会在channelRead中这样做
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
> 5. 编写DiscardServer
>
>
> ```java
> @Slf4j
> public class DiscardServer {
> 
>     private int port;
> 
>     public DiscardServer(int port) {
>         this.port = port;
>     }
> 		/**
>     *  (1)     NioEventLoopGroup是一个多线程的event loop用来进行I/O操作，bossGroup用来连接请求并将其注册到workersGroup
>     *
>     *  (2)     ServerBootstrap是一个用来创建一个server的帮助类，也可以直接使用Channel创建服务器，但那将是一个tedious的过程，在大多数情况下都不需要使用这种方式
>     *
>     *  (3)     NioServerSocketChannel用来接收服务器接收到的连接请求
>     *
>     *  (4)     应用逐渐复杂后，当你需要添加更多的handler到pipeline中时就需要将这个匿名类抽象为一个top-level class
>     *
>     *  (5)     配置bossGroup中的Channel
>     *
>     *  (6)     配置workerGroup中的Channel
>     *
>     *  (7)     bind可以绑定多次，可以绑定多个端口
>     *
>     */
>    public void run() throws Exception {
>        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
>        EventLoopGroup workerGroup = new NioEventLoopGroup();
>        try {
>            ServerBootstrap bootstrap = new ServerBootstrap();  //  (2)
>            bootstrap.group(bossGroup, workerGroup)
>                    .channel(NioServerSocketChannel.class)  //  (3)
>                    .childHandler(new ChannelInitializer<SocketChannel>() { //  (4)
>                        @Override
>                        protected void initChannel(SocketChannel socketChannel) throws Exception {
>                            socketChannel.pipeline().addLast(new DiscardServerHandler());
>                        }
>                    })
>                    .option(ChannelOption.SO_BACKLOG, 128)  //  (5)
>                    .childOption(ChannelOption.SO_KEEPALIVE, true); //  (6)
>            ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).sync();   //  (7)
>            bootstrap.bind(8082).sync();    //可以bind多个端口
>            f.channel().closeFuture().sync();
>        } finally {
>            workerGroup.shutdownGracefully();
>            bossGroup.shutdownGracefully();
>        }
>    }
> 
>    public static void main(String[] args) throws Exception {
>        int port = 8081;
>        if (args.length > 0) {
>            port = Integer.parseInt(args[0]);
>        }
>        new DiscardServer(port).run();
>    }
> }
> ```
>
> 
>
>
>    



### 尝试接收数据

>1. 修改DiscardServerHandler的channelRead代码
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



### 接收到客户端信息后进行响应的服务器

> 1. 创建一个EchoServerHandler，将服务器接收到的数据返回给客户端
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
> https://github.com/ki2ty/DODO/tree/master/src/main/java/com/netty/demo/echo







### 时间服务器

> 服务器在和客户端建立连接后立刻发送当前时间给客户端，之后关闭连接
>
> https://github.com/ki2ty/DODO/tree/master/src/main/java/com/netty/demo/time



### Dealing with a Stream-base Transport - 如何在流式传输中处理fragmentation issue

> 从上面的时间服务器端中进行思考，一个时间在发送时可能会被拆分成多个字节数组，而在连续发送两个独立的时间package后，客户端收到的不是两个message而是a bunch of bytes

#### 解决方案1

> 在Handler的生命周期中维护一个ByteBuf变量
>
> 1. 在handlerAdded中初始化这个ByteBuf，将其大小设定为一个完整数据包的长度
>
> 2. 在handlerRemoved中释放这个ByteBuf的内存
>
> 3. 在channelRead中使用维护的ByteBuf变量读取channelRead参数中的msg，释放msg的内存
>
> 4. 判断ByteBuf变量的readableBytes()返回值是否满足一个完整时间信息的长度，在if判断中做真正的业务逻辑处理
>
> ```java
> public class TimeServerHandlerV1 extends ChannelInboundHandlerAdapter {
> 
>     private ByteBuf buf;
> 
>     @Override
>     public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
>         // define a ByteBuf to cumulate received data
>         buf = ctx.alloc().buffer(4);
>     }
> 
>     @Override
>     public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
>         buf.release();
>         buf = null;
>     }
> 
>     @Override
>     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
>         ByteBuf b = (ByteBuf) msg;
>         buf.readBytes(b);
>         b.release();
> 
>         if (buf.readableBytes() >= 4) {
>             long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
>             System.out.println(new Date(currentTimeMillis));
>             //ctx.close();
>         }
>     }
> 
> 
>     @Override
>     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
>         cause.printStackTrace();;
>         ctx.close();
>     }
> }
> ```
>
> 修改ServerHandler的代码为writeAndFlush两次
>
> 
>
> 



#### 解决方案2

> 对于TimeClient来说解决方案1可以解决定长数据的问题，但对于a variable length field来说，ChannelInboundHanlder的实现将变得unmaintainable
>
> 1. ByteToMessageDecoder是ChannelInboundHandler的实现类，可以更好的处理fragmentation issue
>
> 2. ByteToMessageDecoder在调用decode()时会使用一个内部用来积累接收到数据的buffer
>
> 3. 可以在decode()中决定是否向out中添加，这取决于cumulative buffer中是否有足够的数据
>
> 4. 当有新的数据被接收时ByteToMessageDecoder会再次调用decode()
>
> 5. 当decode()向out中添加了一个Object，就意味着decoder已经成功decode了一个message



> 有段话理解不了
>
> ByteToMessageDecoder will discard the read part of the cumulative buffer. Please remember that you don't need to decode multiple messages. ByteToMessageDecoder will keep calling the decode() method until it adds nothing to out.
>
> 大概理解了，意思就是每次这个decode只处理一个message，它会重复调用decode直到一个message都不能添加到out中去
>
> 
>
> 我的TimeDecoder类
>
> ```java
> public class TimeDecoder extends ByteToMessageDecoder {
>     @Override
>     protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
>         if(in.readableBytes() < 4){
>             return;
>         }
>         out.add(in.readBytes(4));
>     }
> }
> ```
>
> 使用方案2时的TimeClient修改部分代码
>
> ```java
>             b.handler(new ChannelInitializer<SocketChannel>() {
>                 @Override
>                 protected void initChannel(SocketChannel socketChannel) throws Exception {
>                     ChannelPipeline p = socketChannel.pipeline();
>                     p.addLast(new TimeDecoder(), new TimeClientHandler());
>                 }
>             });
> ```
>
> 



### Speaking in POJO instead of ByteBuf

.....

















