package com.isnakebuzz.skywars.Utils.Holograms.tinyprotocol;

import java.util.concurrent.atomic.*;

import com.mojang.authlib.*;
import org.bukkit.plugin.*;
import com.google.common.collect.*;
import org.bukkit.scheduler.*;

import java.util.logging.*;

import org.bukkit.event.player.*;
import org.bukkit.event.server.*;
import org.bukkit.*;

import java.util.*;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import io.netty.channel.*;

public abstract class TinyProtocol {
    private static final AtomicInteger ID;
    private static final Reflection.MethodInvoker getPlayerHandle;
    private static final Reflection.FieldAccessor<Object> getConnection;
    private static final Reflection.FieldAccessor<Object> getManager;
    private static final Reflection.FieldAccessor<Channel> getChannel;
    private static final Class<Object> minecraftServerClass;
    private static final Class<Object> serverConnectionClass;
    private static final Reflection.FieldAccessor<Object> getMinecraftServer;
    private static final Reflection.FieldAccessor<Object> getServerConnection;
    private static final Reflection.MethodInvoker getNetworkMarkers;
    private static final Class<?> PACKET_LOGIN_IN_START;
    private static final Reflection.FieldAccessor<GameProfile> getGameProfile;
    private Map<String, Channel> channelLookup;
    private Listener listener;
    private Set<Channel> uninjectedChannels;
    private List<Object> networkManagers;
    private List<Channel> serverChannels;
    private ChannelInboundHandlerAdapter serverChannelHandler;
    private ChannelInitializer<Channel> beginInitProtocol;
    private ChannelInitializer<Channel> endInitProtocol;
    private String handlerName;
    protected volatile boolean closed;
    protected Plugin plugin;

    public TinyProtocol(final Plugin plugin) {
        this.channelLookup = new MapMaker().weakValues().makeMap();
        this.uninjectedChannels = Collections.newSetFromMap(new MapMaker().weakKeys().makeMap());
        this.serverChannels = Lists.newArrayList();
        this.plugin = plugin;
        this.handlerName = this.getHandlerName();
        this.registerBukkitEvents();
        try {
            this.registerChannelHandler();
            this.registerPlayers(plugin);
        } catch (IllegalArgumentException ex) {
            plugin.getLogger().info("[TinyProtocol] Delaying server channel injection due to late bind.");
            new BukkitRunnable() {
                public void run() {
                    TinyProtocol.this.registerChannelHandler();
                    TinyProtocol.this.registerPlayers(plugin);
                    plugin.getLogger().info("[TinyProtocol] Late bind injection successful.");
                }
            }.runTask(plugin);
        }
    }

    private void createServerChannelHandler() {
        this.endInitProtocol = new ChannelInitializer<Channel>() {
            protected void initChannel(final Channel channel) throws Exception {
                try {
                    synchronized (TinyProtocol.this.networkManagers) {
                        if (!TinyProtocol.this.closed) {
                            channel.eventLoop().submit(() -> TinyProtocol.this.injectChannelInternal(channel));
                        }
                    }
                } catch (Exception e) {
                    TinyProtocol.this.plugin.getLogger().log(Level.SEVERE, "Cannot inject incomming channel " + channel, e);
                }
            }
        };
        this.beginInitProtocol = new ChannelInitializer<Channel>() {
            protected void initChannel(final Channel channel) throws Exception {
                channel.pipeline().addLast(new ChannelHandler[]{TinyProtocol.this.endInitProtocol});
            }
        };
        this.serverChannelHandler = new ChannelInboundHandlerAdapter() {
            public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
                final Channel channel = (Channel) msg;
                channel.pipeline().addFirst(new ChannelHandler[]{TinyProtocol.this.beginInitProtocol});
                ctx.fireChannelRead(msg);
            }
        };
    }

    private void registerBukkitEvents() {
        this.listener = (Listener) new Listener() {
            @EventHandler(priority = EventPriority.LOWEST)
            public final void onPlayerLogin(final PlayerLoginEvent e) {
                if (TinyProtocol.this.closed) {
                    return;
                }
                final Channel channel = TinyProtocol.this.getChannel(e.getPlayer());
                if (!TinyProtocol.this.uninjectedChannels.contains(channel)) {
                    TinyProtocol.this.injectPlayer(e.getPlayer());
                }
            }

            @EventHandler
            public final void onPluginDisable(final PluginDisableEvent e) {
                if (e.getPlugin().equals(TinyProtocol.this.plugin)) {
                    TinyProtocol.this.close();
                }
            }
        };
        this.plugin.getServer().getPluginManager().registerEvents(this.listener, this.plugin);
    }

    private void registerChannelHandler() {
        final Object mcServer = TinyProtocol.getMinecraftServer.get(Bukkit.getServer());
        final Object serverConnection = TinyProtocol.getServerConnection.get(mcServer);
        boolean looking = true;
        this.networkManagers = (List<Object>) TinyProtocol.getNetworkMarkers.invoke(null, serverConnection);
        this.createServerChannelHandler();
        int i = 0;
        while (looking) {
            final List<Object> list = Reflection.getField(serverConnection.getClass(), List.class, i).get(serverConnection);
            for (final Object item : list) {
                if (!ChannelFuture.class.isInstance(item)) {
                    break;
                }
                final Channel serverChannel = ((ChannelFuture) item).channel();
                this.serverChannels.add(serverChannel);
                serverChannel.pipeline().addFirst(new ChannelHandler[]{this.serverChannelHandler});
                looking = false;
            }
            ++i;
        }
    }

    private void unregisterChannelHandler() {
        if (this.serverChannelHandler == null) {
            return;
        }
        for (final Channel serverChannel : this.serverChannels) {
            final ChannelPipeline pipeline = serverChannel.pipeline();
            serverChannel.eventLoop().execute((Runnable) new Runnable() {
                @Override
                public void run() {
                    try {
                        pipeline.remove((ChannelHandler) TinyProtocol.this.serverChannelHandler);
                    } catch (NoSuchElementException ex) {
                    }
                }
            });
        }
    }

    private void registerPlayers(final Plugin plugin) {
        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            this.injectPlayer(player);
        }
    }

    public Object onPacketOutAsync(final Player receiver, final Channel channel, final Object packet) {
        return packet;
    }

    public Object onPacketInAsync(final Player sender, final Channel channel, final Object packet) {
        return packet;
    }

    public void sendPacket(final Player player, final Object packet) {
        this.sendPacket(this.getChannel(player), packet);
    }

    public void sendPacket(final Channel channel, final Object packet) {
        channel.pipeline().writeAndFlush(packet);
    }

    public void receivePacket(final Player player, final Object packet) {
        this.receivePacket(this.getChannel(player), packet);
    }

    public void receivePacket(final Channel channel, final Object packet) {
        channel.pipeline().context("encoder").fireChannelRead(packet);
    }

    protected String getHandlerName() {
        return "tiny-" + this.plugin.getName() + "-" + TinyProtocol.ID.incrementAndGet();
    }

    public void injectPlayer(final Player player) {
        this.injectChannelInternal(this.getChannel(player)).player = player;
    }

    public void injectChannel(final Channel channel) {
        this.injectChannelInternal(channel);
    }

    private PacketInterceptor injectChannelInternal(final Channel channel) {
        try {
            PacketInterceptor interceptor = (PacketInterceptor) channel.pipeline().get(this.handlerName);
            if (interceptor == null) {
                interceptor = new PacketInterceptor();
                channel.pipeline().addBefore("packet_handler", this.handlerName, (ChannelHandler) interceptor);
                this.uninjectedChannels.remove(channel);
            }
            return interceptor;
        } catch (IllegalArgumentException e) {
            return (PacketInterceptor) channel.pipeline().get(this.handlerName);
        }
    }

    public Channel getChannel(final Player player) {
        Channel channel = this.channelLookup.get(player.getName());
        if (channel == null) {
            final Object connection = TinyProtocol.getConnection.get(TinyProtocol.getPlayerHandle.invoke(player, new Object[0]));
            final Object manager = TinyProtocol.getManager.get(connection);
            this.channelLookup.put(player.getName(), channel = TinyProtocol.getChannel.get(manager));
        }
        return channel;
    }

    public void uninjectPlayer(final Player player) {
        this.uninjectChannel(this.getChannel(player));
    }

    public void uninjectChannel(final Channel channel) {
        if (!this.closed) {
            this.uninjectedChannels.add(channel);
        }
        channel.eventLoop().execute((Runnable) new Runnable() {
            @Override
            public void run() {
                channel.pipeline().remove(TinyProtocol.this.handlerName);
            }
        });
    }

    public boolean hasInjected(final Player player) {
        return this.hasInjected(this.getChannel(player));
    }

    public boolean hasInjected(final Channel channel) {
        return channel.pipeline().get(this.handlerName) != null;
    }

    public final void close() {
        if (!this.closed) {
            this.closed = true;
            for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
                this.uninjectPlayer(player);
            }
            HandlerList.unregisterAll(this.listener);
            this.unregisterChannelHandler();
        }
    }

    static {
        ID = new AtomicInteger(0);
        getPlayerHandle = Reflection.getMethod("{obc}.entity.CraftPlayer", "getHandle", (Class<?>[]) new Class[0]);
        getConnection = Reflection.getField("{nms}.EntityPlayer", "playerConnection", Object.class);
        getManager = Reflection.getField("{nms}.PlayerConnection", "networkManager", Object.class);
        getChannel = Reflection.getField("{nms}.NetworkManager", Channel.class, 0);
        minecraftServerClass = Reflection.getUntypedClass("{nms}.MinecraftServer");
        serverConnectionClass = Reflection.getUntypedClass("{nms}.ServerConnection");
        getMinecraftServer = Reflection.getField("{obc}.CraftServer", TinyProtocol.minecraftServerClass, 0);
        getServerConnection = Reflection.getField(TinyProtocol.minecraftServerClass, TinyProtocol.serverConnectionClass, 0);
        getNetworkMarkers = Reflection.getTypedMethod(TinyProtocol.serverConnectionClass, null, List.class, TinyProtocol.serverConnectionClass);
        PACKET_LOGIN_IN_START = Reflection.getMinecraftClass("PacketLoginInStart");
        getGameProfile = Reflection.getField(TinyProtocol.PACKET_LOGIN_IN_START, GameProfile.class, 0);
    }

    private final class PacketInterceptor extends ChannelDuplexHandler {
        public volatile Player player;

        public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
            final Channel channel = ctx.channel();
            this.handleLoginStart(channel, msg);
            try {
                msg = TinyProtocol.this.onPacketInAsync(this.player, channel, msg);
            } catch (Exception e) {
                TinyProtocol.this.plugin.getLogger().log(Level.SEVERE, "Error in onPacketInAsync().", e);
            }
            if (msg != null) {
                super.channelRead(ctx, msg);
            }
        }

        public void write(final ChannelHandlerContext ctx, Object msg, final ChannelPromise promise) throws Exception {
            try {
                msg = TinyProtocol.this.onPacketOutAsync(this.player, ctx.channel(), msg);
            } catch (Exception e) {
                TinyProtocol.this.plugin.getLogger().log(Level.SEVERE, "Error in onPacketOutAsync().", e);
            }
            if (msg != null) {
                super.write(ctx, msg, promise);
            }
        }

        private void handleLoginStart(final Channel channel, final Object packet) {
            if (TinyProtocol.PACKET_LOGIN_IN_START.isInstance(packet)) {
                final GameProfile profile = TinyProtocol.getGameProfile.get(packet);
                TinyProtocol.this.channelLookup.put(profile.getName(), channel);
            }
        }
    }
}
